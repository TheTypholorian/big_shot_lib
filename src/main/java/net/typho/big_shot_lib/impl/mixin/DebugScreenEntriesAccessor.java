package net.typho.big_shot_lib.impl.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
//? } else {
/*import net.minecraft.resources.Identifier;
*///? }

//? if <1.21.9 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(DebugScreenEntries.class)
public interface DebugScreenEntriesAccessor {
    @Invoker("register")
    //? if <1.21.11 {
    static ResourceLocation big_shot_lib$register(ResourceLocation location, DebugScreenEntry debugScreenEntry) {
    //? } else {
    /*static Identifier big_shot_lib$register(Identifier identifier, DebugScreenEntry debugScreenEntry) {
    *///? }
        throw new AssertionError();
    }
}
