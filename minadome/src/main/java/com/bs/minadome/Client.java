package com.bs.minadome;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client {

	public static void main(String[] args) {
		
		IoConnector connector = new NioSocketConnector();
		
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
		
		connector.setConnectTimeoutMillis(30);
		
		connector.setHandler(new ClientHandler());
		
		ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 9009));
		cf.awaitUninterruptibly();
		
		cf.getSession().write("holle server");
		
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		
		connector.dispose();
		
	}
	
}
