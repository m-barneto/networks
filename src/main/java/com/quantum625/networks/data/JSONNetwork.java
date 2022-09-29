package com.quantum625.networks.data;

import com.quantum625.networks.component.InputContainer;
import com.quantum625.networks.component.ItemContainer;
import com.quantum625.networks.component.MiscContainer;
import com.quantum625.networks.Network;
import com.quantum625.networks.utils.Location;

import java.util.ArrayList;
import java.util.UUID;


public class JSONNetwork {
    private String id;
    private String owner;

    private String[] admins;
    private String[] users;

    private Location center;
    private int maxContainers = 20;
    private int maxRange = 40;

    private InputContainer[] input_containers;
    private ItemContainer[] sorting_containers;
    private MiscContainer[] misc_containers;

    public JSONNetwork(Network n) {
        this.id = n.getID();
        this.owner = n.getOwner().toString();

        this.input_containers = n.getInputChests().toArray(new InputContainer[n.getInputChests().size()]);
        this.sorting_containers = n.getSortingChests().toArray(new ItemContainer[n.getSortingChests().size()]);
        this.misc_containers = n.getMiscChests().toArray(new MiscContainer[n.getMiscChests().size()]);
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public InputContainer[] getInputContainers() {
        return input_containers;
    }

    public ItemContainer[] getSortingContainers() {
    return sorting_containers;
    }

    public MiscContainer[] getMiscContainers() {
        return misc_containers;
    }
}
