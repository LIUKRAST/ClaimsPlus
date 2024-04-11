package net.frozenblock.liukrast.claimsplus.entry;

import net.frozenblock.liukrast.claimsplus.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

public enum ClaimPermission {
    SPECTATOR(0, Items.ENDER_EYE, Config.PERMISSION_SPECTATOR), // Default role. Can only enter a claim. But cannot change any of his values
    USER(1, Items.CHEST, Config.PERMISSION_USER), //Able to interact with things, open doors, trapdoors, but not allowed to break any of them
    MEMBER(2, Items.DIAMOND_PICKAXE, Config.PERMISSION_MEMBER), //Able to place/break blocks in the claim
    MANAGER(3, Items.BRUSH, Config.PERMISSION_MANAGER), //Able to modify other people's rank, cannot add managers though.
    OWNER(4, Items.COMMAND_BLOCK, Config.PERMISSION_OWNER); //Kind of self-explicative

    private final int index;
    private final Item item;
    private final ForgeConfigSpec.ConfigValue<String> name;

    ClaimPermission(final int index, final Item item, final ForgeConfigSpec.ConfigValue<String> name) {
        this.index = index;
        this.item = item;
        this.name = name;
    }

    public static ClaimPermission fromString(final String string) {
        return switch (string) {
            case "spectator" -> SPECTATOR;
            case "user" -> USER;
            case "member" -> MEMBER;
            case "manager" -> MANAGER;
            case "owner" -> OWNER;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case SPECTATOR -> "spectator";
            case USER -> "user";
            case MEMBER -> "member";
            case MANAGER -> "manager";
            case OWNER -> "owner";
        };
    }

    public ItemStack getStack() {
        final ItemStack re = new ItemStack(item);
        re.setHoverName(Component.literal(name.get()));
        return re;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name.get();
    }

    public int compare(final ClaimPermission b) {
        return Integer.compare(this.index, b.index);
    }
}
