/**
 * 
 */
package com.byc.smslocated;

import java.lang.reflect.Method;

import android.app.AppOpsManager;
import android.content.Context;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.AppOpsManagerCompat;

/**
 * @author byc
 *
 */
public class PermissionHelper {
	/** @hide No operation specified. */
    public static final int OP_NONE = -1;
    /** @hide Access to coarse location information. */
    public static final int OP_COARSE_LOCATION = 0;
    /** @hide Access to fine location information. */
    public static final int OP_FINE_LOCATION = 1;
    /** @hide Causing GPS to run. */
    public static final int OP_GPS = 2;
    /** @hide */
    public static final int OP_VIBRATE = 3;
    /** @hide */
    public static final int OP_READ_CONTACTS = 4;
    /** @hide */
    public static final int OP_WRITE_CONTACTS = 5;
    /** @hide */
    public static final int OP_READ_CALL_LOG = 6;
    /** @hide */
    public static final int OP_WRITE_CALL_LOG = 7;
    /** @hide */
    public static final int OP_READ_CALENDAR = 8;
    /** @hide */
    public static final int OP_WRITE_CALENDAR = 9;
    /** @hide */
    public static final int OP_WIFI_SCAN = 10;
    /** @hide */
    public static final int OP_POST_NOTIFICATION = 11;
    /** @hide */
    public static final int OP_NEIGHBORING_CELLS = 12;
    /** @hide */
    public static final int OP_CALL_PHONE = 13;
    /** @hide */
    public static final int OP_READ_SMS = 14;
    /** @hide */
    public static final int OP_WRITE_SMS = 15;
    /** @hide */
    public static final int OP_RECEIVE_SMS = 16;
    /** @hide */
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    /** @hide */
    public static final int OP_RECEIVE_MMS = 18;
    /** @hide */
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    /** @hide */
    public static final int OP_SEND_SMS = 20;
    /** @hide */
    public static final int OP_READ_ICC_SMS = 21;
    /** @hide */
    public static final int OP_WRITE_ICC_SMS = 22;
    /** @hide */
    public static final int OP_WRITE_SETTINGS = 23;
    /** @hide */
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;

    public static final String OPSTR_FINE_LOCATION ="android:fine_location";
    /** Read previously received cell broadcast messages. */
    public static final String OPSTR_READ_CELL_BROADCASTS= "android:read_cell_broadcasts";
    /** Inject mock location into the system. */
    public static final String OPSTR_MOCK_LOCATION = "android:mock_location";
    /** Read external storage. */
    public static final String OPSTR_READ_EXTERNAL_STORAGE = "android:read_external_storage";
    /** Write external storage. */
    public static final String OPSTR_WRITE_EXTERNAL_STORAGE= "android:write_external_storage";
    /** Required to draw on top of other apps. */
    public static final String OPSTR_SYSTEM_ALERT_WINDOW= "android:system_alert_window";
    /** Required to SEND_SMS on top of other apps. AppOpsManager.OPSTR_SEND_SMS*/
    public static final String OPSTR_SEND_SMS= "android:send_sms";
    /** Required to OPSTR_RECEIVE_SMS on top of other apps.AppOpsManager.OPSTR_RECEIVE_SMS */
    public static final String OPSTR_RECEIVE_SMS= "android:receive_sms";
    /** Required to OPSTR_READ_SMS on top of other apps. AppOpsManager.OPSTR_READ_SMS */
    public static final String OPSTR_READ_SMS=  "android:read_sms";

    /**返回值**/
    public static int MODE_IGNORED=AppOpsManagerCompat.MODE_IGNORED;
    
    /**
     * 检查权限列表
     *
     * @param context
     * @param op
     * 这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     * @param opString
     *   如判断定位权限 AppOpsManager.OPSTR_FINE_LOCATION
     * @return  @see 如果返回值 AppOpsManagerCompat.MODE_IGNORED 表示被禁用了

     */
    public static int checkOp(Context context, int op, String opString) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
//            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 23) {
                    return AppOpsManagerCompat.noteOp(context, opString,context.getApplicationInfo().uid,
                            context.getPackageName());
                }

            }
        }
        return -1;
    }
    
    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
