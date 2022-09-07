package xyz.yooniks.lobby.config;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Settings extends Config {

  @Ignore
  public static final Settings IMP = new Settings();


  @Comment({
      "Lobby system - cool lobby system for your server!",
      "Made by yooniks, discord: yooniks#0289"
  })

  @Create
  public MESSAGES MESSAGES;

  public void reload(File file) {
    load(file);
    save(file);
  }

  @Comment("Do not use '\\ n', use %nl%")
  public static class MESSAGES {

    public String PREFIX = "&6&lLobby&e&lSystem&7:";

    public String PREMIUM_PERMISSION = "lobby.premium";

    public String CONNECTING_MESSAGE = "{PREFIX} &7Connecting to server &f{SERVER}&f..";
    public String COULD_NOT_CONNECT = "{PREFIX} &7Could not connect to server &f{SERVER}&7, reason: &f{REASON}";

    public String SET_SPAWN = "{PREFIX} &7You have set new &fspawn&7 location.";
    public String NO_PERMISSION = "{PREFIX} &7You have no permission to do that! &8(&f{PERMISSION}&8)";

    public String ARMOR_NAME = "&6&lLobby&e&lSystem";

    public String NORMAL_BAR_PREFIX = "&7";
    public String PREMIUM_BAR_PREFIX = "&b";
    public String ADMIN_BAR_PREFIX = "&c";


    @Create
    public CHAT CHAT;

    public static class CHAT {
      public String FORMAT = "{PREFIX}{PLAYER}&8: {SUFFIX}{MESSAGE}";

      public String PREFIX_ADMIN = "&c";
      public String PREFIX_PREMIUM = "&e";
      public String PREFIX_NORMAL = "&7";
      public String SUFFIX_ADMIN = "&e";
      public String SUFFIX_PREMIUM = "&7";
      public String SUFFIX_NORMAL = "&7";

      public String MESSAGE_DELAY_CHAT = "{PREFIX} &cPlease wait &6{TIME}s&c before next message";
      public String MESSAGE_DELAY_ACTIONBAR = "&cYou can make new message in: &6{TIME}s";
      public String MESSAGE_DELAY_EXPIRED = "&cNow you can use the chat!";

      @Comment("Messaging delay, in milliseconds. 1000ms = 1 second")
      public int MESSAGE_DELAY = 5000;

      public String CHAT_CLEAR = "{PREFIX} &7Player &f{PLAYER} &7has cleared the chat!%nl%";
      public String CHAT_ON = "{PREFIX} &7Player &f{PLAYER} &7has &eenabled&7 the chat!";
      public String CHAT_OFF = "{PREFIX} &7Player &f{PLAYER} &7has &cdisabled&7 the chat!";
      public String CHAT_PREMIUM = "{PREFIX} &7Player &f{PLAYER} &7has changed chat type to premium only!";

      public String CHAT_CORRECT_USAGE = "{PREFIX} &7Correct usage: &f/chat [clear/on/off/premium]";

      public String CHAT_ERROR_DISABLED = "{PREFIX} &7Chat is currently &fdisabled&7!";
      public String CHAT_ERROR_PREMIUM = "{PREFIX} &7Chat is currently available only for &fpremium&7 players!";

    }

    public String FLY_ENABLED = "{PREFIX} &7Fly has been &fenabled&7!";
    public String FLY_DISABLED = "{PREFIX} &7Fly has been &fdisabled&7!";

    public String FLY_NO_PERMISSION = "{PREFIX} &7You have to be &fpremium &7player to use it!";

    public String QUEUE_INFO = "&cYou are &e{PLACE} &cin queue to join &e{SERVER}";
    public String QUEUE_SKIP = "{PREFIX} &7You are &fpremium&7 player, you don't have to wait in queue!";

    @Create
    public JOIN_QUIT_MESSAGES JOIN_QUIT_MESSAGES;

    public static class JOIN_QUIT_MESSAGES {

      public String JOIN_TITLE = "&8&l&m= &e&lVAL&6&lHALLA &8&l&m=";
      public String JOIN_SUBTITLE = "&7Welcome and have &ffun&7!";
      public String PLAYER_JOIN_ALL = "{PREFIX} &f{PLAYER} &7has joined!";
      public String PLAYER_QUIT_ALL = "{PREFIX} &f{PLAYER} &7has left!";

      public String PREMIUM_JOIN_ALL = "{PREFIX} &8(&6PREMIUM&8) &7{PLAYER} &7has joined!";
      public String PREMIUM_QUIT_ALL = "{PREFIX} &8(&6PREMIUM&8) &7{PLAYER} &7has left!";

      public String ADMIN_JOIN_ALL = "{PREFIX} &8(&cAdmin&8) &7{PLAYER} &7has joined!";
      public String ADMIN_QUIT_ALL = "{PREFIX} &8(&cAdmin&8) &7{PLAYER} &7has left!";

      public String PLAYER_JOIN_PV = "%nl%%nl%%nl%%nl%%nl%&7Welcome, &f{PLAYER}&7!%nl%&7We hope you will &fenjoy&7 here!%nl%&7Our website: &fwebsite.com";
    }

    @Create
    public VISIBILITY VISIBILITY;

    public static class VISIBILITY {
      public String SHOWN_ALL = "{PREFIX} &7Now all players are &fshown&7!";
      public String SHOWN_PREMIUM = "{PREFIX} &7Now you can see only &fpremium&7 players!";
      public String HIDDEN = "{PREFIX} &7Now all players are &fhidden&7!";

    }

    @Create
    public TABLIST TABLIST;

    public static class TABLIST {

      public boolean ENABLED = true;

      public String ENTRY = "   {PREFIX}{PLAYER} ";
      public String ENTRY_PREMIUM_PREFIX = "&e";
      public String ENTRY_ADMIN_PREFIX = "&c";
      public String ENTRY_NORMAL_PREFIX = "&7";

      public String HEADER = "&6&lLobby&e&lSystem%nl%&7Your minecraft network!%nl%&7You are currently playing on &cHUB-1";
      public String FOOTER = "&7Website: &6www.website.com &7TS3: &6ts3.eu%nl%&7Ping: &f%player_ping%&7ms &8| &7Online: &e%bungee_total% players &8| &a%server_time_HH:mm:ss%";

      public String NORMAL_PLAYERS = "   &7Players:  ";
      public String PREMIUM_PLAYERS = "  &7Premium players:  ";

      public List<String> THIRD_COLUMN = Arrays
          .asList("", "", "", "&7Online players&8: &e%bungee_total%", "&7Lobby&8: &e%server_online%", "&7Survival&8: &e%bungee_survival%", "", "", "", "", "", "  &7Enjoy the game  ", "", "", "",
              "", "", "", "", "");
      public List<String> FOUR_COLUMN = Arrays
          .asList("", "", "", "&7TPS&8: &e%server_tps%","", "", "", "", "", "", "", "&7 and have fun !!!", "", "", "", "",
              "", "", "", "", "");
    }

    @Create
    public DISCO_ARMOR DISCO_ARMOR;

    public static class DISCO_ARMOR {
      public String CHANGED_MODE = "{PREFIX} &7You have changed disco armor mode to: &f{MODE}";
      public String DISABLED = "{PREFIX} &7You have &cdisabled &7disco armor!";
      public String NO_PERMISSION = "{PREFIX} &7You have to be &fpremium &7player to use it!";
    }
  }

  @Create
  public SETTINGS SETTINGS;

  public static class SETTINGS {

    @Comment("Refresh servers online players count every 10 seconds")
    public int SERVERS_ONLINE_COUNT_REFRESH_TIME = 10;
  }

}