package com.quantum625.networks;

import com.quantum625.networks.component.BaseComponent;
import com.quantum625.networks.component.InputContainer;
import com.quantum625.networks.component.ItemContainer;
import com.quantum625.networks.component.MiscContainer;
import com.quantum625.networks.data.Network;
import com.quantum625.networks.utils.Location;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class StorageNetwork {
    private String id;
    private UUID owner;

    private ArrayList<InputContainer> input_containers = new ArrayList<InputContainer>();
    private ArrayList<ItemContainer> sorting_containers = new ArrayList<ItemContainer>();
    private ArrayList<MiscContainer> misc_containers = new ArrayList<MiscContainer>();



    public StorageNetwork(String id, UUID owner) {
        this.id = id;
        this.owner = owner;
    }

    public StorageNetwork(Network net) {
        this.id = net.getId();
        this.owner = UUID.fromString(net.getOwner());

        for (InputContainer i: net.getInputContainers()) {
            input_containers.add(i);
        }

        for (ItemContainer i: net.getSortingContainers()) {
            sorting_containers.add(i);
        }

        for (MiscContainer i: net.getMiscContainers()) {
            misc_containers.add(i);
        }
    }

    private InputContainer getInputContainerByLocation(Location pos) {
        for (InputContainer i : input_containers) {
            if (i.getPos().equals(pos)) {
                return i;
            }
        }
        return null;
    }
    private ItemContainer getItemContainerByLocation(Location pos) {
        for (ItemContainer i : sorting_containers) {
            if (i.getPos().equals(pos)) {
                return i;
            }
        }
        return null;
    }


    // Unfinished:
    private ItemContainer getItemContainerByItem(String item) {
        for (ItemContainer i : sorting_containers) {
            if (i.getItem().equals(item) && i.getInventory().firstEmpty() != -1) {
                return i;
            }
        }
        return null;
    }


    private MiscContainer getMiscContainerByLocation(Location pos) {
        for (MiscContainer i : misc_containers) {
            if (i.getPos().equals(pos)) {
                return i;
            }
        }
        return null;
    }

    private MiscContainer getMiscContainer() {
        for (MiscContainer i : misc_containers) {
            if (i.getInventory().firstEmpty() != -1) {
                return i;
            }
        }
        return null;
    }



    public String getID() {
        return this.id;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public ArrayList<BaseComponent> getAllComponents() {
        ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
        components.addAll(this.input_containers);
        components.addAll(this.sorting_containers);
        components.addAll(this.misc_containers);
        return components;
    }

    public ArrayList<InputContainer> getInputChests() {
        return input_containers;
    }

    public ArrayList<ItemContainer> getSortingChests() {
        return sorting_containers;
    }

    public ArrayList<MiscContainer> getMiscChests() {
        return misc_containers;
    }


    public void addInputChest(Location pos) {
        input_containers.add(new InputContainer(pos, 20));
    }

    public void addItemChest(Location pos, String item) {
        sorting_containers.add(new ItemContainer(pos, item));
    }

    public void addMiscChest(Location pos, boolean takeOverflow) {
        misc_containers.add(new MiscContainer(pos, takeOverflow));
    }

    public void removeComponent(Location location) {
        for (int i = 0; i < input_containers.size(); i++) {
            if (input_containers.get(i).getPos().equals(location)) {
                input_containers.remove(i);
            }
        }
        for (int i = 0; i < sorting_containers.size(); i++) {
            if (sorting_containers.get(i).getPos().equals(location)) {
                sorting_containers.remove(i);
            }
        }
        for (int i = 0; i < misc_containers.size(); i++) {
            if (misc_containers.get(i).getPos().equals(location)) {
                misc_containers.remove(i);
            }
        }
    }



    public void sortAll() {
        for (int i = 0; i < input_containers.size(); i++) {
            sort(input_containers.get(i).getPos());
        }
    }

    public void sort(Location pos) {
        if (getInputContainerByLocation(pos) != null) {

            Inventory inventory = getInputContainerByLocation(pos).getInventory();

            Bukkit.getLogger().info(inventory.getType().toString());

            for (ItemStack stack : inventory.getContents()) {

                if (stack != null) {

                    ItemContainer container = getItemContainerByItem(stack.getType().toString().toUpperCase());
                    if (container != null) {
                        container.getInventory().addItem(stack);
                        Bukkit.getLogger().info("Item of type " + stack.getType().toString().toUpperCase() + " added to inventory at  X: " + container.getPos().getX() + " Y: " + container.getPos().getY() + " Z:" + container.getPos().getZ() + " World: " + container.getPos().getDim());
                        inventory.remove(stack);
                    } else {
                        MiscContainer miscContainer = getMiscContainer();
                        if (miscContainer != null) {
                            miscContainer.getInventory().addItem(stack);
                            Bukkit.getLogger().info("Item of type " + stack.getType().toString().toUpperCase() + " added to inventory   at X: " + miscContainer.getPos().getX() + " Y: " + miscContainer.getPos().getY() + " Z:" + miscContainer.getPos().getZ() + " World: " + miscContainer.getPos().getDim());
                            inventory.remove(stack);
                        } else {
                            Bukkit.getLogger().info("There are no free containers available");
                        }
                    }
                }
            }
        }

        else {
            Bukkit.getLogger().warning("Network contains no chest at the given position: " + pos.toString());
        }
    }


}
