package de.cuzim1tigaaa.easter.utils.progress;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.*;

import java.util.UUID;

public class Display {

	private static final EasterEggs plugin = EasterEggs.getPlugin(EasterEggs.class);

	@Getter
	private final UUID uuid;
	private final BossBar bossBar;
	private int remainingTime;
	private Integer taskId;

	public Display(UUID uuid) {
		this.uuid = uuid;
		this.bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
	}

	private void showProgressDisplay() {
		taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			if(remainingTime <= 0) {
				bossBar.setVisible(false);
				Bukkit.getScheduler().cancelTask(taskId);
				taskId = null;
				return;
			}

			remainingTime--;
		}, 0L, 20L).getTaskId();

		bossBar.setVisible(true);
		bossBar.addPlayer(Bukkit.getPlayer(uuid));
	}

	public void extendProgressDisplay() {
		remainingTime = 10;
		if(taskId == null)
			this.showProgressDisplay();

		int found = plugin.getProgressUtils().getFoundEggs(uuid);
		int total = EggUtils.getEggs().size();
		double progress = (double) found / total;
		bossBar.setProgress(progress);

		if(progress > .75) {
			bossBar.setColor(BarColor.GREEN);
			bossBar.setTitle(ColorLib.format("&a%d&7/&a%d gefunden", found, total));
		} else if(progress > .5) {
			bossBar.setColor(BarColor.BLUE);
			bossBar.setTitle(ColorLib.format("&b%d&7/&b%d gefunden", found, total));
		} else if(progress > .25) {
			bossBar.setColor(BarColor.YELLOW);
			bossBar.setTitle(ColorLib.format("&e%d&7/&e%d gefunden", found, total));
		} else {
			bossBar.setColor(BarColor.RED);
			bossBar.setTitle(ColorLib.format("&c%d&7/&c%d gefunden", found, total));
		}
	}
}