package dev.kacperm.opmulti.commands;

import dev.kacperm.opmulti.OPMulti;
import dev.kacperm.opmulti.gui.MultiGUI;
import dev.kacperm.opmulti.utils.color.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Color.legacy(Color.translate(OPMulti.getInstance().getLanguage()
                    .getConfiguration().getString("player-only", "&cOnly players can use this command."))));
            return true;
        }

        new MultiGUI().open(player);
        return true;
    }
}
