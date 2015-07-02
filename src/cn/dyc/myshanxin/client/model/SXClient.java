package cn.dyc.myshanxin.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.content.Context;
import cn.dyc.myshanxin.client.view.MainShanxin;
import cn.dyc.myshanxin.common.SXMessage;
import cn.dyc.myshanxin.common.SXMessageType;
import cn.dyc.myshanxin.common.User;

public class SXClient {
	private Context context;
	public Socket s;

	public SXClient(Context context) {
		this.context = context;
	}

	public boolean sendLoginInfo(Object obj) {
		boolean b = false;
		try {
			s = new Socket();
			try {
				s.connect(new InetSocketAddress("10.45.34.117", 5469), 2000);
			} catch (SocketTimeoutException e) {
				// 连接服务器超时
				return false;
			}
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(obj);
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			SXMessage ms = (SXMessage) ois.readObject();
			if (ms.getType().equals(SXMessageType.SUCCESS)) {
				// 个人信息
				MainShanxin.myInfo = ms.getContent();
				// 创建一个该账号和服务器保持连接的线程
				ClientConServerThread ccst = new ClientConServerThread(context,
						s);
				// 启动该通信线程
				ccst.start();
				// 加入到管理类中
				ManageClientConServer.addClientConServerThread(
						((User) obj).getusername(), ccst);
				b = true;
			} else if (ms.getType().equals(SXMessageType.FAIL)) {
				b = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean sendRegisterInfo(Object obj) {
		boolean b = false;
		try {
			s = new Socket();
			try {
				s.connect(new InetSocketAddress("10.45.34.117", 5469), 2000);
			} catch (SocketTimeoutException e) {
				// 连接服务器超时
				return false;
			}
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(obj);
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			SXMessage ms = (SXMessage) ois.readObject();
			if (ms.getType().equals(SXMessageType.SUCCESS)) {
				b = true;
			} else if (ms.getType().equals(SXMessageType.FAIL)) {
				b = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}
}
