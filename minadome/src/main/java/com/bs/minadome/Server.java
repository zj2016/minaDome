package com.bs.minadome;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {

	private static final int PORT = 9009;
	
	public static void main(String[] args) throws IOException {
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());

		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));

		acceptor.setHandler(new ServerHandler());
		
		acceptor.bind(new InetSocketAddress(PORT));
		
		acceptor.bind();
		
	}
	
}
