package svenhjol.charmony.waddles.client.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.waddles.common.features.Waddles.Penguin;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public class PenguinRenderer extends AgeableMobRenderer<Penguin, PenguinModelRenderState, PenguinModel> {
    public PenguinRenderer(EntityRendererProvider.Context context) {
        super(context,
            new PenguinModel(context.bakeLayer(Waddles.feature().registers.adultModel)),
            new PenguinModel(context.bakeLayer(Waddles.feature().registers.babyModel)),
            0.5f);
    }

    @Override
    public PenguinModelRenderState createRenderState() {
        return new PenguinModelRenderState();
    }

    @Override
    public void extractRenderState(Penguin penguin, PenguinModelRenderState state, float f) {
        super.extractRenderState(penguin, state, f);
        state.rotationFlipper = penguin.getRotationFlipper();
        state.swimFlipper = penguin.getSwimFlipper();
        state.isInWater = penguin.getPose().equals(Pose.SWIMMING);
    }

    @Override
    public ResourceLocation getTextureLocation(PenguinModelRenderState state) {
        var fileName = state.isBaby ? "adelie_child" : "adelie";
        return Charmony.id("textures/entity/penguin/" + fileName + ".png");
    }
}