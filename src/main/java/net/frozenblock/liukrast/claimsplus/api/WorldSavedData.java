package net.frozenblock.liukrast.claimsplus.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.function.BiFunction;

public abstract class WorldSavedData extends SavedData {

    private final ServerLevel level;
    public WorldSavedData(CompoundTag ignoreNbt, ServerLevel level) {
        this.level = level;
    }

    public static <T extends WorldSavedData> T create(ServerLevel level, BiFunction<CompoundTag, ServerLevel, T> function) {
        return level.getDataStorage().computeIfAbsent(nbt -> function.apply(nbt, level), () -> function.apply(new CompoundTag(), level), function.apply(new CompoundTag(), level).getName());
    }

    public void reset() {
        clear();
        write();
    }

    public abstract void clear();

    public void write() {
        this.setDirty();
        this.level.getDataStorage().set(getName(), this);
        this.level.getDataStorage().save();
    }

    public abstract String getName();
}
