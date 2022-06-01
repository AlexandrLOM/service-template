package com.intellias.mentorship.servicetemplate.server.config;

public interface StorageServer {

  boolean put(byte[] bytes);

  byte[] take();

}
