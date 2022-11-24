package ru.swenly.swenlycodes.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.swenly.swenlycodes.SwenlyCodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CodeCompleter implements TabCompleter {
    File path = SwenlyCodes.getPlugin(SwenlyCodes.class).getDataFolder();
    File file;
    private static BufferedWriter bw;
    JSONParser jsonParser = new JSONParser();
    JSONObject obj = new JSONObject();
    FileReader fileReader;
    String full_path = path + "/codes";

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        File folder = new File(full_path);
        List<String> list = new ArrayList<>();
        List<String> promocodes = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        commands.add("add_cmd");
        commands.add("del_cmd");
        commands.add("del_cmds");
        commands.add("clear_cmds");

        for (File code : folder.listFiles()) {
            promocodes.add(code.getName().split("\\.", -1)[0]);
        }

        list = promocodes;
        if ((args.length == 1) || (args.length == 2)) {
            for (String promocode : promocodes) {
                if (promocode.equals(args[0])) {
                    list = commands;
                }
            }
        }

        else if (args.length >= 2) {
            for (String promocode : promocodes) {
                if (promocode.equals(args[0])) {
                    return new ArrayList<>();
                }
            }
        }

        try {
            return list;
        }
        catch (Exception e) {
            return null;
        }
    }
}
