package com.comze_instancelabs.mgmobescape.mobtools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgmobescape.AbstractDragon;
import com.comze_instancelabs.mgmobescape.AbstractEntity;
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
	
	private static AbstractEntity getEntityFromType(MEArena arena, String type)
	{
		if (type.equalsIgnoreCase("dragon")) {
			return arena.getDragonUtil();
		}
		if (type.equalsIgnoreCase("wither")) {
			return arena.getWitherUtil();
		}
		return null;
	}

	// the boolean parameters in this function are not used anymore
	public static void destroy(final MEMain m, final Location l, final Location l2, String arena, int length2, String type, boolean mode1_6, boolean mode1_7_5, int blockRatio) {
		final MEArena a = (MEArena) m.getPluginInstance().getArenaByName(arena);
		final AbstractEntity entity = getEntityFromType(a, type);
		if (entity != null)
		{
			if (m.isSphereDestroy())
			{
				int ratio = 0;
				double invRadius = 1.0D / m.getDestroyRadius();
				int radius = (int)Math.ceil(m.getDestroyRadius() + 0.5D);
				double nextXn = 0.0D;
				forX: for (int x = 0; x <= radius; ++x)
				{
					double xn = nextXn;
					nextXn = (x + 1) * invRadius;
					double nextYn = 0.0D;
					forY: for (int y = 0; y <= radius; ++y)
					{
						double yn = nextYn;
						nextYn = (y + 1) * invRadius;
						double nextZn = 0.0D;
						forZ: for (int z = 0; z <= radius; ++z)
						{
							double zn = nextZn;
							nextZn = (z + 1) * invRadius;
							double distanceSq = lengthSq(xn, yn, zn);
							if (distanceSq > 1.0D)
							{
								if (z == 0)
								{
									if (y == 0)
									{
										break forX;
									}
									break forY;
								}
								break forZ;
							}
							
							final Block[] loc =entity.getSphereLoc(m, l, arena, l2, x, y, z);
							a.getSmartReset().addChanged(loc);
							for (final Block b : loc) {
								if (b.getType() != Material.AIR && !m.getBlockBlacklist().contains(new MEMain.MaterialType(b.getTypeId(), b.getData()))) {
									if (m.isSpawnFallingBlocks()) {
										if (b.getType() != Material.WATER && b.getType() != Material.LAVA) {
											entity.playBlockBreakParticles(b.getLocation(), b.getType(), Bukkit.getOnlinePlayers().toArray(new Player[0]));
											ratio += blockRatio;
											if (ratio >= 100) {
												FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
												fb.setMetadata("1337", new FixedMetadataValue(m.getPlugin(), "true"));
												fb.setDropItem(false);
												fb.setVelocity(new Vector(Math.random() * 0.4, 0.4, Math.random() * 0.4));
												ratio = 0;
											}
										}
										else if (m.hasLiquidParticles()) {
											entity.playBlockBreakParticles(b.getLocation(), b.getType(), Bukkit.getOnlinePlayers().toArray(new Player[0]));
										}
									}
									b.setType(Material.AIR);
								}
							}
							
						}
					}
				}
			}
			else
			{
				int ratio = 0;
				for (int i = 0; i < m.getDestroyRadius(); i++) { // length1
					for (int j = 0; j < m.getDestroyRadius(); j++) {
						final Block[] loc = entity.getLoc(m, l, arena, i, j - (m.getDestroyRadius() / 3), l2);
						a.getSmartReset().addChanged(loc);
						for (final Block b : loc) {
							if (b.getType() != Material.AIR && !m.getBlockBlacklist().contains(new MEMain.MaterialType(b.getTypeId(), b.getData()))) {
								if (m.isSpawnFallingBlocks()) {
									if (b.getType() != Material.WATER && b.getType() != Material.LAVA) {
										entity.playBlockBreakParticles(b.getLocation(), b.getType(), Bukkit.getOnlinePlayers().toArray(new Player[0]));
										ratio += blockRatio;
										if (ratio >= 100) {
											FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
											fb.setMetadata("1337", new FixedMetadataValue(m.getPlugin(), "true"));
											fb.setDropItem(false);
											fb.setVelocity(new Vector(Math.random() * 0.4, 0.4, Math.random() * 0.4));
											ratio = 0;
										}
									}
									else if (m.hasLiquidParticles()) {
										entity.playBlockBreakParticles(b.getLocation(), b.getType(), Bukkit.getOnlinePlayers().toArray(new Player[0]));
									}
								}
								b.setType(Material.AIR);
							}
						}
					}
				}
			}
		}
	}
	
	private static double lengthSq(double x, double y, double z)
	{
		return (x * x + y * y + z * z);
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
