package com.comze.instancelabs.mgmobescape.v1_9_R2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgmobescape.AbstractMEWither;
import com.comze_instancelabs.mgmobescape.AbstractWither;
import com.comze_instancelabs.mgmobescape.MEArena;
import com.comze_instancelabs.mgmobescape.MEMain;
import com.comze_instancelabs.mgmobescape.mobtools.Tools;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

import net.minecraft.server.v1_9_R2.EnumParticle;
import net.minecraft.server.v1_9_R2.PacketPlayOutWorldParticles;

public class V1_9_4Wither implements AbstractWither {

	public static HashMap<String, MEWither> wither = new HashMap<String, MEWither>();

	public void playBlockBreakParticles(final Location loc, final Material m, final Player... players) {
		final Block block = loc.getBlock();
		final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
				EnumParticle.BLOCK_CRACK, true,
				(float) loc.getX(), (float) loc.getY(), (float) loc.getZ(),
				1.0f, 1.0f, 1.0f,
				0, 10,
				block.getTypeId(), block.getData());
		for (final Player p : players) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static AbstractMEWither spawnWither(MEMain m, String arena, Location t) {
		/*
		 * if(dragons.containsKey(arena)){ return wither.get(arena); }
		 */
		m.getLogger().info("WITHER SPAWNED " + arena + " " + t.toString());
		Object w = ((CraftWorld) t.getWorld()).getHandle();
		ArrayList<Vector> temp = ((MEArena) MinigamesAPI.getAPI().pinstances.get(m).getArenaByName(arena)).getDragonWayPoints(arena);
		if (temp == null  || temp.isEmpty()) {
			m.getLogger().severe("You forgot to set any FlyPoints! You need to have min. 2 and one of them has to be at finish.");
			return null;
		}
		MEWither t_ = new MEWither(m, arena, t, (net.minecraft.server.v1_9_R2.World) ((CraftWorld) t.getWorld()).getHandle(), temp);
		((net.minecraft.server.v1_9_R2.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
		t_.setCustomName(m.getDragonName());
		wither.put(arena, t_);
		t_.move(0.1D, 0.1D, 0.1D);
		return t_;
	}

	public void removeWither(String arena) {
		try {
			removeWither(wither.get(arena));
			wither.put(arena, null);
		} catch (Exception e) {
			MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
		}
	}

	public Block[] getLoc(MEMain m, final Location l, String arena, int i, int j, Location l2) {
		Block[] b = new Block[4];
		b[0] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX + (m.getDestroyRadius() / 2) - i, wither.get(arena).locY + j - 1, wither.get(arena).locZ + 3));
		b[1] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX + (m.getDestroyRadius() / 2) - i, wither.get(arena).locY + j - 1, wither.get(arena).locZ - 3));
		b[2] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX + 3, wither.get(arena).locY + j - 1, wither.get(arena).locZ + (m.getDestroyRadius() / 2) - i));
		b[3] = l.getWorld().getBlockAt(new Location(l.getWorld(), wither.get(arena).locX - 3, wither.get(arena).locY + j - 1, wither.get(arena).locZ + (m.getDestroyRadius() / 2) - i));

		return b;
	}
	
	public Block[] getSphereLoc(MEMain m, final Location l, String arena, Location l2, int x, int y, int z)
	{
		final MEWither meWither = wither.get(arena);
		final Location loc = new Location(meWither.world.getWorld(), meWither.locX, meWither.locY, meWither.locZ);
		Block[] b = new Block[]{
				loc.clone().add(x, y, z).getBlock(), loc.clone().add(-x, y, z).getBlock(), loc.clone().add(x, -y, z).getBlock(), loc.clone().add(x, y, -z).getBlock(),
				loc.clone().add(-x, -y, z).getBlock(), loc.clone().add(x, -y, -z).getBlock(), loc.clone().add(-x, y, -z).getBlock(), loc.clone().add(-x, -y, -z).getBlock()
		};
		return b;
	}

	public void stop(final MEMain m, BukkitTask t, final String arena) {
		if (t != null) {
			t.cancel();
		}
		removeWither(wither.get(arena));
	}

	public void removeWither(MEWither t) {
		if (t != null) {
			t.getBukkitEntity().remove();
		}
	}

	public void destroy(final MEMain m, final Location l, final Location l2, String arena, int length2, int blockRatio) {
		Tools.destroy(m, l, l2, arena, length2, "wither", false, true, blockRatio);
	}

	@Override
	public boolean isWither(LivingEntity entity) {
		return ((CraftLivingEntity)entity).getHandle() instanceof MEWither;
	}

}
