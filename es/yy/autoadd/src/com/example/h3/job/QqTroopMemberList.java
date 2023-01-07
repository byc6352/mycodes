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
	public  int mIndex=0;//Ⱥ��Ա���
	public int mMemberCount=0;//Ⱥ��Ա������
	public int mMemberAdded=0;//����ӵ�Ⱥ��Ա��
	public boolean bWorking=false;
	//private static List<AccessibilityNodeInfo> chatMembers= new ArrayList<AccessibilityNodeInfo>();
	public List<AccessibilityNodeInfo> mQqMembers= null;//Ⱥ��Ա���Ƽ��ϣ�
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
    /** �Ƿ�ΪȺ������Ϣ*/
    public boolean isQqTroopMemberListUi(AccessibilityNodeInfo rootNode) {
        if(rootNode == null) return false;
        AccessibilityNodeInfo node=AccessibilityHelper.findNodeInfosByText(rootNode, "Ⱥ��Ա", -1);
        if(node==null)return false;else return true;
    }
    /** �Ƿ���Ҫ������ҳ*/
    public boolean isTroopMemberListLastPage(AccessibilityNodeInfo rootNode) {
        if(mMemberAdded >mMemberCount) return true;else return false;
    }
    /** �Ƿ���Ҫ�ش�����*/
    public boolean isAnswer(AccessibilityNodeInfo rootNode) {
    	String className="android.widget.EditText";
    	AccessibilityNodeInfo node=AccessibilityHelper.findNodeInfosByClassName(rootNode, className, 0, true);
    	if(node==null)return false;
    	if(node.getText()==null)return false;
    	String txt=node.getText().toString();
    	if(txt.equals("�����"))return true;else return false;
    }
    /** �õ�Ⱥ��Ա*/
    public List<AccessibilityNodeInfo> getQqTroopMemberList(AccessibilityNodeInfo rootNode) {
    	mQqMembers=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.TroopMemberID);
    	
    	return mQqMembers;
    }
}
