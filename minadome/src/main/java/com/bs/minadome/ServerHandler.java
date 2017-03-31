package com.bs.minadome;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler extends IoHandlerAdapter{

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		System.out.println("server messageReceived --- : " + message.toString());
		
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
		System.out.println("server messageSent --- : " + message.toString());
	}
	
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
		System.out.println("server sessionCreated --- : " + session.getRemoteAddress().toString());
	}
	
}
