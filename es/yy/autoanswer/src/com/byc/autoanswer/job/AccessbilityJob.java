/**
 * 
 */
package com.byc.autoanswer.job;
import android.view.accessibility.AccessibilityEvent;

import com.byc.autoanswer.IStatusBarNotification;
import com.byc.autoanswer.QiangHongBaoService;
/**
 * @author byc
 *
 */
public interface AccessbilityJob {

    void onCreateJob(QiangHongBaoService service);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
    void onNotificationPosted(IStatusBarNotification service);
}
