package cn.dyc.myshanxin.client.view;

import java.io.*;
import java.net.*;

 public class Client {
    static Socket client;
    
    public Client(String site, int port){
        try{
            client = new Socket(site,port);
            System.out.println("Client is created! site:"+site+" port:"+port);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public String sendMsg(String msg){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println(msg);
            out.flush();
            return in.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public void closeSocket(){
        try{
            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{
        
    }

}