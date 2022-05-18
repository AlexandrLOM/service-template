package com.intellias.mentorship.servicetemplate.client;

public interface Client {

  boolean init(String host, int port);

  boolean send(byte[] msg);

  byte[] receive();

  void stop();

}
