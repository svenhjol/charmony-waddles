package svenhjol.charmony.waddles.client.features;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.waddles.WaddlesMod;

public final class Registers extends Setup<Waddles> {
    public final ModelLayerLocation adultModel;
    public final ModelLayerLocation babyModel;

    public Registers(Waddles feature) {
        super(feature);

        adultModel = new ModelLayerLocation(WaddlesMod.id("penguin"), "penguin");
        babyModel = new ModelLayerLocation(WaddlesMod.id("penguin_baby"), "penguin_baby");

        var common = feature.common.get();
        if (common != null) {
            EntityRendererRegistry.register(common.registers.penguin.get(), PenguinRenderer::new);
            EntityModelLayerRegistry.registerModelLayer(adultModel, PenguinModel::createBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(babyModel, PenguinModel::createBabyBodyLayer);

            ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(
                entries -> entries.addAfter(Items.CREAKING_HEART, common.registers.spawnEgg.get()));
        }
    }
}
