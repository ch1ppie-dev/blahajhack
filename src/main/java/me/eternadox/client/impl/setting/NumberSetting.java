package me.eternadox.client.impl.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.eternadox.client.api.setting.Setting;

import java.util.function.Supplier;

public class NumberSetting extends Setting {
    private final double min;
    private final double max;
    private final double increment;
    private double value;

    public NumberSetting(String name, Supplier<Boolean> visible, double defaultValue, double min, double max, double increment){
        super(name, visible);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public NumberSetting(String name, double defaultValue, double min, double max, double increment){
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
       this.value = Math.min(max, Math.max(value, min));
    }

    @Override
    public JsonObject convertToJson(){

        JsonObject settingJsonObject = new JsonObject();
        settingJsonObject.addProperty("settingName", this.getName());
        settingJsonObject.addProperty("settingValue", this.getValue());

        return settingJsonObject;
    }

    @Override
    public void setValueJson(JsonElement element) {
        setValue(element.getAsDouble());
    }
}
