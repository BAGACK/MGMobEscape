package com.comze_instancelabs.mgmobescape.mobtools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgmobescape.AbstractDragon;
import com.comze_instancelabs.mgmobescape.AbstractMEDragon;
import com.comze_instancelabs.mgmobescape.AbstractMEWither;
import com.comze_instancelabs.mgmobescape.AbstractWither;
import com.comze_instancelabs.mgmobescape.MEArena;
import com.comze_instancelabs.mgmobescape.MEMain;

public class Tools {

	// the boolean parameters in this function are not used anymore
	public void stop(final MEMain m, BukkitTask t, final String arena, final String type) {

		if (t != null) {
			t.cancel();
		}

		Bukkit.getScheduler().runTaskLater(m.getPlugin(), new Runnable() {
			public void run() {
				if (type.equalsIgnoreCase("dragon")) {
					final AbstractDragon dragon = m.createDragon();
					dragon.removeEnderdragon(arena);
				} else if (type.equalsIgnoreCase("wither")) {
					final AbstractWither wither = m.createWither();
					wither.removeWither(arena);
				}
			}
		}, 10L);

	}

	// the boolean parameters in this function are not used anymore
	public static void destroy(final MEMain m, final Location l, final Location l2, String arena, int length2, String type, boolean mode1_6, boolean mode1_7_5) {
		final MEArena a = (MEArena) m.getPluginInstance().getArenaByName(arena);
		for (int i = 0; i < m.getDestroyRadius(); i++) { // length1
			for (int j = 0; j < m.getDestroyRadius(); j++) {
				if (type.equalsIgnoreCase("dragon")) {
					final AbstractDragon ad = a.getDragonUtil();

					for (final Block b : ad.getLoc(m, l, arena, i, j - (m.getDestroyRadius() / 3), l2)) {
						// Bukkit.getScheduler().runTask(m, new Runnable() {
						// public void run() {
						if (b.getType() != Material.AIR) {
							if (m.isSpawnFallingBlocks()) {
								ad.playBlockBreakParticles(b.getLocation(), b.getType());
								if (b.getType() != Material.WATER && b.getType() != Material.LAVA && m.isSpawnFallingBlocks()) {
									FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
									fb.setMetadata("1337", new FixedMetadataValue(m.getPlugin(), "true"));
									fb.setDropItem(false);
									fb.setVelocity(new Vector(Math.random() * 0.4, 0.4, Math.random() * 0.4));
								}
							}
							a.getSmartReset().addChanged(b, b.getType().equals(Material.CHEST));
							b.setType(Material.AIR);
						}
						// }
						// });
					}
				} else if (type.equalsIgnoreCase("wither")) {
					final AbstractWither aw = a.getWitherUtil();

					for (final Block b : aw.getLoc(m, l, arena, i, j - (m.getDestroyRadius() / 3), l2)) {
						// Bukkit.getScheduler().runTask(m, new Runnable() {
						// public void run() {
						if (b.getType() != Material.AIR) {
							if (m.isSpawnFallingBlocks()) {
								aw.playBlockBreakParticles(b.getLocation(), b.getType());
								if (b.getType() != Material.WATER && b.getType() != Material.LAVA && m.isSpawnFallingBlocks()) {
									FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
									fb.setMetadata("1337", new FixedMetadataValue(m.getPlugin(), "true"));
									fb.setVelocity(new Vector(Math.random() * 0.4, 0.4, Math.random() * 0.4));
								}
							}
							a.getSmartReset().addChanged(b, b.getType().equals(Material.CHEST));
							b.setType(Material.AIR);
						}
						// }
						// });
					}

				}
			}
		}
	}

	public static void setYawPitchDragon(AbstractMEDragon ad, Vector start, Vector l) {
		double dx = l.getX() - start.getX();
		double dy = l.getY() - start.getY();
		double dz = l.getZ() - start.getZ();

		float yaw = 0F;
		float pitch = 0F;

		if (dx != 0) {
			if (dx < 0) {
				yaw = (float) (1.5 * Math.PI);
			} else {
				yaw = (float) (0.5 * Math.PI);
			}
			yaw = (float) yaw - (float) Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = (float) Math.PI;
		}

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		pitch = (float) -Math.atan(dy / dxz);

		if (ad != null) {
			ad.setYawPitch(-yaw * 180F / (float) Math.PI - 180F, pitch * 180F / (float) Math.PI - 180F);
		}
	}

	public static void setYawPitchWither(AbstractMEWither aw, Vector start, Vector l) {
		double dx = l.getX() - start.getX();
		double dy = l.getY() - start.getY();
		double dz = l.getZ() - start.getZ();

		float yaw = 0F;
		float pitch = 0F;

		if (dx != 0) {
			if (dx < 0) {
				yaw = (float) (1.5 * Math.PI);
			} else {
				yaw = (float) (0.5 * Math.PI);
			}
			yaw = (float) yaw - (float) Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = (float) Math.PI;
		}

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		pitch = (float) -Math.atan(dy / dxz);

		if (aw != null) {
			aw.setYawPitch(-yaw * 180F / (float) Math.PI, pitch * 180F / (float) Math.PI);
		}
	}

}
