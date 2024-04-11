package net.frozenblock.liukrast.claimsplus.entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.frozenblock.liukrast.claimsplus.ClaimsPlus;
import net.frozenblock.liukrast.claimsplus.api.WorldSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class ClaimManager extends WorldSavedData {

    public static HashMap<ResourceLocation, HashMap<ChunkPos, UUID>> WORLD_MAP = new HashMap<>();
    public static BiMap<UUID, Claim> CLAIMS = HashBiMap.create();

    private static ClaimManager INSTANCE;

    @NotNull
    public static ClaimManager getInstance() {
        try {
            if(INSTANCE == null) INSTANCE = (ClaimManager) RegisterSavedData.SAVED_DATA_MAP.get("claims");
            return INSTANCE;
        } catch (final NullPointerException e) {
            ClaimsPlus.LOGGER.error("Cannot access instance before saved data instances are registered!");
            throw e;
        }
    }

    public Claim getClaim(ResourceLocation dimension, ChunkPos pos) {
        if(!WORLD_MAP.containsKey(dimension)) return null;
        if(!WORLD_MAP.get(dimension).containsKey(pos)) return null;
        return getClaim(WORLD_MAP.get(dimension).get(pos));
    }

    public boolean addLand(final UUID owner, final ResourceLocation location, final ChunkPos pos) {
        if(!WORLD_MAP.containsKey(location)) WORLD_MAP.put(location, new HashMap<>());
        if(!WORLD_MAP.get(location).containsKey(pos)) {
            WORLD_MAP.get(location).put(pos, owner);
            getClaim(owner).increase();
        }
        else return false;
        return true;
    }

    public int removeLand(final UUID owner, final ResourceLocation location, final ChunkPos pos) {
        if(!WORLD_MAP.containsKey(location)) return 0;
        if(!WORLD_MAP.get(location).containsKey(pos)) return 0;
        if(WORLD_MAP.get(location).get(pos) == owner) {
            WORLD_MAP.get(location).remove(pos);
            getClaim(owner).decrease();
        }
        else return -1;
        return 0;
    }

    public int getClaimedAmount(UUID owner) {
        if(getClaim(owner) == null) return 0;
        return getClaim(owner).getSize();
    }

    public Claim getClaim(final UUID owner) {
        return CLAIMS.get(owner);
    }

    public Claim getClaim(final String name) {
        return CLAIMS.values().stream().filter(claim -> Objects.equals(claim.getDisplay(), name)).findFirst().orElse(null);
    }

    public Claim addClaim(final UUID uuid, final String name) {
        return addClaim(uuid, name, 0, true);
    }

    public Claim addClaim(final UUID uuid, String name, int amount, boolean write) {
        if(CLAIMS.containsKey(uuid)) return getClaim(uuid);
        final Claim claim = new Claim(uuid, name, amount);
        CLAIMS.put(uuid, claim);
        if(write) write();
        return claim;
    }

    @Override
    public void clear() {
        WORLD_MAP.clear();
        CLAIMS.clear();
    }

    public ClaimManager(CompoundTag nbt, ServerLevel level) {
        super(nbt, level);
        final ListTag list = nbt.getList("claims", CompoundTag.TAG_COMPOUND);
        for(int i = 0; i < list.size(); i++) {
            final CompoundTag element = list.getCompound(i);
            final String display = element.getString("display");
            final UUID owner = element.getUUID("owner");
            final ListTag world = element.getList("world", CompoundTag.TAG_COMPOUND);
            for(int k = 0; k < world.size(); k++) {
                final CompoundTag worldElement = world.getCompound(k);
                final ResourceLocation dimension = new ResourceLocation(worldElement.getString("dimension"));
                final int[] pos = worldElement.getIntArray("pos");
                if(!WORLD_MAP.containsKey(dimension)) WORLD_MAP.put(dimension, new HashMap<>());
                WORLD_MAP.get(dimension).put(new ChunkPos(pos[0], pos[1]), owner);
            }
            final Claim claim = new Claim(owner, display, world.size());
            final ListTag claimPermissions = element.getList("permissions", CompoundTag.TAG_COMPOUND);
            for(int k = 0; k < claimPermissions.size(); k++) {
                final CompoundTag permissionElement = claimPermissions.getCompound(k);
                final String name = permissionElement.getAllKeys().stream().toList().get(0);
                final ClaimPermission permission = ClaimPermission.fromString(permissionElement.getString(name));
                claim.setPermission(name, permission, false);
            }
            CLAIMS.put(owner, claim);
        }
    }

    @Override
    public String getName() {
        return "claims";
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        final ListTag list = new ListTag();
        for(final UUID owner : CLAIMS.keySet()) {
            final CompoundTag element = new CompoundTag();
            final Claim claim = getClaim(owner);
            element.putString("display", claim.getDisplay());
            element.putUUID("owner", owner);
            final ListTag world = new ListTag();
            for(final ResourceLocation dimension : WORLD_MAP.keySet()) {
                for(final ChunkPos pos : WORLD_MAP.get(dimension).keySet()) {
                    final UUID check = WORLD_MAP.get(dimension).get(pos);
                    if(!check.equals(owner)) continue;
                    final CompoundTag worldElement = new CompoundTag();
                    worldElement.putString("dimension", dimension.toString());
                    worldElement.putIntArray("pos", new int[]{pos.x, pos.z});
                    world.add(worldElement);
                }
            }
            final ListTag permissionList = new ListTag();
            for(final String user : claim.getMap().keySet()) {
                final ClaimPermission permission = claim.getPermission(user);
                final CompoundTag permissionElement = new CompoundTag();
                permissionElement.putString(user, permission.toString());
                permissionList.add(permissionElement);
            }
            element.put("world", world);
            element.put("permissions", permissionList);
            list.add(element);
        }
        tag.put("claims", list);
        return tag;
    }
}
