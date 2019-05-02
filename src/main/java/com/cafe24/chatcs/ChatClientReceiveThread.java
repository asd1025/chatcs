package com.cafe24.chatcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;

/**
 * 
 * 서버에서 수신된 데이터를 콘솔에 출력하는 일을 담당
 *
 */
public class ChatClientReceiveThread extends Thread {
	private Socket socket;
	private String nick;
	public ChatClientReceiveThread(String nick,Socket socket) {
		this.socket = socket;
		this.nick=nick;
	}

	@Override
	public void run() {
		try {
			
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			
			while(true) {
				String data=br.readLine();
				if(data==null) {
					System.out.println("closed by client");
					break;
				}
				System.out.println(data);
				
			}
		
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (socket != null && !socket.isClosed())
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
