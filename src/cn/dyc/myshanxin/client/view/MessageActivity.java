package cn.dyc.myshanxin.client.view;

import cn.dyc.myshanxin.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MessageActivity extends Activity{
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		Intent intent =getIntent();
		String message=intent.getStringExtra("message");
		TextView msgtxt=(TextView)findViewById(R.id.dyc_message);
		msgtxt.setText(message);
		
		msgtxt.setOnClickListener(new MessageListener());
	}
private class MessageListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	
}
}
