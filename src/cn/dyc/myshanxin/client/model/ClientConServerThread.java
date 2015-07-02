/**
 * 客户端和服务器端保持通信的线程
 * 不断地读取服务器发来的数据
 */
package cn.dyc.myshanxin.client.model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cn.dyc.myshanxin.common.SXMessage;
import cn.dyc.myshanxin.common.SXMessageType;

public class ClientConServerThread extends Thread {
	private Context context;
	private  Socket s;
	public Socket getS() {return s;}
	public ClientConServerThread(Context context,Socket s){
		this.context=context;
		this.s=s;
	}
	
	@Override
	public void run() {
		while(true){
			ObjectInputStream ois = null;
			SXMessage m;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				m=(SXMessage) ois.readObject();
				if(m.getType().equals(SXMessageType.COM_MES) 
						|| m.getType().equals(SXMessageType.GROUP_MES)){//如果是聊天消息
					//把从服务器获得的消息通过广播发送
					Intent intent = new Intent("cn.dyc.myshanxin.mes");
					String[] message=new String[]{
						m.getSender()+"",
						m.getSenderNick(),
						m.getSenderAvatar()+"",
						m.getContent(),
						m.getSendTime(),
						m.getType()};
					Log.i("--", message.toString());
					intent.putExtra("message", message);
					context.sendBroadcast(intent);
				}else if(m.getType().equals(SXMessageType.RET_ONLINE_FRIENDS)){//如果是好友列表
					//更新好友，群
					String s[] = m.getContent().split(",");
					//Log.i("", "--"+s[0]);
					//Log.i("", "--"+s[1]);
					BuddyActivity.buddyStr=s[0];
					GroupActivity.groupStr=s[1];
				}
				if(m.getType().equals(SXMessageType.SUCCESS)){
					Toast.makeText(context, "操作成功！", Toast.LENGTH_SHORT);
				}
			} catch (Exception e) {
				//e.printStackTrace();
				try {
					if(s!=null){
						s.close();
					}
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
		}
	}
	
}
