package xyz.yooniks.lobby.disco;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.inventory.ItemBuilder;
import xyz.yooniks.lobby.api.inventory.PhasmatosClickableChangeableInventory;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.UserManager;
import xyz.yooniks.lobby.user.premium.DiscoSettings;

public class DiscoInventory extends PhasmatosClickableChangeableInventory {

  private final Map<Integer, DiscoType> discoBySlots = new HashMap<>();
  private final UserManager userManager;

  public DiscoInventory(String title, int size, UserManager userManager) {
    super(title, size);
    this.userManager = userManager;
  }

  @Override
  public ItemStack updateItem(ItemStack item, int slot, Player player) {
    if (this.discoBySlots.containsKey(slot) && this.discoBySlots.get(slot) == DiscoType.CURRENT) {
      final User user = this.userManager.findOrCreate(player.getUniqueId());

      if (user.getDiscoSettings() == null) {
        if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) || player.isOp()) {
          user.setDiscoSettings(new DiscoSettings());
        }
      }

      final String current = user.getDiscoSettings().currentDisco() == null ? "none"
          : user.getDiscoSettings().currentDisco().name();

      final ItemStack cloned = item.clone();
      final ItemMeta meta = cloned.getItemMeta();
      final List<String> lore = meta.getLore();
      if (lore != null) {
        meta.setLore(lore.stream()
            .map(line -> MessageBuilder.newBuilder(line).withField("{CURRENT}",
                user.getDiscoSettings().currentDisco() == null ? "none"
                    : user.getDiscoSettings().currentDisco().name())
                .coloured().toString()).collect(Collectors.toList()));
      }
      meta.setDisplayName(
          MessageBuilder.newBuilder(meta.getDisplayName()).withField("{CURRENT}", current)
              .toString());
      cloned.setItemMeta(meta);
      return cloned;
    }
    return item;
  }

  public static DiscoInventory init(LobbyPlugin plugin) {
    final DiscoInventory inventory = new DiscoInventory(
        MessageBuilder.newBuilder(plugin.getConfig().getString("disco-armor.inventory-name")).coloured().toString(),
        plugin.getConfig().getInt("disco-armor.inventory-size"), plugin.getUserManager());

    for (String id : plugin.getConfig().getConfigurationSection("disco-armor.items").getKeys(false)) {
      final ConfigurationSection data = plugin.getConfig().getConfigurationSection("disco-armor.items." + id);
      final int slot = data.getInt("slot");

      final ItemStack item = ItemBuilder.withSection(data).build();
      inventory.addItem(slot, item);

      if (data.isString("mode")) {
        final DiscoType discoType = DiscoType.findByName(data.getString("mode"));
        inventory.discoBySlots.put(slot, discoType);

        if (discoType != DiscoType.CURRENT) {
          inventory.addItemAction(slot, (player) -> {
            player.closeInventory();
            if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) || player.isOp()) {
              final User user = plugin.getUserManager().findOrCreate(player.getUniqueId());
              if (user.getDiscoSettings() == null) {
                user.setDiscoSettings(new DiscoSettings());
              }
              if (discoType == DiscoType.DISABLE) {
                plugin.getDiscoSetter().removeArmor(player, user.getDiscoSettings());
                player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.DISCO_ARMOR.DISABLED).prefix().placeholders(player).coloured().toString());
                return;
              }
              user.getDiscoSettings().changeDisco(player, discoType, plugin.getDiscoSetter());
              player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.DISCO_ARMOR.CHANGED_MODE)
                  .withField("{MODE}", discoType.name()).prefix().placeholders(player).coloured().toString());
            }
          });
        }
      }
    }

    final ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
    empty.setDurability((short) 7);
    empty.getItemMeta().setDisplayName(" ");

    for (int i = 0; i < inventory.getSize(); i ++) {
      if (inventory.getItems().get(i) == null) {
        inventory.addItem(i, empty);
      }
    }
    return inventory;
  }

}
