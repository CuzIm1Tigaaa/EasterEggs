package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.gui.CategoryGUI;
import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.guimanager.gui.GUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Categories extends SubCommand {
	@Override
	public String getCommand() {
		return "categories";
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "cat" };
	}

	@Override
	public String getDescription() {
		return "Liste aller Kategorien";
	}

	@Override
	public String getUsage() {
		return super.getUsage() + "categories [page]";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission()))
			return;

		int page = 1;
		if(args.length >= 2) {
			try {
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException ignored) {}
		}
		openCategoryGUI(player, page);
	}

	private void openCategoryGUI(Player player, int page) {
		GUI gui = GUI.getPagination().getOrDefault(player.getUniqueId(), null);
		if(!(gui instanceof CategoryGUI)) {
			player.closeInventory();
			gui = new CategoryGUI(1);
		}
		gui.open(player);
		gui.switchPage(page);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if(args.length == 2 && sender.hasPermission(this.getPermission()))
			return EggUtils.getCategories().stream().map(Category::getId).toList();
		return List.of();
	}
}