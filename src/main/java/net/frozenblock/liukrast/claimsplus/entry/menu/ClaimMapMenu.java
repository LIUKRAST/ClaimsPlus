package net.frozenblock.liukrast.claimsplus.entry.menu;

import net.frozenblock.liukrast.claimsplus.api.UnmovableChestMenu;
import net.frozenblock.liukrast.claimsplus.entry.Claim;
import net.frozenblock.liukrast.claimsplus.entry.ClaimManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;

import java.util.UUID;

public final class ClaimMapMenu extends UnmovableChestMenu {
    private final ResourceLocation dimension;
    private final ChunkPos pos;
    private final UUID player;

    public ClaimMapMenu(final int syncId, final Inventory inventory, final ResourceLocation dimension, final ChunkPos pos, final UUID owner) {
        super(MenuType.GENERIC_9x6, syncId, inventory, new SimpleContainer(9 * 6), 6);
        this.dimension = dimension;
        this.pos = pos;
        this.player = owner;
        setup();
    }

    private void setup() {
        //Only instance a single itemStack. No need to make more of them
        //TODO: Configurable items
        final ItemStack backGround = new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
        final ItemStack yourClaim = new ItemStack(Items.LIME_STAINED_GLASS_PANE);

        //TODO: Configurable texts
        backGround.setHoverName(Component.literal(""));
        yourClaim.setHoverName(Component.literal("§aYour §7Claim"));
        int i = 0;
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 6; col++) {

                final int fr = row-pos.x;
                final int fc = col-pos.z;

                final Claim claim = ClaimManager.getInstance().getClaim(dimension, new ChunkPos(fr, fc));
                final ItemStack stack;
                if(claim == null) stack = backGround;
                else if(claim.getOwner() == player) stack = yourClaim;
                else {
                    stack = new ItemStack(Items.RED_STAINED_GLASS_PANE);
                    stack.setHoverName(Component.literal(claim.getDisplay() + "'s claim"));
                }
                this.setItem(i, 0, stack);
                i++;
            }
        }
    }
}
