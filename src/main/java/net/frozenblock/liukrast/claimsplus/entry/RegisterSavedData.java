package net.frozenblock.liukrast.claimsplus.entry;

import net.frozenblock.liukrast.claimsplus.api.WorldSavedData;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;

public final class RegisterSavedData {
    public static final Map<String, WorldSavedData> SAVED_DATA_MAP = new HashMap<>();

    public static void register(final ServerLevel level) {
        registerSavedData(WorldSavedData.create(level, ClaimManager::new));
        System.out.println("CODICE EUROSPIN");
    }

    public static void registerSavedData(final WorldSavedData savedData) {
        SAVED_DATA_MAP.put(savedData.getName(), savedData);
    }
}
