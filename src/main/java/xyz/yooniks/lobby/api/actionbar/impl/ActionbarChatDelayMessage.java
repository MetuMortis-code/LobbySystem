package xyz.yooniks.lobby.api.actionbar.impl;

import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.chat.ChatHandler;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.config.Settings.MESSAGES.CHAT;
import xyz.yooniks.lobby.user.User;

public class ActionbarChatDelayMessage extends ActionbarMessageImpl {

  public ActionbarChatDelayMessage(long time, String id,
      User user) {
    super(time, id, user);
  }

  @Override
  public boolean hasEnded() {
    return this.time < System.currentTimeMillis();//!this.protectionManager.hasProtection(this.getOwner());
  }

  @Override
  public String getMessage() {
    final CHAT chat = Settings.IMP.MESSAGES.CHAT;
    final long difference = System.currentTimeMillis() - this.getUser().getLastChatMessage();
    if (difference > chat.MESSAGE_DELAY) {
      return MessageBuilder.newBuilder(chat.MESSAGE_DELAY_EXPIRED).toString();
    }

    return MessageBuilder.newBuilder(chat.MESSAGE_DELAY_ACTIONBAR).withField("{TIME}",
        ChatHandler.TIME_FORMAT.format(((this.getUser().getLastChatMessage() + chat.MESSAGE_DELAY) - System.currentTimeMillis()) / 1000.0)).toString();
  }

}
