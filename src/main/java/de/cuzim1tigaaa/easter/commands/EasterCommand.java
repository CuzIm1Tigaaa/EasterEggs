package de.cuzim1tigaaa.easter.commands;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.listeners.EasterEvents;
import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.guimanager.CustomHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.util.List;
import java.util.stream.Collectors;

public class EasterCommand implements CommandExecutor, TabCompleter {

	public EasterCommand(EasterEggs plugin) {
		plugin.getCommand("easter").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player player)) {

			return true;
		}

		if(args.length == 0) {

			return true;
		}

		switch(args[0].toLowerCase()) {
			case "place" -> {
				String categoryId = args[1];
				placeEasterEgg(player, categoryId);
			}
			case "remove" -> {
				EasterEvents.getRemovedEggs().add(player.getUniqueId());
				player.sendMessage("§aYou are now in remove mode. Right click an egg to remove it.");
			}
			case "help" -> {

			}
		}

		return true;
	}

	private void placeEasterEgg(Player player, String categoryId) {
		Category category = EggUtils.getCategoryById(categoryId);
		if(category == null) {
			player.sendMessage(ChatColor.RED + "Category not found");
			return;
		}

		Block block = player.getTargetBlockExact(6);
		if(block == null || block.getType().isAir()) {
			player.sendMessage("§cYou are not looking at a block.");
			return;
		}

		PlayerProfile profile;
		if(category.getCustomHead() == null || (profile = CustomHead.getHead(category.getCustomHead())) == null) {
			player.sendMessage("The head from category " + category.getName() + " does not exist.");
			return;
		}

		block.setType(Material.PLAYER_HEAD);
		Skull skull = (Skull) block.getState();
		skull.setOwnerProfile(profile);
		skull.update(true);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1)
			return List.of("place", "remove", "help");
		if(args.length == 2 && args[0].equalsIgnoreCase("place"))
			return EggUtils.getCategories().stream().map(Category::getName).collect(Collectors.toList());
		return List.of();
	}
}
