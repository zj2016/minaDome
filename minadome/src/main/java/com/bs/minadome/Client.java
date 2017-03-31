package com.bs.minadome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client {

	//服务器端的ip地址
	private static final String IP = "127.0.0.1";
	//服务器端监听的端口号
	private static final int PORT = 9009;
	
	public static void main(String[] args) {
		
		//获得一个非阻塞的socket连接
		IoConnector connector = new NioSocketConnector();
		
		//配置日志过滤器
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		//配置编码和解码过滤器，将连入接收的byteBuffer转换为消息POJO 而TextLineCodecFactory是 MINA 提供的一个编解码是，可以方便处理基于文本的协议。
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
		
		//设置连接的超时时间
		connector.setConnectTimeoutMillis(30);
		//设置客户端处理业务逻辑的Handler
		connector.setHandler(new ClientHandler());
		
		//建立连接，指定ip和port的服务器
		ConnectFuture cf = connector.connect(new InetSocketAddress(IP, PORT));
		// 等待连接创建完成
		cf.awaitUninterruptibly();
		
		IoSession session = cf.getSession();
		while (true) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				session.write(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
