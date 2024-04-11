package net.frozenblock.liukrast.claimsplus.api;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class Permissions {
    public static @NotNull Predicate<CommandSourceStack> require(PermissionNode<Boolean> node) {
        return source -> {
            try {
                final ServerPlayer player = source.getPlayerOrException();
                return PermissionAPI.<Boolean>getPermission(player, node);
            } catch (final Exception e) {
                return source.hasPermission(3);
            }
        };
    }
}
