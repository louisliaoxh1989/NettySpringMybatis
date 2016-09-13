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

		// 服务类
		ServerBootstrap b = new ServerBootstrap();

		// 创建boss和worker
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// 设置循环线程组事例
			b.group(bossGroup, workerGroup);

			// 设置channel工厂
			b.channel(NioServerSocketChannel.class);

			// 设置管道
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					System.out.println(appHttpServerHandler);
					ch.pipeline().addLast(new HttpServerCodec());
					ch.pipeline().addLast(new HttpObjectAggregator(65536));
					ch.pipeline().addLast(appHttpServerHandler);
				}
			});

			b.option(ChannelOption.SO_BACKLOG, 2048);// 链接缓冲池队列大小
			// 绑定端口
			b.bind(8848).sync();
			System.out.println("start!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
