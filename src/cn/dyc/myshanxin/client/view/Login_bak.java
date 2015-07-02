package cn.dyc.myshanxin.client.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.dyc.myshanxin.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login_bak extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Button login=(Button)findViewById(R.id.login);
		login.setOnClickListener(new LoginListener());
		
		//记住密码功能
				//在文件中读出密码
						try {
							FileInputStream fis=openFileInput("passSave.txt");
							InputStreamReader inRead= new InputStreamReader(fis);
							BufferedReader bufferedR = new BufferedReader(inRead);
							String str;
							if((str=bufferedR.readLine())!= null){
								String []txt=str.split(" ");
								EditText user=(EditText)findViewById(R.id.dyc_username);
								EditText pass=(EditText)findViewById(R.id.dyc_passwd);
								user.setText(txt[0]);
								pass.setText(txt[1]);
								CheckBox remima=(CheckBox)findViewById(R.id.dyc_rember);
								remima.setChecked(true);
							}
							fis.close();
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

	}
	private class LoginListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//验证用户名，密码，是否输入
			EditText user=(EditText)findViewById(R.id.dyc_username);
			EditText pwd=(EditText)findViewById(R.id.dyc_passwd);
			String username=user.getText().toString();
			String passwd=pwd.getText().toString();
            if(username==null||username.isEmpty()||passwd==null||passwd.isEmpty()){
            	//提示输入用户名密码
            	Intent intent=new Intent();
            	intent.putExtra("message", "请输入用户名和密码！");
            	intent.setClass(Login_bak.this, MessageActivity.class);
            	startActivity( intent);
            	return;
            }
			//验证数据库TODO
			Dyc_DBHelper helper=new Dyc_DBHelper(Login_bak.this);
			SQLiteDatabase db=helper.getReadableDatabase();
			String sql="select count(*) cont from user_info where username=? and passwd=?";
			Cursor result=db.rawQuery(sql, new String[]{username,passwd});
			int count=0;
			while(result.moveToNext()){
				count=result.getInt(result.getColumnIndex("cont"));
			}
			if(count>0){
				//记住密码--在文件中写入密码
				CheckBox remember= (CheckBox)findViewById(R.id.dyc_rember);
					try {
						String writer;
						FileOutputStream fos=openFileOutput("passSave.txt",0);
						OutputStreamWriter outWriter = new OutputStreamWriter(fos);
						BufferedWriter bufferedW = new BufferedWriter(outWriter);
						if (remember.isChecked()) 
							 writer=username+" "+passwd;
						else writer=" ";
						bufferedW.write(writer);
						bufferedW.flush();
						fos.close();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				//进入登录界面
				Intent intent=new Intent();
            	intent.setClass(Login_bak.this, LoadingActivity.class);
            	startActivity( intent);
			}else{
				//提示用户密码错误
				Intent intent=new Intent();
            	intent.putExtra("message", "用户名和密码不正确！");
            	intent.setClass(Login_bak.this, MessageActivity.class);
            	startActivity( intent);
			}
			db.close();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} 
    public void login_back(View v) {     //标题栏 返回按钮
      	this.finish();
      }  
    public void login_pw(View v) {     //忘记密码按钮
    	Uri uri = Uri.parse("http://3g.qq.com"); 
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
    	startActivity(intent);
    	//Intent intent = new Intent();
    	//intent.setClass(Login.this,Whatsnew.class);
        //startActivity(intent);
      }  
}
