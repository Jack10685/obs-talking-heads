import javafx.fxml.FXMLLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used for loading and saving information to .ini files
 */
public class LocalStorage {
    /**
     * used to load saved settings on startup, if they exist
     */
    public static void loadSettings() {
        try {
            File f = new File("settings.ini");
            if (f.exists()) {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                SettingsStore settings = new SettingsStore();
                br.lines().forEach(line -> {
                    ArrayList<String> split = new ArrayList<>(Arrays.asList(line.split(" ")));
                    String key = split.remove(0);
                    String value = "";
                    for (String str : split) {
                        value+=(str+" ");
                    }
                    value = value.stripTrailing();
                    switch(key) {
                        case "TOKEN":
                            settings.token = value;
                            DiscordCommunication.setBotToken(value);
                            break;
                        case "JOIN":
                            settings.joinCommand = value;
                            DiscordCommunication.setJoinCommand(value);
                            break;
                        case "LEAVE":
                            settings.leaveCommand = value;
                            DiscordCommunication.setLeaveCommand(value);
                            break;
                        case "SCENE":
                            settings.scene = value;
                            OBSCommunication.setScene(value);
                            break;
                        case "SHIFT":
                            settings.shift = value;
                            break;
                        default:
                            break;
                    }
                });
                Main.controller.setSettings(settings);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * saves the current settings into the settings.ini file, called from "save" button on GUI settings page
     * @param settings settings storage variable
     */
    public static void saveSettings(SettingsStore settings) {
        try {
            File f = new File("settings.ini");
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            if (settings.token != null)
                bw.write("TOKEN "+settings.token+"\r\n");
            if (settings.joinCommand != null)
                bw.write("JOIN "+settings.joinCommand+"\r\n");
            if (settings.leaveCommand != null)
                bw.write("LEAVE "+settings.leaveCommand+"\r\n");
            if (settings.scene != null)
                bw.write("SCENE "+settings.scene+"\r\n");
            if (settings.shift != null)
                bw.write("SHIFT "+settings.shift);
            bw.flush();
            bw.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * used for storing settings for LocalStorage to PanelController communication
     */
    public static class SettingsStore {
        public String token, joinCommand, leaveCommand, scene, shift;
    }
}
