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
    public static String GalleryID = "com.tencent.mm:id/crf";//相册图库ID
    public static String SnsUserListID = "com.tencent.mm:id/czj";//相册列表ID
    public static String GalleryDateID = "com.tencent.mm:id/cwz";//相册图库日期日，
    public static String GalleryMoonID = "com.tencent.mm:id/cx0";//相册图库日期月
    //public static String pickObject = "b";
    public static String WINDOW_SNSUSER_UI = "com.tencent.mm.plugin.sns.ui.SnsUserUI";//好友相册窗口
    public static String WINDOW_SNSGALLERY_UI = "com.tencent.mm.plugin.sns.ui.SnsGalleryUI";//好友图片库
    public static String WINDOW_CHATROOM_CHATROOMINFO_UI = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";//群成员信息界面
    
    public static void init(int version) {
        if (version== 1080) {//6.5.10
        	chatMemberID = "com.tencent.mm:id/cgv";
        	GalleryID = "com.tencent.mm:id/crf";
        	SnsUserListID = "com.tencent.mm:id/czj";
        	GalleryDateID = "com.tencent.mm:id/cwz";//相册图库日期
        	GalleryMoonID = "com.tencent.mm:id/cx0";//相册图库日期月
        	WINDOW_SNSUSER_UI = "com.tencent.mm.plugin.sns.ui.SnsUserUI";
        	WINDOW_SNSGALLERY_UI = "com.tencent.mm.plugin.sns.ui.SnsGalleryUI";
        	WINDOW_CHATROOM_CHATROOMINFO_UI = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
        }
        if (version== 1120) {//6.5.16
        	chatMemberID = "com.tencent.mm:id/cgv";
        	GalleryID = "com.tencent.mm:id/cvb";
        	SnsUserListID = "com.tencent.mm:id/czj";
        	GalleryDateID = "com.tencent.mm:id/cwz";//相册图库日期
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
