package cn.dyc.myshanxin;


import cn.dyc.myshanxin.R.id;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Register extends Activity{
	private String[] citys={"南京","苏州","无锡","南通"};
	private ArrayAdapter<String> adapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		//初始化城市
		adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,citys);
		Spinner city=(Spinner)findViewById(R.id.dyc_city);
		city.setAdapter(adapter);
		
	    Button reg=(Button)findViewById(R.id.button1);
		reg.setOnClickListener(new RigsterListener());
		
		Button log=(Button)findViewById(R.id.button2);
		log.setOnClickListener(new LogListener());
	}
	
	private class LogListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(Register.this,Login.class );
			startActivity(intent);
		}
		
	}
  private class RigsterListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//验证信息
		EditText user_txt=(EditText)findViewById(R.id.dyc_username);
		EditText pwd_txt=(EditText)findViewById(R.id.dyc_passwd);
		EditText repwd_txt=(EditText)findViewById(R.id.dyc_repasswd);
		
		String username =user_txt.getText().toString();
		String passwd =pwd_txt.getText().toString();
		String repasswd =repwd_txt.getText().toString();
		
		Intent intent=new Intent();
		boolean validate=true;
		if(username==null||username.length()==0){
			intent.putExtra("message", "用户名不能为空");
			validate=false;
			
		}else if(passwd==null||passwd.length()==0){
			intent.putExtra("message", "密码不能为空");
			validate=false;
		}else if(!passwd.equals(repasswd)){
			intent.putExtra("message", "密码和密码确认不一致");
			validate=false;
		}
		if(!validate){
			intent.setClass(Register.this,MessageActivity.class);
			startActivity(intent);
			return;
		}else {
			
			RadioGroup group=(RadioGroup)findViewById(R.id.dycradioGroup1);
			int sexid=group.getCheckedRadioButtonId();
			String sex="";
			if(sexid==R.id.dycradio0){
				sex="1";
				
			}else{
				sex="0";
			}
			CheckBox h1_btn=(CheckBox)findViewById(R.id.dycchoose1);
			CheckBox h2_btn=(CheckBox)findViewById(R.id.checkBox2);
			CheckBox h3_btn=(CheckBox)findViewById(R.id.checkBox3);
			String hobby1="",hobby2="",hobby3="";
			if(h1_btn.isChecked()){
				hobby1=h1_btn.getText().toString();
			}
			if(h2_btn.isChecked()){
				hobby2=h1_btn.getText().toString();
			}
			if(h3_btn.isChecked()){
				hobby3=h1_btn.getText().toString();
			}
			Spinner spnr=(Spinner)findViewById(R.id.dyc_city);
			String city =spnr.getSelectedItem().toString();
			//保存到数据库
			Dyc_DBHelper dbhelper=new Dyc_DBHelper(Register.this );
			//验证用户是否存在？
			SQLiteDatabase readb=dbhelper.getReadableDatabase();
			String sql="select count(*) cont from user_info where username=? ";
			Cursor result=readb.rawQuery(sql, new String[]{username});
			int count=0;
			while(result.moveToNext()){
				count=result.getInt(result.getColumnIndex("cont"));
			}
			if(count>0){
				//提示用户名存在
			
            	intent.putExtra("message", "该用户名已存在！");
            	intent.setClass(Register.this, MessageActivity.class);
            	startActivity( intent);
            	readb.close();
            	return;
			}
		
			
			SQLiteDatabase db=dbhelper.getWritableDatabase();
			db.execSQL("insert into user_info values(?,?,?,?,?,?,?,?,?)",
					new String[]{username,passwd,sex,hobby1,hobby2,hobby3,city,"0","0"});
		  db.close();
		  
		  intent.putExtra("message", "注册成功！");
		  intent.setClass(Register.this,MessageActivity.class);
			startActivity(intent);
			return;
			
		}
		
		
	}
	  
  }
  
  public void btn_back(View v) {     //标题栏 返回按钮
    	this.finish();
    } 
  
}
