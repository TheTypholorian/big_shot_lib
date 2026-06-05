package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.typho.big_shot_lib.api.BigShotApi.toShortString

@Suppress("UNCHECKED_CAST")
open class BlockSetTypeFactory : ContentFactory<BlockSetType> {
    override val registry: ResourceKey<Registry<BlockSetType>> = ResourceKey.createRegistryKey(Identifier.minecraft("block_set_type"))

    override fun begin(key: Identifier): Builder<*> {
        return BuilderImpl(ResourceKey.create(registry, key))
    }

    private class BuilderImpl(
        key: ResourceKey<BlockSetType>
    ) : Builder<BuilderImpl>(key)

    open class Builder<B : Builder<B>>(
        @JvmField
        val key: ResourceKey<BlockSetType>
    ) : ObjectBuilder<RegisteredObject<BlockSetType>> {
        @JvmField
        protected var canOpenByHand = true
        @JvmField
        protected var canOpenByWindCharge = true
        @JvmField
        protected var canButtonBeActivatedByArrows = true
        @JvmField
        protected var pressurePlateSensitivity = BlockSetType.PressurePlateSensitivity.EVERYTHING
        @JvmField
        protected var soundType: SoundType = SoundType.WOOD
        @JvmField
        protected var doorCloseSound: SoundEvent = SoundEvents.WOODEN_DOOR_CLOSE
        @JvmField
        protected var doorOpenSound: SoundEvent = SoundEvents.WOODEN_DOOR_OPEN
        @JvmField
        protected var trapdoorCloseSound: SoundEvent = SoundEvents.WOODEN_TRAPDOOR_CLOSE
        @JvmField
        protected var trapdoorOpenSound: SoundEvent = SoundEvents.WOODEN_TRAPDOOR_OPEN
        @JvmField
        protected var pressurePlateClickOffSound: SoundEvent = SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF
        @JvmField
        protected var pressurePlateClickOnSound: SoundEvent = SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON
        @JvmField
        protected var buttonClickOffSound: SoundEvent = SoundEvents.WOODEN_BUTTON_CLICK_OFF
        @JvmField
        protected var buttonClickOnSound: SoundEvent = SoundEvents.WOODEN_BUTTON_CLICK_ON

        fun canOpenByHand(canOpenByHand: Boolean): B {
            this.canOpenByHand = canOpenByHand
            return this as B
        }

        fun canOpenByWindCharge(canOpenByWindCharge: Boolean): B {
            this.canOpenByWindCharge = canOpenByWindCharge
            return this as B
        }

        fun canButtonBeActivatedByArrows(canButtonBeActivatedByArrows: Boolean): B {
            this.canButtonBeActivatedByArrows = canButtonBeActivatedByArrows
            return this as B
        }

        fun pressurePlateSensitivity(pressurePlateSensitivity: BlockSetType.PressurePlateSensitivity): B {
            this.pressurePlateSensitivity = pressurePlateSensitivity
            return this as B
        }

        fun soundType(soundType: SoundType): B {
            this.soundType = soundType
            return this as B
        }

        fun doorCloseSound(doorCloseSound: SoundEvent): B {
            this.doorCloseSound = doorCloseSound
            return this as B
        }

        fun doorOpenSound(doorOpenSound: SoundEvent): B {
            this.doorOpenSound = doorOpenSound
            return this as B
        }

        fun trapdoorCloseSound(trapdoorCloseSound: SoundEvent): B {
            this.trapdoorCloseSound = trapdoorCloseSound
            return this as B
        }

        fun trapdoorOpenSound(trapdoorOpenSound: SoundEvent): B {
            this.trapdoorOpenSound = trapdoorOpenSound
            return this as B
        }

        fun pressurePlateClickOffSound(pressurePlateClickOffSound: SoundEvent): B {
            this.pressurePlateClickOffSound = pressurePlateClickOffSound
            return this as B
        }

        fun pressurePlateClickOnSound(pressurePlateClickOnSound: SoundEvent): B {
            this.pressurePlateClickOnSound = pressurePlateClickOnSound
            return this as B
        }

        fun buttonClickOffSound(buttonClickOffSound: SoundEvent): B {
            this.buttonClickOffSound = buttonClickOffSound
            return this as B
        }

        fun buttonClickOnSound(buttonClickOnSound: SoundEvent): B {
            this.buttonClickOnSound = buttonClickOnSound
            return this as B
        }

        override fun end(): RegisteredObject<BlockSetType> {
            val type = BlockSetType(
                key.location().toShortString(),
                canOpenByHand,
                canOpenByWindCharge,
                canButtonBeActivatedByArrows,
                pressurePlateSensitivity,
                soundType,
                doorCloseSound,
                doorOpenSound,
                trapdoorCloseSound,
                trapdoorOpenSound,
                pressurePlateClickOffSound,
                pressurePlateClickOnSound,
                buttonClickOffSound,
                buttonClickOnSound
            )

            BlockSetType.register(type)

            return RegisteredObject.Immediate(key, type)
        }
    }
}