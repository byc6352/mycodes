/**
 * 
 */
package com.byc.autoanswer;

import android.app.Notification;

/**
 * @author byc
 *
 */
public interface IStatusBarNotification {
    String getPackageName();
    Notification getNotification();
}
