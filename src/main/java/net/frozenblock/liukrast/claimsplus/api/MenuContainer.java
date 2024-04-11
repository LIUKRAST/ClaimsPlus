package net.frozenblock.liukrast.claimsplus.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuConstructor;


public record MenuContainer(MenuConstructor menu, Component title) {
    public SimpleMenuProvider asProvider() {
        return new SimpleMenuProvider(menu, title);
    }
}
