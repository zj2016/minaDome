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

	//监听的端口号
	private static final int PORT = 9020;
	
	public static void main(String[] args){
		
		//获得一个非阻塞的socket连接
		IoAcceptor acceptor = new NioSocketAcceptor();
		
		//设置会话缓冲区大小
		acceptor.getSessionConfig().setReadBufferSize(2048);
		//设置会话闲置时间
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		//配置日志过滤器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		//配置编码和解码过滤器，将连入接收的byteBuffer转换为消息POJO 而TextLineCodecFactory是 MINA 提供的一个编解码是，可以方便处理基于文本的协议。
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));

		//设置处理服务器端的业务逻辑的Handler
		acceptor.setHandler(new ServerHandler());
		
		try {
			//绑定服务器端监听的端口号
			acceptor.bind(new InetSocketAddress(PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
