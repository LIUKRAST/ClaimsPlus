package net.frozenblock.liukrast.claimsplus.entry;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.frozenblock.liukrast.claimsplus.api.Permissions;
import net.frozenblock.liukrast.claimsplus.api.Utils;
import net.frozenblock.liukrast.claimsplus.entry.menu.ClaimMenu;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

import java.util.UUID;

import static net.frozenblock.liukrast.claimsplus.Config.*;

public final class ClaimCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("claims")
                        .requires(Permissions.require(RegisterPermissions.CLAIM))
                        .executes(ctx -> openClaimMenu(ctx.getSource(), null))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .executes(ctx -> openClaimMenu(ctx.getSource(), StringArgumentType.getString(ctx, "target").toLowerCase()))
                        )

        );
    }

    private static int openClaimMenu(final CommandSourceStack sourceStack, String owner) throws CommandSyntaxException {
        try{
            final ServerPlayer player = sourceStack.getPlayerOrException();
            final UUID ownerAsUUID = Utils.stringOrNull(owner);
            if(owner == null || (ownerAsUUID != null && ownerAsUUID == player.getUUID()) || owner.equals(player.getScoreboardName().toLowerCase())) {
                final Component title = Utils.owner(CLAIM_MENU_TITLE.get(), player.getScoreboardName());
                player.openMenu(new SimpleMenuProvider((a,b,c) -> new ClaimMenu(a, b, title, gp(player.getUUID(), player.getScoreboardName()), true),
                        title
                ));
            } else {
                final Claim claim = ownerAsUUID == null ? ClaimManager.getInstance().getClaim(owner) : ClaimManager.getInstance().getClaim(ownerAsUUID);
                if(claim == null) throw exception(Utils.owner(CLAIM_NOT_FOUND.get(), owner));
                else if(claim.getPermission(player.getScoreboardName()) != ClaimPermission.MANAGER) throw exception(Utils.owner(CLAIM_NOT_ALLOWED.get(), owner));
                else {
                    final Component title = Utils.owner(CLAIM_MENU_TITLE.get(), claim.getDisplay());
                    final UUID uuid = claim.getOwner();
                    player.openMenu(new SimpleMenuProvider((a,b,c) -> new ClaimMenu(a, b, title, gp(uuid, claim.getDisplay()), false),
                            title
                    ));
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static GameProfile gp(final UUID uuid, final String name) {
        return new GameProfile(uuid, name);
    }

    private static CommandSyntaxException exception(final Component value) {
        return new SimpleCommandExceptionType(value).create();
    }
}
