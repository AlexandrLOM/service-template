package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.channels.SelectionKey;

public interface Command {

  void execute(SelectionKey key);

}
