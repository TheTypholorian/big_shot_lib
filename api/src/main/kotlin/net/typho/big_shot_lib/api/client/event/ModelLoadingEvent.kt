package net.typho.big_shot_lib.api.client.event

import com.google.gson.JsonElement
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.client.resources.model.UnbakedModel
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

fun interface ModelLoadingEvent {
    fun load(out: Output)

    interface Output {
        fun register(location: Identifier, model: BlockModel)

        fun registerModelJson(location: Identifier, model: JsonElement) {
            register(location, BlockModel.fromString(model.toString()))
        }

        fun register(template: ModelTemplate, block: Block, textures: TextureMapping): Identifier {
            return template.create(ModelLocationUtils.getModelLocation(block), textures) { location, json -> registerModelJson(location, json.get()) }
        }

        fun register(template: ModelTemplate, block: Block, suffix: String, textures: TextureMapping): Identifier {
            return template.create(ModelLocationUtils.getModelLocation(block, suffix), textures) { location, json -> registerModelJson(location, json.get()) }
        }

        fun register(template: ModelTemplate, item: Item, textures: TextureMapping): Identifier {
            return template.create(ModelLocationUtils.getModelLocation(item), textures) { location, json -> registerModelJson(location, json.get()) }
        }

        fun register(template: ModelTemplate, item: Item, suffix: String, textures: TextureMapping): Identifier {
            return template.create(ModelLocationUtils.getModelLocation(item, suffix), textures) { location, json -> registerModelJson(location, json.get()) }
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
}