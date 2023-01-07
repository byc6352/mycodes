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


	        //设置不用系统浏览器打开,直接显示在当前Webview
	        mWebview.setWebViewClient(new WebViewClient() {
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                view.loadUrl(url);
	                return true;
	            }
	        });

	        //设置WebChromeClient类
	        mWebview.setWebChromeClient(new WebChromeClient() {


	            //获取网站标题
	            @Override
	            public void onReceivedTitle(WebView view, String title) {
	                System.out.println("标题在这里");
	                mTitle=title;
	                mtitle.setVisibility(View.GONE);;
	                WebActivity.this.setTitle(title);
	            }


	            //获取加载进度
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


	        //设置WebViewClient类
	        mWebview.setWebViewClient(new WebViewClient() {
	            //设置加载前的函数
	            @Override
	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                System.out.println("开始加载了");
	                //beginLoading.setText("开始加载了");
	                beginLoading.setVisibility(View.GONE);
	                WebActivity.this.setTitle("正在加载...");

	            }

	            //设置结束加载函数
	            @Override
	            public void onPageFinished(WebView view, String url) {
	                //endLoading.setText("结束加载了");
	            	WebActivity.this.setTitle(mTitle+"(加载完成!)");

	            }
	        });
	        mWebview.setWebViewClient(new WebViewClient() {    
	            @Override    
	            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {    
	             handler.proceed();    //表示等待证书响应
	             handler.cancel();      //表示挂起连接，为默认方式
	             handler.handleMessage(null);    //可做其他处理
	            }    
	        });
	      //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
	        mWebSettings.setJavaScriptEnabled(true); 
	      //支持插件
	       // mWebSettings.setPluginsEnabled(true); 

	        //设置自适应屏幕，两者合用
	        mWebSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小 
	        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

	        //缩放操作
	        mWebSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
	        mWebSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
	        //webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

	        //其他细节操作
	        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 
	        mWebSettings.setAllowFileAccess(true); //设置可以访问文件 
	        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口 
	        mWebSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
	        mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
	        //优先使用缓存: 
	        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); 
	}
	  //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
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
