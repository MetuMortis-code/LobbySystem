package xyz.yooniks.lobby.listener;

import static xyz.yooniks.lobby.api.inventory.MessageHelper.colored;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.tablist.SimpleTabList;
import com.keenant.tabbed.tablist.TabList;
import io.github.theluca98.textapi.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.config.Settings.MESSAGES.JOIN_QUIT_MESSAGES;
import xyz.yooniks.lobby.disco.setter.DiscoSetter;
import xyz.yooniks.lobby.event.UserInitializationEvent;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.UserManager;

public class PlayerJoinQuitListener implements Listener {

  private final UserManager userManager;
  private final DiscoSetter discoSetter;

  public PlayerJoinQuitListener(UserManager userManager,
      DiscoSetter discoSetter) {
    this.userManager = userManager;
    this.discoSetter = discoSetter;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    final JOIN_QUIT_MESSAGES messages = Settings.IMP.MESSAGES.JOIN_QUIT_MESSAGES;

    if (player.isOp()) {
      event.setJoinMessage(MessageBuilder.newBuilder(messages.ADMIN_JOIN_ALL)
          .withField("{PLAYER}", player.getName())
          .prefix().coloured().toString());
      player.setDisplayName(colored(Settings.IMP.MESSAGES.ADMIN_BAR_PREFIX) + player.getName());
    }
    else if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION)) {
      event.setJoinMessage(MessageBuilder.newBuilder(messages.PREMIUM_JOIN_ALL)
          .withField("{PLAYER}", player.getName())
          .prefix().coloured().toString());
      player.setDisplayName(colored(Settings.IMP.MESSAGES.PREMIUM_BAR_PREFIX) + player.getName());
    }
    else {
      event.setJoinMessage(MessageBuilder.newBuilder(messages.PLAYER_JOIN_ALL)
          .withField("{PLAYER}", player.getName())
          .prefix().coloured().toString());
      player.setDisplayName(colored(Settings.IMP.MESSAGES.NORMAL_BAR_PREFIX) + player.getName());
    }

    player.sendMessage(MessageBuilder.newBuilder(messages.PLAYER_JOIN_PV)
        .withField("{PLAYER}", player.getName())
        .withField("%nl%", "\n")
        .prefix().placeholders(player).coloured().toString());

    new Title(MessageBuilder.newBuilder(messages.JOIN_TITLE).placeholders(player).coloured().toString(),
        MessageBuilder.newBuilder(messages.JOIN_SUBTITLE).placeholders(player).coloured().toString(),
        45, 45, 45).send(player);

    player.teleport(player.getWorld().getSpawnLocation());

    player.setLevel(2021);
    player.setFoodLevel(20);
    player.setHealth(20.0);

    final User user = this.userManager.findOrCreate(player.getUniqueId(), player.getName());
    Bukkit.getPluginManager().callEvent(new UserInitializationEvent(player, user));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    final Player player = event.getPlayer();

    final JOIN_QUIT_MESSAGES messages = Settings.IMP.MESSAGES.JOIN_QUIT_MESSAGES;

    if (player.isOp()) {
      event.setQuitMessage(MessageBuilder.newBuilder(messages.ADMIN_QUIT_ALL)
          .withField("{PLAYER}", player.getName())
          .prefix().placeholders(player).coloured().toString());
    }
    else if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION)) {
      event.setQuitMessage(MessageBuilder.newBuilder(messages.PREMIUM_QUIT_ALL)
          .withField("{PLAYER}", player.getName())
          .prefix().placeholders(player).coloured().toString());
    }
    else {
      event.setQuitMessage(MessageBuilder.newBuilder(messages.PLAYER_QUIT_ALL)
          .withField("{PLAYER}", player.getName())
          .prefix().placeholders(player).coloured().toString());
    }
    final User user = this.userManager.findOrCreate(player.getUniqueId());
    if (user.getDiscoSettings() != null) {
      this.discoSetter.removeArmor(player, user.getDiscoSettings());
    }
  }

}
