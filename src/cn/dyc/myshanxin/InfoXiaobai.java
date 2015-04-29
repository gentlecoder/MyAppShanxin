package cn.dyc.myshanxin;

import cn.dyc.myshanxin.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class InfoXiaobai extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_xiaobai);              
    }

   public void btn_back(View v) {     //标题栏 返回按钮
      	this.finish();
      } 
   public void btn_back_send(View v) {     //标题栏 返回按钮
     	this.finish();
     } 
   public void head_xiaobai(View v) {     //头像按钮
	   Intent intent = new Intent();
		intent.setClass(InfoXiaobai.this,InfoXiaobaiHead.class);
		startActivity(intent);
    } 
    
}
