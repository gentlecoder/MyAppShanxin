package cn.dyc.myshanxin.client.view;


import cn.dyc.myshanxin.R;
import cn.dyc.myshanxin.wxapi.WXEntryActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainTopRightDialog extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_top_right_dialog);
		//dialog=new MyDialog(this);
		layout=(LinearLayout)findViewById(R.id.main_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：此功能尚未实现！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	
	public void share(View v) {                           //分享照片
		Intent intent = new Intent (MainTopRightDialog.this,WXEntryActivity.class);			
		startActivity(intent);	
	 }
	public void chat(View v) {                           //聊天
		Intent intent = new Intent (MainTopRightDialog.this,ChatActivity.class);			
		startActivity(intent);	
	 }
	public void burn(View v) {                           //聊天
		Intent intent = new Intent (MainTopRightDialog.this,Burn.class);			
		startActivity(intent);	
	 }
	
	
	/*
	public void exitbutton1(View v) {  
    	this.finish();    	
      }  
	public void exitbutton0(View v) {  
    	this.finish();
    	MainWeixin.instance.finish();//关闭Main 这个Activity
      }  
	*/
}
