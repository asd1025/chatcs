package com.cafe24.chatcs;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/*
 * 서버 소켓을 생성하고 클라이언트의 접속을 받아 쓰레드를 만들어 클라이언트의 처리를 함
 * */
public class MainThread {
	public static final int PORT=6000;
 public static void main(String[] args) {
	 List<PrintWriter> listWriters=new ArrayList<PrintWriter>();
	 ServerSocket serverSocket=null;
	 try {
		 
		 //1. 서버 소켓 생성
		serverSocket=new ServerSocket();
		//2. 바인딩
		String hostAddress="0.0.0.0";
		serverSocket.bind(new InetSocketAddress(hostAddress,PORT));
		log("연결 기다림 "+hostAddress+" : "+PORT);
		//3. 요청대기
		while(true) {
			//wait for connecting ( accept )
			Socket socket=serverSocket.accept();
			new ChatServerThread(socket,listWriters).start();
 		}
		 
	}catch (SocketException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}finally {
	
			try {
				if(!serverSocket.isClosed()&&serverSocket!=null) {
				serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	 
}
 public static void log(String msg) {
	 System.out.println("[Main Thread]: "+msg);
 }
}
