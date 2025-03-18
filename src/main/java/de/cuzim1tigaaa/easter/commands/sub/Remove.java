package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.listeners.EasterEvents;
import org.bukkit.entity.Player;

public class Remove extends SubCommand {
	@Override
	public String getCommand() {
		return "remove";
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String getDescription() {
		return "Entferne ein Oster-Ei";
	}

	@Override
	public String getUsage() {
		return super.getUsage() + "remove";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission()))
			return;

		EasterEvents.getRemoveEggs().add(player.getUniqueId());
		player.sendMessage("§aYou are now in remove mode. Right click an egg to remove it.");
	}
}
