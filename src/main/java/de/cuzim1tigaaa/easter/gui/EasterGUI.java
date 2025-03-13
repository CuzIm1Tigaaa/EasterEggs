package de.cuzim1tigaaa.easter.gui;

import de.cuzim1tigaaa.guimanager.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EasterGUI extends GUI {

	public EasterGUI(int size) {
		super(size, 1);
	}

	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(null, 27, "&eOster Eier Übersicht");
	}

	@Override
	public void open(Player player) {
		super.open(player);
	}

	@Override
	protected void setItems() {
		super.setItems();
	}

	@Override
	public boolean click(Player player, int slot) {
		if(!super.click(player, slot))
			return false;

		return super.click(player, slot);
	}
}
