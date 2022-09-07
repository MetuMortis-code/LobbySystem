package xyz.yooniks.lobby.api.actionbar.task;

import static xyz.yooniks.lobby.api.inventory.MessageHelper.colored;

import io.github.theluca98.textapi.ActionBar;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.actionbar.ActionbarHelper;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessage;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessageEndable;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessageManager;

public class ActionbarMessagesSender implements Runnable {

  private final ActionbarMessageManager messageManager;

  public ActionbarMessagesSender(ActionbarMessageManager messageManager) {
    this.messageManager = messageManager;
  }

  @Override
  public synchronized void run() {
    for (UUID uuid : this.messageManager.getMessages().keys()) {

      final Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.messageManager.removeMessages(uuid).stream()
            .filter(message -> message instanceof ActionbarMessageEndable)
            .map(message -> (ActionbarMessageEndable) message)
            .forEach(ActionbarMessageEndable::end);
        return;
      }

      final StringBuilder builder = new StringBuilder();
      final Collection<ActionbarMessage> messages = this.messageManager.getMessages().get(uuid);

      for (ActionbarMessage message : messages) {
        builder.append(message.getMessage());
        if (message.hasEnded() || !player.isOnline()) {
          if (message instanceof ActionbarMessageEndable) {
            ((ActionbarMessageEndable) message).end();
          }
          this.messageManager.removeMessage(uuid, message);
          return;
        }
        if (messages.size() > 1) {
          builder.append(" &8&l:: ");
        }
      }
      ActionbarHelper.sendActionBar(colored(builder.toString()), player);
      //new ActionBar(colored(builder.toString())).send(player);
    }
  }

  public static void start(LobbyPlugin plugin) {
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new ActionbarMessagesSender(plugin.getActionbarMessageManager()), 1L, 1L);
  }

}

