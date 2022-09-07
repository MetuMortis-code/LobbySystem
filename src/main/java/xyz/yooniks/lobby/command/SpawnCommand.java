package xyz.yooniks.lobby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;

public class SpawnCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }
    final Player player = (Player) sender;
    return player.teleport(LobbyPlugin.SPAWN_LOCATION);
  }

}
