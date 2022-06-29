package com.intellias.mentorship.servicetemplate.echo;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import java.util.concurrent.ExecutorService;

public class EchoServer implements Echo, Runnable {

  private Server server;
  private ExecutorService executorService;
  byte[] data;

  public EchoServer(ConfigServer configServer) {
    this.server = new TcpServer(configServer);
    this.executorService = configServer.getExecutorService();
  }

  @Override
  public boolean start() {
    executorService.execute(this);
    return false;
  }

  @Override
  public boolean stop() {
    return false;
  }

  @Override
  public void run() {
    server.start();
    while (true) {
      server.handle(this::echoProcess);
//      String dataString = Base64.getEncoder().encodeToString(data);
//      String answer = "echo - ".concat(dataString);
//      System.out.println(answer);
    }
  }

  public void echoProcess(byte[] data){
    executorService.execute(() -> print(data));
  }

  public void print(byte[] data) {
    String dataString = new String(data);
    String answer = "echo - ".concat(dataString);
    System.out.println(answer);
    server.send(answer.getBytes());
  }
}
