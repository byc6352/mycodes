/**
 * 
 */
package com.example.h3.util;

/**
 * @author byc
 *
 */
public class VersionParam {
    public static String chatMemberID = "com.tencent.mm:id/cgv";
    public static String TroopMemberID = "com.tencent.mobileqq:id/tv_name";
    public static String GalleryID = "com.tencent.mm:id/crf";//���ͼ��ID
    public static String SnsUserListID = "com.tencent.mm:id/czj";//����б�ID
    public static String GalleryDateID = "com.tencent.mm:id/cwz";//���ͼ�������գ�
    public static String GalleryMoonID = "com.tencent.mm:id/cx0";//���ͼ��������
    //public static String pickObject = "b";
    public static String WINDOW_SNSUSER_UI = "com.tencent.mm.plugin.sns.ui.SnsUserUI";//������ᴰ��
    public static String WINDOW_SNSGALLERY_UI = "com.tencent.mm.plugin.sns.ui.SnsGalleryUI";//����ͼƬ��
    public static String WINDOW_CHATROOM_CHATROOMINFO_UI = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";//Ⱥ��Ա��Ϣ����
    
    public static void init(int version) {
        if (version== 1080) {//6.5.10
        	chatMemberID = "com.tencent.mm:id/cgv";
        	GalleryID = "com.tencent.mm:id/crf";
        	SnsUserListID = "com.tencent.mm:id/czj";
        	GalleryDateID = "com.tencent.mm:id/cwz";//���ͼ������
        	GalleryMoonID = "com.tencent.mm:id/cx0";//���ͼ��������
        	WINDOW_SNSUSER_UI = "com.tencent.mm.plugin.sns.ui.SnsUserUI";
        	WINDOW_SNSGALLERY_UI = "com.tencent.mm.plugin.sns.ui.SnsGalleryUI";
        	WINDOW_CHATROOM_CHATROOMINFO_UI = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
        }
        if (version== 1120) {//6.5.16
        	chatMemberID = "com.tencent.mm:id/cgv";
        	GalleryID = "com.tencent.mm:id/cvb";
        	SnsUserListID = "com.tencent.mm:id/czj";
        	GalleryDateID = "com.tencent.mm:id/cwz";//���ͼ������
        	WINDOW_SNSUSER_UI = "com.tencent.mm.plugin.sns.ui.SnsUserUI";
        	WINDOW_SNSGALLERY_UI = "com.tencent.mm.plugin.sns.ui.SnsGalleryUI";
        	WINDOW_CHATROOM_CHATROOMINFO_UI = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
        }

    }
    public static void initQq(int version) {
        if (version== 692) {//7.1.0.3175
        	TroopMemberID = "com.tencent.mobileqq:id/tv_name";
        }
    }
}
