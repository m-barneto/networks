package com.quantum625.networks.listener;

import com.quantum625.networks.NetworkManager;
import com.quantum625.networks.Network;
import com.quantum625.networks.commands.LanguageModule;
import com.quantum625.networks.component.BaseComponent;
import com.quantum625.networks.component.InputContainer;
import com.quantum625.networks.component.SortingContainer;
import com.quantum625.networks.component.MiscContainer;
import com.quantum625.networks.data.Config;
import com.quantum625.networks.utils.Location;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockBreakEventListener implements Listener {

    private NetworkManager net;
    private Config config;
    private LanguageModule lang;


    public BlockBreakEventListener(NetworkManager networkManager, Config config,  LanguageModule languageModule) {
        net = networkManager;
        lang = languageModule;
        this.config = config;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event) {
        for (Network network : net.listAll()) {
            for (BaseComponent component : network.getAllComponents()) {
                if (component.getPos().equals(new Location(event.getBlock()))) {
                    if (net.checkNetworkPermission(event.getPlayer(), network) > 1) {
                        if (!config.getEconomyState()) {
                            if (component instanceof InputContainer) {

                                ItemStack inputContainer = new ItemStack(event.getBlock().getType());
                                ItemMeta meta = inputContainer.getItemMeta();
                                meta.setDisplayName("§rInput Container");
                                meta.setLore(Arrays.asList("§r§9Sorts items into sorting chests and misc chests"));
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                data.set(new NamespacedKey("networks", "component_type"), PersistentDataType.STRING, "input");
                                inputContainer.setItemMeta(meta);
                                Bukkit.getServer().getWorld(component.getPos().getDim()).dropItem(component.getPos().getBukkitLocation(), inputContainer);
                                event.setDropItems(false);
                            }
                            if (component instanceof SortingContainer) {

                                String items = "";
                                for (String item : ((SortingContainer) component).getItems()) {
                                    items += item + ",";
                                }

                                List<String> itemslist = new ArrayList<String>();
                                itemslist.add(0, "§rFilter Items:");
                                for (String item : ((SortingContainer) component).getItems()) {
                                    itemslist.add("§r§f"+item);
                                }

                                ItemStack sortingContainer = new ItemStack(event.getBlock().getType());
                                ItemMeta meta = sortingContainer.getItemMeta();
                                meta.setDisplayName("§rSorting Container");
                                meta.setLore(itemslist);
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                data.set(new NamespacedKey("networks", "component_type"), PersistentDataType.STRING, "sorting");
                                data.set(new NamespacedKey("networks", "filter_items"), PersistentDataType.STRING, items);
                                sortingContainer.setItemMeta(meta);
                                Bukkit.getServer().getWorld(component.getPos().getDim()).dropItem(component.getPos().getBukkitLocation(), sortingContainer);
                                event.setDropItems(false);
                            }
                            if (component instanceof MiscContainer) {

                                ItemStack miscContainer = new ItemStack(event.getBlock().getType());
                                ItemMeta meta = miscContainer.getItemMeta();
                                meta.setDisplayName("§rMiscellaneous Container");
                                meta.setLore(Arrays.asList("§r§9All remaining items will go into these chests"));
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                data.set(new NamespacedKey("networks", "component_type"), PersistentDataType.STRING, "misc");
                                miscContainer.setItemMeta(meta);
                                Bukkit.getServer().getWorld(component.getPos().getDim()).dropItem(component.getPos().getBukkitLocation(), miscContainer);
                                event.setDropItems(false);
                            }

                            for (ItemStack stack : component.getInventory()) {
                                Bukkit.getServer().getWorld(component.getPos().getDim()).dropItem(component.getPos().getBukkitLocation(), stack);
                            }
                        }
                        network.removeComponent(new Location(event.getBlock()));
                        lang.returnMessage(event.getPlayer(), "component.remove", new Location(event.getBlock()));
                    }
                    else {
                        lang.returnMessage(event.getPlayer(), "permission.admin");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
