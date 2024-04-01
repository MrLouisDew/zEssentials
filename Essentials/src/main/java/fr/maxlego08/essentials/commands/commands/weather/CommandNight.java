package fr.maxlego08.essentials.commands.commands.weather;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.WorldInfo;

import java.util.stream.Collectors;

public class CommandNight extends VCommand {

    public CommandNight(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.NIGHT_USE);
        this.setDescription(Message.DESCRIPTION_NIGHT);
        this.addOptionalArg("world", (a, b) -> Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList()));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        World world = this.argAsWorld(0, isPlayer() ? this.player.getWorld() : null);
        world.setFullTime(13000);

        message(this.sender, Message.COMMAND_NIGHT, "%world%", world.getName());

        return CommandResultType.SUCCESS;
    }
}
