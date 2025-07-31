package me.eternadox.client.api.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Supplier;

public class Setting {
    private final String name;
    private final Supplier<Boolean> visible;

    public Setting(String name, Supplier<Boolean> visible){
        this.name = name;
        this.visible = visible;
    }

    public Setting(String name){
        this.name = name;
        this.visible = () -> true;
    }

    public String getName() {
        return name;
    }

    public Supplier<Boolean> getVisible() {
        return visible;
    }

    public JsonObject convertToJson(){

        JsonObject settingJsonObject = new JsonObject();
        settingJsonObject.addProperty("settingName", this.getName());

        return settingJsonObject;
    }

    public void setValueJson(JsonElement element) {

    }

}
