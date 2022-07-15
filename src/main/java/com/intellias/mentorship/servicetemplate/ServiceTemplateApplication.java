package com.intellias.mentorship.servicetemplate;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.TcpServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceTemplateApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ServiceTemplateApplication.class, args);

		Server tcpServer = new TcpServer(TcpServerConfig.getBaseConfig("localhost", 8098));
		tcpServer.start();

		byte[] data = tcpServer.handle();
		tcpServer.send(data);

	}

}
