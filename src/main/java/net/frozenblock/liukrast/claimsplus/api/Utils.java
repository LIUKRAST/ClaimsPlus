package net.frozenblock.liukrast.claimsplus.api;

import net.frozenblock.liukrast.claimsplus.entry.Claim;
import net.frozenblock.liukrast.claimsplus.entry.ClaimManager;
import net.frozenblock.liukrast.claimsplus.entry.ClaimPermission;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {

    public static String formatting(final String value, final HashMap<String, String> params) {
        AtomicReference<String> re = new AtomicReference<>(value);
        params.forEach((a,b) -> re.set(re.get().replace(a, b)));
        return re.get();
    }
    public static Component format(final String value, final HashMap<String, String> params) {
        return Component.literal(formatting(value, params));
    }

    public static Component owner(final String value, final String owner) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("${owner}", owner);
        return format(value, map);
    }

    public static Component ownerAndUser(final String value, final String owner, final String user) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("${owner}", owner);
        map.put("${user}", user);
        return format(value, map);
    }

    public static UUID stringOrNull(String value) {
        try {
            return UUID.fromString(value);
        } catch (final Exception e) {
            return null;
        }
    }

    public static Component count(final String value, final int count) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("${count}", String.valueOf(count));
        return format(value, map);
    }

    public static void setLore(final ItemStack stack, final String text) {
        final String[] array = text.split("\n");
        final MutableComponent[] lore = new MutableComponent[array.length];
        for(int k = 0; k < array.length; k++) {
            lore[k] = Component.literal(array[k]);
        }
        setLore(stack, lore);
    }

    public static void setLore(final ItemStack stack, final MutableComponent[] lore) {
        final ListTag tagList = new ListTag();
        final CompoundTag tag = new CompoundTag();
        for (final MutableComponent component : lore) {
            tagList.add(StringTag.valueOf(Component.Serializer.toJson(component)));
        }
        tag.put("Lore", tagList);
        stack.getOrCreateTag().put("display", tag);
    }

    public static ClaimPermission getPermission(final Player player) {
        return getPermission(player, player.getOnPos());
    }

    public static ClaimPermission getPermission(final Player player, final BlockPos pos) {
        return getPermission(player, pos, player.level().dimension().location());
    }

    public static ClaimPermission getPermission(final Player player, final BlockPos targetPos, final ResourceLocation dimension) {
        final ChunkPos pos = new ChunkPos(targetPos);
        final Claim claim = ClaimManager.getInstance().getClaim(dimension, pos);
        if(claim == null || claim.getOwner() == player.getUUID()) return ClaimPermission.OWNER;
        return claim.getPermission(player.getScoreboardName());
    }
}
