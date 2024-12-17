package svenhjol.charmony.waddles.client.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class PenguinModel extends EntityModel<PenguinModelRenderState> {
    public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.6F);
    private ModelPart head;
    private ModelPart beak;
    private ModelPart body;
    private ModelPart flipperLeft;
    private ModelPart flipperRight;
    private ModelPart feetLeft;
    private ModelPart feetRight;
    private ModelPart tail;

    public PenguinModel(ModelPart part) {
        super(part);
        this.head = part.getChild("head");
        this.beak = part.getChild("beak");
        this.body = part.getChild("body");
        this.flipperLeft = part.getChild("flipper_left");
        this.flipperRight = part.getChild("flipper_right");
        this.feetLeft = part.getChild("feet_left");
        this.feetRight = part.getChild("feet_right");
        this.tail = part.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        def.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4, 4, 5), PartPose.offset(0.0F, 12.0F, 0.0F));
        def.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, -3.0F, -4.0F, 1, 2, 3), PartPose.offset(0.0F, 12.0F, 0.0F));
        def.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-2.5F, 0.0F, -2.0F, 5, 11, 5), PartPose.offset(0.0F, 12.0F, 1.0F));
        def.addOrReplaceChild("flipper_left", CubeListBuilder.create().texOffs(20, 10).mirror().addBox(0.0F, 0.0F, -1.0F, 1, 7, 3), PartPose.offsetAndRotation(2.5F, 12.0F, 0.0F, 0.0F, 0.0F, -0.08726646259971647F));
        def.addOrReplaceChild("flipper_right", CubeListBuilder.create().texOffs(20, 10).addBox(-1.0F, 0.0F, -1.0F, 1, 7, 3), PartPose.offsetAndRotation(-2.5F, 12.0F, 0.0F, 0.0F, 0.0F, 0.08726646259971647F));
        def.addOrReplaceChild("feet_left", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(0.0F, 0.0F, -3.0F, 2, 1, 3), PartPose.offsetAndRotation(1.0F, 23.0F, 0.0F, 0.0F, -0.2617993877991494F, 0.0F));
        def.addOrReplaceChild("feet_right", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, 0.0F, -3.0F, 2, 1, 3), PartPose.offsetAndRotation(-1.0F, 23.0F, 0.0F, 0.0F, 0.2617993877991494F, 0.0F));
        def.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 20).addBox(-1.5F, -1.0F, 0.0F, 3, 3, 1), PartPose.offsetAndRotation(0.0F, 23.0F, 3.0F, 1.2566370614359172F, 0.0F, 0.0F));
        return LayerDefinition.create(modelDefinition, 32, 32);
    }

    public static LayerDefinition createBabyBodyLayer() {
        return createBodyLayer().apply(BABY_TRANSFORMER);
    }

    @Override
    public void setupAnim(PenguinModelRenderState state) {
        super.setupAnim(state);
        setupWalkingAnimation(state);
        setupSwimmingAnimation(state);
    }

    private void setupWalkingAnimation(PenguinModelRenderState state) {
        if (!state.isInWater) {
            this.head.xRot = state.xRot * 0.017453292F;
            this.head.yRot = state.yRot * 0.017453292F;
            this.head.zRot = (Mth.cos(state.walkAnimationPos * 1.3324F) * 1.4F * state.walkAnimationSpeed) / 6;
            this.beak.xRot = this.head.xRot;
            this.beak.yRot = this.head.yRot;
            this.body.xRot = 0;
            this.body.zRot = (Mth.cos(state.walkAnimationPos * 1.3324F) * 1.4F * state.walkAnimationSpeed) / 6;
            this.feetRight.xRot = Mth.cos(state.walkAnimationPos * 1.3324F) * 1.2F * state.walkAnimationSpeed;
            this.feetLeft.xRot = Mth.cos(state.walkAnimationPos * 1.3324F + (float) Math.PI) * 1.2F * state.walkAnimationSpeed;
            this.flipperRight.zRot = 0.18726646259971647F + (Mth.cos(state.rotationFlipper) * state.walkAnimationSpeed);
            this.flipperLeft.zRot = -0.18726646259971647F + (Mth.cos((float) state.rotationFlipper + (float) Math.PI) * state.walkAnimationSpeed);
            this.tail.yRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * 1.4F * state.walkAnimationSpeed;
        }
    }

    private void setupSwimmingAnimation(PenguinModelRenderState state) {
        if (state.isInWater) {
            var yoffset = 7.5f;
            var xoffset = 0f;
            var zoffset = -5f;
            this.head.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.beak.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.body.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.flipperLeft.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.flipperRight.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.feetLeft.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.feetRight.offsetPos(new Vector3f(xoffset, yoffset, zoffset));
            this.tail.offsetPos(new Vector3f(xoffset, yoffset, zoffset));

            this.beak.xRot = this.head.xRot;
            this.beak.yRot = this.head.yRot;
            this.body.xRot = 20;

            flipperLeft.offsetPos(new Vector3f(-1f, 0.3f, 2f));
            flipperRight.offsetPos(new Vector3f(1f, 0.3f, 2f));

            this.flipperLeft.yRot = 0.8f;
            this.flipperRight.yRot = -0.8f;

            this.flipperRight.zRot = 1.4f + (Mth.sin(state.swimFlipper));
            this.flipperLeft.zRot = -1.4f + (Mth.sin(state.swimFlipper + (float) Math.PI));
            this.tail.yRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * 1.4F * state.walkAnimationSpeed;
            this.tail.offsetPos(new Vector3f(0, -8f, 8f));
            this.tail.offsetScale(new Vector3f(0.75f, 0f, 0.75f));
            this.feetLeft.offsetPos(new Vector3f(0, -6.5f, 10f));
            this.feetRight.offsetPos(new Vector3f(0, -6.5f, 10f));
        }
    }
}