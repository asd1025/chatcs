package com.cafe24.chatcs.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.cafe24.chatcs.ChatServerThread;

public class ChatServer {
	public static final int PORT=6000;
public static void main(String[] args) {
	ServerSocket serverSocket=null;
	String hostAddress;
	List<PrintWriter> listWriters=new ArrayList<PrintWriter>();

	
	try {
		serverSocket=new ServerSocket();
		hostAddress = InetAddress.getLocalHost().getHostAddress();
		System.out.println(hostAddress+" ............");
		serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
		
		while(true) {
			Socket socket=serverSocket.accept();
			new ChatServerThread(socket, listWriters).start();
		}
	}  catch (SocketException e1) {
		System.out.println("..");
	}  catch (UnknownHostException e1) {
		e1.printStackTrace();
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
}
