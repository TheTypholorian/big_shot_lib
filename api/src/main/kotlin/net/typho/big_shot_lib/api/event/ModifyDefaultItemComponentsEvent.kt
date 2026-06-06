package net.typho.big_shot_lib.api.event

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.TypedDataComponent
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer
import java.util.function.Predicate

fun interface ModifyDefaultItemComponentsEvent {
    fun modify(out: Output)

    interface Output {
        fun modify(item: ItemLike, out: Consumer<Builder>)

        fun modify(item: Predicate<Item>, out: Consumer<Builder>)
    }

    interface Builder {
        fun <T : Any> remove(type: DataComponentType<T>)

        fun <T : Any> set(type: DataComponentType<T>, value: T)

        fun <T : Any> set(component: TypedDataComponent<T>) = set(component.type, component.value)
    }
}