package net.frozenblock.liukrast.claimsplus.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionType;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

public class PermissionRegistry {

    public static PermissionNode<Boolean> registerBoolean(final ResourceLocation location) {
        return registerPermission(location, PermissionTypes.BOOLEAN, (user, uuid, ctx) -> user == null || user.hasPermissions(3));
    }

    public static PermissionNode<Integer> registerInteger(final ResourceLocation location, final int opDefault) {
        return registerPermission(location, PermissionTypes.INTEGER, (user, uuid, ctx) -> user == null ? 0 : user.hasPermissions(3) ? opDefault : 0);
    }

    public static <T> PermissionNode<T> registerPermission(final ResourceLocation location, final PermissionType<T> type, final PermissionNode.PermissionResolver<T> lambda) {
        return new PermissionNode<>(location.getNamespace(), location.getPath(), type, lambda);
    }
}
