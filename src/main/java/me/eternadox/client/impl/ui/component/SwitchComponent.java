package me.eternadox.client.impl.ui.component;

import me.eternadox.client.api.setting.Setting;
import me.eternadox.client.api.ui.component.Component;

public class SwitchComponent extends Component {
    private final Setting parent;


    public SwitchComponent(int x, int y, Setting parent) {
        super(x, y);
        this.parent = parent;
    }




}
