package xyz.yooniks.lobby.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;

public class SetSpawnCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    if (!sender.isOp()) {
      sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.NO_PERMISSION)
          .withField("{PERMISSION}","op")
          .prefix().coloured().toString());
      return true;
    }
    final Player player = (Player) sender;
    final Location location = player.getLocation();
    LobbyPlugin.SPAWN_LOCATION = location;
    LobbyPlugin.getInstance().getConfig().set("spawn-location", location);
    LobbyPlugin.getInstance().reloadConfig();

    player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.SET_SPAWN).prefix().coloured().toString());

    return true;
  }

}
