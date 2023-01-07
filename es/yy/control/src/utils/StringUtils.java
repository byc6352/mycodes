/**
 * 
 */
package utils;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/16
 *     desc  : �ַ�����ع�����
 * </pre>
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * �ж��ַ����Ƿ�Ϊnull�򳤶�Ϊ0
     *
     * @param string ��У���ַ���
     * @return {@code true}: ��<br> {@code false}: ��Ϊ��
     */
    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    /**
     * �ж��ַ����Ƿ�Ϊnull��ȫΪ�ո�
     *
     * @param string ��У���ַ���
     * @return {@code true}: null��ȫ�ո�<br> {@code false}: ��Ϊnull�Ҳ�ȫ�ո�
     */
    public static boolean isSpace(String string) {
        return (string == null || string.trim().length() == 0);
    }
}
