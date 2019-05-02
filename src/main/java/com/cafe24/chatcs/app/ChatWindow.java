package com.cafe24.chatcs.app;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;

import com.cafe24.chatcs.ChatServerThread;

public class ChatWindow  extends Thread {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	private Socket socket;
	BufferedReader br=null;
	PrintWriter pw=null;
	public ChatWindow(String name,Socket socket) {
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		this.socket=socket;
	}
	
	private void finish() {
		//socket 정리
		try {
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	@Override
	public void run() {
		
		try {
			br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
			while(true) {
				String data=br.readLine();
				if(data==null) {
					System.out.println("closed by client");
					break;
				}
				updateTextArea(data);
				
			}
		}catch (SocketException e) {
			pw.write(frame+"님이 퇴장하셨습니다");
			pw.flush();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			//@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage();
			}
		});

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char keyCode=e.getKeyChar();
				if(keyCode== KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
		
		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			// exit
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}
	private void updateTextArea(String message) {
		textArea.append(message);
		textArea.append("\n");
	}
	private void sendMessage() {
		String message=textField.getText();
//		pw 메시지 보내기
		if("quit".equals(message)) {
			pw.println("quit:true");
			pw.flush();
			finish();
		}else pw.println("message:"+message);
		textField.setText("");
		textField.requestFocus();
	}
}
