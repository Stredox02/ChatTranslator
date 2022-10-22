package it.stredox02.chattranslator.bukkit.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import it.stredox02.chattranslator.common.consts.Languages;
import it.stredox02.chattranslator.common.data.PlayerData;
import it.stredox02.chattranslator.common.itembuilder.ItemBuilder;
import it.stredox02.chattranslator.common.managers.PlayerDataManager;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import it.stredox02.chattranslator.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@RequiredArgsConstructor
public class SourceLanguageGui implements InventoryProvider {

    private final ChatTranslatorPlugin enhancedTranslator;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 15)));

        PlayerData playerData = PlayerDataManager.getInstance().getData(player);
        if(playerData == null){
            return;
        }

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1,1);
        slotIterator.blacklist(1, 8)
                .blacklist(2, 0).blacklist(2, 8)
                .blacklist(3, 0).blacklist(3, 8)
                .blacklist(4, 0).blacklist(4, 8);

        ClickableItem[] items = new ClickableItem[Languages.values().length];
        for (int i = 0; i < items.length; i++) {
            Languages translation = Languages.values()[i];

            items[i] = ClickableItem.of(new ItemBuilder()
                    .displayname("&8» &b" + translation.getFormatName())
                    .toSkullBuilder().texture(translation.getHeadTextureValue()).buildSkull(), event -> {
                playerData.setTargetLanguage(translation);

                enhancedTranslator.getScheduler().getAsyncScheduler().run(() -> enhancedTranslator.getDatabaseDriver().savePlayer(player.getUniqueId(),playerData));

                player.sendMessage(Utils.colorize(enhancedTranslator.getConfigManager().getString("messages", "gui.translator.success") + translation.getFormatName()));
            });
        }

        Pagination pagination = contents.pagination();
        pagination.setItems(items);
        pagination.setItemsPerPage(28);
        pagination.addToIterator(slotIterator);

        contents.set(5, 3, ClickableItem.of(new ItemBuilder().displayname("&8» &aIndietro").toSkullBuilder()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .buildSkull(), event -> contents.inventory().open(player, pagination.previous().getPage())));

        contents.set(5, 5, ClickableItem.of(new ItemBuilder().displayname("&8» &aAvanti").toSkullBuilder()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .buildSkull(), event -> contents.inventory().open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}
