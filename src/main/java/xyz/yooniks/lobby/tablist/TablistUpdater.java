package xyz.yooniks.lobby.tablist;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.item.BlankTabItem;
import com.keenant.tabbed.item.TabItem;
import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.SimpleTabList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.config.Settings.MESSAGES.TABLIST;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.UserManager;

public class TablistUpdater implements Runnable {

  private final UserManager userManager;

  public TablistUpdater(UserManager userManager) {
    this.userManager = userManager;
  }

  @Override
  public void run() {
    final TABLIST tabConfig = Settings.IMP.MESSAGES.TABLIST;

    for (User user : this.userManager.getUsers()) {

      final Player player = Bukkit.getPlayer(user.getId());
      if (player == null)
        continue;

      if (user.getTabList() == null)
        continue;

      final SimpleTabList tabList = (SimpleTabList) user.getTabList();

      final List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
      final List<Player> normalPlayers = players.stream().filter(online -> !online.isOp() && !online.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION)).collect(Collectors.toList());
      final List<Player> premiumPlayers = players.stream().filter(online -> online.isOp() || online.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION)).collect(Collectors.toList());

      final Map<Integer, TabItem> items = new HashMap<>();
      items.put(1, new TextTabItem(" "));
      items.put(21, new TextTabItem(" "));

      for (int i = 0; i < 20; i++) {
        final String name = normalPlayers.size() > i ? this.entry(normalPlayers.get(i), tabConfig): "";
        items.put(i + 2, new TextTabItem(name));
      }

      for (int i = 0; i < 20; i++) {
        final String name = premiumPlayers.size() > i ? this.entry(premiumPlayers.get(i), tabConfig): "";
        items.put(i + 22, new TextTabItem(name));
      }

      for (int i = 40; i < 60; i ++) {
        items.put(i, new TextTabItem(MessageBuilder.newBuilder(tabConfig.THIRD_COLUMN.get(i - 40)).placeholders(player).coloured().toString()));
      }

      for (int i = 60; i < 80; i ++) {
        items.put(i, new TextTabItem(MessageBuilder.newBuilder(tabConfig.FOUR_COLUMN.get(i - 60)).placeholders(player).coloured().toString()));
      }

      items.put(0, new TextTabItem(MessageBuilder.newBuilder(tabConfig.NORMAL_PLAYERS)
          .withField("{NORMAL-PLAYERS}", String.valueOf(normalPlayers.size())).coloured().toString()));
      items.put(20, new TextTabItem(MessageBuilder.newBuilder(tabConfig.PREMIUM_PLAYERS)
          .withField("{VIP-PLAYERS}", String.valueOf(premiumPlayers.size())).coloured().toString()));

      tabList.set(items);

      tabList.setHeader(MessageBuilder.newBuilder(tabConfig.HEADER)
          .stripped().placeholders(player).coloured().toString());

      tabList.setFooter(MessageBuilder.newBuilder(tabConfig.FOOTER)
          .stripped().placeholders(player).coloured().toString());

      tabList.update();
    }
  }

  private String entry(Player player, TABLIST tablist) {
    final String prefix;
    if (player.isOp()) {
      prefix = tablist.ENTRY_ADMIN_PREFIX;
    }
    else if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION)) {
      prefix = tablist.ENTRY_PREMIUM_PREFIX;
    }
    else {
      prefix = tablist.ENTRY_NORMAL_PREFIX;
    }

    return MessageBuilder.newBuilder(tablist.ENTRY)
        .withField("{PREFIX}", prefix)
        .withField("{PLAYER}", player.getName())
        .placeholders(player)
        .coloured().toString();
  }

  public static void start(LobbyPlugin plugin) {
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new TablistUpdater(plugin.getUserManager()), 20L, 20L);
  }

}
