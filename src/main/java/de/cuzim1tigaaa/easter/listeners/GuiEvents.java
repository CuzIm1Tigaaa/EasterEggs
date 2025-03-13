package de.cuzim1tigaaa.easter.listeners;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.guimanager.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GuiEvents implements Listener {

	private final EasterEggs plugin;
	private final Set<UUID> cooldown;

	public GuiEvents(EasterEggs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.cooldown = new HashSet<>();
	}

	@EventHandler
	public void updatedClickInventory(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof final Player player))
			return;

		if(event.getClickedInventory() == null)
			return;

		final Inventory inventory = event.getClickedInventory();
		if(!GUI.getPagination().containsKey(player.getUniqueId()))
			return;

		final GUI gui = GUI.getPagination().get(player.getUniqueId());
		final Inventory inv = gui.getGuiItem().inventory();

		if(!inventory.equals(inv))
			return;

		event.setCancelled(true);
		if(cooldown.contains(player.getUniqueId()))
			return;

		gui.click(player, event.getRawSlot());
		cooldown.add(player.getUniqueId());
		Bukkit.getScheduler().runTaskLater(plugin, () -> cooldown.remove(player.getUniqueId()), 2L);
	}

	@EventHandler
	public void updatedCloseInventory(InventoryCloseEvent event) {
		if(!(event.getPlayer() instanceof final Player player))
			return;

		final Inventory inventory = event.getInventory();
		if(!GUI.getPagination().containsKey(player.getUniqueId()))
			return;

		final GUI gui = GUI.getPagination().get(player.getUniqueId());
		final Inventory inv = gui.getGuiItem().inventory();

		if(inventory.equals(inv))
			GUI.getPagination().remove(player.getUniqueId());
	}
}