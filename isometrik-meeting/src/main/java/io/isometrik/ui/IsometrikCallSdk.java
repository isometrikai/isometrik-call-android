package io.isometrik.ui;

import android.app.Application;
import android.content.Context;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.enums.IMLogVerbosity;
import io.isometrik.meeting.enums.IMRealtimeEventsVerbosity;
import io.isometrik.ui.utils.UserSession;

/**
 * The IsometrikUiSdk singleton to expose sdk functionality to other modules.
 */
public class IsometrikCallSdk {

    private Isometrik isometrik;
    private UserSession userSession;
    private String applicationId;
    private String applicationName;
    private Context applicationContext;
    private static volatile IsometrikCallSdk isometrikCallSdk;

    /**
     * private constructor.
     */
    private IsometrikCallSdk() {

        //Prevent form the reflection api.
        if (isometrikCallSdk != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
    }

    /**
     * Gets instance.
     *
     * @return the IsometrikUiSdk instance
     */
    public static IsometrikCallSdk getInstance() {
        if (isometrikCallSdk == null) {
            synchronized (IsometrikCallSdk.class) {
                if (isometrikCallSdk == null) {
                    isometrikCallSdk = new IsometrikCallSdk();
                }
            }
        }
        return isometrikCallSdk;
    }

    /**
     * Sdk initialize.
     *
     * @param applicationContext the application context
     */
    public void sdkInitialize(Context applicationContext) {
        if (applicationContext == null) {
            throw new RuntimeException(
                    "Sdk initialization failed as application context cannot be null.");
        } else if (!(applicationContext instanceof Application)) {
            throw new RuntimeException(
                    "Sdk initialization failed as context passed in parameter is not application context.");
        }

        this.applicationContext = applicationContext;

    }

    /**
     * Create configuration.
     *
     * @param appSecret        the app secret
     * @param userSecret       the user secret
     * @param connectionString the connection string
     * @param licenseKey       the license key
     * @param applicationId    the application id
     * @param applicationName  the application name
     */
    public void createConfiguration(String appSecret, String userSecret, String connectionString,
                                    String licenseKey, String applicationId, String applicationName) {

        if (applicationContext == null) {
            throw new RuntimeException("Initialize the sdk before creating configuration.");
        } else if (appSecret == null || appSecret.isEmpty()) {
            throw new RuntimeException("Pass a valid appSecret for isometrik sdk initialization.");
        } else if (userSecret == null || userSecret.isEmpty()) {
            throw new RuntimeException("Pass a valid userSecret for isometrik sdk initialization.");
        } else if (connectionString == null || connectionString.isEmpty()) {
            throw new RuntimeException("Pass a valid connectionString for isometrik sdk initialization.");
        } else if (licenseKey == null || licenseKey.isEmpty()) {
            throw new RuntimeException("Pass a valid licenseKey for isometrik sdk initialization.");
        } else if (applicationId == null || applicationId.isEmpty()) {
            throw new RuntimeException("Pass a valid applicationId for isometrik sdk initialization.");
        } else if (applicationName == null || applicationName.isEmpty()) {
            throw new RuntimeException("Pass a valid applicationName for isometrik sdk initialization.");
        }
        this.applicationId = applicationId;
        this.applicationName = applicationName;

        IMConfiguration imConfiguration =
                new IMConfiguration(applicationContext, licenseKey, appSecret, userSecret,
                        connectionString);
        imConfiguration.setLogVerbosity(IMLogVerbosity.NONE);
        imConfiguration.setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity.NONE);
        isometrik = new Isometrik(imConfiguration);
        userSession = new UserSession(applicationContext);
    }

    /**
     * Gets isometrik.
     *
     * @return the isometrik
     */
    public Isometrik getIsometrik() {
        if (isometrik == null) {
            throw new RuntimeException("Create configuration before trying to access isometrik object.");
        }

        return isometrik;
    }

    /**
     * Gets user session.
     *
     * @return the user session
     */
    public UserSession getUserSession() {
        if (userSession == null) {
            throw new RuntimeException(
                    "Create configuration before trying to access user session object.");
        }

        return userSession;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public Context getContext() {
        if (isometrik == null) {
            throw new RuntimeException("Create configuration before trying to access context object.");
        }
        return applicationContext;
    }

    /**
     * On terminate.
     */
    public void onTerminate() {
        if (isometrik == null) {
            throw new RuntimeException("Create configuration before trying to access isometrik object.");
        }


        isometrik.destroy();
    }

    /**
     * Gets application id.
     *
     * @return the application id
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Gets application name.
     *
     * @return the application name
     */
    public String getApplicationName() {
        return applicationName;
    }

}
