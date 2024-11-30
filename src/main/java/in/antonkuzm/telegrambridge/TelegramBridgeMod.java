package in.antonkuzm.telegrambridge;


import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod("telegrambridge")
public class TelegramBridgeMod {

    private final TelegramBot telegramBot;

    public TelegramBridgeMod() {
        System.out.println("\n\n\nМод для сервера загружен!\n\n\n");
        NeoForge.EVENT_BUS.register(this);

        telegramBot = new TelegramBot();
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        System.out.println("\n\n\nСервер стартует! Давай делать вещи!\n\n\n");

        telegramBot.sendMessageToTelegram("Server started!");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                net.minecraft.commands.Commands.literal("hello")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal("Привет, это сервер!"), false);
                            return 1;
                        })
        );
    }

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String playerName = player.getName().getString();
        String message = event.getMessage().getString();

        System.out.println("Игрок " + playerName + " отправил сообщение: " + message);

        String telegramMessage = "[" + playerName + "]: " + message;
        telegramBot.sendMessageToTelegram(telegramMessage);
    }
}
