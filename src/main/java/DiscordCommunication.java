import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class DiscordCommunication {
    private static String token;
    private static JDA discord;
    private static boolean ready = false;

    /**
     * sets the token for the discord bot
     * @param token discord bot token, you can get a token here: https://discord.com/developers/applications
     */
    public static void setBotToken(String token) {
        DiscordCommunication.token = token;
        discord = JDABuilder.createDefault(token)
                .addEventListeners(new DiscordEventListener())
                .build();
    }

    /**
     * Listens for Discord Events we want to watch for (currently just ready state listener)
     */
    public static class DiscordEventListener implements EventListener {

        @Override
        public void onEvent(@NotNull GenericEvent event) {
            if (event instanceof ReadyEvent) {
                ready = true;
            }
        }
    }
}
