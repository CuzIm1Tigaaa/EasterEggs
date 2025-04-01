package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.gui.RewardsGUI;
import de.cuzim1tigaaa.guimanager.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Rewards extends SubCommand {

	private final EasterEggs plugin;

	public Rewards(EasterEggs plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommand() {
		return "rewards";
	}

	@Override
	public String getPermission() {
		return Paths.PERMISSIONS_REWARDS;
	}

	@Override
	public String getDescription() {
		return "Zeige deine aktuellen Belohnungen an";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(getPermission())) {
			Messages.getMessages().send(player, Paths.MESSAGES_PERMISSION);
			return;
		}

		int page = 1;
		if(player.hasPermission(Paths.PERMISSIONS_PROGRESS_OTHER)) {
			if(args.length >= 2) {
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(!target.hasPlayedBefore() && !target.isOnline()) {
					Messages.getMessages().send(player, Paths.MESSAGES_PLAYER_NOT_FOUND, "%player%", args[1]);
					return;
				}

				if(args.length >= 3)
					page = getInt(args[2]);
				openRewardsGUI(player, target, page);
				return;
			}
			return;
		}

		if(args.length >= 2)
			page = getInt(args[1]);
		openRewardsGUI(player, null, page);
	}

	private void openRewardsGUI(Player player, OfflinePlayer target, int page) {
		GUI gui = GUI.getPagination().getOrDefault(player.getUniqueId(), null);
		if(!(gui instanceof RewardsGUI)) {
			player.closeInventory();
			gui = new RewardsGUI(plugin, target, 1);
		}
		gui.open(player);
		gui.switchPage(page);
	}

	private int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return 1;
		}
	}

	/*
	PlayerProgress progress = plugin.getProgressUtils().getPlayerProgress(player.getUniqueId());
		player.sendMessage("Du hast folgende Belohnungen gesammelt:");

		for(RewardType rewardType : RewardType.values()) {
			if(rewardType == RewardType.ITEMS) {
				for(Map.Entry<Material, Integer> materials : progress.getRewardCount().entrySet()) {
					Material material = materials.getKey();
					int amount = materials.getValue();
					if(amount > 0)
						player.sendMessage(ColorLib.format("&7- &a%dx %s", amount, material.name()));
				}
				continue;
			}

			int amount = progress.getRewardCount(rewardType);
			if(amount > 0)
				player.sendMessage(ColorLib.format("&7- &a%d %s", amount, rewardType.getName()));
		}
	 */
}