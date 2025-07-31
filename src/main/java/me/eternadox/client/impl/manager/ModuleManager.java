package me.eternadox.client.impl.manager;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.manager.Manager;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.util.Methods;
import me.eternadox.client.impl.event.EventKey;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModuleManager extends Manager {
    private final CopyOnWriteArrayList<Module> moduleList = new CopyOnWriteArrayList<>();
    @Override
    public void initialize(){
        super.initialize();
        Reflections reflections = new Reflections("me.eternadox.client.impl");

        for (Class<? extends Module> clazz : reflections.getSubTypesOf(Module.class)){
            try {
                moduleList.add(clazz.newInstance());
            } catch (Exception ignored){

            }
        }

    }

    public CopyOnWriteArrayList<Module> getModuleList() {
        return moduleList;
    }

    public List<Module> getEnabledModuleList() {
        return moduleList.stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    public List<Module> getModuleListSorted() {
        List<Module> sortedList = moduleList.stream().sorted(Comparator.comparingInt(m -> -Methods.mc.fontRendererObj.getStringWidth(m.getRenderText()))).collect(Collectors.toList());
        return sortedList;
    }

    public List<Module> getEnabledModuleListSorted() {
        return getModuleListSorted().stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    @Subscribe
    public Consumer<EventKey> onKey = e -> {
        for (Module m : moduleList){
            if (m.getKey() == e.getKey()){
                m.toggle();
            }
        }
    };

    public Module getModule(String moduleName){
        List<Module> modules = getModuleList().stream().filter(m -> m.getDisplayName().replace(" ", "").equalsIgnoreCase(moduleName.replace(" ", ""))).collect(Collectors.toList());

        if (!modules.isEmpty())
            return modules.get(0);
        else
            return null;

    }


}
