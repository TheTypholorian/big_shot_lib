package net.typho.big_shot_lib.mixin.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import net.typho.eye_spy.EyeSpy;
import net.typho.eye_spy.LensItem;
import net.typho.eye_spy.SpyglassData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(SpyglassItem.class)
public abstract class SpyglassItemMixin extends Item {
    public SpyglassItemMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private void eye_spy$playSound(Entity entity) {
        entity.playSound(SoundEvents.SPYGLASS_USE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltip, flag);

        SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

        if (data != null) {
            if (data.lens.getItem() instanceof LensItem lens) {
                if (lens.color == null) {
                    tooltip.add(data.lens.getDisplayName());
                } else {
                    tooltip.add(data.lens.getDisplayName().copy().withColor(lens.color.toPackedRGB()));
                }
            }
        }
    }

    @Override
    public boolean overrideStackedOnOther(
            @NotNull ItemStack stack,
            @NotNull Slot slot,
            @NotNull ClickAction action,
            @NotNull Player player
    ) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

            if (slot.hasItem()) {
                if (slot.getItem().getItem() instanceof LensItem) {
                    if (data == null || data.lens.isEmpty()) {
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(slot.safeTake(1, 1, player)));
                        slot.setByPlayer(ItemStack.EMPTY);
                        eye_spy$playSound(player);
                        return true;
                    } else if (slot.getItem().getCount() == 1) {
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(slot.getItem()));
                        slot.setByPlayer(data.lens);
                        eye_spy$playSound(player);
                        return true;
                    }
                }
            } else {
                if (data != null && !data.lens.isEmpty()) {
                    stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(ItemStack.EMPTY));
                    slot.setByPlayer(data.lens);
                    eye_spy$playSound(player);
                    return true;
                }
            }
        }

        return super.overrideStackedOnOther(stack, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            @NotNull ItemStack stack,
            @NotNull ItemStack other,
            @NotNull Slot slot,
            @NotNull ClickAction action,
            @NotNull Player player,
            @NotNull SlotAccess access
    ) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player) && other.getItem() instanceof LensItem) {
            SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

            if (data == null || data.lens.isEmpty()) {
                stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(other.copyWithCount(1)));
                other.shrink(1);
                eye_spy$playSound(player);
                return true;
            }
        }

        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }
}
