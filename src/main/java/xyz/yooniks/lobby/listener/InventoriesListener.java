package xyz.yooniks.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.queue.Queue;
import xyz.yooniks.lobby.queue.QueueSystem;
import xyz.yooniks.lobby.server.ServerManager;
import xyz.yooniks.lobby.server.ServersInventory;

public class InventoriesListener implements Listener {

  private final ServerManager serverManager;
  private final ServersInventory serversInventory;
  private final QueueSystem queueSystem;

  public InventoriesListener(ServerManager serverManager,
      ServersInventory serversInventory, QueueSystem queueSystem) {
    this.serverManager = serverManager;
    this.serversInventory = serversInventory;
    this.queueSystem = queueSystem;
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) return;

    final Inventory inventory = event.getInventory();
    if (inventory == null || !inventory.getTitle().equals(this.serversInventory.getInvName())) {
      return;
    }
      //if (!event.getView().getTitle().equals(this.serversInventory.getInvName())) {
      //    return;
      //}

    event.setCancelled(true);

    if (event.getClickedInventory() instanceof PlayerInventory) return;

    final Player player = (Player) event.getWhoClicked();
    this.serverManager.findBySlot(event.getSlot())
        .ifPresent((server) -> {
          player.closeInventory();
          if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) || player.isOp()) {
            player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.QUEUE_SKIP).prefix().placeholders(player).coloured().toString());
            this.serverManager.connect(player, server.getName());
          }
          else {
            this.queueSystem.addQueue(new Queue(player, server.getName(), this.queueSystem.getQueueSize()));
          }
        });
  }

}
