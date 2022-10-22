package it.stredox02.chattranslator.common.itembuilder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import it.stredox02.chattranslator.common.utils.Utils;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class ItemBuilder {

    //ItemStack
    private Material material;
    private int amount = 1;
    private int data;

    //ItemMeta
    private String displayname;
    private List<String> lore = new ArrayList<>();

    public ItemBuilder(Material material, int amount, int data){
        this.material = material;
        this.amount = amount;
        this.data = data;
    }

    public ItemBuilder(Material material, int amount){
        this(material,amount,0);
    }

    public ItemBuilder(Material material){
        this(material,1);
    }

    public ItemBuilder material(Material material){
        this.material = material;
        return this;
    }

    public ItemBuilder amount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(int data){
        this.data = data;
        return this;
    }

    public ItemBuilder displayname(String displayname){
        this.displayname = displayname;
        return this;
    }

    public ItemBuilder lore(List<String> lore){
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(String... lore){
        this.lore = Arrays.asList(lore);
        return this;
    }

    public @Nullable ItemStack build(){
        if(material == null){
            return null;
        }
        ItemStack itemStack = new ItemStack(material, amount, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.colorize(displayname));
        itemMeta.setLore(Utils.colorize(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public SkullBuilder toSkullBuilder(){
        return new SkullBuilder();
    }

    public final class SkullBuilder{

        private String texture;

        public SkullBuilder texture(@NotNull String texture){
            this.texture = texture;
            return this;
        }

        public @NotNull ItemStack buildSkull(){
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM,amount, (short) 3);

            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setDisplayName(Utils.colorize(displayname));
            skullMeta.setLore(Utils.colorize(lore));

            if(texture != null) {
                UUID skullUUID = new UUID(texture.substring(texture.length() - 20).hashCode(), texture.substring(texture.length() - 10).hashCode());
                GameProfile profile = new GameProfile(skullUUID, null);
                profile.getProperties().put("textures", new Property("textures", texture));

                try {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException |
                         SecurityException e) {
                    e.printStackTrace();
                }
            }

            itemStack.setItemMeta(skullMeta);

            return itemStack;
        }

    }

}
