/**
 * 
 */
package com.byc.autoanswer;

/**
 * @author byc
 *
 */
public class VersionParam {
	  public static String WINDOW_LUCKYMONEY_RECEIVEUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
	  public static String WINDOW_LUCKYMONEY_DETAILUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
	  public static String pickObject = "b";

	    public static void init(int version) {
	        if (version < 1020) {
	        	WINDOW_LUCKYMONEY_RECEIVEUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

	        } else{
	        	WINDOW_LUCKYMONEY_RECEIVEUI = "com.tencent.mm.plugin.luckymoney.ui.En_";
	        	
	        }//if (version < 1020) {
	    }
}
