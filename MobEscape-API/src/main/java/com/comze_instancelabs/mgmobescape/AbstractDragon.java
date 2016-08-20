package com.comze_instancelabs.mgmobescape;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public interface AbstractDragon {

	public void removeEnderdragon(String arena);

	public void stop(final MEMain m, BukkitTask t, final String arena);

	public void destroy(final MEMain m, final Location l, final Location l2, String arena, int length2, int blockRatio);

	public Block[] getLoc(MEMain m, final Location l, String arena, int i, int j, Location l2);

	public void playBlockBreakParticles(final Location loc, final Material m, final Player... players);

	public boolean isDragon(LivingEntity entity);
	
}
