package net.typho.big_shot_lib.mixin.impl;

import kotlin.collections.CollectionsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.typho.eye_spy.AttachmentItem;
import net.typho.eye_spy.EyeSpy;
import net.typho.eye_spy.LensItem;
import net.typho.eye_spy.SpyglassData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
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
                    tooltip.add(Component.translatable("eye_spy.lens", data.lens.getHoverName()));
                } else {
                    tooltip.add(Component.translatable("eye_spy.lens", data.lens.getHoverName().copy().withColor(lens.color.toPackedRGB())));
                }
            } else {
                tooltip.add(Component.translatable("eye_spy.no_lens"));
            }

            if (data.attachments.isEmpty()) {
                tooltip.add(Component.translatable("eye_spy.no_attachments"));
            } else {
                tooltip.add(Component.translatable("eye_spy.attachments"));

                for (ItemStack attachment : data.attachments) {
                    tooltip.add(Component.translatable("eye_spy.attachment", attachment.getHoverName()));
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
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, slot.safeTake(1, 1, player)));
                        slot.setByPlayer(ItemStack.EMPTY);
                        eye_spy$playSound(player);
                        return true;
                    } else if (slot.getItem().getCount() == 1) {
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, slot.getItem()));
                        slot.setByPlayer(data.lens);
                        eye_spy$playSound(player);
                        return true;
                    }
                } else if (slot.getItem().getItem() instanceof AttachmentItem) {
                    List<ItemStack> attachments = data == null ? new ArrayList<>() : CollectionsKt.toMutableList(data.attachments);

                    if (attachments.size() < EyeSpy.MAX_ATTACHMENTS) {
                        attachments.add(slot.safeTake(1, 1, player));
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, attachments));
                        slot.setByPlayer(ItemStack.EMPTY);
                        eye_spy$playSound(player);
                        return true;
                    } else if (slot.getItem().getCount() == 1) {
                        ItemStack attachment = attachments.removeLast();
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, attachments));
                        slot.setByPlayer(attachment);
                        eye_spy$playSound(player);
                        return true;
                    }
                }
            } else {
                if (data != null) {
                    if (!data.lens.isEmpty()) {
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, ItemStack.EMPTY));
                        slot.setByPlayer(data.lens);
                        eye_spy$playSound(player);
                        return true;
                    } else if (!data.attachments.isEmpty()) {
                        List<ItemStack> attachments = CollectionsKt.toMutableList(data.attachments);
                        ItemStack attachment = attachments.removeLast();
                        stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, attachments));
                        slot.setByPlayer(attachment);
                        eye_spy$playSound(player);
                        return true;
                    }
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
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            SpyglassData data = stack.get(EyeSpy.spyglassDataComponent.get());

            if (other.getItem() instanceof LensItem) {
                if (data == null || data.lens.isEmpty()) {
                    stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, other.copyWithCount(1)));
                    other.shrink(1);
                    eye_spy$playSound(player);
                    return true;
                }
            } else if (other.getItem() instanceof AttachmentItem) {
                List<ItemStack> attachments = data == null ? new ArrayList<>() : CollectionsKt.toMutableList(data.attachments);

                if (attachments.size() < EyeSpy.MAX_ATTACHMENTS) {
                    attachments.add(other.copyWithCount(1));
                    stack.set(EyeSpy.spyglassDataComponent.get(), new SpyglassData(data, attachments));
                    other.shrink(1);
                    eye_spy$playSound(player);
                    return true;
                }
            }
        }

        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    public void use(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        SpyglassData data = player.getItemInHand(usedHand).get(EyeSpy.spyglassDataComponent.get());

        if (data == null || data.lens.isEmpty()) {
            cir.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(usedHand)));
        } else if (level.isClientSide && player == Minecraft.getInstance().cameraEntity) {
            if (data.lens.is(EyeSpy.creeperLens.get())) {
                Minecraft.getInstance().gameRenderer.loadEffect(Identifier.minecraft("shaders/post/creeper.json"));
            } else if (data.lens.is(EyeSpy.endermanLens.get())) {
                Minecraft.getInstance().gameRenderer.loadEffect(Identifier.minecraft("shaders/post/invert.json"));
            }
        }
    }

    @Inject(
            method = "stopUsing",
            at = @At("HEAD")
    )
    public void stopUsing(LivingEntity user, CallbackInfo ci) {
        if (user.level().isClientSide && user == Minecraft.getInstance().cameraEntity) {
            Minecraft.getInstance().gameRenderer.shutdownEffect();
        }
    }
}
