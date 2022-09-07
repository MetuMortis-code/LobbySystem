package xyz.yooniks.lobby.user.premium;

import java.util.Optional;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.yooniks.lobby.disco.DiscoType;
import xyz.yooniks.lobby.helper.ColorUtil;
import xyz.yooniks.lobby.disco.setter.DiscoSetter;

public class DiscoSettings {

  private DiscoType discoType = DiscoType.NONE;
  private Color lastColor;

  private BukkitTask bukkitTask;

  public Color findNextColor() {
    if (this.discoType == null) {
      this.discoType = DiscoType.RANDOM;
    }
    switch (this.discoType) {
      case RANDOM:
      case ULTRA:
      case FAST: {
        return ColorUtil.randomColor();
      }
      case SMOOTH: {
        final Color color = ColorUtil.nextColor(this.lastColor);
        this.lastColor = color;
        return color;
      }
      default:
        return null;
    }
  }

  public Optional<BukkitTask> findTask() {
    return Optional.ofNullable(this.bukkitTask);
  }

  public void makeTask(BukkitTask bukkitTask) {
    this.bukkitTask = bukkitTask;
  }

  public DiscoType currentDisco() {
    return this.discoType;
  }

  public void changeDisco(Player player, DiscoType disco, DiscoSetter discoSetter) {
    if (disco == DiscoType.SMOOTH) {
      this.lastColor = Color.fromRGB(255, 0, 0);
    }
    this.discoType = disco;
    this.findTask().ifPresent(BukkitTask::cancel);
    discoSetter.createArmor(player, this, this.delayByDisco());
  }

  public Color lastColor() {
    return this.lastColor;
  }

  public long delayByDisco() {
    if (this.discoType == null) {
      this.discoType = DiscoType.RANDOM;
    }
    switch (this.discoType) {
      case SMOOTH:
      case ULTRA: {
        return 5L;
      }
      case FAST: {
        return 10L;
      }
      case RANDOM:
      default: {
        return 15L;
      }
    }
  }

}
