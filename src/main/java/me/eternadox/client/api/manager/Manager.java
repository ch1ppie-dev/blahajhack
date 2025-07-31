package me.eternadox.client.api.manager;

import me.eternadox.client.impl.Client;

public class Manager {

    public void initialize(){
        Client.INSTANCE.getBus().subscribe(this);
    }




}
