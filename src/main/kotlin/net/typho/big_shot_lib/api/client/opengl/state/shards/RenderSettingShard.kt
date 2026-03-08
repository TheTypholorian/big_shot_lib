package net.typho.big_shot_lib.api.client.opengl.state.shards

import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.state.CullShard
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlAdvancedBindable
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegistrationConsumer.Companion.register
import net.typho.big_shot_lib.api.util.RegistrationFactory
import net.typho.big_shot_lib.api.util.RegistryFactory
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey

interface RenderSettingShard : GlAdvancedBindable {
    val type: Type<*>

    interface Type<S : RenderSettingShard> : NamedResource {
        val default: S
        val codec: MapCodec<S>?
    }

    open class Basic(
        override val type: Type<*>,
        @JvmField
        val list: List<GlBindable>
    ) : RenderSettingShard {
        override fun bind(arguments: RenderArguments, pushStack: Boolean): GlBindResult {
            return GlBindResult.Multiple(list.map { it.bind(arguments, pushStack) })
        }

        override fun unbind(popStack: Boolean) {
            list.forEach { it.unbind(popStack) }
        }
    }

    companion object : BigShotCommonEntrypoint {
        val REGISTRY_KEY = NeoResourceKey.registry<Type<*>>(BigShotApi.id("render_setting_shards"))
        var REGISTRY: NeoRegistry<Type<*>>? = null
            private set
        val CODEC: MapCodec<RenderSettingShard> = NeoResourceKey.codec(REGISTRY_KEY).dispatchMap(
            { REGISTRY!!.getKey(it.type) },
            { REGISTRY!!.get(it)?.codec },
        )

        override fun registerRegistries(factory: RegistryFactory) {
            REGISTRY = factory.create(REGISTRY_KEY)
        }

        override fun registerContent(factory: RegistrationFactory) {
            val registrar = factory.begin(REGISTRY_KEY, BigShotApi.MOD_ID)
            registrar.register(BindBufferBaseShard)
            registrar.register(BlendShard)
            registrar.register(ColorMaskShard)
            registrar.register(CullShard)
            registrar.register(DepthMaskShard)
            registrar.register(DepthTestShard)
            registrar.register(DisableFlagsShard)
            registrar.register(EnableFlagsShard)
            registrar.register(FramebufferShard)
            registrar.register(PolygonModeShard)
            registrar.register(PolygonOffsetShard)
            registrar.register(ShaderShard)
            registrar.register(StencilShard)
            registrar.register(VertexArrayShard)
        }
    }
}