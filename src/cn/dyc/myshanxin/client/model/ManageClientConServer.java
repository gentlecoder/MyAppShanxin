package cn.dyc.myshanxin.client.model;
import java.util.HashMap;

public class ManageClientConServer {
	private static HashMap hm=new HashMap<Integer,ClientConServerThread>();
	//把创建好的ClientConServerThread放入到hm
	public static void addClientConServerThread(String account,ClientConServerThread ccst){
		hm.put(account, ccst);
	}
	
	//可以通过account取得该线程
	public static ClientConServerThread getClientConServerThread(String i){
		return (ClientConServerThread)hm.get(i);
	}
}
