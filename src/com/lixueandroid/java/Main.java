package com.lixueandroid.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {  
    private static final int PORT = 9999;  
    private List<Socket> mList = new ArrayList<Socket>();  
    private ServerSocket server = null;  
    private ExecutorService mExecutorService = null; //线程池  
      
    public static void main(String[] args) {  
        new Main();  
    }  
    public Main() {  
        try {  
            server = new ServerSocket(PORT);  
            mExecutorService = Executors.newCachedThreadPool();  //创建一个线程池
            System.out.print("server start ...");  
            Socket client = null;  
            while(true) {  
                client = server.accept();  
                mList.add(client);  
                mExecutorService.execute(new Service(client)); //开户新的线程处理连接
            }  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    class Service implements Runnable {  
            private Socket socket;  
            private BufferedReader in = null;  
            private String msg = "";  
              
            public Service(Socket socket) {  
                this.socket = socket;  
                try {  
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                    msg = "user" +this.socket.getInetAddress() + "come toal:"  
                        +mList.size();  
                    this.sendmsg();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                  
            }  
  
            @Override  
            public void run() {  
                try {  
                    while(true) {  
                        if((msg = in.readLine())!= null) {  
                            if(msg.equals("exit")) {  
                                System.out.println("ssssssss");  
                                mList.remove(socket);  
                                in.close();  
                                msg = "user:" + socket.getInetAddress()+ "exit total:" + mList.size();  
                                socket.close();  
                                this.sendmsg();  
                                break;  
                            } else {  
                                msg = socket.getInetAddress() + ":" + msg;  
                                this.sendmsg();  
                            }  
                        }  
                    }  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            
           public void sendmsg() {  
               System.out.println(msg);  
               int num =mList.size();  
               for (int index = 0; index < num; index ++) {  
                   Socket mSocket = mList.get(index);  
                   PrintWriter pout = null;  
                   try {  
                       pout = new PrintWriter(new BufferedWriter(  
                               new OutputStreamWriter(mSocket.getOutputStream())),true);  
                       pout.println(msg);  
                   }catch (IOException e) {  
                       e.printStackTrace();  
                   }  
               }  
           }  
        }      
}  