package com.cafe24.chatcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/*
 * 클라이언트와 채팅 데이터 통신은 ChatServerThread가 한다
 * 클라이언트의 서비스를 직접적으로 처리하는 프로그램으로 클라이언트의 메시지를 연결된 다른
 * 클라이언트에게 전송하는 역할을 함
 * 
 * */
public class ChatServerThread extends Thread {

	/**
	 * 1.스레드의 인스턴스 변수 - 통신을 위한 스트림을 얻어 오기 위해 Socket객체를 저장해야함 - 연결된 클라이언트의 닉네임을 저장하고
	 * 있어야 함
	 */
	private String nickname;
	private Socket socket;
	public static List<PrintWriter> listWriters;
	BufferedReader bufferedReader = null;
	PrintWriter printWriter = null;
 
//
//	public ChatServerThread(Socket socket) {
//		this.socket = socket;
//	}

	public ChatServerThread(Socket socket, List<PrintWriter> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	/**
	 * 2. 요청 처리를 위한 Loop작성 - run 메소드 오버라이딩 - main thread로 부터 전달받은 Socket을 통해 IO
	 * Stream 을 받아오는데, 문자단위처리와 라인단위읽기를 위해 보조 스트림 객체를 생성해서 사용함
	 */
	@Override
	public void run() {
		// 1. Remote Host Information

		// 2. 스트림 얻기
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
			// 3. 요청 처리
			while (true) {
				
				String request = bufferedReader.readLine(); // blocking
				if (request == null) {
					doQuit(printWriter);
					break;
				}

				// 4. 프로토콜 분석
				String[] tokens = request.split(":");
				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], printWriter);
				} else if ("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				} else if ("quit".equals(tokens[0])) {
					doQuit(printWriter);
				} else {
					ChatServer.log("에러: 알수 없는 요청(" + tokens[0] + ")");
				}
			}

		}catch (SocketException e) { 
			//이거 없으면 에러 뜸.. 클라이언트에서 접속 끊을 때 
		}   catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void doQuit(PrintWriter writer) {
		removeWriter(writer);
		broadcast(nickname+"님이 퇴장했습니다.",true);
	}

	private void removeWriter(PrintWriter writer) {
		// 구현하기
		synchronized (listWriters) {
			listWriters.remove(writer);
		}
	}

	private void doMessage(String data) {
		// 구현하기
		broadcast(data,false);
		
	}

	private void doJoin(String nickName, PrintWriter writer) {
		this.nickname = nickName;
		String data = nickName + "님이 입장했습니다.";
		//System.out.println(data);
		broadcast(data,true);
		/* Writer을 Writer pool에 저장 */
		addWriter(writer);
		// ack
		writer.println("채팅방에 입장했습니다.");
		writer.flush();
	}

	private void addWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}

	}

	/***
	 * 서버에 연결된 모든 클라이언트에 메시지를 보내는 메소드 스레드간 공유객체인 listWriters에 접근하기 때문에 동기화 처리를 해줘야함
	 */
	private void broadcast(String data,boolean intro) {
		synchronized (listWriters) {
			for (PrintWriter writer : listWriters) {
				if(intro==false)
				writer.println(nickname+":"+data);
				else writer.println(data);
				writer.flush();
			}
		}
	}

	public static void log(String msg) {
		System.out.println("[ChatServerThread] : " + msg);
	}
}
