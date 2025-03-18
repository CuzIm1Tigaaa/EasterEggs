package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.commands.EasterCommand;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class Help extends SubCommand {

	private final EasterCommand command;

	public Help(EasterCommand command) {
		this.command = command;
	}

	@Override
	public String getCommand() {
		return "help";
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
		ComponentBuilder c = new ComponentBuilder("Liste aller Befehle:").color(ChatColor.GRAY).append("\n");
		for(SubCommand sub : command.getSubCommands()) {
			c.append(" » ").color(ChatColor.DARK_GRAY).append(sub.getUsage()).color(ChatColor.BLUE);
			c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(sub.getDescription())));
			c.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/easter %s", sub.getCommand())));
			c.append("\n");
		}
		player.spigot().sendMessage(c.create());
	}
}
