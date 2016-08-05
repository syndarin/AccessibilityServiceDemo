package name.vtiahotenkov.accessibilityservice;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vtiahotenkov on 8/4/16.
 */
public class WatchdogAccessibilityService extends AccessibilityService {

    private List<String> mAppsWithInternetPermission;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppsWithInternetPermission = new ArrayList<>();

        PackageManager pm = getPackageManager();
        String[] dangerousPermission = new String[]{Manifest.permission.INTERNET};
        List<PackageInfo> packages = pm.getPackagesHoldingPermissions(dangerousPermission, PackageManager.GET_PERMISSIONS);

        for (PackageInfo pi : packages) {
            mAppsWithInternetPermission.add(pi.packageName);
        }

        mAppsWithInternetPermission.remove("com.android.settings");
        mAppsWithInternetPermission.remove(getLauncherPackageName());
    }

    private String getLauncherPackageName(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;
        Log.i("tag", "Launcher package name detected - " + currentHomePackage);
        return currentHomePackage;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d("AS", "event");

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

            String packageName = accessibilityEvent.getPackageName().toString();
            Log.d("zzz", "Package - " + packageName);

            if (mAppsWithInternetPermission.contains(packageName)) {
                runApplication();
            }
        }
    }

    private void runApplication(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                performGlobalAction(GLOBAL_ACTION_BACK);

                Intent i = new Intent(WatchdogAccessibilityService.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        }, 300);

    }

    @Override
    public void onInterrupt() {

    }

}
