package net.typho.big_shot_lib.api.client.event

import com.google.gson.JsonElement
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

interface BlockModelLoadingEvent {
    fun loadModels(out: ModelOutput)

    fun loadStates(out: StateOutput)

    interface ModelOutput {
        fun register(location: Identifier, model: () -> BlockModel)

        fun registerJson(location: Identifier, model: () -> JsonElement) {
            register(location) {
                BlockModel.fromString(model().toString())
            }
        }

        fun register(template: ModelTemplate, block: () -> Block, textures: () -> TextureMapping) {
            template.create(block(), textures()) { location, json -> registerJson(location) { json.get() } }
        }

        fun register(template: ModelTemplate, location: Identifier, textures: () -> TextureMapping) {
            template.create(location, textures()) { location, json -> registerJson(location) { json.get() } }
        }
    }

    interface StateOutput {
        fun register(location: Identifier, state: () -> BlockStateGenerator) {
            registerJson(location) { state().get() }
        }

        fun registerJson(location: Identifier, state: () -> JsonElement)

        fun register(block: Block, state: () -> BlockStateGenerator) {
            register(BuiltInRegistries.BLOCK.getKey(block), state)
        }

        fun registerJson(block: Block, state: () -> JsonElement) {
            registerJson(BuiltInRegistries.BLOCK.getKey(block), state)
        }
    }
}