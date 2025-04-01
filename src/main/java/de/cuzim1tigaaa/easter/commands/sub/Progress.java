package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.gui.ProgressGUI;
import de.cuzim1tigaaa.guimanager.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Progress extends SubCommand {

	private final EasterEggs plugin;

	public Progress(EasterEggs plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommand() {
		return "progress";
	}

	@Override
	public String getPermission() {
		return Paths.PERMISSIONS_PROGRESS;
	}

	@Override
	public String getDescription() {
		return "Sehe deinen Fortschritt";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission())) {
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
				openProgressGUI(player, target, page);
				return;
			}
			return;
		}

		if(args.length >= 2)
			page = getInt(args[1]);
		openProgressGUI(player, null, page);
	}

	private int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return 1;
		}
	}

	private void openProgressGUI(Player player, OfflinePlayer target, int page) {
		GUI gui = GUI.getPagination().getOrDefault(player.getUniqueId(), null);
		if(!(gui instanceof ProgressGUI)) {
			player.closeInventory();
			gui = new ProgressGUI(plugin, target, 1);
		}
		gui.open(player);
		gui.switchPage(page);
	}
}