package xyz.yooniks.lobby;

import com.keenant.tabbed.Tabbed;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.yooniks.lobby.api.actionbar.ActionbarMessageManager;
import xyz.yooniks.lobby.api.actionbar.task.ActionbarMessagesSender;
import xyz.yooniks.lobby.api.hook.PlaceholderAPIHook;
import xyz.yooniks.lobby.api.inventory.ItemBuilder;
import xyz.yooniks.lobby.api.inventory.PhasmatosBukkitInventoryAPI;
import xyz.yooniks.lobby.api.item.HoldableItemManager;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.chat.ChatHandler;
import xyz.yooniks.lobby.command.ChatCommand;
import xyz.yooniks.lobby.command.SetSpawnCommand;
import xyz.yooniks.lobby.command.SpawnCommand;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.disco.DiscoInventory;
import xyz.yooniks.lobby.disco.setter.DiscoSetter;
import xyz.yooniks.lobby.disco.setter.DefaultDiscoSetter;
import xyz.yooniks.lobby.listener.HoldableItemListener;
import xyz.yooniks.lobby.listener.InventoriesListener;
import xyz.yooniks.lobby.listener.PlayerActionsListener;
import xyz.yooniks.lobby.listener.PlayerChatListener;
import xyz.yooniks.lobby.listener.PlayerJoinQuitListener;
import xyz.yooniks.lobby.listener.UserInitializationListener;
import xyz.yooniks.lobby.queue.QueueSystem;
import xyz.yooniks.lobby.server.ServerCountListener;
import xyz.yooniks.lobby.server.ServerManager;
import xyz.yooniks.lobby.server.ServerOnlineCountUpdater;
import xyz.yooniks.lobby.server.ServersInventory;
import xyz.yooniks.lobby.tablist.TablistUpdater;
import xyz.yooniks.lobby.user.UserManager;

public final class LobbyPlugin extends JavaPlugin {

  private ServerManager serverManager;
  private UserManager userManager;
  private ServersInventory serversInventory;
  private ActionbarMessageManager actionbarMessageManager;
  private HoldableItemManager holdableItemManager;
  private ChatHandler chatHandler;
  private DiscoSetter discoSetter;
  private DiscoInventory discoInventory;
  private QueueSystem queueSystem;

  public static boolean PAPI = false;

  public static Location SPAWN_LOCATION;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    Settings.IMP.reload(new File(this.getDataFolder(), "lobby.yml"));
    SPAWN_LOCATION = (Location) this.getConfig().get("spawn-location", Bukkit.getWorlds().get(0).getSpawnLocation());

    this.serverManager = new ServerManager(this);
    this.serverManager.load();

    this.userManager = new UserManager();

    if (this.getServer().getPluginManager()
        .getPlugin("PlaceholderAPI") != null) {
      PAPI = true;
      new PlaceholderAPIHook(this).register();
      this.getLogger().info("Registered PlaceholderAPI hook");
    }

    this.serversInventory = new ServersInventory(Bukkit.createInventory(null,
        this.getConfig().getInt("servers.inventory-settings.size"),
        MessageBuilder.newBuilder(this.getConfig().getString("servers.inventory-settings.name")).coloured().toString()),
        this.serverManager, this.getConfig().getBoolean("servers.fill-empty.enabled")
        ? ItemBuilder.withSection(this.getConfig().getConfigurationSection("servers.fill-empty")).build() : null, MessageBuilder.newBuilder(this.getConfig().getString("servers.inventory-settings.name")).coloured().toString());
    this.serversInventory.update();

    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord",
        new ServerCountListener(this.serverManager));

    this.actionbarMessageManager = new ActionbarMessageManager();
    this.chatHandler = new ChatHandler();

    final PhasmatosBukkitInventoryAPI phasmatosBukkitInventoryAPI = new PhasmatosBukkitInventoryAPI();
    phasmatosBukkitInventoryAPI.addInventory(this.discoInventory = DiscoInventory.init(this));
    phasmatosBukkitInventoryAPI.register(this);

    this.discoSetter = new DefaultDiscoSetter();

    this.holdableItemManager = new HoldableItemManager();
    this.holdableItemManager.load(this);

    final Tabbed tabListApi = Settings.IMP.MESSAGES.TABLIST.ENABLED ? new Tabbed(this) : null;

    this.queueSystem = QueueSystem.init(this);

    this.registerEvents(
        new HoldableItemListener(this.holdableItemManager, this.userManager),
        new InventoriesListener(this.serverManager, this.serversInventory, this.queueSystem),
        new PlayerActionsListener(), new PlayerChatListener(this.userManager, this.actionbarMessageManager, this.chatHandler),
        new PlayerJoinQuitListener(this.userManager, this.discoSetter),
        new UserInitializationListener(tabListApi, this.holdableItemManager)
    );

    this.getCommand("chat").setExecutor(new ChatCommand(this.chatHandler));
    this.getCommand("spawn").setExecutor(new SpawnCommand());
    this.getCommand("setspawn").setExecutor(new SetSpawnCommand());

    ServerOnlineCountUpdater.start(this);
    if (tabListApi!=null)
      TablistUpdater.start(this);
    ActionbarMessagesSender.start(this);
    //ExpUpdater.start(this);
  }

  private void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = this.getServer().getPluginManager();
    for (Listener listener : listeners)
      pluginManager.registerEvents(listener, this);
  }

  public QueueSystem getQueueSystem() {
    return queueSystem;
  }

  public DiscoSetter getDiscoSetter() {
    return discoSetter;
  }

  public DiscoInventory getDiscoInventory() {
    return discoInventory;
  }

  public ServersInventory getServersInventory() {
    return serversInventory;
  }

  public ServerManager getServerManager() {
    return serverManager;
  }

  public UserManager getUserManager() {
    return userManager;
  }

  public ActionbarMessageManager getActionbarMessageManager() {
    return actionbarMessageManager;
  }

  public static LobbyPlugin getInstance() {
    return getPlugin(LobbyPlugin.class);
  }

}
