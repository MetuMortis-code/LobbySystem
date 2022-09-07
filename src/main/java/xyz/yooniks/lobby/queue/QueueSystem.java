package xyz.yooniks.lobby.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessageManager;
import xyz.yooniks.lobby.api.actionbar.impl.ActionbarQueuePlaceMessage;
import xyz.yooniks.lobby.server.ServerManager;

public class QueueSystem implements Runnable {

  private final ActionbarMessageManager messageManager;
  private final ServerManager serverManager;

  public QueueSystem(ActionbarMessageManager messageManager,
      ServerManager serverManager) {
    this.messageManager = messageManager;
    this.serverManager = serverManager;
  }

  private final java.util.Queue<Queue> queues = new ConcurrentLinkedQueue<>();

  public int getQueueSize() {
    return this.queues.size() + 1;
  }

  @Override
  public void run() {
    final Queue poll = this.queues.poll();
    if (poll == null) {
      return;
    }

    int place = 0;
    final int places = this.queues.size() + 1;
    for (Queue queue : this.queues) {

      final Player player = queue.getPlayer();
      if (player == null || !player.isOnline()) {
        this.queues.remove(queue);
        continue;
      }

      queue.setPlace(++place);
      this.messageManager.addMessage(player, new ActionbarQueuePlaceMessage(
          System.currentTimeMillis() + 1000L, "queue-info", null, place, places, queue.getTargetServer()));
    }

    final Player player = poll.getPlayer();
    if (player != null) {
      this.messageManager.addMessage(player, new ActionbarQueuePlaceMessage(
          System.currentTimeMillis() + 1000L, "queue-info", null, place, places, poll.getTargetServer()));
      this.serverManager.connect(player, poll.getTargetServer());
    }

  }

  public void addQueue(Queue queue) {
    this.queues.offer(queue);
  }

  public static QueueSystem init(LobbyPlugin plugin) {
    final QueueSystem system = new QueueSystem(plugin.getActionbarMessageManager(), plugin.getServerManager());
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, system, 20L, 20L);
    return system;
  }

}
