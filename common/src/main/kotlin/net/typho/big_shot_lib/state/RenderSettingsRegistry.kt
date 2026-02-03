package net.typho.big_shot_lib.state

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.files.ResourceRegistry

object RenderSettingsRegistry : ResourceRegistry<NeoRenderSettings>("render_settings") {
    override fun decode(
        id: ResourceLocation,
        json: JsonObject,
        manager: ResourceManager
    ) = NeoRenderSettings(id, RenderSettingsInfo.CODEC.codec().decode(JsonOps.INSTANCE, json).orThrow.first)
}