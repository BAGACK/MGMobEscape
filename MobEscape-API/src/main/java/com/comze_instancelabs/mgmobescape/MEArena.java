package com.comze_instancelabs.mgmobescape;

import java.util.ArrayList;

import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesapi.SmartReset;

public interface MEArena {

	void stop();

	ArrayList<String> getAllPlayers();

	void spectate(String p);

	AbstractMEDragon getDragon();

	AbstractMEWither getWither();

	ArrayList<Vector> getDragonWayPoints(String arena);

	AbstractDragon getDragonUtil();
	
	SmartReset getSmartReset();

	AbstractWither getWitherUtil();

}
