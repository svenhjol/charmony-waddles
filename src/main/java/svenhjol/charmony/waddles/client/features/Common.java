package svenhjol.charmony.waddles.client.features;

import svenhjol.charmony.waddles.common.features.Waddles.Registers;
import svenhjol.charmony.waddles.common.features.Waddles.Waddles;

public final class Common {
    public final Registers registers;

    public Common() {
        var common = Waddles.feature();
        registers = common.registers;
    }
}
