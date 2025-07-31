package me.eternadox.client.api.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.media.jfxmedia.logging.Logger;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.setting.Setting;
import me.eternadox.client.impl.Client;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class ConfigReader {
    private static final String CONFIGS_PATH = "blahajhack/configs/";
    private static final Gson GSON = new Gson();

    public boolean loadConfig(String path) {
        JsonObject object = parseConfig(path);
        if (object == null || object.isJsonNull())
            return false;

        Client.INSTANCE.setName(object.get("clientName").getAsString());

        for (JsonElement m : object.get("modules").getAsJsonArray().asList()) {
            JsonObject moduleJsonObject = m.getAsJsonObject();
            Module module = Client.INSTANCE.getModuleManager().getModule(moduleJsonObject.get("displayName").getAsString());

            module.setKey(moduleJsonObject.get("bind").getAsInt());

            if (module.isToggled() != moduleJsonObject.get("toggled").getAsBoolean()) {
                module.toggle();
            }

            for (JsonElement s : moduleJsonObject.get("settings").getAsJsonArray().asList()){
                JsonObject settingJsonObject = s.getAsJsonObject();

                Optional<Setting> matchingSetting = module.getSettings().stream().filter(sg -> sg.getName().equalsIgnoreCase(settingJsonObject.get("settingName").getAsString())).findFirst();

                if (matchingSetting.isPresent()) {
                    Setting setting = matchingSetting.get();

                    setting.setValueJson(settingJsonObject.get("settingValue"));

                }

            }

        }

        if (!object.get("clientVersion").getAsString().equals(Client.INSTANCE.getVersion())) {
            Logger.logMsg(Logger.WARNING, "The config loaded may be incompatible with this version of the client.");
        }

        return true;

    }

    public boolean configExists(String fileName) {
        return new File(CONFIGS_PATH + fileName).exists();
    }

    public boolean deleteConfig(String fileName) {
        return new File(CONFIGS_PATH + fileName).delete();
    }


    public boolean createConfig(String fileName) {
        JsonObject configJsonObject = new JsonObject();

        configJsonObject.addProperty("clientName", Client.INSTANCE.getName());
        configJsonObject.addProperty("clientVersion", Client.INSTANCE.getVersion());
        configJsonObject.add("modules", parseModules());

        File configFile = new File(CONFIGS_PATH + fileName);

        if (fileName.contains("../"))
            return false;

        deleteConfig(fileName);

        try {
            if (!configFile.createNewFile()) return false;

            FileWriter configWriter = new FileWriter(CONFIGS_PATH + fileName);

            configWriter.write(GSON.newBuilder().setPrettyPrinting().create().toJson(configJsonObject));
            configWriter.close();

        } catch (IOException e) {
            return false;

        }

        return true;


    }


    private JsonArray parseModules() {
        JsonArray modules = new JsonArray();

        for (Module module : Client.INSTANCE.getModuleManager().getModuleList()) {
            JsonObject moduleJsonObject = module.convertToJson();

            moduleJsonObject.add("settings", parseSettings(module.getSettings()));

            modules.add(moduleJsonObject);
        }

        return modules;
    }

    private JsonArray parseSettings(List<Setting> settingList) {
        JsonArray settings = new JsonArray();

        for (Setting s : settingList) {
            settings.add(s.convertToJson());
        }

        return settings;
    }

    private JsonObject parseConfig(String path) {
        String plainTextJson;

        try {
            FileInputStream inputStream = new FileInputStream(CONFIGS_PATH + path);
            StringBuilder builder = new StringBuilder();

            for (String line : IOUtils.readLines(inputStream)) {
                builder.append(line);
            }

            plainTextJson = builder.toString();

        } catch (Exception e) {
            return null;
        }

        return GSON.fromJson(plainTextJson, JsonObject.class);


    }

}
