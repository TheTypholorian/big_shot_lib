package net.typho.eye_spy

import com.google.gson.Gson
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.BlockModelRotation
import net.minecraft.client.resources.model.Material
import net.minecraft.client.resources.model.ModelBaker
import net.minecraft.client.resources.model.ModelIdentifier
import net.minecraft.client.resources.model.ModelState
import net.minecraft.client.resources.model.UnbakedModel
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.Items
import net.typho.big_shot_lib.api.client.NeoClientInitializer
import net.typho.big_shot_lib.api.client.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.api.client.event.ModelLoadingEvent
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.plugin.Environment
import net.typho.big_shot_lib.api.plugin.OnlyIn
import net.typho.big_shot_lib.api.util.resource.SingleStepNeoReloadListener
import net.typho.big_shot_lib.mixin.impl.client.assets.model.ModelBakeryAccessor
import java.util.Optional
import java.util.function.Function
import kotlin.collections.component1
import kotlin.collections.component2

@OnlyIn(Environment.CLIENT)
object EyeSpyClient : NeoClientInitializer {
    override val modId: String = EyeSpy.modId

    @JvmField
    val textureLayers = arrayOf(
        TextureSlot.LAYER0,
        TextureSlot.LAYER1,
        TextureSlot.LAYER2,
    )
    @JvmField
    val spyglassModelCache = hashMapOf<SpyglassData, BakedModel>()
    @JvmField
    val spyglassInventoryTemplate = ModelTemplate(
        Optional.of(Identifier.minecraft("item/generated")),
        Optional.empty(),
        textureLayers[0],
        textureLayers[1],
        textureLayers[2]
    )
    @JvmField
    val spyglassInventoryNoLensTemplate = ModelTemplate(
        Optional.of(Identifier.minecraft("item/generated")),
        Optional.empty(),
        textureLayers[0],
        textureLayers[1]
    )

    @JvmStatic
    fun getSpyglassModel(data: SpyglassData): BakedModel {
        return spyglassModelCache.computeIfAbsent(data) {
            var model: UnbakedModel? = null

            val mapping = TextureMapping()
                    .put(textureLayers[0], data.base.textureLocation)
                    .put(textureLayers[1], data.trim.textureLocation)

            if (!data.lens.isEmpty) {
                mapping.put(textureLayers[2], TextureMapping.getItemTexture(data.lens.item, "_in_spyglass"))
            }

            (if (data.lens.isEmpty) spyglassInventoryNoLensTemplate else spyglassInventoryTemplate).create(
                EyeSpy.id("spyglass"),
                mapping
            ) { _, json ->
                model = BlockModel.fromString(json.get().toString())
            }
            model!!
            val modelBaker = object : ModelBaker {
                override fun getModel(id: Identifier): UnbakedModel {
                    return (Minecraft.getInstance().modelManager.modelBakery as ModelBakeryAccessor).`big_shot_lib$getModel`(id)
                }

                @Deprecated("Deprecated in Java")
                override fun bake(
                    id: Identifier,
                    state: ModelState
                ): BakedModel {
                    return Minecraft.getInstance().modelManager.getModel(ModelIdentifier.inventory(id))
                }

                override fun bake(
                    id: Identifier,
                    state: ModelState,
                    textures: Function<Material, TextureAtlasSprite>
                ): BakedModel? {
                    return bakeUncached(getModel(id), state, textures)
                }

                override fun getTopLevelModel(id: ModelIdentifier): UnbakedModel? {
                    throw UnsupportedOperationException()
                }

                override fun bakeUncached(
                    model: UnbakedModel,
                    state: ModelState,
                    textures: Function<Material, TextureAtlasSprite>
                ): BakedModel? {
                    return model.bake(this, textures, state)
                }

                override fun getModelTextureGetter(): Function<Material, TextureAtlasSprite> {
                    return Function { material ->
                        Minecraft.getInstance().modelManager.getAtlas(material.atlasLocation()).getSprite(material.texture())
                    }
                }
            }
            model.resolveParents(modelBaker::getModel)
            model.bake(modelBaker, modelBaker.modelTextureGetter, BlockModelRotation.X0_Y0)!!
        }
    }

    override fun onInitializeClient(bus: NeoClientEventBus) {
        bus.register(AddAssetReloadListenersEvent { out ->
            out(object : SingleStepNeoReloadListener {
                override fun onResourceManagerReload(p0: ResourceManager) {
                    spyglassModelCache.clear()
                }

                override val location: Identifier = EyeSpy.id("clear_spyglass_model_cache")
            })
        })
        bus.register(ModelLoadingEvent { out ->
            val spyglassInHandTemplate = ModelTemplate(
                Optional.of(EyeSpy.id("item/spyglass_in_hand")),
                Optional.empty(),
                TextureSlot.LAYER1
            )

            out.register(
                ModelTemplates.TWO_LAYERED_ITEM,
                EyeSpy.id("item/empty_spyglass"),
                TextureMapping.layered(
                    TextureMapping.getItemTexture(Items.SPYGLASS),
                    EyeSpy.id("item/empty_spyglass")
                )
            )
            out.register(
                spyglassInHandTemplate,
                EyeSpy.id("item/empty_spyglass_in_hand"),
                TextureMapping().put(TextureSlot.LAYER1, EyeSpy.id("item/empty_spyglass_model"))
            )

            BuiltInRegistries.ITEM.entrySet().forEach { (key, item) ->
                if (item is LensItem) {
                    out.register(
                        ModelTemplates.TWO_LAYERED_ITEM,
                        item,
                        "_in_spyglass",
                        TextureMapping.layered(
                            TextureMapping.getItemTexture(Items.SPYGLASS),
                            TextureMapping.getItemTexture(item, "_in_spyglass")
                        )
                    )
                    out.register(
                        spyglassInHandTemplate,
                        item,
                        "_in_spyglass_in_hand",
                        TextureMapping().put(TextureSlot.LAYER1, TextureMapping.getItemTexture(item, "_in_spyglass_model"))
                    )
                }
            }
        })
    }
}