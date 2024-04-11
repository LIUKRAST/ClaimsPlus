package net.frozenblock.liukrast.claimsplus.entry;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Claim {
    private final UUID owner;
    private final String name;
    private final HashMap<String, ClaimPermission> map = new HashMap<>();
    private int amount;

    public Claim(final UUID owner, final String name, int size) {
        this.owner = owner;
        this.name = name;
        this.amount = size;
    }

    public ClaimPermission getPermission(final String user) {
        if(Objects.equals(user, name)) return ClaimPermission.MANAGER;
        final ClaimPermission perm = map.get(user);
        return perm == null ? ClaimPermission.SPECTATOR : perm;
    }

    public HashMap<String, ClaimPermission> getMap() {
        return map;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getDisplay() {
        return name;
    }

    public void setPermission(final String user, final ClaimPermission permission, final boolean mark) {
        map.put(user, permission);
        if(mark) markDirty();
    }

    public void markDirty() {
        ClaimManager.getInstance().write();
    }

    public void setPermission(final String user, final ClaimPermission permission) {
        setPermission(user, permission, true);
    }

    public void removePermission(final String name, final boolean mark) {
        map.remove(name);
        if(mark) markDirty();
    }

    public void removePermission(final String name) {
        removePermission(name, true);
    }

    public void increase() {
        amount++;
    }

    public void decrease() {
        amount--;
    }

    public int getSize() {
        return amount;
    }
}
