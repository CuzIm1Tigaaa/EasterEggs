package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.listeners.EasterEvents;
import org.bukkit.entity.Player;

public class Remove extends SubCommand {
	@Override
	public String getCommand() {
		return "remove";
	}

	@Override
	public String getPermission() {
		return Paths.PERMISSIONS_REMOVE;
	}

	@Override
	public String getDescription() {
		return "Entferne ein Oster-Ei";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission())) {
			Messages.getMessages().send(player, Paths.MESSAGES_PERMISSION);
			return;
		}

		EasterEvents.getRemoveEggs().add(player.getUniqueId());
		Messages.getMessages().send(player, Paths.COMMANDS_REMOVE_JOIN);
		player.sendMessage("§aYou are now in remove mode. Right click an egg to remove it.");
	}
}