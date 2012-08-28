package me.quantum.blockswitch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockSwitch extends JavaPlugin {

	static String mainDirectory = "plugins" + File.separator + "BlockSwitch";
	static File configFile = new File(mainDirectory + File.separator + "config");
	static Properties prop = new Properties();
	
	private ArrayList<Integer> blocks = new ArrayList<Integer>();
	private int changeTo = 0;
	
	@Override
	public void onEnable() {
		new File(mainDirectory).mkdir();
		
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				FileOutputStream out = new FileOutputStream(configFile);
				prop.put("replace-with", "0");
				prop.put("blocks-strip", "0");
				prop.store(out, "Seperate the block-strips with commas");
				out.flush();
				out.close();
				prop.clear();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		FileInputStream configIn;
		try {
			configIn = new FileInputStream(configFile);
			prop.load(configIn);
			configIn.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		if (prop.getProperty("blocks-strip") != null) {
			String blocksStrip = prop.getProperty("blocks-strip");
			if (blocksStrip.contains(",")) {
				String[] ids = blocksStrip.split(",");
				for (String b : ids) {
					try {
						blocks.add(Integer.parseInt(b));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (prop.getProperty("replace-with") != null) {
			try {
				changeTo = Integer.parseInt(prop.getProperty("replace-with"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		final WorldListener worldListener = new WorldListener(this);
		
		getServer().getPluginManager().registerEvents(worldListener, this);
		
	}
	
	public void stripChunk(Chunk chunk) {
		World world = chunk.getWorld();
		int bx = chunk.getX()<<4;
		int bz = chunk.getZ()<<4;
		for(int xx = bx; xx < bx+16; xx++) {
		    for(int zz = bz; zz < bz+16; zz++) {
		        for(int yy = 0; yy < 256; yy++) {
		            int typeId = world.getBlockTypeIdAt(xx, yy, zz);
		            if(blocks.contains(typeId)) world.getBlockAt(xx,yy,zz).setTypeId(changeTo);
		        }
		    }
		}
	}
	
}
