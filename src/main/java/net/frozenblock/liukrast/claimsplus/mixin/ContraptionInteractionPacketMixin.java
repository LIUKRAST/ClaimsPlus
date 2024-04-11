package net.frozenblock.liukrast.claimsplus.mixin;

import com.simibubi.create.content.contraptions.sync.ContraptionInteractionPacket;
import net.frozenblock.liukrast.claimsplus.api.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.frozenblock.liukrast.claimsplus.Config.CLAIM_EVENT_CANCELLED;

@Mixin(ContraptionInteractionPacket.class)
public class ContraptionInteractionPacketMixin {
    @Shadow private BlockPos localPos;

    @Inject(at = @At("HEAD"), method = "handle", cancellable = true, remap = false)
    private void cancelIfNecessary(NetworkEvent.Context context, CallbackInfoReturnable<Boolean> cir) {
        final ServerPlayer player = context.getSender();
        if(Utils.getPermission(player, localPos).getIndex() < 1) {
            player.sendSystemMessage(Component.literal(CLAIM_EVENT_CANCELLED.get()));
            cir.cancel();
        }
    }
}
