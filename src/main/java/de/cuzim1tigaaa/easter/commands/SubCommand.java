package de.cuzim1tigaaa.easter.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SubCommand {

	public String getCommand() {
		return "";
	}

	public String[] getAliases() {
		return new String[0];
	}

	public String getPermission() {
		return null;
	}

	public String getDescription() {
		return "Default description";
	}

	public String getUsage() {
		return "/easter " + getCommand();
	}

	public void execute(Player player, String[] args) {
	}

	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return Collections.emptyList();
	}

}