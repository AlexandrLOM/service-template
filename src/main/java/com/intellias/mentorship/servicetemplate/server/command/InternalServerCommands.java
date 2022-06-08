package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.channels.SocketChannel;

public class InternalServerCommands {

  private Command accept;
  private Command read;
  private Command write;

  public InternalServerCommands(Command accept, Command read, Command write) {
    this.accept = accept;
    this.read = read;
    this.write = write;
  }

  public void accept(SocketChannel socketChannel){
    accept.execute(socketChannel);
  }

  public void read(SocketChannel socketChannel){
    read.execute(socketChannel);
  }

  public void write(SocketChannel socketChannel){
    write.execute(socketChannel);
  }
}
