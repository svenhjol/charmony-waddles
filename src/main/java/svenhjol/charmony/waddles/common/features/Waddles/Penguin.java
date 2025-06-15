package svenhjol.charmony.waddles.common.features.Waddles;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import svenhjol.charmony.core.base.Log;
import svenhjol.charmony.waddles.WaddlesMod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings({"resource", "unused"})
public class Penguin extends Animal {
    private static final Log LOGGER = new Log(WaddlesMod.ID, "Penguin");
    public short rotationFlipper;
    public float swimFlipper;
    public boolean swimming;
    public int ticksSinceSwimming = 0;
    public int ticksSinceStanding = 0;
    public BlockPos swimPos;

    public static final Map<Pose, EntityDimensions> ADULT_POSES = new HashMap<>();
    public static final Map<Pose, EntityDimensions> BABY_POSES = new HashMap<>();

    public Penguin(EntityType<? extends Penguin> penguin, Level level) {
        super(penguin, level);

        if (Waddles.feature().seekWater()) {
            this.setPathfindingMalus(PathType.WATER, 0.0f);
        }
        this.moveControl = new PenguinGoals.PenguinMoveControl(this);

        ADULT_POSES.put(Pose.SWIMMING, EntityDimensions.scalable(0.95f, 0.5f).withEyeHeight(0.45f));
        ADULT_POSES.put(Pose.STANDING, EntityDimensions.scalable(0.4f, 0.95f).withEyeHeight(0.9f));
        BABY_POSES.put(Pose.SWIMMING, EntityDimensions.scalable(0.475f, 0.25f).withEyeHeight(0.225f));
        BABY_POSES.put(Pose.STANDING, EntityDimensions.scalable(0.2f, 0.6f).withEyeHeight(0.56f));
    }

    @Override
    protected void registerGoals() {
        Predicate<ItemStack> ingredient = stack -> stack.is(Tags.PENGUIN_LOVED);
        this.goalSelector.addGoal(1, new EntityAIExtinguishFire());
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5d));
        this.goalSelector.addGoal(3, new BreedGoal(this, 0.8d));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PolarBear.class, 6.0f, 2.0d, 2.2d));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0d, ingredient, false));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new PenguinGoals.PenguinTravelGoal(this, 1.0d));
        this.goalSelector.addGoal(9, new FollowParentGoal(this, 1.1d));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new PenguinGoals.PenguinRandomStrollGoal(this, 1.0d));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 8.0d)
            .add(Attributes.MOVEMENT_SPEED, 0.16d)
            .add(Attributes.STEP_HEIGHT, 1.5)
            .add(Attributes.TEMPT_RANGE, 4.0);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader levelReader) {
        if (levelReader.getFluidState(pos).is(FluidTags.WATER)) {
            return 10.0f;
        } else {
            var stateDown = levelReader.getBlockState(pos.below());
            return stateDown.is(Tags.PENGUIN_SPAWNABLE) ? 10.0f : super.getWalkTargetValue(pos, levelReader);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isInWater()) {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new PenguinGoals.PenguinWaterPathNavigation(this, level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isInWater()) return SoundEvents.EMPTY;
        return isBaby() ? feature().registers.babyAmbientSound.get() : feature().registers.ambientSound.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return isBaby() ? SoundEvents.EMPTY : feature().registers.hurtSound.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return isBaby() ? SoundEvents.EMPTY : feature().registers.deathSound.get();
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        var poses = isBaby() ? BABY_POSES : ADULT_POSES;
        return poses.getOrDefault(pose, super.getDefaultDimensions(pose));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        refreshDimensions();
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) {
            if (getX() != zo && isPanicking()) {
                rotationFlipper++;
            }
        }

        if (isInWater()) {
            if (ticksSinceStanding++ > 10) {
                setPose(Pose.SWIMMING);
            }
            if (walkAnimation.speed() > 0) {
                swimFlipper += (0.35f * walkAnimation.speed());
            }
            ticksSinceSwimming = 0;
        } else {
            if (ticksSinceSwimming++ > 10) {
                setPose(Pose.STANDING);
            }
            ticksSinceStanding = 0;
        }
    }

    @Override
    protected void dropExperience(ServerLevel serverLevel, @Nullable Entity entity) {
        super.dropExperience(serverLevel, entity);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel serverLevel) {
        if (feature().dropExeperience()) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int i) {
        return getMaxAirSupply();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Tags.PENGUIN_LOVED);
    }

    @Override
    protected boolean shouldDropLoot() {
        return feature().dropFish();
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return feature().registers.penguin.get().create(this.level(), EntitySpawnReason.BREEDING);
    }

    public static boolean canPenguinSpawn(EntityType<Penguin> entityType, ServerLevelAccessor level, EntitySpawnReason spawnResponse, BlockPos pos, RandomSource random) {
        var below = level.getBlockState(pos.below());
        var canSpawn = below.is(Tags.PENGUIN_SPAWNABLE) && isBrightEnoughToSpawn(level, pos);

        if (canSpawn) {
            LOGGER.debug("Spawning penguin at " + pos);
        }
        return canSpawn;
    }

    public boolean isSwimming() {
        return swimming;
    }

    public BlockPos getSwimPos() {
        return swimPos;
    }

    public void setSwimming(boolean flag) {
        this.swimming = flag;
    }

    public void setSwimPos(BlockPos pos) {
        this.swimPos = pos;
    }

    public short getRotationFlipper() {
        return rotationFlipper;
    }

    public float getSwimFlipper() {
        return swimFlipper;
    }

    private class EntityAIExtinguishFire extends PanicGoal {
        EntityAIExtinguishFire() {
            super(Penguin.this, 2.0d);
        }

        @Override
        public boolean canUse() {
            return (Penguin.this.isBaby() || Penguin.this.isOnFire()) && super.canUse();
        }
    }

    private Waddles feature() {
        return Waddles.feature();
    }
}