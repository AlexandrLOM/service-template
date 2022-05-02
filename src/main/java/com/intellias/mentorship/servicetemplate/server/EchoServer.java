package com.intellias.mentorship.servicetemplate.server;


import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EchoServer {

  private static final String DISCONNECT_PHRASE = "stop_server";

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private ByteBuffer buffer;

  @Value("${app.echo.server.url}")
  private String url;
  @Value("${app.echo.server.port}")
  private Integer port;

  @PostConstruct
  private void init() throws IOException {
    System.out.println("init EchoServer");
    this.selector = Selector.open();
    this.serverSocket = ServerSocketChannel.open();
    this.serverSocket.bind(new InetSocketAddress(url, port));
    this.serverSocket.configureBlocking(false);
    this.serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    this.buffer = ByteBuffer.allocate(256);
  }

  public void startServer() throws IOException {
    while (true) {
      System.out.println("Start EchoServer");
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> iter = selectedKeys.iterator();
      while (iter.hasNext()) {

        SelectionKey key = iter.next();

        if (key.isAcceptable()) {
          register(selector, serverSocket);
        }

        if (key.isReadable()) {
          answerWithEcho(buffer, key);
        }
        iter.remove();
      }
    }
  }

  private static void answerWithEcho(ByteBuffer buffer, SelectionKey key)
      throws IOException {

    SocketChannel client = (SocketChannel) key.channel();
    client.read(buffer);
    if (new String(buffer.array()).trim().equals(DISCONNECT_PHRASE)) {
      client.close();
      System.out.println("Not accepting client messages anymore");
    } else {
      buffer.flip();
      client.write(buffer);
      buffer.clear();
    }
  }

  private static void register(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {

    SocketChannel client = serverSocket.accept();
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
  }

  public static Process start() throws IOException, InterruptedException {
    String javaHome = System.getProperty("java.home");
    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
    String classpath = System.getProperty("java.class.path");
    String className = EchoServer.class.getCanonicalName();

    ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);

    return builder.start();
  }

}
