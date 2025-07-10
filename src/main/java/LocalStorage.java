/*import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;*/

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used for loading and saving information to .ini files
 */
public class LocalStorage {

    /**
     * saves the talking heads information in a json-formatted file
     */
    public static void saveTalkingHeads() {
        try {
            Gson json = new GsonBuilder()
                    .registerTypeAdapter(OBSTHObject.class, new THSerializer())
                    .create();

            FileWriter fw = new FileWriter("heads.json");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toJson(Main.links));
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTalkingHeads() {
        try {
            Gson json = new GsonBuilder()
                    .registerTypeAdapter(OBSTHObject.class, new THDeserializer())
                    .create();

            String str = Files.readString(Path.of("heads.json"));

            OBSTHObject[] objs = json.fromJson(str, OBSTHObject[].class);
            Main.links = (ArrayList<OBSTHObject>) Arrays.asList(objs);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


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

    public static class THSerializer implements JsonSerializer<OBSTHObject> {

        @Override
        public JsonElement serialize(OBSTHObject obsthObject, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            obj.addProperty("userID", obsthObject.getUser().getId());
            obj.addProperty("itemName", obsthObject.getOBSItemName());
            obj.addProperty("offset", obsthObject.getOffset());
            return obj;
        }
    }

    public static class THDeserializer implements JsonDeserializer<OBSTHObject> {

        @Override
        public OBSTHObject deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            return new OBSTHObject(obj.get("userID").getAsString(), obj.get("itemName").getAsString(), obj.get("offset").getAsInt());
        }
    }

}
