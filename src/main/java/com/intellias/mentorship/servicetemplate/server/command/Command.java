package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public interface Command {

  void execute(SelectionKey key);

}
