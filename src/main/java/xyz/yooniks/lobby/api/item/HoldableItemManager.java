package xyz.yooniks.lobby.api.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.inventory.ItemBuilder;
import xyz.yooniks.lobby.user.User;

public class HoldableItemManager {

  private final List<HoldableItem> items = new ArrayList<>();

  public void load(LobbyPlugin plugin) {
    final FileConfiguration config = plugin.getConfig();
    outer: for (String itemId : config.getConfigurationSection("holdable-items").getKeys(false)) {

      final ConfigurationSection section = config.getConfigurationSection("holdable-items." + itemId);

      if (!section.getBoolean("enabled")) continue;

      final int slot = section.getInt("slot", 4);

      final HoldableItem item;
      switch (itemId.toLowerCase()) {
        case "switch-visibility-item": {
          final ItemStack visibilityShown = ItemBuilder
              .withSection(section.getConfigurationSection("item-per-state.SHOWN")).build();
          final ItemStack visibilityPremium = ItemBuilder
              .withSection(section.getConfigurationSection("item-per-state.PREMIUM")).build();
          final ItemStack visibilityHidden = ItemBuilder
              .withSection(section.getConfigurationSection("item-per-state.HIDDEN")).build();
          item = new SwitchVisibilityItem(visibilityShown, visibilityPremium, visibilityHidden, slot);
          break;
        }
        case "servers-menu": {
          item = new ServersItem(ItemBuilder.withSection(section).build(), slot, plugin.getServersInventory());
          break;
        }
        case "disco-armor": {
          item = new DiscoArmorItem(ItemBuilder.withSection(section).build(), slot, plugin.getDiscoInventory());
          break;
        }
        case "fly": {
          item = new FlyItem(ItemBuilder.withSection(section).build(), slot);
          break;
        }
        default:
          continue outer;
      }

      this.items.add(item);
    }
  }

  public void putItems(Player player, User user) {
    final Inventory inventory = player.getInventory();
    inventory.clear();

    for (HoldableItem item : this.items) {
      inventory.setItem(item.getSlot(), item.getItem(user));
    }
  }

  public Optional<HoldableItem> findBySlot(int slot) {
    return this.items.stream()
        .filter(item -> item.getSlot() == slot)
        .findFirst();
  }

}
