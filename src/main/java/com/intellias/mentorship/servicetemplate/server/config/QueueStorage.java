package com.intellias.mentorship.servicetemplate.server.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueStorage implements StorageServer {

  private static final Logger LOG = Logger.getLogger(ConfigServer.class.getName());

  private BlockingQueue<byte[]> queue;

  public QueueStorage() {
    queue = new LinkedBlockingDeque();
  }

  @Override
  public boolean put(byte[] bytes) {
    try {
      queue.put(bytes);
      return true;
    } catch (InterruptedException e) {
      LOG.log(Level.INFO, e.getMessage());
      return false;
    }
  }

  @Override
  public byte[] take() {
    try {
      return queue.take();
    } catch (InterruptedException e) {
      LOG.log(Level.INFO, e.getMessage());
      return null;
    }
  }
}
