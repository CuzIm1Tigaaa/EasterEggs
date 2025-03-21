package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Test extends SubCommand {

	@Override
	public String getCommand() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "Zeigt eine Übersicht aller Befehle";
	}

	@Override
	public String getUsage() {
		return super.getUsage() + "help";
	}

	@Override
	public void execute(Player player, String[] args) {
		args = Arrays.copyOfRange(args, 1, args.length);
		String txt = String.join(" ", args);
		System.out.println(txt);
		String finalTxt = ColorLib.format(txt);
		System.out.println(finalTxt + ": " + finalTxt.length());
		player.sendMessage(finalTxt);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return List.of();
	}
}
