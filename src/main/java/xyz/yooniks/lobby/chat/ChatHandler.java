package xyz.yooniks.lobby.chat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.config.Settings.MESSAGES.CHAT;

public class ChatHandler {

  public static final NumberFormat TIME_FORMAT = new DecimalFormat("#.#");

  private ChatState state = ChatState.ENABLED;

  private final char[] emptyMessage = new char[8270];
  {
    Arrays.fill(emptyMessage, ' ');
  }

  public void setState(ChatState state) {
    this.state = state;
  }

  public ChatState getState() {
    return state;
  }

  public void clearChat(CommandSender executor) {
    final String message = new String(this.emptyMessage);

    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(message);
    }

    Bukkit.broadcastMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_CLEAR)
        .withField("{PLAYER}", executor.getName())
        .prefix().stripped().coloured().toString());
  }

  public void changeTo(ChatState newState, CommandSender executor) {
    this.state = newState;
    if (newState == ChatState.ENABLED) {
      Bukkit.broadcastMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_ON)
          .withField("{PLAYER}", executor.getName())
          .prefix().stripped().coloured().toString());
    }
    if (newState == ChatState.DISABLED) {
      Bukkit.broadcastMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_OFF)
          .withField("{PLAYER}", executor.getName())
          .prefix().stripped().coloured().toString());
    }
    if (newState == ChatState.PREMIUM) {
      Bukkit.broadcastMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_PREMIUM)
          .withField("{PLAYER}", executor.getName())
          .prefix().stripped().coloured().toString());
    }
  }

  public String coolFormat(AsyncPlayerChatEvent event) {

    final Player player = event.getPlayer();
    final CHAT chat = Settings.IMP.MESSAGES.CHAT;

    return MessageBuilder.newBuilder(chat.FORMAT)
        .withField("{PLAYER}", "%1$s")
        .withField("{PREFIX}", this.prefix(player, chat))
        .withField("{SUFFIX}", this.suffix(player, chat))
        .withField("{MESSAGE}", "%2$s")
        .coloured().toString();
  }

  private String prefix(Player player, CHAT chat) {
    if (player.isOp()) {
      return chat.PREFIX_ADMIN;
    }
    return player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) ? chat.PREFIX_PREMIUM : chat.PREFIX_NORMAL;
  }

  private String suffix(Player player, CHAT chat) {
    if (player.isOp()) {
      return chat.SUFFIX_ADMIN;
    }
    return player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) ? chat.SUFFIX_PREMIUM : chat.SUFFIX_NORMAL;
  }

}
