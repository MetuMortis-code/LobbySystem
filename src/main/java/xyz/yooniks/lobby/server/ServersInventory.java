package xyz.yooniks.lobby.server;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.server.Server;
import xyz.yooniks.lobby.server.ServerManager;

public class ServersInventory {

  private final Inventory inventory;

  private final ServerManager serverManager;
  private final ItemStack fillEmptyBy;
  private final String invName;

  public ServersInventory(Inventory inventory, ServerManager serverManager,
      ItemStack fillEmptyBy, String invName) {
    this.inventory = inventory;
    this.serverManager = serverManager;
    this.fillEmptyBy = fillEmptyBy;
    this.invName = invName;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void update() {
    for (Server server : this.serverManager.getServers()) {

      final ItemStack item = server.getItem();
      final List<String> lore = item.getItemMeta().getLore();

      final ItemMeta meta = item.getItemMeta();
      meta.setLore(lore.stream()
          .map(line -> MessageBuilder.newBuilder(line).withField("{PLAYERS}", String.valueOf(server.getOnline())).coloured().toString())
          .collect(Collectors.toList()));

      item.setItemMeta(meta);

      this.inventory.setItem(server.getSlot(), item);
    }
    if (this.fillEmptyBy != null) {
      for (int i = 0; i < this.inventory.getSize(); i ++) {
        if (this.inventory.getItem(i) != null) continue;
        this.inventory.setItem(i, this.fillEmptyBy);
      }
    }
  }

  public String getInvName() {
    return invName;
  }
}
