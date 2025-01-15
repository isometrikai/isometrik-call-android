package io.isometrik.samples.meeting;

import android.app.Application;
import io.isometrik.ui.IsometrikUiSdk;

public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    IsometrikUiSdk.getInstance().sdkInitialize(this);
    IsometrikUiSdk.getInstance()
        .createConfiguration(getString(R.string.app_secret), getString(R.string.user_secret),
            getString(R.string.connection_string), getString(R.string.license_key),
            BuildConfig.APPLICATION_ID,getString(R.string.app_name));
  }

  @Override
  public void onTerminate() {
    IsometrikUiSdk.getInstance().onTerminate();
    super.onTerminate();
  }
}
