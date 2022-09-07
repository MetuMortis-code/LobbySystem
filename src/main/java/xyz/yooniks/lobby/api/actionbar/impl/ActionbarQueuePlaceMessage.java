package xyz.yooniks.lobby.api.actionbar.impl;

import org.bukkit.Bukkit;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.chat.ChatHandler;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.config.Settings.MESSAGES.CHAT;
import xyz.yooniks.lobby.user.User;

public class ActionbarQueuePlaceMessage extends ActionbarMessageImpl {

  private final int place;
  private final int places;
  private final String targetServer;

  public ActionbarQueuePlaceMessage(long time, String id, User user, int place, int places,
      String targetServer) {
    super(time, id, user);
    this.place = place;
    this.places = places;
    this.targetServer = targetServer;
  }

  @Override
  public boolean hasEnded() {
    return this.time < System.currentTimeMillis();//!this.protectionManager.hasProtection(this.getOwner());
  }

  @Override
  public String getMessage() {
    return MessageBuilder.newBuilder(Settings.IMP.MESSAGES.QUEUE_INFO)
        .withField("{PLACE}", String.valueOf(this.place))
        .withField("{PLACES}", String.valueOf(this.places))
        .withField("{SERVER}", this.targetServer)
        .prefix().coloured().toString();
  }

}
