package com.comze_instancelabs.mgmobescape;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.comze_instancelabs.minigamesapi.PluginInstance;

public interface MEMain {

	Plugin getPlugin();

	AbstractDragon createDragon();

	AbstractWither createWither();

	int getDestroyRadius();

	boolean isSpawnFallingBlocks();

	double getMobSpeed();

	HashMap<String, Integer> getPPoint();

	Logger getLogger();

	String getDragonName();

	PluginInstance getPluginInstance();

}
