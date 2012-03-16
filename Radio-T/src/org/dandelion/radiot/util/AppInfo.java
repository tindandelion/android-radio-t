package org.dandelion.radiot.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import org.dandelion.radiot.RadiotApplication;

public class AppInfo {
    private static AppInfo instance;
    private PackageManager packageManager;
    private String packageName;
    private ApplicationInfo applicationInfo;

    public static void initialize(Context context) {
        instance = new AppInfo(context);
    }
    
    public static AppInfo getInstance() {
        return instance;
    }

    public String getVersion() {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public CharSequence getLabel() {
        return packageManager.getApplicationLabel(applicationInfo);
    }

    private AppInfo(Context context) {
        packageManager = context.getPackageManager();
        packageName = context.getPackageName();
        applicationInfo = context.getApplicationInfo();
    }
}
