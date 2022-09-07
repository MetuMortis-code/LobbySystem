package xyz.yooniks.lobby.chat;

import java.util.Arrays;
import java.util.List;
import xyz.yooniks.lobby.config.Settings;

public enum ChatState {

  ENABLED(Settings.IMP.MESSAGES.CHAT.CHAT_ON, "on", "wlacz", "enable", "enabled"),
  PREMIUM(Settings.IMP.MESSAGES.CHAT.CHAT_PREMIUM, "premium", "vip", "admin"),
  DISABLED(Settings.IMP.MESSAGES.CHAT.CHAT_OFF, "off", "wylacz", "disable", "disabled");

  String message;
  List<String> aliases;

  ChatState(String message, String... aliases) {
    this.message = message;
    this.aliases = Arrays.asList(aliases);
  }

  public String getMessage() {
    return message;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public static ChatState findByAlias(String alias) {
    alias = alias.toLowerCase();
    for (ChatState state : values()) {
      if (state.getAliases().contains(alias))
        return state;
    }
    return null;
  }
}
