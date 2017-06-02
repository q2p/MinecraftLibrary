package q2p.mclibrary;

import org.bukkit.scheduler.BukkitRunnable;

public class LogicTick extends BukkitRunnable {
	public void run() {
		Wire.world.setFullTime(18000);
	}
}