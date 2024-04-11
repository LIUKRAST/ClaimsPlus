package net.frozenblock.liukrast.claimsplus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClaimsPlus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_TITLE = BUILDER.define("claims.menu.title", "${owner}'s Claim: Menu");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_PERMISSION = BUILDER.define("claims.menu.permission", "Claim Permissions");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_MAP = BUILDER.define("claims.menu.map", "Claims Map");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_COUNT = BUILDER.define("claims.menu.status", "Claims status");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_COUNT_LORE = BUILDER.define("claims.menu.status_desc", "Owned claims: ${owned}\nMax: ${max}");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_CLAIM = BUILDER.define("claims.menu.claim", "Claim this chunk");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU_UNCLAIM = BUILDER.define("claims.menu.unclaim", "Unclaim this chunk");

    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_PERMISSION_TITLE = BUILDER.define("claims.permission.title", "${owner}'s Claim: Permissions");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_PREVIOUS = BUILDER.define("claims.previous", "Previous page");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_NEXT = BUILDER.define("claims.next", "Next page");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MENU = BUILDER.define("claims.menu", "Back to menu");
    public static final ForgeConfigSpec.ConfigValue<String> ADD_USER = BUILDER.define("add.user", "Add/Edit user");

    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_ADD_PLAYER_TITLE = BUILDER.define("claims.add_player.title", "${owner}'s Claim: Add Player");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MANAGE_USER_TITLE = BUILDER.define("claims.manage_user.title", "Manage ${user} in ${owner}'s claim");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MANAGE_USER_REMOVE = BUILDER.define("claims.manage_user.remove", "Remove ${user} from ${owner}'s claim");



    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MAP_TITLE = BUILDER.define("claims.map.title", "${owner}'s Claim: Map");

    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_NOT_FOUND = BUILDER.define("claims.not_found", "§c§lHey!§7 This claim does not exist.");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_NOT_ALLOWED = BUILDER.define("claims.not_allowed", "§c§lHey!§7 You are not allowed to manage this claim.");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_NOT_MANAGER = BUILDER.define("claims.not_manager", "§cYou must be ${owner} to set this role");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_LIMIT_REACHED = BUILDER.define("claims.limit_reached", "§c§lHey!§7 ${owner} has reached already his max [${max}] amount of claimed lands!");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_ALREADY_TAKEN = BUILDER.define("claims.already_taken", "§c§lHey!§7 This chunk is already taken by ${owner}");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_NOT_HERE = BUILDER.define("claims.not_here", "§c§lHey!§7 This claim is not yours");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_CLAIMED = BUILDER.define("claims.claimed", "§a§lHey!§7 Successfully§a claimed§7 this chunk [${current}/${max}]");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_UNCLAIMED = BUILDER.define("claims.unclaimed", "§a§lHey!§7 Successfully§c unclaimed§7 this chunk [${current}/${max}]");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_EVENT_CANCELLED = BUILDER.define("claims.event_cancelled", "§a§lHey!§7 You cannot do this action here");

    public static final ForgeConfigSpec.ConfigValue<String> PERMISSION_SPECTATOR = BUILDER.define("claims.permission.spectator", "Spectator");
    public static final ForgeConfigSpec.ConfigValue<String> PERMISSION_USER = BUILDER.define("claims.permission.user", "User");
    public static final ForgeConfigSpec.ConfigValue<String> PERMISSION_MEMBER = BUILDER.define("claims.permission.member", "Member");
    public static final ForgeConfigSpec.ConfigValue<String> PERMISSION_MANAGER = BUILDER.define("claims.permission.manager", "Manager");
    public static final ForgeConfigSpec.ConfigValue<String> PERMISSION_OWNER = BUILDER.define("claims.permission.owner", "Owner");

    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_SELECT_USER_DESC = BUILDER.define("claims.select_user.description", "§cRole: ${role}\n§aPress to modify!");
    public static final ForgeConfigSpec.ConfigValue<String> CLAIM_MODIFY_USER_DESC = BUILDER.define("claims.modify_user.description", "§cRole: ${role}");



    static final ForgeConfigSpec SPEC = BUILDER.build();

}
