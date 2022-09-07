package xyz.yooniks.lobby.server;

import org.bukkit.Bukkit;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.config.Settings;

public class ServerOnlineCountUpdater implements Runnable {

  private final ServerManager serverManager;
  private final ServersInventory serversInventory;

  public ServerOnlineCountUpdater(ServerManager serverManager,
      ServersInventory serversInventory) {
    this.serverManager = serverManager;
    this.serversInventory = serversInventory;
  }

  @Override
  public void run() {
    if (Bukkit.getOnlinePlayers().size() <= 0) return;

    for (Server server : this.serverManager.getServers()) {
      this.serverManager.requestPlayerCount(server.getName());
    }
    this.serversInventory.update();
  }

  public static void start(LobbyPlugin plugin) {
    final int refreshTime = Settings.IMP.SETTINGS.SERVERS_ONLINE_COUNT_REFRESH_TIME;

    plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin,
        new ServerOnlineCountUpdater(plugin.getServerManager(), plugin.getServersInventory()), refreshTime, refreshTime);
  }

}
