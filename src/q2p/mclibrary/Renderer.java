package q2p.mclibrary;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class Renderer extends MapRenderer {
	private static Image image = null;
	private static boolean rendered = false;
	private static ArrayList<File> files = new ArrayList<File>();
	private static final String path = "E:/@MyFolder/MEGA/p/";
	
	public void render(MapView view, MapCanvas canvas, Player player) {
		if(rendered) return;
		Bukkit.broadcastMessage("rended " + (System.currentTimeMillis() % 1000));
		if(image != null) canvas.drawImage(0, 0, image);
		rendered = true;
	}
	
	public Renderer() {
		loadFiles();
	}
	
	static void changeImage(String path) {
		try { image = ImageIO.read(new File(Renderer.path + path)); }
		catch (IOException e) {
			image = null;
			return;
		}
		float aspect = (float)image.getWidth(null)/(float)image.getHeight(null);
		int width = 128;
		int height = 128;
		if(aspect > 1) height = Math.min(128,(int)((float)width/(float)aspect));
		else if(aspect < 1) width = Math.min(128,(int)((float)height*(float)aspect));
		image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		rendered = false;
	}
	
	private static void loadFiles() {
		files.clear();
		loadDir(path + "all/");
		String nwsDr = path + "nws/";
		ArrayList<String> nws = new ArrayList<String>(Arrays.asList(new File(nwsDr).list()));
		for(int i = 0; i < nws.size();) {
			if(!nws.get(i).startsWith("nw")) {
				nws.remove(i);
				continue;
			}
			try {
			Integer.parseInt(nws.get(i).substring(2));
			} catch(NumberFormatException e) {
				nws.remove(i);
				continue;
			}
			i++;
		}
		while (!nws.isEmpty()) loadDir(nwsDr + nws.remove(0) + "/");
	}

	private static void loadDir(String directory) {
		File[] filesList = new File(directory).listFiles();
		for(File f : filesList) if(f.getName().endsWith(".png") || f.getName().endsWith(".jpg") || f.getName().endsWith(".jpeg") || f.getName().endsWith(".gif")) files.add(f);
	}

	
	static boolean hasNext() {
		return !files.isEmpty();
	}

	static String next() {
		return files.get(Wire.random.nextInt(files.size())).getAbsolutePath().substring(path.length());
	}
}
