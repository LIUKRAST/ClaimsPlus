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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static net.frozenblock.liukrast.claimsplus.Config.*;

public class ManagePlayerMenu extends UnmovableChestMenu {
    private final GameProfile profile;
    private final GameProfile user;
    private final MenuContainer parent;
    private final boolean permit;
    public ManagePlayerMenu(final int syncId, final Inventory inventory, final GameProfile profile, final GameProfile user, final MenuContainer parent, boolean permit) {
        super(MenuType.GENERIC_9x6, syncId, inventory, new SimpleContainer(9 * 6), 6);
        this.profile = profile;
        this.parent = parent;
        this.permit = permit;
        this.user = user;
        setup();
    }

    private void setup() {
        final ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        SkullBlockEntity.updateGameprofile(user, (t) -> head.getOrCreateTag().put(SkullBlockEntity.TAG_SKULL_OWNER, NbtUtils.writeGameProfile(new CompoundTag(), t)));
        Claim claim = ClaimManager.getInstance().getClaim(profile.getId());
        if(claim == null) claim = ClaimManager.getInstance().addClaim(profile.getId(), profile.getName());
        final ClaimPermission permission = claim.getPermission(user.getName());
        final HashMap<String, String> map = new HashMap<>();
        map.put("${user}", user.getName());
        map.put("${role}", permission.getName());
        final String[] lines = Utils.formatting(CLAIM_MODIFY_USER_DESC.get(),map).split("\n");
        final MutableComponent[] array = new MutableComponent[lines.length];
        for(int k = 0; k < lines.length; k++) {
            array[k] = Component.literal(lines[k]);
        }
        Utils.setLore(head, array);
        final int permissionLevel = claim.getPermission(user.getName()).getIndex() - 1;
        final ItemStack a = ClaimPermission.USER.getStack();
        final ItemStack b = ClaimPermission.MEMBER.getStack();
        final ItemStack c = permit ? ClaimPermission.MANAGER.getStack() : new ItemStack(Items.BARRIER);
        if (!permit) c.setHoverName(Utils.ownerAndUser(CLAIM_NOT_MANAGER.get(), profile.getName(), user.getName()));
        switch (permissionLevel) {
            case 0 -> a.enchant(Enchantments.UNBREAKING, 1);
            case 1 -> b.enchant(Enchantments.UNBREAKING, 1);
            case 2 -> c.enchant(Enchantments.UNBREAKING, 1);
        }

        final ItemStack remove = new ItemStack(Items.BARRIER);
        remove.setHoverName(Utils.ownerAndUser(CLAIM_MANAGE_USER_REMOVE.get(), profile.getName(), user.getName()));
        setItem(31, 0, remove);

        setItem(21, 0, a);
        setItem(22, 0, b);
        setItem(23, 0, c);
        setItem(13, 0, head);
        final ItemStack menu = new ItemStack(Items.BARRIER);
        menu.setHoverName(Component.literal(CLAIM_MENU.get()));
        this.setItem(49, 0, menu);
    }

    @Override
    public void clicked(int index, int button, @NotNull ClickType type, @NotNull Player player) {
        Claim claim = ClaimManager.getInstance().getClaim(profile.getId());
        if(claim == null) claim = ClaimManager.getInstance().addClaim(profile.getId(), profile.getName());
        switch (index) {
            case 49 -> player.openMenu(parent.asProvider());
            case 21 -> {
                claim.setPermission(user.getName(), ClaimPermission.USER);
                setup();
            }
            case 22 -> {
                claim.setPermission(user.getName(), ClaimPermission.MEMBER);
                setup();
            }
            case 23 -> {
                if(permit) {
                    claim.setPermission(user.getName(), ClaimPermission.MANAGER);
                    setup();
                } else {
                    player.sendSystemMessage(Utils.owner(CLAIM_NOT_ALLOWED.get(), profile.getName()));
                }
            }
            case 31 -> {
                claim.removePermission(user.getName());
                player.openMenu(parent.asProvider());
            }
        }
    }
}
