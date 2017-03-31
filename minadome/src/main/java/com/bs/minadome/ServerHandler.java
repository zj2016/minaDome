package com.bs.minadome;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler extends IoHandlerAdapter{

	//所有连接到服务器的session集合
	private final Map<String, IoSession> sessionMap = Collections.synchronizedMap(new HashMap<String, IoSession>());
	
	/**
	 * 服务器接收到客户端发送过来的消息
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		broadcast(message.toString(), session);
	}
	
	/**
	 * 发送消息
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println(message.toString());
	}
	
	/**
	 * 新连接session创建
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}
	
	/**
	 * client关闭连接
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}
	
	/**
	 * 广播方法，将消息广播发送到没有个客户端
	 * @param msg	消息体
	 * @param exceptSession	排除的客户端session
	 */
	private void broadcast(String msg, IoSession exceptSession){
		synchronized (sessionMap) {
			
			String[] m = msg.split("-");
			String key = m[0];
			msg = m[1];
			
			if (!sessionMap.containsValue(exceptSession)){
				//第一次，则注册
				sessionMap.put(key, exceptSession);
				exceptSession.write("[SERVER] " + "注册成功");
				return;
			}
			
			IoSession session = sessionMap.get(key);
			if (session == null) {
				exceptSession.write("[SERVER] " + "目标不存在");
			}
			
			String exceptKey = null;
			for (Entry<String, IoSession> entry : sessionMap.entrySet()) {
				if (entry.getValue().equals(exceptSession)) {
					exceptKey = entry.getKey();
				}
			}
			session.write("[" + exceptKey + "] " + msg);
		}
	}
	
}
