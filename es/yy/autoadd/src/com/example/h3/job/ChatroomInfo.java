/**
 * 
 */
package com.example.h3.job;

import java.util.ArrayList;
import java.util.List;

import com.example.h3.Config;
import com.example.h3.util.AccessibilityHelper;
import com.example.h3.util.SpeechUtil;
import com.example.h3.util.VersionParam;

import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class ChatroomInfo {
	
	private static ChatroomInfo current;
	private Context context;
	private SpeechUtil speaker ;
	public  int mIndex=0;
	public boolean bWorking=false;
	//private static List<AccessibilityNodeInfo> chatMembers= new ArrayList<AccessibilityNodeInfo>();
	public List<AccessibilityNodeInfo> mChatMembers= null;//Ⱥ��Ա���Ƽ��ϣ�
	//private static final String CHAT_MEMBER_ID="com.tencent.mm:id/cgv";
	
	
	
    public static synchronized ChatroomInfo getChatroomInfo(Context context) {
    	
        if(current == null) {
            current = new ChatroomInfo(context);
        }
        return current;
    }
    private ChatroomInfo(Context context){
    	this.context = context;
    	//bReg=Config.getConfig(context).getREG();
    	speaker=SpeechUtil.getSpeechUtil(context);
    }
    /** �Ƿ�ΪȺ������Ϣ*/
    public boolean isChatroomInfoUi(AccessibilityNodeInfo rootNode) {
        if(rootNode == null) return false;
        AccessibilityNodeInfo node=AccessibilityHelper.findNodeInfosByText(rootNode, "������Ϣ(", -1);
        if(node==null)return false;else return true;
    }
    /** �Ƿ���Ҫ������ҳ*/
    public boolean isChatroomInfoLastPage(AccessibilityNodeInfo rootNode) {
        if(rootNode == null) return false;
        AccessibilityNodeInfo node=AccessibilityHelper.findNodeInfosByText(rootNode, "��ӳ�Ա", -1);
        if(node==null)return false;else return true;
    }

    /** �õ�Ⱥ��Ա*/
    public List<AccessibilityNodeInfo> getChatMembers(AccessibilityNodeInfo rootNode) {
    	mChatMembers=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.chatMemberID);
    	return mChatMembers;
    }
    /**���һ��Ⱥ��Ա*/
    public boolean ClickChatMember(AccessibilityNodeInfo chatMemberNameNode){
    	if(chatMemberNameNode==null)return false;
    	AccessibilityNodeInfo parent=chatMemberNameNode.getParent();
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    		if (parent != null) {
    			int i=0;
    			while(!parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
    				i=i+1;
    				if(i>10)return false;
    			}
    			return true;
    		}
    	 }
    	return false;
    }
}
