/**
 * 
 */
package com.example.h3.job;

import java.util.List;

import com.example.h3.util.AccessibilityHelper;
import com.example.h3.util.SpeechUtil;
import com.example.h3.util.VersionParam;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class QqTroopMemberList {
	private static QqTroopMemberList current;
	private Context context;
	private SpeechUtil speaker ;
	public  int mIndex=0;//群成员序号
	public int mMemberCount=0;//群成员总数；
	public int mMemberAdded=0;//已添加的群成员；
	public boolean bWorking=false;
	//private static List<AccessibilityNodeInfo> chatMembers= new ArrayList<AccessibilityNodeInfo>();
	public List<AccessibilityNodeInfo> mQqMembers= null;//群成员名称集合；
	//private static final String CHAT_MEMBER_ID="com.tencent.mm:id/cgv";
	
	
	
    public static synchronized QqTroopMemberList getQqTroopMemberList(Context context) {
    	
        if(current == null) {
            current = new QqTroopMemberList(context);
        }
        return current;
    }
    private QqTroopMemberList(Context context){
    	this.context = context;
    	//bReg=Config.getConfig(context).getREG();
    	speaker=SpeechUtil.getSpeechUtil(context);
    }
    /** 是否为群聊天信息*/
    public boolean isQqTroopMemberListUi(AccessibilityNodeInfo rootNode) {
        if(rootNode == null) return false;
        AccessibilityNodeInfo node=AccessibilityHelper.findNodeInfosByText(rootNode, "群成员", -1);
        if(node==null)return false;else return true;
    }
    /** 是否需要继续翻页*/
    public boolean isTroopMemberListLastPage(AccessibilityNodeInfo rootNode) {
        if(mMemberAdded >mMemberCount) return true;else return false;
    }
    /** 是否需要回答问题*/
    public boolean isAnswer(AccessibilityNodeInfo rootNode) {
    	String className="android.widget.EditText";
    	AccessibilityNodeInfo node=AccessibilityHelper.findNodeInfosByClassName(rootNode, className, 0, true);
    	if(node==null)return false;
    	if(node.getText()==null)return false;
    	String txt=node.getText().toString();
    	if(txt.equals("输入答案"))return true;else return false;
    }
    /** 得到群成员*/
    public List<AccessibilityNodeInfo> getQqTroopMemberList(AccessibilityNodeInfo rootNode) {
    	mQqMembers=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.TroopMemberID);
    	
    	return mQqMembers;
    }
}
