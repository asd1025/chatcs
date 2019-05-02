package com.cafe24.chatcs.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class ChatClientApp {
	private static String server_ip;
	public static void main(String[] args) {
		String name = null;
		Scanner scanner = new Scanner(System.in);
		BufferedReader br=null;
		PrintWriter pw=null;
		while (true) {

			System.out.println("대화명을 입력하세요.");
			System.out.print(">>> ");
			name = scanner.nextLine();

			if (name.isEmpty() == false) {
				break;
			}

			System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
		}

		 Socket  socket = null;
		// 1. 소켓 만들고
		// 2. iostream
		// 3. join 성공
		 
			try {
				server_ip="0.0.0.0";
				socket = new  Socket();
				socket.connect(new InetSocketAddress(server_ip, ChatServer.PORT));
				
				 br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
				 pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
				
				pw.println("join:" + name);
				pw.flush();
			
		ChatWindow chatWindow= new ChatWindow(name,socket);
		chatWindow.show();
		chatWindow.start();
		
		// keyboard input
		while(true) {
			String input=scanner.nextLine();
			if("quit".equals(input)) {
				pw.println("quit:true");
				break;
			}else {
				pw.println("message:"+input);
			}
		}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(scanner!=null) scanner.close();
				if(!socket.isClosed()&&socket!=null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}

}
