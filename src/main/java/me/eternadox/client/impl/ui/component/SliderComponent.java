package me.eternadox.client.impl.ui.component;

import me.eternadox.client.api.setting.Setting;
import me.eternadox.client.api.ui.component.Component;

public class SliderComponent extends Component {
    private final Setting parent;


    public SliderComponent(int x, int y, Setting parent) {
        super(x, y);
        this.parent = parent;
    }




}
