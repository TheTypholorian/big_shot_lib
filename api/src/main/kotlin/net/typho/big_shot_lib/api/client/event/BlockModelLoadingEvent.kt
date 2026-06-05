package net.typho.big_shot_lib.api.client.event

import com.google.gson.JsonElement
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

fun interface BlockModelLoadingEvent {
    fun load(out: Output)

    interface Output {
        fun register(location: Identifier, model: BlockModel)

        fun registerModelJson(location: Identifier, model: JsonElement) {
            register(location, BlockModel.fromString(model.toString()))
        }

        fun register(template: ModelTemplate, block: Block, textures: TextureMapping): Identifier {
            return template.create(block, textures) { location, json -> registerModelJson(location, json.get()) }
        }

        fun register(template: ModelTemplate, location: Identifier, textures: TextureMapping): Identifier {
            return template.create(location, textures) { location, json -> registerModelJson(location, json.get()) }
        }

        fun register(location: Identifier, state: BlockStateGenerator) {
            registerStateJson(location, state.get())
        }

        fun registerStateJson(location: Identifier, state: JsonElement)

        fun register(block: Block, state: BlockStateGenerator) {
            register(BuiltInRegistries.BLOCK.getKey(block), state)
        }

        fun registerStateJson(block: Block, state: JsonElement) {
            registerStateJson(BuiltInRegistries.BLOCK.getKey(block), state)
        }
    }

    data class Storage(
        @JvmField
        val models: MutableList<Pair<Identifier, BlockModel>> = arrayListOf(),
        @JvmField
        val states: MutableList<Pair<Identifier, JsonElement>> = arrayListOf()
    ) : Output {
        override fun register(
            location: Identifier,
            model: BlockModel
        ) {
            models.add(location to model)
        }

        override fun registerStateJson(
            location: Identifier,
            state: JsonElement
        ) {
            states.add(location to state)
        }
    }
}