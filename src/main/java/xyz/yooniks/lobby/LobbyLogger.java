package xyz.yooniks.lobby;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyLogger {

  private static final Logger LOGGER = LobbyPlugin.getInstance().getLogger();

  public static void log(Level level, String message, Object... params) {
    LOGGER.log(level, message, params);
  }

  public static void exception(String message, Exception ex) {
    LOGGER.log(Level.WARNING, message, ex);
  }


}
