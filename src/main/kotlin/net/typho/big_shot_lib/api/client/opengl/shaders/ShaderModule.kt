package net.typho.big_shot_lib.api.client.opengl.shaders

import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegistrationConsumer.Companion.register
import net.typho.big_shot_lib.api.util.RegistrationFactory
import net.typho.big_shot_lib.api.util.RegistryFactory
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import kotlin.reflect.KClass

interface ShaderModule {
    val type: Type<*>

    fun loadGLSL(files: ShaderFileResolver, key: ShaderProgramKey, source: ShaderSourceType): String? {
        return files.loadFile(type.location, source)
    }

    interface Type<M : ShaderModule> : NamedResource {
        val codec: MapCodec<M>
        val dependencies: Set<Pair<Type<*>, () -> ShaderModule?>>
    }

    abstract class ServiceType<M : ShaderModule>(
        @JvmField
        val cls: Class<M>
    ) : Type<M> {
        val impl = cls.kotlin.loadService()
        override val codec: MapCodec<M> = MapCodec.unit(impl)

        constructor(cls: KClass<M>) : this(cls.java)
    }

    abstract class UnitType<M : ShaderModule> : ShaderModule, Type<M> {
        override val type: Type<M> = this
        @Suppress("UNCHECKED_CAST")
        override val codec: MapCodec<M> = MapCodec.unit(this as M)
    }

    companion object : BigShotCommonEntrypoint {
        val REGISTRY_KEY = NeoResourceKey.registry<Type<*>>(BigShotApi.id("shader_modules"))
        var REGISTRY: NeoRegistry<Type<*>>? = null
            private set
        val CODEC: MapCodec<ShaderModule> = NeoResourceKey.codec(REGISTRY_KEY).dispatchMap(
            { REGISTRY!!.getKey(it.type) },
            { REGISTRY!!.get(it)?.codec },
        )

        override fun registerRegistries(factory: RegistryFactory) {
            REGISTRY = factory.create(REGISTRY_KEY)
        }

        override fun registerContent(factory: RegistrationFactory) {
            val registrar = factory.begin(REGISTRY_KEY, BigShotApi.MOD_ID)
            registrar.register(FogShaderModule)
            registrar.register(InverseMatricesShaderModule)
            registrar.register(TerrainTextureSampleShaderModule)
            registrar.register(TimeShaderModule)
            registrar.register(VertexMatricesShaderModule)
            registrar.register(ViewportShaderModule)
        }
    }
}