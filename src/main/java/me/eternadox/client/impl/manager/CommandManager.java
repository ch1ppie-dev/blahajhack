package me.eternadox.client.impl.manager;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.manager.Manager;
import me.eternadox.client.impl.event.EventChat;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CommandManager extends Manager {
    private final CopyOnWriteArrayList<Command> commandList = new CopyOnWriteArrayList<>();

    @Override
    public void initialize(){
        super.initialize();
        Reflections reflections = new Reflections("me.eternadox.client.impl");

        for (Class<? extends Command> clazz : reflections.getSubTypesOf(Command.class)){
           try {
               commandList.add(clazz.newInstance());
           } catch (Exception ignored){

           }
        }

    }

    public CopyOnWriteArrayList<Command> getCommandList() {
        return commandList;
    }


    @Subscribe
    public Consumer<EventChat> onChat = e -> {
        if (!e.getMessage().startsWith(".")) return;

        e.setCancelled(true);

        String[] args = e.getMessage().substring(1).split(" ");
        for (Command c : commandList){
            if (c.getName().equalsIgnoreCase(args[0]) || Arrays.stream(c.getAliases()).anyMatch(a -> a.equalsIgnoreCase(args[0]))){
                if (args.length > 1){
                    String[] argsWithoutCommand = Arrays.stream(args).skip(1).collect(Collectors.toList()).toArray(new String[] {});
                    c.onExecute(argsWithoutCommand);
                } else {
                    c.onExecute(new String[] {});
                }
            }
        }
    };



}
