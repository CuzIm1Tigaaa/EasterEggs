package de.cuzim1tigaaa.easter.commands.sub;

import de.cuzim1tigaaa.easter.commands.SubCommand;
import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.guimanager.CustomHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.util.List;

public class Place extends SubCommand {
	@Override
	public String getCommand() {
		return "place";
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String getDescription() {
		return "Platziere ein Oster-Ei";
	}

	@Override
	public String getUsage() {
		return super.getUsage() + "place <category>";
	}

	@Override
	public void execute(Player player, String[] args) {
		if(!player.hasPermission(this.getPermission()))
			return;

		if(args.length < 2)
			return;

		Category category = EggUtils.getCategoryById(args[1]);
		if(category == null) {
			player.sendMessage(ChatColor.RED + "Category not found");
			return;
		}

		Block block = player.getTargetBlockExact(6);
		if(block == null || block.getType().isAir()) {
			player.sendMessage("§cYou are not looking at a block.");
			return;
		}

		PlayerProfile profile = null;
		if(category.getCustomHead() == null || (profile = CustomHead.getHead(category.getCustomHead())) == null)
			player.sendMessage("§6WARNING: The category " + category.getName() + " has no custom head defined!");

		block.setType(Material.PLAYER_HEAD);
		Skull skull = (Skull) block.getState();
		if(profile != null)
			skull.setOwnerProfile(profile);
		skull.update(true);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if(args.length == 2 && sender.hasPermission(this.getPermission()))
			return EggUtils.getCategories().stream().map(Category::getId).toList();
		return List.of();
	}
}
