package svenhjol.charmony.waddles.client.features;

import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.api.core.Side;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class Waddles extends SidedFeature {
    public final Supplier<Common> common;
    public final Registers registers;

    public Waddles(Mod mod) {
        super(mod);
        common = Common::new;
        registers = new Registers(this);
    }

    public static Waddles feature() {
        return Mod.getSidedFeature(Waddles.class);
    }
}
