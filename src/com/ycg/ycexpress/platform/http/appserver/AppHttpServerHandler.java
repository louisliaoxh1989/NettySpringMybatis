package com.ycg.ycexpress.platform.http.appserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycg.ycexpress.beans.User;
import com.ycg.ycexpress.server.UserService;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

@Sharable
@Service
public class AppHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private static final String USERNAME = "name";
	private static final String PASSWORD = "age";
	
	@Autowired
	private UserService userService;

	public AppHttpServerHandler() {
		super();
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {		
		
		String uri;
		String contextPath;
		boolean isKeepAlive;
		Map<String, List<String>> params = null;

		if (msg.getMethod() == HttpMethod.GET){
			
			//ServerLogger.consoleLogger.info("����GET����");
			
			isKeepAlive = HttpHeaders.isKeepAlive(msg);			
			uri = msg.getUri();
			
			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, Charset.forName("UTF-8"));
	
			contextPath = queryStringDecoder.path();
	
			params = queryStringDecoder.parameters();	
		}else{
			//����POST
			//ServerLogger.consoleLogger.info("����POST����");
			
			isKeepAlive = HttpHeaders.isKeepAlive(msg);
			uri = msg.getUri();
			contextPath = uri;
			
			params = new HashMap<String, List<String>>();
			
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
					new DefaultHttpDataFactory(false), msg);			
			List<InterfaceHttpData> list = decoder.getBodyHttpDatas();
			
			System.out.println(list);
			
			List<String> postData = null;
			for(InterfaceHttpData ifh: list){
				String name = ifh.getName();
				InterfaceHttpData data = decoder.getBodyHttpData(name);
				
				Attribute attribute = (Attribute) data;
				String question = attribute.getValue();
//				System.out.println(question);
				if(params.get(name)==null){
					postData = new ArrayList<String>();
				}
				postData.add(question);
				
				params.put(name, postData);
				
			}
			
			String name = ParamUtil.getParameterByName(USERNAME, params);
			String password = ParamUtil.getParameterByName(PASSWORD, params);
			User user = new User();
			user.setName(name);
			user.setPassWord(password);
			userService.save(user);
			
			FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer("�ɹ�".getBytes("UTF-8")));
			fullHttpResponse.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
			fullHttpResponse.headers().set(CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
			fullHttpResponse.headers().set("Access-Control-Allow-Origin", "*");
			ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
		}		

	}

//	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//		if (evt instanceof IdleStateEvent) {
//			ctx.close();
//		}
//	}
	

}
