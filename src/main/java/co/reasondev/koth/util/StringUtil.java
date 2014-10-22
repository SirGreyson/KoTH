package co.reasondev.koth.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringUtil {

    private static Random random = new Random();

    public static int asInt(String input) {
        return Integer.parseInt(input);
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> colorAll(List<String> input) {
        List<String> output = new ArrayList<String>();
        for(String i : input) output.add(color(i));
        return output;
    }

    public static String formatLocation(Location input) {
        return input.getBlockX() + ", " + input.getBlockY() + ", " + input.getBlockZ();
    }

    public static String parseLocation(Location input) {
        return input.getWorld().getName() + "," + input.getBlockX() + "," + input.getBlockY() + "," + input.getBlockZ();
    }

    public static Location parseLocationString(String input) {
        String[] args = input.split(",");
        return new Location(Bukkit.getWorld(args[0]), asInt(args[1]), asInt(args[2]), asInt(args[3]));
    }

    private static List<String> parseLore(String input) {
        List<String> output = new ArrayList<String>();
        for(int i = 0; i < input.split(",").length; i++) output.add(color(input.split(",")[i]));
        return output;
    }

    public static ItemStack parseItemString(String input) {
        String[] args = input.split(":");
        ItemStack output = new ItemStack(Material.valueOf(args[0]), asInt(args[1]));
        if(args.length < 3) return output;
        ItemMeta meta = output.getItemMeta();
        if(!args[2].equalsIgnoreCase("none")) meta.setDisplayName(color(args[2]));
        if(args.length >= 4 && !args[3].equalsIgnoreCase("none")) meta.setLore(parseLore(args[3]));
        if(args.length >= 5 && !args[4].equalsIgnoreCase("none")) meta.addEnchant(Enchantment.getByName(args[4].split("/")[0]), asInt(args[4].split("/")[1]), true);
        output.setItemMeta(meta);
        return output;
    }

    public static List<ItemStack> parseChestLoadout(List<String> input) {
        List<ItemStack> output = new ArrayList<ItemStack>();
        for(String i : input)
            if(!i.contains("=")) output.add(parseItemString(i));
            else if(random.nextInt(100) <= asInt(i.split("=")[1])) output.add(parseItemString(i.split("=")[0]));
        return output;
    }
}
