package svenhjol.charmony.waddles;

import svenhjol.charmony.api.core.ModDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;

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
}
