package io.isometrik.ui.utils;

import android.app.ActivityManager;

public class ApplicationStateHelper {


    public static boolean isApplicationInForeground() {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);

        return (myProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
    }
}
