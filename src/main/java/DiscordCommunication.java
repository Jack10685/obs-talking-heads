import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DiscordCommunication {
    /**
     * the JDA object for communicating with discord API, everything "starts" here for discord communication
     */
    private static JDA discord;
    /**
     * true once discord api is ready to receive api calls
     */
    private static boolean ready = false;
    private static Guild server;
    /**
     * shortcut for AudioManager pulled when a user types the join command (MessageReceivedEvent.getGuild().getAudioManager())
     */
    private static AudioManager audio;
    private static String leaveCommand = "!thjoin";
    private static String joinCommand = "!thleave";
    private static String token;

    /**
     * sets the token for the discord bot
     * @param token discord bot token, you can get a token here: https://discord.com/developers/applications
     */
    public static void setBotToken(String token) {
        if (discord != null) {
            if (audio != null && audio.getConnectedChannel() != null) {
                audio.closeAudioConnection();
            }
            discord.cancelRequests();
            discord.shutdownNow();
        }
        DiscordCommunication.token = token;
        try {
            discord = JDABuilder.createDefault(token)
                    .addEventListeners(new ReadyListener())
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build();
        } catch(InvalidTokenException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns whether or not the passed in token argument matches the current token
     * @param token token to check for matching
     * @return whether the token matches or not
     */
    public static boolean checkToken(String token) {
        if (DiscordCommunication.token != null && DiscordCommunication.token.equals(token)) {
            return true;
        }
        return false;
    }

    /**
     * Sets the command to make the discord bot join a voice channel
     * @param joinCommand command to join voice channel
     */
    public static void setJoinCommand(String joinCommand) {
        DiscordCommunication.joinCommand = joinCommand;
    }

    /**
     * Sets the command to make the discord bot leave a voice channel
     * @param leaveCommand command to leave voice channel
     */
    public static void setLeaveCommand(String leaveCommand) {
        DiscordCommunication.leaveCommand = leaveCommand;
    }

    /**
     * find user by ID
     * @param id ID of the user
     * @return user associated with ID
     */
    public static User findUserById(String id) {
        if (ready) {
            return discord.getUserById(id);
        }
        return null;
    }

    /**
     * Listens for Ready state
     */
    public static class ReadyListener implements EventListener {

        @Override
        public void onEvent(@NotNull GenericEvent event) {
            if (event instanceof ReadyEvent) {
                ready = true;
                discord.addEventListener(new DiscordEventListener());
            }
        }


    }

    /**
     * gets all the users in the current voice channel, returns an empty list if not connected
     * @return List of users
     */
    public static List<User> getTalkingUsers() {
        if (audio == null) {
            return new ArrayList<>();
        }
        ArrayList<User> ret = new ArrayList<>();
        if (audio.getConnectedChannel() == null) {
            return List.of();
        }
        for (Member member : audio.getConnectedChannel().getMembers()) {
            if (!member.getUser().isBot())
                ret.add(member.getUser());
        }
        return ret;
    }

    /**
     * listens for general Discord Events
     */
    public static class DiscordEventListener extends ListenerAdapter implements AudioReceiveHandler {

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            Message m = event.getMessage();
            if (m.getContentRaw().equals(joinCommand)) {
                // save guild
                server = m.getGuild();
                // join author's vc
                audio = m.getGuild().getAudioManager();
                if (m.getMember().getVoiceState() != null && m.getMember().getVoiceState().getChannel() != null) {
                    //audio.setReceivingHandler(this);
                    audio.openAudioConnection(m.getMember().getVoiceState().getChannel());
                    audio.setReceivingHandler(this);
                } else {
                    m.getChannel().sendMessage(m.getMember().getEffectiveName()+" is not in a voice channel.");
                }
            } else if (m.getContentRaw().equals(leaveCommand)) {
                if (audio != null) {
                    audio.closeAudioConnection();
                }
            }
        }

        /**
         * Sets talking heads to STATE_TALKING or STATE_NOT_TALKING based on received combined audio
         * @param audio audio information
         */
        @Override
        public void handleCombinedAudio(CombinedAudio audio) {
            for (OBSTHObject link : Main.links) {
                if (audio.getUsers().contains(link.getUser())) {
                    link.setTalkingState(OBSTHObject.STATE_TALKING);
                } else {
                    link.setTalkingState(OBSTHObject.STATE_NOT_TALKING);
                }
            }
        }

        @Override
        public boolean canReceiveCombined() {
            return true;
        }
    }


}
