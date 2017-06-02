package q2p.mclibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Stairs;

class Generator {
	private static final int roomHeight = 10;
	private static final int roomSize = 21;
	private static World w;
	private static ArrayList<Chest> chests = new ArrayList<Chest>();
	private static int floors;
	
	static void generate() {
		w = Wire.world;
		int offsetY = 0;
		while(w.getMaxHeight() - offsetY >= roomHeight+1) {
			generateRoom(offsetY);
			offsetY += roomHeight;
		}
		offsetY -= roomHeight;
		for(int x = 0; x < roomSize; x++) {
			for(int z = 0; z < roomSize; z++) {
				setBlock(x, 0, z, Material.WOOD);
				setBlock(x, offsetY+roomHeight, z, Material.WOOD);
			}
		}
		fillChests();
		clearDrops();
	}
	
	private static void clearDrops() {
		List<Entity> entList = w.getEntities();
		for(Entity current : entList) if (current instanceof Item) current.remove();
	}

	private static void fillChests() {
		while(Renderer.hasNext() && !chests.isEmpty()) {
			String path = Renderer.next();
			ItemStack page = new ItemStack(Material.PAPER);
			ItemMeta meta = page.getItemMeta();
			meta.setDisplayName(path);
			page.setItemMeta(meta);
			addToChest(chests.get(Wire.random.nextInt(chests.size())), page);
		}
	}
	
	private static void addToChest(Chest chest, ItemStack stack) {
		Inventory inventory = chest.getInventory();
		inventory.addItem(stack);
		if(inventory.firstEmpty() == -1) chests.remove(chest);
	}

	private static void generateRoom(int y) {
		for(int x = -1; x < roomSize+1; x++) {
			for(int by = y; by < y+roomHeight; by++) {
				for(int z = -1; z < roomSize+1; z++) {
					setBlock(x, by, z, Material.AIR);
				}
			}
		}
		for(int x = 0; x < roomSize; x++) {
			for(int by = y; by < y+roomHeight; by++) {
				setBlock(x, by, -1, Material.WOOD);
				setBlock(-1, by, x, Material.WOOD);
				setBlock(x, by, roomSize, Material.WOOD);
				setBlock(roomSize, by, x, Material.WOOD);
			}
		}
		for(int x = 0; x < roomSize; x++) {
			for(int z = 0; z < roomSize; z++) {
				setBlock(x, y, z, Material.WOOD);
			}
		}
		for(int x = 3; x < roomSize-3; x++) {
			for(int z = 3; z < roomSize-3; z++) {
				setBlock(x, y, z, Material.AIR);
			}
		}
		setBlock(1, y, roomSize/2, Material.AIR);
		setBlock(2, y, roomSize/2, Material.AIR);
		for(int h = y+1; h < y+roomHeight; h++) {
			for(int i = 1; i < roomSize - 1; i++) {
				setBlock(0, h, i, Material.BOOKSHELF);
				setBlock(roomSize-1, h, i, Material.BOOKSHELF);
				setBlock(i, h, 0, Material.BOOKSHELF);
				setBlock(i, h, roomSize-1, Material.BOOKSHELF);
			}
			setBlock(0, h, 0, Material.WOOD);
			setBlock(roomSize-1, h, 0, Material.WOOD);
			setBlock(roomSize-1, h, roomSize-1, Material.WOOD);
			setBlock(0, h, roomSize-1, Material.WOOD);
			setBlock(0, h, roomSize/2, Material.WOOD);
		}

		for(int h = y+2; h+1 < y+roomHeight; h+=3) {
			for(int i = 2; i+2 < roomSize-1; i+=3) {
				for(int j = i; j < i+2; j++) {
					addChest(0, h, j, BlockFace.EAST, j%2==1);
					addChest(j, h, 0, BlockFace.SOUTH, j%2==1);
					addChest(roomSize-1, h, j, BlockFace.WEST, j%2==1);
					addChest(j, h, roomSize-1, BlockFace.NORTH, j%2==1);
				}
			}
		}
		floors++;
		clearDrops();
	}

	private static void addChest(int x, int y, int z, BlockFace face, boolean last) {
		setStairs(x, y+1, z, face);
		setBlock(x, y, z, Material.CHEST);
		if(last) chests.add((Chest)w.getBlockAt(x, y, z).getState());
	}
	
	private static void setStairs(int x, int y, int z, BlockFace face) {
		Block block = w.getBlockAt(x, y, z);
		block.setType(Material.WOOD_STAIRS);
		BlockState state = block.getState();
		Stairs stairs = new Stairs(Material.WOOD);
		stairs.setFacingDirection(face);
		state.setData(stairs);
		state.update();
	}

	private static void setBlock(int x, int y, int z, Material material) {
		w.getBlockAt(x, y, z).setType(material);
	}
}