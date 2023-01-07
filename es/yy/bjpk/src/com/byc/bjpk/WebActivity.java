package com.byc.bjpk;

import com.example.h3.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;

import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends Activity {
    WebView mWebview;
    WebSettings mWebSettings;
    TextView beginLoading,endLoading,loading,mtitle;
    String mTitle="";
    String mUrl="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		  mWebview = (WebView) findViewById(R.id.webView1);
	        beginLoading = (TextView) findViewById(R.id.text_beginLoading);
	        endLoading = (TextView) findViewById(R.id.text_endLoading);
	        loading = (TextView) findViewById(R.id.text_Loading);
	        mtitle = (TextView) findViewById(R.id.title);

	        mWebSettings = mWebview.getSettings();
	        Intent intent = getIntent();
	        mUrl=intent.getStringExtra("url");
	        mWebview.loadUrl(mUrl);


	        //���ò���ϵͳ�������,ֱ����ʾ�ڵ�ǰWebview
	        mWebview.setWebViewClient(new WebViewClient() {
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                view.loadUrl(url);
	                return true;
	            }
	        });

	        //����WebChromeClient��
	        mWebview.setWebChromeClient(new WebChromeClient() {


	            //��ȡ��վ����
	            @Override
	            public void onReceivedTitle(WebView view, String title) {
	                System.out.println("����������");
	                mTitle=title;
	                mtitle.setVisibility(View.GONE);;
	                WebActivity.this.setTitle(title);
	            }


	            //��ȡ���ؽ���
	            @Override
	            public void onProgressChanged(WebView view, int newProgress) {
	            	loading.setVisibility(View.GONE);
	                if (newProgress < 100) {
	                    String progress = newProgress + "%";
	                    
	                    WebActivity.this.setTitle(mTitle+progress);
	                    //loading.setText(progress);
	                } else if (newProgress == 100) {
	                    String progress = newProgress + "%";
	                    //loading.setText(progress);
	                    WebActivity.this.setTitle(mTitle+progress);
	                }
	            }
	        });


	        //����WebViewClient��
	        mWebview.setWebViewClient(new WebViewClient() {
	            //���ü���ǰ�ĺ���
	            @Override
	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                System.out.println("��ʼ������");
	                //beginLoading.setText("��ʼ������");
	                beginLoading.setVisibility(View.GONE);
	                WebActivity.this.setTitle("���ڼ���...");

	            }

	            //���ý������غ���
	            @Override
	            public void onPageFinished(WebView view, String url) {
	                //endLoading.setText("����������");
	            	WebActivity.this.setTitle(mTitle+"(�������!)");

	            }
	        });
	        mWebview.setWebViewClient(new WebViewClient() {    
	            @Override    
	            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {    
	             handler.proceed();    //��ʾ�ȴ�֤����Ӧ
	             handler.cancel();      //��ʾ�������ӣ�ΪĬ�Ϸ�ʽ
	             handler.handleMessage(null);    //������������
	            }    
	        });
	      //������ʵ�ҳ����Ҫ��Javascript��������webview��������֧��Javascript
	        mWebSettings.setJavaScriptEnabled(true); 
	      //֧�ֲ��
	       // mWebSettings.setPluginsEnabled(true); 

	        //��������Ӧ��Ļ�����ߺ���
	        mWebSettings.setUseWideViewPort(true); //��ͼƬ�������ʺ�webview�Ĵ�С 
	        mWebSettings.setLoadWithOverviewMode(true); // ��������Ļ�Ĵ�С

	        //���Ų���
	        mWebSettings.setSupportZoom(true); //֧�����ţ�Ĭ��Ϊtrue���������Ǹ���ǰ�ᡣ
	        mWebSettings.setBuiltInZoomControls(true); //�������õ����ſؼ�����Ϊfalse�����WebView��������
	        //webSettings.setDisplayZoomControls(false); //����ԭ�������ſؼ�

	        //����ϸ�ڲ���
	        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //�ر�webview�л��� 
	        mWebSettings.setAllowFileAccess(true); //���ÿ��Է����ļ� 
	        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //֧��ͨ��JS���´��� 
	        mWebSettings.setLoadsImagesAutomatically(true); //֧���Զ�����ͼƬ
	        mWebSettings.setDefaultTextEncodingName("utf-8");//���ñ����ʽ
	        //����ʹ�û���: 
	        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); 
	}
	  //���������һҳ��������˳������
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //����Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_web_return) {
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
