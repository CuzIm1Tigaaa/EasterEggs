package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.commands.EasterCommand;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {

	private final EasterEggs plugin;

	public Reload(EasterEggs plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommand() {
		return "reload";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "rl" };
	}

	@Override
	public String getDescription() {
		return "Lade die Plugin-Dateien neu";
	}

	@Override
	public String getUsage() {
		return super.getUsage() + "reload";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission()))
			return;


	}
}
