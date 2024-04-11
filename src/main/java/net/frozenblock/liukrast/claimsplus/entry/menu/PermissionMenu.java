package net.frozenblock.liukrast.claimsplus.entry.menu;

import com.mojang.authlib.GameProfile;
import net.frozenblock.liukrast.claimsplus.api.MenuContainer;
import net.frozenblock.liukrast.claimsplus.api.UnmovableChestMenu;
import net.frozenblock.liukrast.claimsplus.api.Utils;
import net.frozenblock.liukrast.claimsplus.entry.Claim;
import net.frozenblock.liukrast.claimsplus.entry.ClaimManager;
import net.frozenblock.liukrast.claimsplus.entry.ClaimPermission;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.frozenblock.liukrast.claimsplus.Config.*;

public class PermissionMenu extends UnmovableChestMenu {

    private static final int WIDTH = 7,HEIGHT = 4;

    private final GameProfile profile;
    private final MenuContainer parent;
    private final boolean permit;
    private int start = 0;
    public PermissionMenu(final int syncId, final Inventory inventory, final GameProfile profile, final MenuContainer parent, boolean permit) {
        super(MenuType.GENERIC_9x6, syncId, inventory, new SimpleContainer(9 * 6), 6);
        this.profile = profile;
        this.parent = parent;
        this.permit = permit;
        setup();
    }

    public void setup() {
        final ItemStack menu = new ItemStack(Items.BARRIER);
        final ItemStack add = new ItemStack(Items.ANVIL);
        menu.setHoverName(Component.literal(CLAIM_MENU.get()));
        add.setHoverName(Component.literal(ADD_USER.get()));
        this.setItem(49, 0, menu);
        this.setItem(48, 0, add);
        buildList();
    }

    private List<String> list = new ArrayList<>();


    private void buildList() {
        if(start > 0) {
            final ItemStack previous = new ItemStack(Items.ARROW);
            previous.setHoverName(Component.literal(CLAIM_PREVIOUS.get()));
            this.setItem(45, 0, previous);
        } else {
            this.setItem(45, 0, ItemStack.EMPTY);
        }

        Claim claim = ClaimManager.getInstance().getClaim(profile.getId());
        if(claim == null) claim = ClaimManager.getInstance().addClaim(profile.getId(), profile.getName());
        final HashMap<String, ClaimPermission> userMap = claim.getMap();
        Claim finalClaim = claim;
        list = userMap.keySet().stream().filter(name -> finalClaim.getPermission(name) != ClaimPermission.SPECTATOR).sorted((o1,o2) -> finalClaim.getPermission(o1).compare(finalClaim.getPermission(o2))).toList();
        int i = 0;
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                final int indexI = i + start;
                if(indexI >= list.size()) {
                    setItem(x + 10 + y * 9, 0, ItemStack.EMPTY);
                } else {
                    final GameProfile temp = new GameProfile(null, list.get(indexI));
                    final ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
                    SkullBlockEntity.updateGameprofile(temp, (t) -> stack.getOrCreateTag().put(SkullBlockEntity.TAG_SKULL_OWNER, NbtUtils.writeGameProfile(new CompoundTag(), t)));
                    final ClaimPermission permission = claim.getPermission(temp.getName());
                    final HashMap<String, String> map = new HashMap<>();
                    map.put("${user}", temp.getName());
                    map.put("${role}", permission.getName());
                    Utils.setLore(stack, Utils.formatting(CLAIM_SELECT_USER_DESC.get(),map));
                    setItem(x + 10 + y * 9, 0, stack);
                }
                i++;
            }
        }
        if(i + start < list.size()) {
            final ItemStack next = new ItemStack(Items.ARROW);
            next.setHoverName(Component.literal(CLAIM_NEXT.get()));
            this.setItem(53, 0, next);
        } else {
            this.setItem(53, 0, ItemStack.EMPTY);
        }
    }

    @Override
    public void clicked(int index, int button, @NotNull ClickType type, @NotNull Player player) {
        switch (index) {
            case 49 -> player.openMenu(parent.asProvider());
            case 45 -> {
                if(start > 0) {
                    start-=WIDTH*HEIGHT;
                    buildList();
                }
            }
            case 53 -> {
                if(start + 1 < list.size()) {
                    start+=WIDTH*HEIGHT;
                    buildList();
                }
            }
            case 48 -> {
                final Component title = Utils.owner(CLAIM_ADD_PLAYER_TITLE.get(), profile.getName());
                player.openMenu(new SimpleMenuProvider((a,b,c) -> new AddPlayerMenu(a,b, profile, permit, new MenuContainer(copy(), title)), title));
            }
        }

        if(index > 9 && index < 9 + WIDTH*HEIGHT) {
            final int finalI = index - 10 + start;
            if(finalI < list.size()) {
                final String name = list.get(finalI);
                final HashMap<String, String> map = new HashMap<>();
                map.put("${owner}", profile.getName());
                map.put("${user}", name);
                Claim claim = ClaimManager.getInstance().getClaim(profile.getId());
                if(claim == null) claim = ClaimManager.getInstance().addClaim(profile.getId(), profile.getName());
                if(permit || claim.getPermission(name).getIndex() < 3) {
                    player.openMenu(new SimpleMenuProvider((a, b, c) -> new ManagePlayerMenu(a, b, profile, new GameProfile(null, name), parent,
                            permit), Utils.format(CLAIM_MANAGE_USER_TITLE.get(), map)));
                } else {
                    player.sendSystemMessage(Utils.ownerAndUser(CLAIM_NOT_MANAGER.get(), profile.getName(), name));
                }
            }
        }
    }

    private MenuConstructor copy() {
        return (a,b,c) -> new PermissionMenu(a,b, profile, parent, permit);
    }
}
