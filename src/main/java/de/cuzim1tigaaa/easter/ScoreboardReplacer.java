package de.cuzim1tigaaa.easter;

import com.github.games647.scoreboardstats.ScoreboardStats;
import com.github.games647.scoreboardstats.variables.ReplaceManager;
import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.progress.PlayerProgress;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardReplacer {

	private final EasterEggs plugin;

	public ScoreboardReplacer(EasterEggs plugin) {
		this.plugin = plugin;

		if(!Bukkit.getPluginManager().isPluginEnabled("ScoreboardStats"))
			return;

		ScoreboardStats scoreboardStats = EasterEggs.getPlugin(ScoreboardStats.class);
		registerReplacer(scoreboardStats.getReplaceManager());
	}

	private void registerReplacer(ReplaceManager replaceManager) {
		List<String> categories = EggUtils.getCategories().stream().map(c -> "easter_" + c.getId()).toList();
		List<String> variables = new ArrayList<>(categories);
		variables.add("easter_total");

		replaceManager.register((player, var, replaceEvent) -> {
			PlayerProgress progress = plugin.getProgressUtils().getPlayerProgress(player.getUniqueId());
			if(var.equalsIgnoreCase("easter_total")) {
				replaceEvent.setScore(progress.getTotalProgress());
				return;
			}

			if(categories.contains(var)) {
				String id = var.replace("easter_", "");
				Category category = EggUtils.getCategoryById(id);
				if(category == null)
					return;

				replaceEvent.setScore(progress.getProgressByCategory(id));
			}
		}, this.plugin, variables.toArray(new String[0]));
	}
}