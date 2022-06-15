package com.intellias.mentorship.servicetemplate.server;

public interface Server {

  boolean start();

  boolean stop();

  boolean send(byte[] data);

  void handle(Receiver receiver);

}
