package me.eternadox.client.impl.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.eternadox.client.api.setting.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModeSetting extends Setting {
    private final String[] modes;
    private String mode;

    public ModeSetting(String name, Supplier<Boolean> visible, String defaultMode, String... modes){
        super(name, visible);
        this.mode = defaultMode;
        this.modes = modes;
    }

    public ModeSetting(String name, String defaultMode, String... modes){
        super(name);
        this.mode = defaultMode;
        this.modes = modes;
    }

    public String[] getModes() {
        return modes;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        List<String> matchingModes = Arrays.stream(this.modes).filter(m -> m.replace(" ", "").equalsIgnoreCase(mode)).collect(Collectors.toList());
        if (!matchingModes.isEmpty())
            this.mode = matchingModes.get(0);
    }

    public void nextMode() {
        if (findIndexOfMode(mode) == modes.length - 1)
            mode = modes[0];
        else
            mode = modes[findIndexOfMode(mode) + 1];

    }

    public void previousMode() {
        if (findIndexOfMode(mode) == 0)
            mode = modes[modes.length - 1];
        else
            mode = modes[findIndexOfMode(mode) - 1];

    }

    public int findIndexOfMode(String mode)
    {
        int index = Arrays.binarySearch(modes, mode);
        return (index < 0) ? -1 : index;
    }

    @Override
    public JsonObject convertToJson(){

        JsonObject settingJsonObject = new JsonObject();
        settingJsonObject.addProperty("settingName", this.getName());
        settingJsonObject.addProperty("settingValue", this.getMode());

        return settingJsonObject;
    }

    @Override
    public void setValueJson(JsonElement element) {
        setMode(element.getAsString());
    }
}
