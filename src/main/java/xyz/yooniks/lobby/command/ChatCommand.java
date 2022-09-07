package xyz.yooniks.lobby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.chat.ChatHandler;
import xyz.yooniks.lobby.chat.ChatState;
import xyz.yooniks.lobby.config.Settings;

public class ChatCommand implements CommandExecutor {

  private final ChatHandler chatHandler;

  public ChatCommand(ChatHandler chatHandler) {
    this.chatHandler = chatHandler;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission("valhallalobby.chat")) {
      sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.NO_PERMISSION)
          .withField("{PERMISSION}", "valhallalobby.chat")
          .prefix().coloured().toString());
      return true;
    }
    if (args.length < 1) {
      sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_CORRECT_USAGE).prefix().coloured().toString());
      return true;
    }
    if (args[0].equalsIgnoreCase("clear")) {
      this.chatHandler.clearChat(sender);
      return true;
    }
    final String stateId = args[0];
    final ChatState state = ChatState.findByAlias(stateId);
    if (state == null) {
      sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CHAT.CHAT_CORRECT_USAGE).prefix().coloured().toString());
      return true;
    }
    this.chatHandler.changeTo(state, sender);
    return true;
  }

}
