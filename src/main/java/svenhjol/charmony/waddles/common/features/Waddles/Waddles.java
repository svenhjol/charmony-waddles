package svenhjol.charmony.waddles.common.features.Waddles;

import net.minecraft.util.Mth;
import svenhjol.charmony.api.core.Configurable;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.api.core.Side;

@FeatureDefinition(
    side = Side.Common,
    description = "Waddles adds somewhat realistic Adélie penguins that waddle."
)
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public final class Waddles extends SidedFeature {
    public final Registers registers;

    @Configurable(
        name = "Drop fish",
        description = "If true, penguins drop fish (0 - 2 raw cod).",
        requireRestart = false
    )
    private static boolean dropFish = true;

    @Configurable(
        name = "Drop experience",
        description = "If true, penguins drop experience.",
        requireRestart = false
    )
    private static boolean dropExperience = true;

    @Configurable(
        name = "Attracted to water",
        description = "If true, penguins will seek out water to play in."
    )
    private static boolean seekWater = true;

    @Configurable(
        name = "Spawn chance",
        description = """
            Chance of the game spawning one or more penguins if possible. Higher values give more chance.
            A weight of zero disables penguins from being able to spawn."""
    )
    private static int weight = 10;

    @Configurable(
        name = "Maximum group size",
        description = "The maximum number of penguins that will spawn in a group."
    )
    private static int maxGroupSize = 4;

    public Waddles(Mod mod) {
        super(mod);
        registers = new Registers(this);
    }

    public static Waddles feature() {
        return Mod.getSidedFeature(Waddles.class);
    }

    public boolean dropFish() {
        return dropFish;
    }

    public boolean dropExeperience() {
        return dropExperience;
    }

    public boolean seekWater() {
        return seekWater;
    }

    public int maxGroupSize() {
        return Mth.clamp(maxGroupSize, 1, 16);
    }

    public int weight() {
        return Mth.clamp(weight, 0, 200);
    }
}
