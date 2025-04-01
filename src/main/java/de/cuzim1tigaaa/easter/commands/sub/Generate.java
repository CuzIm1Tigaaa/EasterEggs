package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.commands.EasterCommand;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.files.data.CommandFile;
import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.progress.PlayerProgress;
import de.cuzim1tigaaa.easter.utils.progress.ProgressUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Generate extends SubCommand {

	private final EasterCommand command;

	public Generate(EasterCommand command) {
		this.command = command;
	}

	@Override
	public String getCommand() {
		return "generate";
	}

	@Override
	public String getPermission() {
		return Paths.PERMISSIONS_GENERATE;
	}

	@Override
	public String getDescription() {
		return "Generiert Befehle für die Belohnungen";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission())) {
			Messages.getMessages().send(player, Paths.MESSAGES_PERMISSION);
			return;
		}

		CommandFile commandFile = command.getCommandFile();
		commandFile.getCommands().clear();

		final Set<String> commands = new HashSet<>();
		for(PlayerProgress progress : ProgressUtils.getPlayerProgress()) {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(progress.getUuid());
			for(RewardType rewardType : RewardType.values()) {
				if(rewardType == RewardType.ITEMS) {
					for(Map.Entry<ItemStack, Integer> items : progress.getRewardCount().entrySet()) {
						Material material = items.getKey().getType();
						int amount = items.getValue();
						if(amount > 0) {
							String finalCommand = rewardType.getCommand()
									.replace("%player%", offlinePlayer.getName())
									.replace("%item%", material.toString().toLowerCase())
									.replace("%amount%", String.valueOf(amount));
							commands.add(finalCommand);
						}
					}
					continue;
				}

				int amount = progress.getRewardCount(rewardType);
				if(amount > 0) {
					String finalCommand = rewardType.getCommand()
							.replace("%player%", offlinePlayer.getName())
							.replace("%amount%", String.valueOf(amount));
					commands.add(finalCommand);
				}
			}
		}

		commandFile.getCommands().addAll(commands);
		String fileName = "eastercommands";
		if(args.length >= 2)
			fileName = args[1];

		commandFile.saveEasterEggs(fileName);
	}
}