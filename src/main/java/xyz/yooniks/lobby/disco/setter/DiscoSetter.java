package xyz.yooniks.lobby.disco.setter;

import org.bukkit.entity.Player;
import xyz.yooniks.lobby.user.premium.DiscoSettings;

public interface DiscoSetter {

  void createArmor(Player player, DiscoSettings settings, long delay);

  void sendNormalArmor(Player player, DiscoSettings settings);

  void removeArmor(Player player, DiscoSettings settings);

}
