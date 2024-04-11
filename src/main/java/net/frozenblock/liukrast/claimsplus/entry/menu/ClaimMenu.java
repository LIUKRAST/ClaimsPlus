package net.frozenblock.liukrast.claimsplus.entry.menu;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.Hash;
import net.frozenblock.liukrast.claimsplus.api.MenuContainer;
import net.frozenblock.liukrast.claimsplus.api.Utils;
import net.frozenblock.liukrast.claimsplus.entry.Claim;
import net.frozenblock.liukrast.claimsplus.entry.ClaimManager;
import net.frozenblock.liukrast.claimsplus.entry.RegisterPermissions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraftforge.server.permission.PermissionAPI;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static net.frozenblock.liukrast.claimsplus.Config.*;

public class ClaimMenu extends HopperMenu {
    private final GameProfile profile;
    private final boolean permit;
    private final Component title;
    public ClaimMenu(final int syncId, final Inventory inv, final Component title, final GameProfile profile, boolean permit) {
        super(syncId, inv);
        this.permit = permit;
        this.profile = profile;
        this.title = title;
        this.setup();
    }

    private void setup() {
        final ItemStack permission = new ItemStack(Items.OAK_DOOR);
        final ItemStack map = new ItemStack(Items.MAP);
        final ItemStack claim = new ItemStack(Items.GRASS_BLOCK);
        final ItemStack unclaim = new ItemStack(Items.BARRIER);
        final ItemStack claimCount = new ItemStack(Items.PLAYER_HEAD);
        permission.setHoverName(Component.literal(CLAIM_MENU_PERMISSION.get()).withStyle(s -> s.withItalic(false)));
        map.setHoverName(Component.literal(CLAIM_MENU_MAP.get()).withStyle(s -> s.withItalic(false)));
        claim.setHoverName(Component.literal(CLAIM_MENU_CLAIM.get()).withStyle(s -> s.withItalic(false)));
        unclaim.setHoverName(Component.literal(CLAIM_MENU_UNCLAIM.get()).withStyle(s -> s.withItalic(false)));
        claimCount.setHoverName(Utils.format(CLAIM_MENU_COUNT.get(), new HashMap<>()));
        SkullBlockEntity.updateGameprofile(profile, (t) -> claimCount.getOrCreateTag().put(SkullBlockEntity.TAG_SKULL_OWNER, NbtUtils.writeGameProfile(new CompoundTag(), t)));
        final HashMap<String, String> loreMap = new HashMap<>();
        loreMap.put("${owned}", String.valueOf(ClaimManager.getInstance().getClaimedAmount(profile.getId())));
        loreMap.put("${max}", String.valueOf(PermissionAPI.getOfflinePermission(profile.getId(), RegisterPermissions.CLAIM_AMOUNT)));
        Utils.setLore(claimCount, Utils.formatting(CLAIM_MENU_COUNT_LORE.get(), loreMap));
        this.setItem(0, 0, claim);
        this.setItem(1, 0, permission);
        this.setItem(2, 0, claimCount);
        this.setItem(3, 0, map);
        this.setItem(4, 0, unclaim);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(final @NotNull Player player, final int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(final int index, final int button, final @NotNull ClickType type, final @NotNull Player player) {
        final int max = PermissionAPI.getOfflinePermission(profile.getId(), RegisterPermissions.CLAIM_AMOUNT);
        final int current = ClaimManager.getInstance().getClaimedAmount(profile.getId());
        switch (index) {
            case 0 -> {
                if(current < max) {
                    boolean added = ClaimManager.getInstance().addLand(profile.getId(), player.level().dimension().location(), new ChunkPos(player.getOnPos()));
                    if(!added) {
                        player.closeContainer();
                        final Claim claim = ClaimManager.getInstance().getClaim(player.level().dimension().location(), new ChunkPos(player.getOnPos()));
                        final HashMap<String, String> map = new HashMap<>();
                        map.put("${user}", profile.getName());
                        map.put("${owner}", claim.getDisplay());
                        player.sendSystemMessage(Utils.format(CLAIM_ALREADY_TAKEN.get(), map));
                    } else {
                        player.closeContainer();
                        final HashMap<String, String> map = new HashMap<>();
                        map.put("${owner}", profile.getName());
                        map.put("${max}", String.valueOf(max));
                        map.put("${current}", String.valueOf(current + 1));
                        player.sendSystemMessage(Utils.format(CLAIM_CLAIMED.get(), map));
                    }
                } else {
                    player.closeContainer();
                    final HashMap<String, String> map = new HashMap<>();
                    map.put("${owner}", profile.getName());
                    map.put("${max}", String.valueOf(max));
                    map.put("${current}", String.valueOf(current));
                    player.sendSystemMessage(Utils.format(CLAIM_LIMIT_REACHED.get(), map));
                }
            }
            case 1 -> player.openMenu(new SimpleMenuProvider((id, inv, user) -> new PermissionMenu(id, inv, profile, new MenuContainer(copy(), title), permit),
                    Utils.owner(CLAIM_PERMISSION_TITLE.get(), profile.getName())
                    ));
            case 3 -> player.openMenu(new SimpleMenuProvider((id, inv, user) -> new ClaimMapMenu(id, inv, user.level().dimension().location(), new ChunkPos(user.getOnPos()), user.getUUID()),
                    Utils.owner(CLAIM_MAP_TITLE.get(), profile.getName())
            ));
            case 4 -> {
                boolean state = false;
                if(current > 0) {
                    final int tr = ClaimManager.getInstance().removeLand(profile.getId(), player.level().dimension().location(), new ChunkPos(player.getOnPos()));
                    if(tr <= 0) {
                        state = true;
                    } else {
                        player.closeContainer();
                        final HashMap<String, String> map = new HashMap<>();
                        map.put("${owner}", profile.getName());
                        map.put("${max}", String.valueOf(max));
                        map.put("${current}", String.valueOf(current - 1));
                        player.sendSystemMessage(Utils.format(CLAIM_UNCLAIMED.get(), map));
                    }
                } else {
                    state = true;
                }
                if(state) {
                    player.closeContainer();
                    final HashMap<String, String> map = new HashMap<>();
                    map.put("${owner}", profile.getName());
                    map.put("${max}", String.valueOf(max));
                    map.put("${current}", String.valueOf(current));
                    player.sendSystemMessage(Utils.format(CLAIM_NOT_HERE.get(), map));
                }
            }
        }
    }

    private MenuConstructor copy() {
        return (a,b,c) -> new ClaimMenu(a, b, title, profile, permit);
    }
}