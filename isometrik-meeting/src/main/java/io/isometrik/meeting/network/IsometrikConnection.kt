package io.isometrik.meeting.network

import android.util.Log
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import io.isometrik.meeting.Isometrik
import io.isometrik.meeting.enums.IMRealtimeEventsVerbosity
import io.isometrik.meeting.events.connection.ConnectEvent
import io.isometrik.meeting.events.connection.DisconnectEvent
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * The class to handle isometrik connection to receive realtime message and events.
 */
class IsometrikConnection(isometrikInstance: Isometrik) {
    private var mqttClient: Mqtt3AsyncClient? = null
    private var messagingTopic: String? = null
    private var presenceEventsTopic: String? = null
    private lateinit var isometrikInstance: Isometrik
    private var scheduler: ScheduledExecutorService? = null
    private var ATTEMP = 0L

    /**
     * Create connection.
     *
     * @param isometrikInstance the isometrik instance
     * @see io.isometrik.meeting.Isometrik
     */
    fun createConnection(isometrikInstance: Isometrik) {
        this.isometrikInstance = isometrikInstance
        val imConfiguration = isometrikInstance.configuration
        val baseConnectionPath: String = isometrikInstance.connectionBaseUrl
        val port = isometrikInstance.port


        val connectionString: String = imConfiguration.connectionString

        val secretPhraseA = connectionString.substring(0, 24)
        val secretPhraseB = connectionString.substring(24, 60)
        val secretPhraseC = connectionString.substring(60, 96)

        val licenseKey: String = imConfiguration.licenseKey
        val clientId: String = imConfiguration.clientId

        val clientIdForTopicSubscribe = clientId.substring(0, 24)

        val username = "2$secretPhraseA$secretPhraseB"
        val password = licenseKey + secretPhraseC
        this.messagingTopic =
            "/$secretPhraseA/$secretPhraseB/Message/$clientIdForTopicSubscribe"
        this.presenceEventsTopic =
            "/$secretPhraseA/$secretPhraseB/Status/$clientIdForTopicSubscribe"

        checkConnectionStatus(baseConnectionPath,port, clientId, username, password)
    }

    /**
     * @param baseConnectionPath path to be used for creating connection
     * @param clientId id of the client making the connection(should be unique)
     * @param username username to be unnecsed for authentication at time of connection
     * @param password password to be used for authentication at time of connection
     */
    private fun checkConnectionStatus(
        baseConnectionPath: String, port: Int, clientId: String, username: String,
        password: String
    ) {
        val uniqueClientId = clientId + "CALL"
        if (isometrikInstance.configuration.realtimeEventsVerbosity
            == IMRealtimeEventsVerbosity.FULL
        ) {
            Log.d(
                ISOMETRIK_MQTT_TAG,
                " ClientId: $uniqueClientId username: $username password: $password baseConnectionPath: $baseConnectionPath"
            )
        }

        if (mqttClient != null && mqttClient!!.state.isConnected) {

            Log.d(
                ISOMETRIK_MQTT_TAG,
                "  Already Connected ClientId: $uniqueClientId username: $username password: $password baseConnectionPath: $baseConnectionPath"
            )
        } else {
            mqttClient = MqttClient.builder()
                .useMqttVersion3()
                .identifier(uniqueClientId)
                .serverHost(baseConnectionPath.replace("tcp://",""))
                .serverPort(port)
                .addDisconnectedListener { context ->
                    this@IsometrikConnection.onDisconnected(
                        context,username, password) }
                .buildAsync()

            connectToBroker(username, password)

            // Set message listener
            mqttClient!!.publishes(MqttGlobalPublishFilter.ALL, this::onMqttDataReceived)
        }

    }

    private fun connectToBroker(username: String, password: String) {
        mqttClient!!.connectWith()
            .keepAlive(60)
            .cleanSession(true)
            .simpleAuth()
            .username(username)
            .password(password.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    // handle failure
                    Log.d(ISOMETRIK_MQTT_TAG, "Connection failed due to $throwable")
                    if(throwable.message?.contains("already connected") != true){
                        scheduleReconnect(username, password)
                    }
                } else {
                    // matt connected
                    if (isometrikInstance.configuration.realtimeEventsVerbosity
                        == IMRealtimeEventsVerbosity.FULL
                    ) {
                        Log.d(ISOMETRIK_MQTT_TAG, "Connected")
                    }

                    subscribeToTopic(messagingTopic!!,0)
                    subscribeToTopic(presenceEventsTopic!!,0)
                }
            }
    }

    private fun onMqttDataReceived(message: Mqtt3Publish) {
        val topic = message.topic.toString()

        if (topic == messagingTopic) {
            if (isometrikInstance.configuration.realtimeEventsVerbosity
                == IMRealtimeEventsVerbosity.FULL
            ) {
                Log.d(
                    "Realtime Event-Message",
                    JSONObject(String(message.payloadAsBytes)).toString()
                )
            }
            isometrikInstance.messageEvents
                .handleMessageEvent(
                    JSONObject(String(message.payloadAsBytes)),
                    isometrikInstance
                )
        } else if (topic == presenceEventsTopic) {
            if (isometrikInstance.configuration.realtimeEventsVerbosity
                == IMRealtimeEventsVerbosity.FULL
            ) {
                Log.d(
                    "Realtime Event-Presence",
                    JSONObject(String(message.payloadAsBytes)).toString()
                )
            }

            isometrikInstance.presenceEvents
                .handlePresenceEvent(
                    JSONObject(String(message.payloadAsBytes)),
                    isometrikInstance
                )
        }
    }

    private fun onDisconnected(
        context: MqttClientDisconnectedContext,
        username: String,
        password: String
    ) {
        Log.d(ISOMETRIK_MQTT_TAG, " Disconnected due to ${context.cause.message}")
        isometrikInstance.realtimeEventsListenerManager
            .connectionListenerManager
            .announce(DisconnectEvent(context.cause))

        scheduleReconnect(username, password)
    }

    private fun scheduleReconnect(username: String, password: String) {
        if (mqttClient == null) {
            return
        }
        if (scheduler == null || scheduler?.isShutdown == true) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
        }
        ATTEMP++
        Log.d(ISOMETRIK_MQTT_TAG, " scheduleReconnect")
        scheduler?.schedule({ connectToBroker(username,password) }, ATTEMP*10, TimeUnit.SECONDS)
    }

    fun subscribeToTopic(topic: String, qos: Int) {
        mqttClient?.subscribeWith()
            ?.topicFilter(topic)
            ?.qos(MqttQos.AT_MOST_ONCE)
            ?.callback { publish ->
                Log.e("MQTT_CHAT_SUBSCRIBE", "==> ${publish.topic}")
            }
            ?.send()
//            ?.whenComplete { subAck, throwable ->
//                if (throwable != null) {
//                    // Handle failure to subscribe
//                    Log.e("MQTT_CHAT_SUBSCRIBE", "$topic Error ==> $throwable")
//                } else {
//                    // Handle successful subscription,
//                    Log.e("MQTT_CHAT_SUBSCRIBE", "$topic Success")
//                }
//            }
    }



    /**
     * Drop connection to isometrik backend.
     *
     * @param force whether to drop a connection forcefully or allow graceful drop of connection
     */
    fun dropConnection(force: Boolean) {
        if ( mqttClient?.state?.isConnected == true) {
            mqttClient?.disconnect()
        }
    }

    /**
     * Drop connection to isometrik backend.
     */
    fun dropConnection() {
        if ( mqttClient?.state?.isConnected == true) {
            mqttClient?.disconnect()
        }
        mqttClient = null
    }

    /**
     * Re connect to isometrik backend.
     */
    fun reConnect() {

    }

    companion object {
        var ISOMETRIK_MQTT_TAG: String = "ISOMETRIK_MQTT: CALL"
    }
}
