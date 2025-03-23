package de.cuzim1tigaaa.easter.gui;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.colorlib.gradients.SingleColor;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.guimanager.*;
import de.cuzim1tigaaa.guimanager.gui.MultiPageGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CategoryGUI extends MultiPageGUI<Category> {

	public CategoryGUI(int page) {
		super(27, page, 7);
	}

	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(null, getSize(), ColorLib.format("&e&lKategorie Übersicht", new SingleColor()));
	}

	@Override
	public void open(Player player) {
		List<Category> categories = new ArrayList<>(EggUtils.getCategories());
		if(categories.isEmpty()) {
			Messages.getMessages().send(player, Paths.MESSAGES_GUI_NO_DATA);
			return;
		}

		categories.sort(Category::compareTo);
		setItems(categories);
		super.open(player);
	}

	@Override
	protected void setItems() {
		super.setItems();
		Inventory gui = guiItem.inventory();

		GuiUtils.setPlaceholders(gui);
		int maxPage = GuiUtils.calcMaxPage(this.getItems().size(), entriesPerPage);
		int currentPage = Math.min(maxPage, Math.max(page, 1));
		setPage(currentPage);
		int index = ((currentPage - 1) * entriesPerPage);

		GuiUtils.setNavigationItems(gui, currentPage > 1, false, currentPage != maxPage);

		int slot = 10;
		for(; index < this.getItems().size() && slot < gui.getSize() - 9; index++) {
			Category category = this.getItems().get(index);
			int eggs = EggUtils.getEggsByCategory(category).size();

			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add("&7Diese Kategorie enthält " + Messages.capitalizeFully(category.getRewardType().name()) + " als Belohnung.");
			lore.add("&7Es wurden bereits &e" + eggs + " &7Eier gesetzt.");
			lore.add("&7Mindestanzahl: &e" + category.getMin());
			lore.add("&7Höchstanzahl: &e" + category.getMax());

			if(category.getRewardType().equals(RewardType.ITEMS)) {
				lore.add("");
				lore.add("&7Materialien:");
				for(Material material : category.getMaterials())
					lore.add("&8- &e" + material.name());
			}

			ItemStack item = ItemUtils.getCustomHead(category.getCustomHead(),
					category.getName(),
					lore.toArray(new String[0]));
			gui.setItem(slot, item);
			slot += (slot + 2) % 9 == 0 ? 3 : 1;
		}
	}

	@Override
	public boolean click(Player player, int slot) {
		if(!super.click(player, slot))
			return false;

		if(slot == getSize() - 9) {
			--page;
			if(page > 0)
				switchPage(page);
			return true;
		}

		if(slot == getSize() - 5) {
			setItems();
			return true;
		}

		if(slot == getSize() - 1) {
			switchPage(++page);
			return true;
		}

		return false;
	}
}