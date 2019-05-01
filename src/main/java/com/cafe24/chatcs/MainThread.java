package com.cafe24.chatcs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * 서버 소켓을 생성하고 클라이언트의 접속을 받아 쓰레드를 만들어 클라이언트의 처리를 함
 * */
public class MainThread {
	public static final int PORT=6000;
 public static void main(String[] args) {
	 ServerSocket serverSocket=null;
	 Scanner scanner=null;
		List<PrintWriter> listWriters=new ArrayList<PrintWriter>();
	 try {
		 
		 //1. 서버 소켓 생성
		serverSocket=new ServerSocket();
		scanner=new Scanner(System.in);
		//2. 바인딩
		String hostAddress=InetAddress.getLocalHost().getHostAddress();
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
