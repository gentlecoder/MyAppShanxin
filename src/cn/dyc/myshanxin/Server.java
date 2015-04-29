package cn.dyc.myshanxin;

import java.io.*;
import java.net.*;

public class Server {

	ServerSocket sever;

	public Server(int port) {
		try {
			sever = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void beginListen() {
		while (true) {
			try {
				final Socket socket = sever.accept();

				new Thread(new Runnable() {
					public void run() {
						BufferedReader in;
						try {
							in = new BufferedReader(new InputStreamReader(
									socket.getInputStream(), "UTF-8"));
							PrintWriter out = new PrintWriter(
									socket.getOutputStream());
							while (!socket.isClosed()) {
								String str;
								str = in.readLine();
								out.println("Hello!world!! " + str);
								out.flush();
								if (str == null || str.equals("end"))
									break;
								System.out.println(str);
							}
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}