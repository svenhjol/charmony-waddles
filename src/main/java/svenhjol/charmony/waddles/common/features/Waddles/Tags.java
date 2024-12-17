package svenhjol.charmony.waddles.common.features.Waddles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import svenhjol.charmony.waddles.WaddlesMod;

public final class Tags {
    public static final TagKey<Block> PENGUIN_SPAWNABLE = blockTag("penguin_spawnable");
    public static final TagKey<Item> PENGUIN_LOVED = itemTag("penguin_loved");
    public static final TagKey<Biome> SPAWNS_PENGUINS = biomeTag("spawns_penguins");

    public static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(WaddlesMod.ID, name));
    }

    public static TagKey<Biome> biomeTag(String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(WaddlesMod.ID, name));
    }

    public static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WaddlesMod.ID, name));
    }
}
