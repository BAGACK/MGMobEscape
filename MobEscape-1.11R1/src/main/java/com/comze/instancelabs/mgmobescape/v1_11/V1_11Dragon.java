package com.comze.instancelabs.mgmobescape.v1_11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgmobescape.AbstractDragon;
import com.comze_instancelabs.mgmobescape.AbstractMEDragon;
import com.comze_instancelabs.mgmobescape.MEArena;
import com.comze_instancelabs.mgmobescape.MEMain;
import com.comze_instancelabs.mgmobescape.mobtools.Tools;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.MinecraftKey;
import net.minecraft.server.v1_11_R1.PacketPlayOutWorldParticles;

public class V1_11Dragon implements AbstractDragon {

	public static HashMap<String, MEDragon> dragons = new HashMap<String, MEDragon>();

	public static boolean registerEntities() {
		
		MinecraftKey localMinecraftKey = new MinecraftKey("MEWither");
		EntityTypes.b.a(64, localMinecraftKey, MEWither.class);
		EntityTypes.d.add(localMinecraftKey);
		
		localMinecraftKey = new MinecraftKey("MEDragon");
		EntityTypes.b.a(63, localMinecraftKey, MEDragon.class);
		EntityTypes.d.add(localMinecraftKey);
		
		return true;
	}

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

	public static AbstractMEDragon spawnEnderdragon(MEMain m, String arena, Location t) {
		m.getLogger().info("DRAGON SPAWNED " + arena + " " + t.toString());
		Object w = ((CraftWorld) t.getWorld()).getHandle();
		ArrayList<Vector> temp = ((MEArena) MinigamesAPI.getAPI().pinstances.get(m).getArenaByName(arena)).getDragonWayPoints(arena);
		if (temp == null || temp.isEmpty()) {
			m.getLogger().severe("You forgot to set any FlyPoints! You need to have min. 2 and one of them has to be at finish.");
			return null;
		}
		MEDragon t_ = new MEDragon(m, arena, t, (net.minecraft.server.v1_11_R1.World) ((CraftWorld) t.getWorld()).getHandle(), temp);
		((net.minecraft.server.v1_11_R1.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
		t_.setCustomName(m.getDragonName());
		dragons.put(arena, t_);
		return t_;
	}

	public void removeEnderdragon(String arena) {
		try {
			removeEnderdragon(dragons.get(arena));
			dragons.put(arena, null);
		} catch (Exception e) {
		    MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", e);
		}
	}

	public void stop(final MEMain m, BukkitTask t, final String arena) {
		if (t != null) {
			t.cancel();
		}
		removeEnderdragon(dragons.get(arena));
	}

	public void removeEnderdragon(MEDragon t) {
		if (t != null) {
			t.getBukkitEntity().remove();
		}
	}

	public Block[] getLoc(MEMain m, final Location l, String arena, int i, int j, Location l2) {
		Block[] b = new Block[4];
		b[0] = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons.get(arena).locX + (m.getDestroyRadius() / 2) - i, dragons.get(arena).locY + j - 1, dragons.get(arena).locZ + 3));
		b[1] = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons.get(arena).locX + (m.getDestroyRadius() / 2) - i, dragons.get(arena).locY + j - 1, dragons.get(arena).locZ - 3));
		b[2] = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons.get(arena).locX + 3, dragons.get(arena).locY + j - 1, dragons.get(arena).locZ + (m.getDestroyRadius() / 2) - i));
		b[3] = l.getWorld().getBlockAt(new Location(l.getWorld(), dragons.get(arena).locX - 3, dragons.get(arena).locY + j - 1, dragons.get(arena).locZ + (m.getDestroyRadius() / 2) - i));

		return b;
	}
	
	public Block[] getSphereLoc(MEMain m, final Location l, String arena, Location l2, int x, int y, int z)
	{
		final MEDragon meDragon = dragons.get(arena);
		final Location loc = new Location(meDragon.world.getWorld(), meDragon.locX, meDragon.locY, meDragon.locZ);
		Block[] b = new Block[]{
				loc.clone().add(x, y, z).getBlock(), loc.clone().add(-x, y, z).getBlock(), loc.clone().add(x, -y, z).getBlock(), loc.clone().add(x, y, -z).getBlock(),
				loc.clone().add(-x, -y, z).getBlock(), loc.clone().add(x, -y, -z).getBlock(), loc.clone().add(-x, y, -z).getBlock(), loc.clone().add(-x, -y, -z).getBlock()
		};
		return b;
	}

	public void destroy(final MEMain m, final Location l, final Location l2, String arena, int length2, int blockRatio) {
		Tools.destroy(m, l, l2, arena, length2, "dragon", false, true, blockRatio);
	}

	@Override
	public boolean isDragon(LivingEntity entity) {
		final EntityLiving handle = ((CraftLivingEntity)entity).getHandle();
		return handle instanceof MEDragon;
	}

}
