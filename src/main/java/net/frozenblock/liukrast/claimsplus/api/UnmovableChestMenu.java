package net.frozenblock.liukrast.claimsplus.api;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class UnmovableChestMenu extends ChestMenu {
    public UnmovableChestMenu(MenuType<?> type, int syncId, Inventory inventory, Container container, int rows) {
        super(type, syncId, inventory, container, rows);

    }

    @Override
    public @NotNull ItemStack quickMoveStack(final @NotNull Player player, final int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(final int index, final int button, final @NotNull ClickType type, final @NotNull Player player) {

    }
}
