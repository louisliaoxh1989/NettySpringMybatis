package com.ycg.ycexpress.server.httpserver.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ycg.ycexpress.platform.http.appserver.StartServer;
import com.ycg.ycexpress.utils.Common;

public class Main {

	// 函数入口
	public static void main(String[] args) throws Exception {
		checkApplicaitonStatus();
		startDB();
	}

	/**
	 * 启动Spring集成的数据服务
	 */
	private static void startDB() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath:applicationContext.xml");
			StartServer server = context.getBean(StartServer.class);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void checkApplicaitonStatus() {
		if (Common.getInstance().isApplicaitonAlreadyRunning()) {
			System.out.println("请勿多次打开同一程序, 程序已经运行......");
			System.exit(0);
		}
	}

}
