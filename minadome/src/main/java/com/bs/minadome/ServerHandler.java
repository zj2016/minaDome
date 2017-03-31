package com.bs.minadome;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler extends IoHandlerAdapter{

	//所有连接到服务器的session集合
	private final Set<IoSession> sessionSet = Collections.synchronizedSet(new HashSet<IoSession>());
	
	/**
	 * 服务器接收到客户端发送过来的消息
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("server messageReceived --- : " + message.toString());
		broadcast(message.toString(), session);
	}
	
	/**
	 * 发送消息
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("server messageSent --- : " + message.toString());
	}
	
	/**
	 * 新连接session创建
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		sessionSet.add(session);
		broadcast("create", session);
		System.out.println("server sessionCreated --- : " + session.getRemoteAddress().toString());
	}
	
	/**
	 * client关闭连接
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		sessionSet.remove(session);
		broadcast("close", session);
		System.out.println("server sessionClosed --- : " + session.getRemoteAddress().toString());
	}
	
	/**
	 * 广播方法，将消息广播发送到没有个客户端
	 * @param msg	消息体
	 * @param exceptSession	排除的客户端session
	 */
	private void broadcast(String msg, IoSession exceptSession){
		synchronized (sessionSet) {
			for (IoSession session : sessionSet) {
				if (session.isConnected()) {
					if (session.equals(exceptSession)) {
						session.write("[SERVER -> ME] " + msg);
					} else {
						session.write("[SERVER -> CLIENT] " + msg);
					}
				}
			}
		}
	}
	
}
