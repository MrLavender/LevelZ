package net.levelz.mixin.misc;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.data.LevelLists;
import net.levelz.init.ConfigInit;
import net.levelz.stats.PlayerStatsManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.registry.Registry;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyVariable(method = "getEquipmentLevel", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"), ordinal = 0)
    private static int getEquipmentLevelMixin(int original, Enchantment enchantment, LivingEntity entity) {
        if (original != 0 && entity instanceof PlayerEntity && (float) ((PlayerStatsManagerAccess) entity).getPlayerStatsManager((PlayerEntity) entity).getLevel("alchemy")
                * ConfigInit.CONFIG.alchemyEnchantmentChance > entity.world.random.nextFloat())
            return original += 1;
        else
            return original;
    }

    @Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
    private static void onTargetDamagedMixin(LivingEntity user, Entity target, CallbackInfo info) {
        if (!(user instanceof PlayerEntity)) return;
        ItemStack itemStack = user.getStackInHand(user.getActiveHand());
        if (itemStack.getItem() instanceof ToolItem) {
            PlayerEntity playerEntity = (PlayerEntity) user;
            ArrayList<Object> levelList = LevelLists.customItemList;
            if (!PlayerStatsManager.playerLevelisHighEnough(playerEntity, levelList, Registry.ITEM.getId(itemStack.getItem()).toString(), true))
                info.cancel();
            else {
                levelList = null;
                if (itemStack.isIn(FabricToolTags.SWORDS)) {
                    levelList = LevelLists.swordList;
                } else if (itemStack.isIn(FabricToolTags.AXES))
                    levelList = LevelLists.axeList;
                else if (itemStack.isIn(FabricToolTags.HOES))
                    levelList = LevelLists.hoeList;
                else if (itemStack.isIn(FabricToolTags.PICKAXES) || itemStack.isIn(FabricToolTags.SHOVELS))
                    levelList = LevelLists.toolList;
                if (levelList != null)
                    if (!PlayerStatsManager.playerLevelisHighEnough(playerEntity, levelList, ((ToolItem) itemStack.getItem()).getMaterial().toString().toLowerCase(), true))
                        info.cancel();
            }
        }
    }

    @Inject(method = "onUserDamaged", at = @At("HEAD"), cancellable = true)
    private static void onUserDamagedMixin(LivingEntity user, Entity attacker, CallbackInfo info) {
        if (!(user instanceof PlayerEntity)) return;
        ItemStack itemStack = user.getStackInHand(user.getActiveHand());
        if (itemStack.getItem() instanceof ToolItem) {
            PlayerEntity playerEntity = (PlayerEntity) user;
            ArrayList<Object> levelList = LevelLists.customItemList;
            if (!PlayerStatsManager.playerLevelisHighEnough(playerEntity, levelList, Registry.ITEM.getId(itemStack.getItem()).toString(), true))
                info.cancel();
            else {
                levelList = null;
                if (itemStack.isIn(FabricToolTags.SWORDS)) {
                    levelList = LevelLists.swordList;
                } else if (itemStack.isIn(FabricToolTags.AXES))
                    levelList = LevelLists.axeList;
                else if (itemStack.isIn(FabricToolTags.HOES))
                    levelList = LevelLists.hoeList;
                else if (itemStack.isIn(FabricToolTags.PICKAXES) || itemStack.isIn(FabricToolTags.SHOVELS))
                    levelList = LevelLists.toolList;
                if (levelList != null)
                    if (!PlayerStatsManager.playerLevelisHighEnough(playerEntity, levelList, ((ToolItem) itemStack.getItem()).getMaterial().toString().toLowerCase(), true))
                        info.cancel();
            }
        }
    }
}
