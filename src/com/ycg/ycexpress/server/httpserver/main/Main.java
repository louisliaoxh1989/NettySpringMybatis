package com.ycg.ycexpress.server.httpserver.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ycg.ycexpress.platform.http.appserver.StartServer;
import com.ycg.ycexpress.utils.Common;

public class Main {

	// �������
	public static void main(String[] args) throws Exception {
		checkApplicaitonStatus();
		startDB();
	}

	/**
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
			System.out.println(".....");
			System.exit(0);
		}
	}

}
