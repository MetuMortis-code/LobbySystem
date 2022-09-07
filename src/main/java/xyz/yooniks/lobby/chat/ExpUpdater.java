package xyz.yooniks.lobby.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.UserManager;

public class ExpUpdater implements Runnable {

  private final UserManager userManager;

  public ExpUpdater(UserManager userManager) {
    this.userManager = userManager;
  }

  @Override
  public void run() {
    for (User user : this.userManager.getUsers()) {

      final Player player = Bukkit.getPlayer(user.getId());
      if (player == null) continue;

      final long lastMessage = user.getLastChatMessage();
      if (lastMessage <= 0L) continue;

      final int delayInMillis = Settings.IMP.MESSAGES.CHAT.MESSAGE_DELAY;
      final long difference = ((user.getLastChatMessage() + delayInMillis) - System.currentTimeMillis());

      if (difference < 0) {
        if (player.getLevel() == 0)
          continue;
        player.setLevel(0);
      }

      else if (difference > 4000) {
        if (player.getLevel() != 4)
          player.setLevel(4);
      }
      else if (difference > 3000) {
        if (player.getLevel() != 3)
          player.setLevel(3);
      }
      else if (difference > 2000) {
        if (player.getLevel() != 2)
          player.setLevel(2);
      }
      else if (difference > 1000) {
        if (player.getLevel() != 1)
          player.setLevel(1);
      }
      else {
        if (player.getLevel() != 0)
          player.setLevel(0);
      }
    }
  }

  public static void start(LobbyPlugin plugin) {
    plugin.getServer().getScheduler().runTaskTimer(plugin, new ExpUpdater(plugin.getUserManager()), 10L, 10L);
  }

}
