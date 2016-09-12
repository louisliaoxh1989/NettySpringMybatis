package com.ycg.ycexpress.platform.http.appserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

@Service
public class StartServer {
	
	@Autowired
	private AppHttpServerHandler appHttpServerHandler;

	public void start() {

		ServerBootstrap b = new ServerBootstrap();

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			b.group(bossGroup, workerGroup);

			b.channel(NioServerSocketChannel.class);

			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					System.out.println(appHttpServerHandler);
					ch.pipeline().addLast(new HttpServerCodec());
					ch.pipeline().addLast(new HttpObjectAggregator(65536));
					ch.pipeline().addLast(appHttpServerHandler); 
				}
			});

			b.option(ChannelOption.SO_BACKLOG, 2048);// ���ӻ���ض��д�С

			b.bind(8848).sync();

			System.out.println("start!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
