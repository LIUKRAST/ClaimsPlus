package net.frozenblock.liukrast.claimsplus.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface IAnvilMenuMixin {
    @Accessor("itemName")
    String getItemName();
}
