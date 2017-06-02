package q2p.mclibrary;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

public class Library extends JavaPlugin implements Listener {
	/*
	TODO:
	инструкция по использованию
	убрать лишние функции
	рефактор кода
	*/
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getLabel().equals("begin") && sender instanceof Player) {
			Wire.init(this, (Player) sender);
		}
		return true;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&  event.getItem() != null && event.getItem().getType() == Material.PAPER) {
			Renderer.changeImage(event.getItem().getItemMeta().getDisplayName());
		}
	}
	
	@EventHandler
	public void onMapInitilize(MapInitializeEvent event) {
		MapView map = event.getMap();
		for(MapRenderer renderer : map.getRenderers()) map.removeRenderer(renderer);
		
		map.addRenderer(Wire.renderer);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if(event.toWeatherState()){
			event.getWorld().setStorm(false);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onThunerChange(ThunderChangeEvent event) {
		if(event.toThunderState()){
			event.getWorld().setStorm(false);
			event.setCancelled(true);
		}
	}
}
