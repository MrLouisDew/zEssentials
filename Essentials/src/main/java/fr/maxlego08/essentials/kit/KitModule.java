package fr.maxlego08.essentials.kit;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.kit.KitDisplay;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permissible;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KitModule extends ZModule {

    private final List<Kit> kits = new ArrayList<>();
    private KitDisplay display = KitDisplay.IN_LINE;

    public KitModule(ZEssentialsPlugin plugin) {
        super(plugin, "kits");
        this.copyAndUpdate = false;
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadKits();

        // this.loadInventory("kits");
    }

    public boolean exist(String name) {
        return this.getKit(name).isPresent();
    }

    public Optional<Kit> getKit(String name) {
        return this.kits.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
    }

    private void loadKits() {

        this.kits.clear();

        YamlConfiguration configuration = getConfiguration();
        File file = new File(getFolder(), "config.yml");

        ConfigurationSection configurationSection = configuration.getConfigurationSection("kits");
        if (configurationSection == null) return;

        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());

        for (String key : configurationSection.getKeys(false)) {

            String path = "kits." + key + ".";
            String name = configuration.getString(path + "name");
            long cooldown = configuration.getLong(path + "cooldown");

            ConfigurationSection configurationSectionItems = configuration.getConfigurationSection(path + "items");
            if (configurationSectionItems == null) continue;

            List<MenuItemStack> menuItemStacks = new ArrayList<>();

            for (String itemName : configurationSectionItems.getKeys(false)) {
                try {
                    menuItemStacks.add(loader.load(configuration, path + "items." + itemName + ".", file));
                } catch (InventoryException exception) {
                    exception.printStackTrace();
                }
            }

            if (this.exist(name)) {
                this.plugin.getLogger().severe("Kit " + name + " already exist !");
                return;
            }

            Kit kit = new ZKit(name, cooldown, menuItemStacks);
            this.kits.add(kit);
            this.plugin.getLogger().info("Register kit: " + name);
        }
    }

    public List<Kit> getKits(Permissible permissible) {
        return this.kits.stream().filter(kit -> permissible.hasPermission(Permission.ESSENTIALS_KIT.asPermission(kit.getName()))).toList();
    }

    public boolean giveKit(User user, Kit kit, boolean bypassCooldown) {

        long cooldown = kit.getCooldown();
        String key = "kit:" + kit.getName();
        if (cooldown != 0 && !bypassCooldown && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN)) {
            if (user.isCooldown(key)) {
                long milliSeconds = user.getCooldown(key) - System.currentTimeMillis();
                message(user, Message.COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(milliSeconds));
                return false;
            }
        }

        kit.give(user.getPlayer());

        if (cooldown != 0 && !bypassCooldown && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN)) {
            user.addCooldown(key, cooldown);
        }

        return true;
    }

    public void showKits(User user) {

        if (display != KitDisplay.INVENTORY) {

            List<Kit> kits = getKits(user.getPlayer());

            if (display == KitDisplay.IN_LINE) {
                List<String> homesAsString = kits.stream().map(kit -> {

                    String key = "kit:" + kit.getName();
                    long cooldown = kit.getCooldown();
                    long milliSeconds = 0;
                    if (cooldown != 0 && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN)) {
                        milliSeconds = user.getCooldown(key) - System.currentTimeMillis();
                    }

                    return getMessage(milliSeconds != 0 ? Message.COMMAND_KIT_INFORMATION_IN_LINE_INFO_UNAVAILABLE : Message.COMMAND_KIT_INFORMATION_IN_LINE_INFO_AVAILABLE, "%name%", kit.getName(), "%time%", TimerBuilder.getStringTime(milliSeconds));
                }).toList();
                message(user, Message.COMMAND_KIT_INFORMATION_IN_LINE, "%kits%", Strings.join(homesAsString, ','));
            } else {

            }

        } else {
            // ToDo
        }
    }
}
