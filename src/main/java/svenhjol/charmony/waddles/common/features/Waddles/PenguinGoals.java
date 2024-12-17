package svenhjol.charmony.waddles.common.features.Waddles;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class PenguinGoals {
    public static class PenguinWaterPathNavigation extends AmphibiousPathNavigation {
        public PenguinWaterPathNavigation(Penguin penguin, Level level) {
            super(penguin, level);
        }

        @Override
        public boolean isStableDestination(BlockPos blockPos) {
            if (this.mob instanceof Penguin penguin && penguin.isSwimming()) {
                return this.level.getBlockState(blockPos).is(Blocks.WATER);
            }

            return !this.level.getBlockState(blockPos.below()).isAir();
        }
    }

    public static class PenguinMoveControl extends MoveControl {
        private final Penguin penguin;

        public PenguinMoveControl(Penguin penguin) {
            super(penguin);
            this.penguin = penguin;
        }

        private void updateSpeed() {
            if (penguin.isInWater()) {
                penguin.setDeltaMovement(penguin.getDeltaMovement().add(0.0, 0.005, 0.0));
                if (penguin.isBaby()) {
                    penguin.setSpeed(Math.max(penguin.getSpeed() / 2.0F, 0.06F));
                }
            } else if (penguin.onGround()) {
                penguin.setSpeed(0.05f);
            }
        }

        @Override
        public void tick() {
            updateSpeed();
            if (this.operation == Operation.MOVE_TO && penguin.isInWater() && !penguin.getNavigation().isDone()) {
                double d = this.wantedX - penguin.getX();
                double e = this.wantedY - penguin.getY();
                double f = this.wantedZ - penguin.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                if (g < 1.0E-5F) {
                    this.mob.setSpeed(0.0F);
                } else {
                    e /= g;
                    float h = (float) (Mth.atan2(f, d) * 180.0F / (float) Math.PI) - 90.0F;
                    penguin.setYRot(this.rotlerp(penguin.getYRot(), h, 90.0F));
                    penguin.yBodyRot = penguin.getYRot();
                    float i = (float) (this.speedModifier * penguin.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    penguin.setSpeed(Mth.lerp(0.0001F, penguin.getSpeed(), i));
                    penguin.setDeltaMovement(penguin.getDeltaMovement().add(0.0, (double) penguin.getSpeed() * e * 0.1, 0.0));
                }
            } else {
                super.tick();
            }
        }
    }

    public static class PenguinRandomStrollGoal extends RandomStrollGoal {
        private final Penguin penguin;

        public PenguinRandomStrollGoal(Penguin penguin, double d) {
            super(penguin, d);
            this.penguin = penguin;
        }

        @Override
        public boolean canUse() {
            var inWater = penguin.isInWater();
            return !inWater && super.canUse();
        }
    }

    @SuppressWarnings("deprecation")
    public static class PenguinTravelGoal extends Goal {
        private final Penguin penguin;
        private final double speedModifier;
        private boolean stuck;

        public PenguinTravelGoal(Penguin penguin, double d) {
            this.penguin = penguin;
            this.speedModifier = d;
        }

        @Override
        public boolean canUse() {
            return penguin.isInWater();
        }

        @Override
        public void start() {
            int i = 512;
            int j = 4;
            RandomSource randomSource = penguin.getRandom();
            int k = randomSource.nextInt(1025) - i;
            int l = randomSource.nextInt(9) - j;
            int m = randomSource.nextInt(1025) - i;
            if ((double)l + penguin.getY() > (double)(penguin.level().getSeaLevel() - 1)) {
                l = 0;
            }

            BlockPos blockPos = BlockPos.containing((double)k + penguin.getX(), (double)l + penguin.getY(), (double)m + penguin.getZ());
            penguin.setSwimPos(blockPos);
            penguin.setSwimming(true);
            stuck = false;
        }

        @Override
        public void tick() {
            if (penguin.getNavigation().isDone()) {
                Vec3 vec3 = Vec3.atBottomCenterOf(penguin.getSwimPos());
                Vec3 vec32 = DefaultRandomPos.getPosTowards(penguin, 16, 3, vec3, (float) (Math.PI / 10));
                if (vec32 == null) {
                    vec32 = DefaultRandomPos.getPosTowards(penguin, 8, 7, vec3, (float) (Math.PI / 2));
                }

                if (vec32 != null) {
                    int i = Mth.floor(vec32.x);
                    int j = Mth.floor(vec32.z);
                    int k = 34;
                    if (!penguin.level().hasChunksAt(i - k, j - k, i + k, j + k)) {
                        vec32 = null;
                    }
                }

                if (vec32 == null) {
                    stuck = true;
                    return;
                }

                penguin.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, speedModifier);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !penguin.getNavigation().isDone() && !stuck && !penguin.isInLove();
        }

        @Override
        public void stop() {
            penguin.setSwimming(false);
            super.stop();
        }
    }
}
