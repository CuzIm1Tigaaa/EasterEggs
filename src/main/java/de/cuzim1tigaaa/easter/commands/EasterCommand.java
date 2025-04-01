package de.cuzim1tigaaa.easter.commands;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.commands.sub.*;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.files.data.CommandFile;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EasterCommand implements CommandExecutor, TabCompleter {

	private final EasterEggs plugin;
	@Getter
	private final CommandFile commandFile;

	@Getter
	private final List<SubCommand> subCommands = new ArrayList<>();

	public EasterCommand(EasterEggs plugin) {
		this.plugin = plugin;
		this.commandFile = new CommandFile(plugin);

		plugin.getCommand("easter").setExecutor(this);

		subCommands.add(new Categories());
		subCommands.add(new Generate(this));
		subCommands.add(new Help(this));
		subCommands.add(new Place());
		subCommands.add(new Progress(plugin));
		subCommands.add(new Reload(plugin));
		subCommands.add(new Remove());
		subCommands.add(new Rewards(plugin));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player player)) {
			Messages.getMessages().send(sender, Paths.MESSAGES_SENDER);
			return true;
		}

		if(args.length > 0) {
			getSubCommands().stream().filter(subCommand -> {
				if(args[0].equalsIgnoreCase(subCommand.getCommand()))
					return true;

				return List.of(subCommand.getAliases()).contains(args[0]);
			}).findFirst().ifPresentOrElse(subCommand ->
					subCommand.execute(player, args), () -> player.sendMessage("/easter help"));
		}
		if(args.length < 1) {
			sender.sendMessage(getHelpMessage());
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if(args.length == 1)
			return getSubCommands().stream().map(SubCommand::getCommand).toList();

		if(args.length > 1) {
			getSubCommands().stream().filter(subCommand -> {
				if(subCommand.getCommand().equalsIgnoreCase(args[0]))
					return true;

				if(subCommand.getPermission() != null &&
						!sender.hasPermission(subCommand.getPermission()))
					return false;

				return List.of(subCommand.getAliases()).contains(args[0]);
			}).forEach(subCommand -> tab.addAll(subCommand.onTabComplete(sender, args)));
		}
		return tab;
	}

	public String getHelpMessage() {
		return ColorLib.format("""
				&l#CEF4F8Ea#D1CCEBst#FED3D9er#FDF0D7Eg#C5EBD5gs &fv%s
				&7by CuzIm1Tigaaa; &7&o© 2025
				&7All rights reserved
				""", plugin.getDescription().getVersion());
	}
}