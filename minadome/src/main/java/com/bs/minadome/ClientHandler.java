package com.bs.minadome;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter{

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		System.out.println("client messageReceived --- : " + message.toString());
		
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
		System.out.println("client messageSent --- : " + message.toString());
	}
	
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
		System.out.println("client sessionCreated --- : " + session.getRemoteAddress().toString());
	}
	
}
