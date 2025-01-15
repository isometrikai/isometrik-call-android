package io.isometrik.meeting;

import android.content.Context;

import io.isometrik.meeting.enums.IMLogVerbosity;
import io.isometrik.meeting.enums.IMRealtimeEventsVerbosity;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;

/**
 * The configuration class for Isometrik sdk initialization.
 */
public class IMConfiguration {
    private static final int REQUEST_TIMEOUT = 10;
    private static final int CONNECT_TIMEOUT = 10;

    private final Context context;
    /**
     * License Key provided by Isometrik.
     */
    private final String licenseKey;
    private String clientId;
    /**
     * If proxies are forcefully caching requests, set to true to allow the client to randomize the
     * subdomain.
     * This configuration is not supported if custom origin is enabled.
     */
    private boolean cacheBusting;

    /**
     * set to true to switch the client to HTTPS:// based communications.
     */

    private final boolean secure;
    /**
     * toggle to enable verbose logging for api request.
     */

    private IMLogVerbosity logVerbosity;

    /**
     * toggle to enable verbose logging for realtime events.
     */

    private IMRealtimeEventsVerbosity realtimeEventsVerbosity;

    /**
     * Stores the maximum number of seconds which the client should wait for connection before timing
     * out.
     */
    private int connectTimeout;

    /**
     * Reference on number of seconds which is used by client during operations to
     * check whether response potentially failed with 'timeout' or not.
     */
    private int requestTimeout;

    private final String appSecret;
    private final String userSecret;
    private final String connectionString;

    private String userToken;

    /**
     * Instantiates a new Im configuration.
     *
     * @param context          the context
     * @param licenseKey       the license key
     * @param appSecret        the app secret
     * @param userSecret       the user secret
     * @param connectionString the connection string
     */
    public IMConfiguration(Context context, String licenseKey, String appSecret, String userSecret,
                           String connectionString) {

        requestTimeout = REQUEST_TIMEOUT;

        connectTimeout = CONNECT_TIMEOUT;

        logVerbosity = IMLogVerbosity.NONE;

        realtimeEventsVerbosity = IMRealtimeEventsVerbosity.NONE;

        cacheBusting = false;
        secure = true;
        this.context = context;

        this.licenseKey = licenseKey;
        this.appSecret = appSecret;
        this.userSecret = userSecret;
        this.connectionString = connectionString;

    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Is cache busting boolean.
     *
     * @return the cache busting
     */
    public boolean isCacheBusting() {
        return cacheBusting;
    }

    /**
     * Is secure boolean.
     *
     * @return the secure
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * Gets log verbosity.
     *
     * @return the log verbosity
     */
    public IMLogVerbosity getLogVerbosity() {
        return logVerbosity;
    }

    /**
     * Gets realtime message verbosity.
     *
     * @return the realtime message verbosity
     */
    public IMRealtimeEventsVerbosity getRealtimeEventsVerbosity() {
        return realtimeEventsVerbosity;
    }

    /**
     * Gets connect timeout.
     *
     * @return the connect timeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Gets request timeout.
     *
     * @return the request timeout
     */
    public int getRequestTimeout() {
        return requestTimeout;
    }

    /**
     * Gets license key.
     *
     * @return the license key
     */
    public String getLicenseKey() {
        return licenseKey;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets client id.
     *
     * @param clientId the client id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets app secret.
     *
     * @return the app secret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * Gets user secret.
     *
     * @return the user secret
     */
    public String getUserSecret() {
        return userSecret;
    }

    /**
     * Sets cache busting.
     *
     * @param cacheBusting the cache busting
     */
    public void setCacheBusting(boolean cacheBusting) {
        this.cacheBusting = cacheBusting;
    }

    /**
     * Sets log verbosity.
     *
     * @param logVerbosity the log verbosity
     */
    public void setLogVerbosity(IMLogVerbosity logVerbosity) {
        this.logVerbosity = logVerbosity;
    }

    /**
     * Sets connect timeout.
     *
     * @param connectTimeout the connect timeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Sets request timeout.
     *
     * @param requestTimeout the request timeout
     */
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    /**
     * Sets realtime events verbosity.
     *
     * @param realtimeEventsVerbosity the realtime events verbosity
     */
    public void setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity realtimeEventsVerbosity) {
        this.realtimeEventsVerbosity = realtimeEventsVerbosity;
    }

    /**
     * Validate remote network call common params isometrik error.
     *
     * @param userTokenAvailable the user token available
     * @return the isometrik error
     */
    public IsometrikError validateRemoteNetworkCallCommonParams(boolean userTokenAvailable) {

        if (appSecret == null || appSecret.isEmpty()) {

            return IsometrikErrorBuilder.IMERROBJ_APP_SECRET_MISSING;
        } else if (licenseKey == null || licenseKey.isEmpty()) {

            return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
        } else {
            if (userTokenAvailable) {
                return null;
            } else {
                if (userSecret == null || userSecret.isEmpty()) {

                    return IsometrikErrorBuilder.IMERROBJ_USER_SECRET_MISSING;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Validate connection configuration isometrik error.
     *
     * @return the isometrik error
     */
    IsometrikError validateConnectionConfiguration() {
        if (licenseKey == null || licenseKey.isEmpty()) {

            return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
        } else if (connectionString == null || connectionString.isEmpty()) {
            return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_MISSING;
        } else if (connectionString.length() < 96) {
            return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_INVALID_VALUE;
        } else {
            return null;
        }
    }

    /**
     * Gets connection string.
     *
     * @return the connection string
     */
    public String getConnectionString() {
        return connectionString;
    }


    /**
     * Gets user token.
     *
     * @return the user token
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
