package com.intellias.mentorship.client;

import com.intellias.mentorship.client.tcp.Client;
import com.intellias.mentorship.client.tcp.TcpClient;
import com.intellias.mentorship.client.tcp.TcpClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientTcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientTcpApplication.class, args);

		Client tcpClient = new TcpClient(TcpClientConfig.getBaseConfig("localhost", 8098));

		String message =  "Hello world !!";

		System.out.println("request: ".concat(message));

		tcpClient.send(message.getBytes());
		byte[] answer = tcpClient.receive();

		System.out.println("response:".concat(new String(answer).trim()));

	}

}
