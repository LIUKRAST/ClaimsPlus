package net.frozenblock.liukrast.claimsplus.entry.menu;

import com.mojang.authlib.GameProfile;
import net.frozenblock.liukrast.claimsplus.api.MenuContainer;
import net.frozenblock.liukrast.claimsplus.api.Utils;
import net.frozenblock.liukrast.claimsplus.entry.Claim;
import net.frozenblock.liukrast.claimsplus.entry.ClaimManager;
import net.frozenblock.liukrast.claimsplus.entry.ClaimPermission;
import net.frozenblock.liukrast.claimsplus.mixin.IAnvilMenuMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static net.frozenblock.liukrast.claimsplus.Config.CLAIM_MANAGE_USER_TITLE;

public final class AddPlayerMenu extends AnvilMenu {
    private final GameProfile profile;
    private final boolean permit;
    private final MenuContainer parent;
    public AddPlayerMenu(final int syncId, final Inventory inventory, final GameProfile profile, final boolean permit, MenuContainer parent) {
        super(syncId, inventory);
        this.profile = profile;
        this.permit = permit;
        this.parent = parent;
        setup();
    }

    private void setup() {
        final ItemStack stack = new ItemStack(Items.NAME_TAG);
        stack.setHoverName(Component.literal("Insert name here"));
        setItem(0, 0, stack);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(final @NotNull Player player, final int p_39793_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(final int index, final int button, final @NotNull ClickType type, final @NotNull Player player) {
        if(index == 2) {
            final String text = ((IAnvilMenuMixin)(Object)this).getItemName().toLowerCase();
            Claim claim = ClaimManager.getInstance().getClaim(profile.getId());
            if(claim == null) claim = ClaimManager.getInstance().addClaim(profile.getId(), profile.getName());
            claim.setPermission(text, ClaimPermission.USER);
            final HashMap<String, String> map = new HashMap<>();
            map.put("${owner}", profile.getName());
            map.put("${user}", text);
            player.openMenu(new SimpleMenuProvider((a,b,c) -> new ManagePlayerMenu(a,b,profile,new GameProfile(null, text), parent,
                    permit), Utils.format(CLAIM_MANAGE_USER_TITLE.get(), map)));
        }
    }

    @Override
    public int getCost() {
        return 0;
    }
}
