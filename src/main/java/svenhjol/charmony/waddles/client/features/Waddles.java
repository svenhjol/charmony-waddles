package svenhjol.charmony.waddles.client.features;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.waddles.WaddlesMod;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, showInConfig = false)
public final class Waddles extends SidedFeature {
    public final Supplier<Common> common;
    public final Registers registers;

    public Waddles(Mod mod) {
        super(mod);
        common = Common::new;
        registers = new Registers(this);
    }

    public static Waddles feature() {
        return WaddlesMod.instance().sidedFeature(Waddles.class);
    }
}
