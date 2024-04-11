package net.frozenblock.liukrast.claimsplus.entry;

import net.frozenblock.liukrast.claimsplus.ClaimsPlus;
import net.frozenblock.liukrast.claimsplus.api.PermissionRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public class RegisterPermissions {
    public static final PermissionNode<Boolean> CLAIM = PermissionRegistry.registerBoolean(new ResourceLocation(ClaimsPlus.MOD_ID, "claim"));
    public static final PermissionNode<Boolean> CLAIM_BYPASS = PermissionRegistry.registerBoolean(new ResourceLocation(ClaimsPlus.MOD_ID, "claim_bypass"));
    public static final PermissionNode<Boolean> CLAIM_MANAGER = PermissionRegistry.registerBoolean(new ResourceLocation(ClaimsPlus.MOD_ID, "claim_manager"));
    public static final PermissionNode<Integer> CLAIM_AMOUNT = PermissionRegistry.registerInteger(new ResourceLocation(ClaimsPlus.MOD_ID, "claim_amount"), Integer.MAX_VALUE);
}
