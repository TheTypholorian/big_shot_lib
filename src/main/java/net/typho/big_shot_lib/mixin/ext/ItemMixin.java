package net.typho.big_shot_lib.mixin.ext;

import net.minecraft.world.item.Item;
import net.typho.big_shot_lib.api.content.NeoItem;
import net.typho.big_shot_lib.api.ext.ItemExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension {
    @Override
    public @NotNull NeoItem getNeoItem() {
        return null;
    }

    @Override
    public void setNeoItem(@NotNull NeoItem neoItem) {

    }

    @Override
    public @Nullable NeoItem getNeoItemNullable() {
        return null;
    }

    @Override
    public void setNeoItemNullable(@Nullable NeoItem neoItem) {

    }
}
