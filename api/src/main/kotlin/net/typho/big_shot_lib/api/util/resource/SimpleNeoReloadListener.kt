package net.typho.big_shot_lib.api.util.resource

import net.minecraft.server.packs.resources.SimplePreparableReloadListener

abstract class SimpleNeoReloadListener<T> : SimplePreparableReloadListener<T>(), NeoReloadListener