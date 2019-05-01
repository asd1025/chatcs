package com.cafe24.chatcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
	private static String SERVER_IP;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;

		try {
			SERVER_IP = InetAddress.getLocalHost().getHostAddress();
			scanner = new Scanner(System.in);
			// 소켓 생성
			socket = new Socket();
			// 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, 6000));
			// reader/writer 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
			while (true) {
				// join 프로토콜
				System.out.print("닉네임>>");
				String nickName = scanner.nextLine();
				System.out.println(nickName);
				pw.println("join:" + nickName);
				pw.flush();
				//String data=br.readLine();
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
			if (!socket.isClosed() && socket != null) {
				socket.isClosed();
			}
		}

	}
}
