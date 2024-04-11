package net.frozenblock.liukrast.claimsplus.entry;

import net.frozenblock.liukrast.claimsplus.ClaimsPlus;
import net.frozenblock.liukrast.claimsplus.api.DataCommand;
import net.frozenblock.liukrast.claimsplus.api.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;

import static net.frozenblock.liukrast.claimsplus.Config.CLAIM_EVENT_CANCELLED;

@Mod.EventBusSubscriber(modid = ClaimsPlus.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onCommandsRegister(final RegisterCommandsEvent event) {
        ClaimCommand.register(event.getDispatcher());
        DataCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPermissionRegistry(final PermissionGatherEvent.Nodes event) {
        event.addNodes(RegisterPermissions.CLAIM);
        event.addNodes(RegisterPermissions.CLAIM_BYPASS);
        event.addNodes(RegisterPermissions.CLAIM_MANAGER);
        event.addNodes(RegisterPermissions.CLAIM_AMOUNT);
    }

    @SubscribeEvent
    public static void loadSavedData(final LevelEvent.Load event) {
        final LevelAccessor levelAccessor = event.getLevel();
        if(!levelAccessor.isClientSide()) {
            final ResourceKey<Level> location = ((ServerLevel) levelAccessor).dimension();
            if (location.equals(Level.OVERWORLD)) {
                RegisterSavedData.register((ServerLevel) levelAccessor);
            }
        }
    }

    // CLAIM EVENTS:

    @SubscribeEvent
    public static void onBreak(final BlockEvent.BreakEvent event) {
        if(Utils.getPermission(event.getPlayer(), event.getPos()).getIndex() < 2) {
            event.setCanceled(true);
            event.getPlayer().sendSystemMessage(Component.literal(CLAIM_EVENT_CANCELLED.get()));
        }

    }


    @SubscribeEvent
    public static void onInteract(final PlayerInteractEvent.EntityInteract event) {
        if(Utils.getPermission(event.getEntity(), event.getTarget().getOnPos()).getIndex() < 1) {
            event.setCanceled(true);
            event.getEntity().sendSystemMessage(Component.literal(CLAIM_EVENT_CANCELLED.get()));
        }
    }

    @SubscribeEvent
    public static void onInteract(final PlayerInteractEvent.RightClickBlock event) {
        if(Utils.getPermission(event.getEntity(), event.getPos()).getIndex() < 1) {
            event.setCanceled(true);
            event.getEntity().sendSystemMessage(Component.literal(CLAIM_EVENT_CANCELLED.get()));
        }
    }

    @SubscribeEvent
    public static void onPlace(final BlockEvent.EntityPlaceEvent event) {
        if(event.getEntity() instanceof Player player) {
            if (Utils.getPermission(player, event.getPos()).getIndex() < 2) {
                event.setCanceled(true);
                player.sendSystemMessage(Component.literal(CLAIM_EVENT_CANCELLED.get()));
            }
        }
    }

    @SubscribeEvent
    public static void onFarmBreak(final BlockEvent.FarmlandTrampleEvent event) {
        if(event.getEntity() instanceof Player player) {
            if (Utils.getPermission(player, event.getPos()).getIndex() < 2) {
                event.setCanceled(true);
                player.sendSystemMessage(Component.literal(CLAIM_EVENT_CANCELLED.get()));
            }
        }
    }

}
