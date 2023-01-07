/**
 * 
 */
package util;

/**
 * <pre>
 *     author: byc
 *     blog  : http://blankj.com
 *     time  : 2016/8/11
 *     desc  : ������ع�����
 * </pre>
 */
public class ConstUtils {

    private ConstUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /******************** �洢��س��� ********************/
    /**
     * Byte��Byte�ı���
     */
    public static final int BYTE = 1;
    /**
     * KB��Byte�ı���
     */
    public static final int KB = 1024;
    /**
     * MB��Byte�ı���
     */
    public static final int MB = 1048576;
    /**
     * GB��Byte�ı���
     */
    public static final int GB = 1073741824;

    /******************** ʱ����س��� ********************/
    /**
     * ���������ı���
     */
    public static final int MSEC = 1;
    /**
     * �������ı���
     */
    public static final int SEC = 1000;
    /**
     * �������ı���
     */
    public static final int MIN = 60000;
    /**
     * ʱ�����ı���
     */
    public static final int HOUR = 3600000;
    /**
     * �������ı���
     */
    public static final int DAY = 86400000;

    /******************** ������س��� ********************/
    /**
     * �����ֻ��ţ��򵥣�
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * �����ֻ��ţ���ȷ��
     * <p>�ƶ���134(0-8)��135��136��137��138��139��147��150��151��152��157��158��159��178��182��183��184��187��188</p>
     * <p>��ͨ��130��131��132��145��155��156��175��176��185��186</p>
     * <p>���ţ�133��153��173��177��180��181��189</p>
     * <p>ȫ���ǣ�1349</p>
     * <p>������Ӫ�̣�170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-8])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * ���򣺵绰����
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * ��������֤����15λ
     */
    public static final String REGEX_IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * ��������֤����18λ
     */
    public static final String REGEX_IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * ��������
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * ����URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    /**
     * ���򣺺���
     */
    public static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    /**
     * �����û�����ȡֵ��ΧΪa-z,A-Z,0-9,"_",���֣�������"_"��β,�û���������6-20λ
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * ����yyyy-MM-dd��ʽ������У�飬�ѿ���ƽ����
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * ����IP��ַ
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
}