package svenhjol.charmony.waddles.common.features.Waddles;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.function.Supplier;

public final class Registers extends Setup<Waddles> {
    public final Supplier<Item> spawnEgg;
    public final Supplier<EntityType<Penguin>> penguin;

    public final Supplier<SoundEvent> ambientSound;
    public final Supplier<SoundEvent> babyAmbientSound;
    public final Supplier<SoundEvent> deathSound;
    public final Supplier<SoundEvent> hurtSound;

    public Registers(Waddles feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);

        ambientSound = registry.sound("adelie.ambient");
        babyAmbientSound = registry.sound("adelie.baby.ambient");
        deathSound = registry.sound("adelie.death");
        hurtSound = registry.sound("adelie.hurt");

        penguin = registry.entity("adelie_penguin", () -> EntityType.Builder
            .of(Penguin::new, MobCategory.CREATURE)
            .sized(0.4f, 0.95f)
            .clientTrackingRange(10)
            .eyeHeight(0.9f));

        spawnEgg = registry.item("adelie_penguin_spawn_egg",
            key -> new SpawnEggItem(penguin.get(), (new Item.Properties().setId(key))));

        registry.mobAttributes(penguin, Penguin::createAttributes);

        registry.mobSpawnPlacement(penguin,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Penguin::canPenguinSpawn);

        registry.biomeSpawn(biomeHolder -> biomeHolder.is(Tags.SPAWNS_PENGUINS),
            MobCategory.CREATURE,
            penguin,
            feature.weight(),
            1,
            feature.maxGroupSize());
    }
}
