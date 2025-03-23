package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
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
	public String getPermission() {
		return "easter.reload";
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

		long start = System.currentTimeMillis();
		plugin.reload();
		Messages.getMessages().send(player, Paths.MESSAGES_RELOAD, "TIME", System.currentTimeMillis() - start);
	}
}