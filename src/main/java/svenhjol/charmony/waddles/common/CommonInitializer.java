package svenhjol.charmony.waddles.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.waddles.WaddlesMod;
import svenhjol.charmony.waddles.common.features.Waddles.Waddles;

public class CommonInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.common.CommonInitializer.init();

        var waddles = WaddlesMod.instance();
        waddles.addSidedFeature(Waddles.class);
        waddles.run(Side.Common);
    }
}
