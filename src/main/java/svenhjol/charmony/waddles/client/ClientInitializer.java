package svenhjol.charmony.waddles.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.waddles.WaddlesMod;
import svenhjol.charmony.waddles.client.features.Waddles;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.client.ClientInitializer.init();

        var waddles = WaddlesMod.instance();
        waddles.addSidedFeature(Waddles.class);
        waddles.run(Side.Client);
    }
}
