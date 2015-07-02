package cn.dyc.myshanxin.client.view;



import cn.dyc.myshanxin.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Burn extends Activity{
	private WebView webview; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.burn);
		webview = (WebView) findViewById(R.id.webview); 
        //设置WebView属性，能够执行Javascript脚本 
        webview.getSettings().setJavaScriptEnabled(true); 
        //加载需要显示的网页 
        webview.loadUrl("http://210.28.32.234:8080/bar/ws/input"); 
        //设置Web视图 
        webview.setWebViewClient(new HelloWebViewClient ());
   }
	@Override
    //设置回退 
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
            webview.goBack(); //goBack()表示返回WebView的上一页面 
            return true; 
        } 
        return false; 
    } 
     
    //Web视图 
    private class HelloWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }
    
    public void burn_activity_back(View v) {     //标题栏 返回按钮
      	this.finish();
      }  
}