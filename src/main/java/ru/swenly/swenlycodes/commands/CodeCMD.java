package ru.swenly.swenlycodes.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.swenly.swenlycodes.SwenlyCodes;

import java.io.*;

public class CodeCMD implements CommandExecutor {
    File path = SwenlyCodes.getPlugin(SwenlyCodes.class).getDataFolder();
    JSONParser jsonParser = new JSONParser();
    private static BufferedWriter bw;
    JSONObject obj = new JSONObject();
    File file;
    FileReader fileReader;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String full_path = path + "/codes/code.json";
        String help_message = ChatColor.GRAY + "\n[" + ChatColor.WHITE + "SwenlyCodes" + ChatColor.GRAY + "]" + ChatColor.GREEN + "\n/code <code> " + ChatColor.WHITE + "-" + ChatColor.GRAY + " Использует промокод" + ChatColor.GREEN + "\n/code <add> <code> [cmd] " + ChatColor.WHITE + "-" + ChatColor.GRAY + " Создает промокод" + ChatColor.GREEN + "\n/code <code> <add_cmd> <cmd> " + ChatColor.WHITE + "-" + ChatColor.GRAY + " Добавляет команду" + ChatColor.GREEN + "\n/code <code> <del_cmd> <cmd> " + ChatColor.WHITE + "-" + ChatColor.GRAY + " Удаляет команду" + ChatColor.GREEN + "\n/code <code> <del_cmds> <cmd> " + ChatColor.WHITE + "-" + ChatColor.GRAY + " Удоляет команды с текстом cmd" + ChatColor.GREEN + "\n/code <code> <clear_cmds> " + ChatColor.WHITE + "-" + ChatColor.GRAY + " Очищает команды" + ChatColor.GREEN + "\n/code help" + ChatColor.WHITE + "-" + ChatColor.GRAY + " Помощь";

        if (args.length == 0) {
            sender.sendMessage(help_message);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equals("help")) {
                sender.sendMessage(help_message);
                return true;
            }
        }

        if (args[0].equals("add")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите название промокода!");
                return true;
            }

            String code_cmd = "";
            full_path = path + "/codes/" + args[1] + ".json";
            if (!(args.length == 2)) {
                for (String arg : args) {
                    if ((!arg.equals(args[0])) && (!arg.equals(args[1]))) {
                        code_cmd += (arg + " ");
                    }
                }
            }

            try {
                File file_for_create = new File(full_path);
                file_for_create.getParentFile().mkdir();
                file_for_create.createNewFile();

                JSONObject main_obj = new JSONObject();
                obj = new JSONObject();
                JSONArray who_used = new JSONArray();
                JSONArray code_commands = new JSONArray();
                if (!code_cmd.equals("")) {
                    StringBuffer sb = new StringBuffer(code_cmd);
                    sb.deleteCharAt(sb.length()-1);

                    code_commands.add(sb.toString());
                }

                obj.put("Creator", player.getName());
                obj.put("Commands", code_commands);
                obj.put("Who Used", who_used);
                main_obj.put("Code", obj);

                FileWriter fileWriter = new FileWriter(full_path);
                bw = new BufferedWriter(fileWriter);
                bw.write(main_obj.toJSONString());
                sender.sendMessage(ChatColor.GREEN + "Успешно!" + ChatColor.WHITE + " Вы создали промокод " + ChatColor.YELLOW + args[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    bw.flush();
                    bw.close();
                }
                catch (Exception e) {

                }
            }
        }

        else {
            full_path = path + "/codes/" + args[0] + ".json";
            try {
                file = new File(full_path);

                if (!file.exists()) {
                    sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                    return true;
                }

                boolean isJustCode = false;
                if (args.length > 1) {
                    if ((args[1].equals("add_cmd")) || (args[1].equals("add_command")) ||(args[1].equals("create_cmd")) || (args[1].equals("create_command"))) {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите название промокода!");
                            return true;
                        }

                        if (args.length == 2) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите комманду для промокода!");
                            return true;
                        }

                        try {
                            file = new File(full_path);

                            if (!file.exists()) {
                                sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                                return true;
                            }

                            fileReader = new FileReader(full_path);
                            Object read_obj = jsonParser.parse(fileReader);
                            JSONObject read_obj1 = (JSONObject) read_obj;
                            obj = (JSONObject) read_obj1.get("Code");
                            JSONArray old_code_cmds = (JSONArray) obj.get("Commands");
                            String code_cmd = "";
                            for (String arg : args) {
                                if ((!arg.equals(args[0])) && (!arg.equals(args[1]))) {
                                    code_cmd += (arg + " ");
                                }
                            }
                            old_code_cmds.add(code_cmd);

                            read_obj1.put("Code", obj);
                            FileWriter fileWriter = new FileWriter(full_path);
                            bw = new BufferedWriter(fileWriter);
                            bw.write(read_obj1.toJSONString());
                            bw.flush();
                            bw.close();
                            fileReader.close();

                            sender.sendMessage(ChatColor.GREEN + "Успешно!" + ChatColor.WHITE + " Вы добавили команду к промокоду " + ChatColor.YELLOW + args[0]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                            return true;
                        }
                    }

                    else if ((args[1].equals("del_cmd")) || (args[1].equals("del_command")) || (args[1].equals("delete_cmd")) || (args[1].equals("delete_command")) || (args[1].equals("remove_cmd")) || (args[1].equals("remove_command"))) {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите название промокода!");
                            return true;
                        }

                        if (args.length == 2) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите комманду для промокода!");
                            return true;
                        }

                        try {
                            file = new File(full_path);

                            if (!file.exists()) {
                                sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                                return true;
                            }

                            fileReader = new FileReader(full_path);
                            Object read_obj = jsonParser.parse(fileReader);
                            JSONObject read_obj1 = (JSONObject) read_obj;
                            obj = (JSONObject) read_obj1.get("Code");
                            JSONArray old_code_cmds = (JSONArray) obj.get("Commands");
                            String code_cmd = "";
                            for (String arg : args) {
                                if ((!arg.equals(args[0])) && (!arg.equals(args[1]))) {
                                    code_cmd += (arg + " ");
                                }
                            }
                            old_code_cmds.remove(code_cmd);

                            read_obj1.put("Code", obj);
                            FileWriter fileWriter = new FileWriter(full_path);
                            bw = new BufferedWriter(fileWriter);
                            bw.write(read_obj1.toJSONString());
                            bw.flush();
                            bw.close();
                            fileReader.close();

                            sender.sendMessage(ChatColor.GREEN + "Успешно!" + ChatColor.WHITE + " Вы удалили команду у промокода " + ChatColor.YELLOW + args[0]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                            return true;
                        }
                    }

                    else if ((args[1].equals("del_cmds")) || (args[1].equals("del_commands")) || (args[1].equals("delete_cmds")) || (args[1].equals("delete_commands")) || (args[1].equals("remove_cmds")) || (args[1].equals("remove_commands"))) {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите название промокода!");
                            return true;
                        }

                        if (args.length == 2) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите комманду для промокода!");
                            return true;
                        }

                        try {
                            file = new File(full_path);

                            if (!file.exists()) {
                                sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                                return true;
                            }

                            fileReader = new FileReader(full_path);
                            Object read_obj = jsonParser.parse(fileReader);
                            JSONObject read_obj1 = (JSONObject) read_obj;
                            obj = (JSONObject) read_obj1.get("Code");
                            JSONArray old_code_cmds = (JSONArray) obj.get("Commands");
                            String code_cmd = "";
                            for (String arg : args) {
                                if ((!arg.equals(args[0])) && (!arg.equals(args[1]))) {
                                    code_cmd += (arg + " ");
                                }
                            }
                            for (Object old_code_cmd : old_code_cmds) {
                                String real_old_code_cmd = (String) old_code_cmd;

                                if (real_old_code_cmd.contains(code_cmd)) {
                                    old_code_cmds.remove(real_old_code_cmd);
                                }
                            }

                            read_obj1.put("Code", obj);
                            FileWriter fileWriter = new FileWriter(full_path);
                            bw = new BufferedWriter(fileWriter);
                            bw.write(read_obj1.toJSONString());
                            bw.flush();
                            bw.close();
                            fileReader.close();

                            sender.sendMessage(ChatColor.GREEN + "Успешно!" + ChatColor.WHITE + " Вы удалили команду у промокода " + ChatColor.YELLOW + args[0]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                            return true;
                        }
                    }

                    else if ((args[1].equals("clear_cmds")) || (args[1].equals("clear_commands"))) {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Укажите название промокода!");
                            return true;
                        }

                        if (!file.exists()) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                            return true;
                        }

                        try {
                            file = new File(full_path);

                            if (!file.exists()) {
                                sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                                return true;
                            }

                            fileReader = new FileReader(full_path);
                            Object read_obj = jsonParser.parse(fileReader);
                            JSONObject read_obj1 = (JSONObject) read_obj;
                            obj = (JSONObject) read_obj1.get("Code");
                            JSONArray old_code_cmds = (JSONArray) obj.get("Commands");
                            old_code_cmds.clear();

                            read_obj1.put("Code", obj);
                            FileWriter fileWriter = new FileWriter(full_path);
                            bw = new BufferedWriter(fileWriter);
                            bw.write(read_obj1.toJSONString());
                            bw.flush();
                            bw.close();
                            fileReader.close();

                            sender.sendMessage(ChatColor.GREEN + "Успешно!" + ChatColor.WHITE + " Вы очистили все команды промокода " + ChatColor.YELLOW + args[0]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                            return true;
                        }
                    }

                    else {
                        isJustCode = true;
                    }
                }

                else {
                    isJustCode = true;
                }

                if (isJustCode) {
                    if (args.length > 1) {
                        sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Передано слишком много аргументов!");
                        return true;
                    }

                    if (!file.exists()) {
                        sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Промокод не найден!");
                        return true;
                    }

                    Object read_obj = jsonParser.parse(new FileReader(full_path));
                    JSONObject read_obj1 = (JSONObject) read_obj;
                    obj = (JSONObject) read_obj1.get("Code");
                    JSONArray old_code_cmds = (JSONArray) obj.get("Commands");
                    JSONArray who_used = (JSONArray) obj.get("Who Used");

                    for (Object user : who_used) {
                        if (user.toString().equals(sender.getName())) {
                            sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Ты уже использовал этот код!");
                            return true;
                        }
                    }
                    who_used.add(player.getName());

                    String code_cmd;
                    for (Object old_code_cmd : old_code_cmds) {
                        String real_old_code_cmd = (String) old_code_cmd;

                        code_cmd = real_old_code_cmd.replace("%player%", sender.getName()).replace("%player_name%", sender.getName()).replace("%user%", sender.getName()).replace("%username%", sender.getName()).replace("%user_name%", sender.getName());
                        player.getServer().dispatchCommand(Bukkit.getConsoleSender(), code_cmd);
                    }

                    read_obj1.put("Code", obj);
                    FileWriter fileWriter = new FileWriter(full_path);
                    bw = new BufferedWriter(fileWriter);
                    bw.write(read_obj1.toJSONString());
                    bw.flush();
                    bw.close();

                    sender.sendMessage(ChatColor.GREEN + "Успешно!" + ChatColor.WHITE + " Вы использовали промокод " + ChatColor.YELLOW + args[0]);
                }

            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Ошибка!" + ChatColor.WHITE + " Кодер чот нашаманил!");
                return true;
            }
        }

        return true;
    }
}
