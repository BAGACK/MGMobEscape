package com.comze_instancelabs.mgmobescape;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgmobescape.v1_6.V1_6Dragon;
import com.comze_instancelabs.mgmobescape.v1_6.V1_6Wither;
import com.comze_instancelabs.mgmobescape.v1_7.V1_7Dragon;
import com.comze_instancelabs.mgmobescape.v1_7.V1_7Wither;
import com.comze_instancelabs.mgmobescape.v1_7._R2.V1_7_5Dragon;
import com.comze_instancelabs.mgmobescape.v1_7._R2.V1_7_5Wither;
import com.comze_instancelabs.mgmobescape.v1_7._R3.V1_7_8Dragon;
import com.comze_instancelabs.mgmobescape.v1_7._R3.V1_7_8Wither;
import com.comze_instancelabs.mgmobescape.v1_7._R4.V1_7_10Dragon;
import com.comze_instancelabs.mgmobescape.v1_7._R4.V1_7_10Wither;
import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.Util;

public class IArena extends Arena {

	public static Main m;
	public String mobtype;
	BukkitTask currenttask;

	AbstractMEDragon dragon = null;
	AbstractMEWither wither = null;

	public IArena(Main m, String arena_id, String mobmobtype) {
		super(m, arena_id, ArenaType.REGENERATION);
		m.m = m;
	}

	@Override
	public void joinPlayerLobby(String playername) {
		super.joinPlayerLobby(playername);
	}

	@Override
	public void spectate(String playername) {

	}

	@Override
	public void start(boolean tp) {
		this.start();
		super.start(tp);
	}

	@Override
	public void started() {
		started_();
	}

	public void started_() {
		final String arena = this.getName();
		final IArena a = this;
		if (mobtype.equalsIgnoreCase("dragon")) {
			AbstractDragon ad_ = null;
			if (m.mode1_6) {
				ad_ = new V1_6Dragon();
			} else if (m.mode1_7_5) {
				ad_ = new V1_7_5Dragon();
			} else if (m.mode1_7_8) {
				ad_ = new V1_7_8Dragon();
			} else if (m.mode1_7_10) {
				ad_ = new V1_7_10Dragon();
			} else {
				ad_ = new V1_7Dragon();
			}

			final AbstractDragon ad = ad_;

			final Location l1 = Util.getComponentForArena(m, arena, "bounds.low");
			final Location l2 = Util.getComponentForArena(m, arena, "bounds.high");

			int length1 = l1.getBlockX() - l2.getBlockX();
			final int length2 = l1.getBlockY() - l2.getBlockY();
			int length3 = l1.getBlockZ() - l2.getBlockZ();
			boolean f = false;
			boolean f_ = false;
			if (l2.getBlockX() > l1.getBlockX()) {
				length1 = l2.getBlockX() - l1.getBlockX();
				f = true;
			}

			if (l2.getBlockZ() > l1.getBlockZ()) {
				length3 = l2.getBlockZ() - l1.getBlockZ();
				f_ = true;
			}

			currenttask = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(m, new Runnable() {
				@Override
				public void run() {
					Vector v = dragon.getNextPosition();
					if (v != null && dragon != null) {
						// TODO add setPosition to each MEDragon class
						dragon.setPosition(v.getX(), v.getY(), v.getZ());
					}

					ad.destroy(m, l1, l2, arena, length2);
				}
			}, 3 + 20, 3);

		} else {
			AbstractWither aw = null;
			if (m.mode1_6) {
				aw = new V1_6Wither();
			} else if (m.mode1_7_5) {
				aw = new V1_7_5Wither();
			} else if (m.mode1_7_8) {
				aw = new V1_7_8Wither();
			} else if (m.mode1_7_10) {
				aw = new V1_7_10Wither();
			} else {
				aw = new V1_7Wither();
			}
		}
	}

	public void start() {
		final String arena = this.getName();
		final IArena a = this;
		if (mobtype.equalsIgnoreCase("dragon")) {
			AbstractDragon ad = null;
			if (m.mode1_6) {
				ad = new V1_6Dragon();
				dragon = V1_6Dragon.spawnEnderdragon1_6(m, arena, a.getDragonSpawn());
			} else if (m.mode1_7_5) {
				ad = new V1_7_5Dragon();
				dragon = V1_7_5Dragon.spawnEnderdragon(m, arena, a.getDragonSpawn());
			} else if (m.mode1_7_8) {
				ad = new V1_7_8Dragon();
				dragon = V1_7_8Dragon.spawnEnderdragon(m, arena, a.getDragonSpawn());
			} else if (m.mode1_7_10) {
				ad = new V1_7_10Dragon();
				dragon = V1_7_10Dragon.spawnEnderdragon(m, arena, a.getDragonSpawn());
			} else {
				ad = new V1_7Dragon();
				dragon = V1_7Dragon.spawnEnderdragon(m, arena, a.getDragonSpawn());
			}
		} else {
			AbstractWither aw = null;
			if (m.mode1_6) {
				aw = new V1_6Wither();
				wither = V1_6Wither.spawnWither1_6(m, arena, a.getDragonSpawn());
			} else if (m.mode1_7_5) {
				aw = new V1_7_5Wither();
				wither = V1_7_5Wither.spawnWither(m, arena, a.getDragonSpawn());
			} else if (m.mode1_7_8) {
				aw = new V1_7_8Wither();
				wither = V1_7_8Wither.spawnWither(m, arena, a.getDragonSpawn());
			} else if (m.mode1_7_10) {
				aw = new V1_7_10Wither();
				wither = V1_7_10Wither.spawnWither(m, arena, a.getDragonSpawn());
			} else {
				aw = new V1_7Wither();
				wither = V1_7Wither.spawnWither(m, arena, a.getDragonSpawn());
			}
		}
	}

	@Override
	public void stop() {
		this.stop(currenttask, this.getName());
	}

	public void stop(BukkitTask t, final String arena) {
		if (m.mode1_6) {
			if (mobtype.equalsIgnoreCase("dragon")) {
				V1_6Dragon v = new V1_6Dragon();
				v.stop(m, t, arena);
			} else if (mobtype.equalsIgnoreCase("wither")) {
				V1_6Wither v = new V1_6Wither();
				v.stop(m, t, arena);
			} else {
				V1_6Dragon v = new V1_6Dragon();
				v.stop(m, t, arena);
			}
		} else if (m.mode1_7_5) {
			if (mobtype.equalsIgnoreCase("dragon")) {
				V1_7_5Dragon v = new V1_7_5Dragon();
				v.stop(m, t, arena);
			} else if (mobtype.equalsIgnoreCase("wither")) {
				V1_7_5Wither v = new V1_7_5Wither();
				v.stop(m, t, arena);
			} else {
				V1_7_5Dragon v = new V1_7_5Dragon();
				v.stop(m, t, arena);
			}
		} else if (m.mode1_7_8) {
			if (mobtype.equalsIgnoreCase("dragon")) {
				V1_7_8Dragon v = new V1_7_8Dragon();
				v.stop(m, t, arena);
			} else if (mobtype.equalsIgnoreCase("wither")) {
				V1_7_8Wither v = new V1_7_8Wither();
				v.stop(m, t, arena);
			} else {
				V1_7_8Dragon v = new V1_7_8Dragon();
				v.stop(m, t, arena);
			}
		} else if (m.mode1_7_10) {
			if (mobtype.equalsIgnoreCase("dragon")) {
				V1_7_10Dragon v = new V1_7_10Dragon();
				v.stop(m, t, arena);
			} else if (mobtype.equalsIgnoreCase("wither")) {
				V1_7_10Wither v = new V1_7_10Wither();
				v.stop(m, t, arena);
			} else {
				V1_7_10Dragon v = new V1_7_10Dragon();
				v.stop(m, t, arena);
			}
		} else {
			if (mobtype.equalsIgnoreCase("dragon")) {
				V1_7Dragon v = new V1_7Dragon();
				v.stop(m, t, arena);
			} else if (mobtype.equalsIgnoreCase("wither")) {
				V1_7Wither v = new V1_7Wither();
				v.stop(m, t, arena);
			} else {
				V1_7Dragon v = new V1_7Dragon();
				v.stop(m, t, arena);
			}
		}
	}

	public Location getDragonSpawn() {
		return Util.getComponentForArena(m, this.getName(), "mobspawn");
	}

	public ArrayList<Vector> getDragonWayPoints(String arena) {
		ArrayList<Vector> ret = new ArrayList<Vector>();
		for (Location l : getAllPoints(m, arena)) {
			ret.add(new Vector(l.getX(), l.getY(), l.getZ()));
		}
		return ret;
	}

	public static ArrayList<Location> getAllPoints(JavaPlugin plugin, String arena) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig();
		ArrayList<Location> ret = new ArrayList<Location>();
		if (!config.isSet("arenas." + arena + ".flypoint")) {
			return ret;
		}
		for (String spawn : config.getConfigurationSection("arenas." + arena + ".flypoint.").getKeys(false)) {
			ret.add(Util.getComponentForArena(plugin, arena, "flypoint." + spawn));
		}
		return ret;
	}

}
