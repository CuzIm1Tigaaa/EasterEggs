package de.cuzim1tigaaa.easter.gui;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.progress.PlayerProgress;
import de.cuzim1tigaaa.guimanager.*;
import de.cuzim1tigaaa.guimanager.gui.MultiPageGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RewardsGUI extends MultiPageGUI<Map.Entry<ItemStack, Integer>> {

	private static final CustomHead money = new CustomHead("", "bd005ebbf9817d6928de8bc5f7d1c389276020ac87647d28ab8f99eb39ffde76");
	private static final CustomHead coins = new CustomHead("", "a4285382178f7abd9ee8291340aafbe3f616785cee39dab654e135c9525f1a87");

	private final EasterEggs plugin;
	private OfflinePlayer target;
	private PlayerProgress progress;

	public RewardsGUI(EasterEggs plugin, OfflinePlayer target, int page) {
		super(45, page, 21);
		this.plugin = plugin;
		this.target = target;
	}

	@Override
	public Inventory getInventory() {
		String title = "&e&lBelohnungen";
		if(target != null)
			title += " von &c" + target.getName();

		return Bukkit.createInventory(null, getSize(), ColorLib.format(title));
	}

	@Override
	public void open(Player player) {
		if(target == null)
			target = player;

		this.progress = plugin.getProgressUtils().getPlayerProgress(target.getUniqueId());
		List<Map.Entry<ItemStack, Integer>> foundItems = new ArrayList<>(progress.getRewardCount().entrySet());
		if(foundItems.isEmpty()) {
			Messages.getMessages().send(player, Paths.MESSAGES_GUI_NO_DATA);
			return;
		}

		foundItems.sort(Map.Entry.comparingByValue());
		setItems(foundItems);
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

		gui.setItem(4, ItemUtils.getCustomHead(CustomHead.BLACK_EXCLAMATION,
				"&c&lFortschritt",
				"", "&7Du hast folgende Belohnungen gesammelt"));

		if(page == 1) {
			int moneyAmount = progress.getRewardCount(RewardType.TALER);
			gui.setItem(10, ItemUtils.getCustomHead(money,
					"&e&lTaler",
					"", "&7Du hast &e" + moneyAmount + " Taler &7gefunden!"));

			int coinsAmount = progress.getRewardCount(RewardType.COINS);
			gui.setItem(11, ItemUtils.getCustomHead(coins,
					"&6&lCoins",
					"", "&7Du hast &e" + coinsAmount + " Coins &7gefunden!"));
		}

		int slot = 10 + (page == 1 ? 2 : 0);
		for(; index < this.getItems().size() && slot < gui.getSize() - 9; index++) {
			Map.Entry<ItemStack, Integer> entry = this.getItems().get(index);
			ItemStack item = ItemUtils.getAmountItem(gui, entry.getKey(), entry.getValue());
			item = ItemUtils.translateNameLore(item);
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