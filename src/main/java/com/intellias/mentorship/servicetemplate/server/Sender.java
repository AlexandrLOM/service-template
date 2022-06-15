package com.intellias.mentorship.servicetemplate.server;

import com.intellias.mentorship.servicetemplate.server.command.ReadCommand;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {

  private static final Logger LOG = Logger.getLogger(Sender.class.getName());

  private BlockingQueue<byte[]> queue;

  public Sender(BlockingQueue<byte[]> queue) {
    this.queue = queue;
  }

  public void accept(byte[] data) {
    try {
      queue.put(data);
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }

}
