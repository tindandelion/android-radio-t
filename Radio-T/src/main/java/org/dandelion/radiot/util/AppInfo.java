package org.dandelion.radiot.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class AppInfo {
    private PackageManager packageManager;
    private String packageName;
    private ApplicationInfo applicationInfo;

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
    
    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public AppInfo(Context context) {
        packageManager = context.getPackageManager();
        packageName = context.getPackageName();
        applicationInfo = context.getApplicationInfo();
    }
}
