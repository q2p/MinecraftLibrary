package q2p.mclibrary;

import java.io.File;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

class Wire {
	static Plugin plugin;
	static World world;
	static LogicTick logicTick;
	private static int logicTickId;
	static MapRenderer renderer;
	static final String DIRECTORY = "librarydata/";
	static final Random random = new Random();
	
	@SuppressWarnings("deprecation")
	static void init(JavaPlugin plugin, Player player) {
		Wire.plugin = plugin;
		world = player.getWorld();
		renderer = new Renderer();
		Generator.generate();
		logicTick = new LogicTick();
		logicTickId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, logicTick, 0, 1);
		player.teleport(new Location(world, 1, 1, 1));
	}
	
	private static void save() {
		
	}
	
	private static void load() {
		File dir = new File(DIRECTORY);
		if(!dir.exists()) {
			// first start
		} else {
			// second start
		}
	}
	
	static void deinit() {
		save();
		plugin.getServer().getScheduler().cancelTask(logicTickId);
	}
}