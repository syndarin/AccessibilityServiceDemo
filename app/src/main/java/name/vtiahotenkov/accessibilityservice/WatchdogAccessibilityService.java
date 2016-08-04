package name.vtiahotenkov.accessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by vtiahotenkov on 8/4/16.
 */
public class WatchdogAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d("AS", "event");
    }

    @Override
    public void onInterrupt() {

    }

}
