package svenhjol.charmony.waddles;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.api.core.ModDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.api.core.Side;

@ModDefinition(
    id = WaddlesMod.ID, name = "Waddles", description = "Port of Waddles by GirafiStudios", sides = {Side.Client, Side.Common})
public class WaddlesMod extends Mod {
    public static final String ID = "charmony-waddles";
    private static WaddlesMod instance;

    public static WaddlesMod instance() {
        if (instance == null) {
            instance = new WaddlesMod();
        }
        return instance;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
