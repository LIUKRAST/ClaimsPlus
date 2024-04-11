package net.frozenblock.liukrast.claimsplus.api;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.frozenblock.liukrast.claimsplus.entry.RegisterSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;

public class DataCommand {
    public static final SimpleCommandExceptionType ERROR = new SimpleCommandExceptionType(Component.literal("§c§lHey! §7Questa data non esiste"));
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("persistentData")
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            RegisterSavedData.SAVED_DATA_MAP.keySet().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> gatherData(ctx.getSource(), StringArgumentType.getString(ctx, "target")))
                                )
                        )
        );
    }

    private static int gatherData(CommandSourceStack sourceStack, String dataName) throws CommandSyntaxException {
        if(!RegisterSavedData.SAVED_DATA_MAP.containsKey(dataName)) throw ERROR.create();
        sourceStack.sendSuccess(() -> Component.literal(dataName + " data: ").append(NbtUtils.toPrettyComponent(RegisterSavedData.SAVED_DATA_MAP.get(dataName).save(new CompoundTag()))), false);
        return 0;
    }
}
