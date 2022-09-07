package xyz.yooniks.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessage;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessageManager;
import xyz.yooniks.lobby.api.actionbar.impl.ActionbarChatDelayMessage;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.chat.ChatHandler;
import xyz.yooniks.lobby.chat.ChatState;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.UserManager;

public class PlayerChatListener implements Listener {

  private final UserManager userManager;
  private final ActionbarMessageManager actionbarMessageManager;
  private final ChatHandler chatHandler;

  public PlayerChatListener(UserManager userManager,
      ActionbarMessageManager actionbarMessageManager, ChatHandler chatHandler) {
    this.userManager = userManager;
    this.actionbarMessageManager = actionbarMessageManager;
    this.chatHandler = chatHandler;
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    final Player player = event.getPlayer();

    if (!player.isOp()) {
      if (this.chatHandler.getState() == ChatState.DISABLED) {
        player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_ERROR_DISABLED)
            .prefix().placeholders(player).coloured().toString());
        event.setCancelled(true);
        return;
      }
      if (!player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) && this.chatHandler.getState() == ChatState.PREMIUM) {
        player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_ERROR_PREMIUM)
            .prefix().placeholders(player).coloured().toString());
        event.setCancelled(true);
        return;
      }
    }

    final User user = this.userManager.findOrCreate(player.getUniqueId());

    final long messageDelay = System.currentTimeMillis() - user.getLastChatMessage();

    final int requiredDelay = Settings.IMP.MESSAGES.CHAT.MESSAGE_DELAY;
    if (messageDelay < requiredDelay) {

      player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.MESSAGE_DELAY_CHAT)
          .withField("{TIME}", ChatHandler.TIME_FORMAT.format(((user.getLastChatMessage() + requiredDelay) - System.currentTimeMillis()) / 1000.0))
          .prefix().placeholders(player).coloured().toString());

      event.setCancelled(true);
      return;
    }

    event.setFormat(this.chatHandler.coolFormat(event));
    if (!player.isOp()) {
      user.setLastChatMessage(System.currentTimeMillis());
      this.actionbarMessageManager.addMessage(player, new ActionbarChatDelayMessage(System.currentTimeMillis() + requiredDelay + 1000L, "chat-delay", user));
    }
  }

}
