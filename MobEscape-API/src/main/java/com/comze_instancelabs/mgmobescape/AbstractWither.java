package com.comze_instancelabs.mgmobescape;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public interface AbstractWither extends AbstractEntity {

	public void removeWither(String arena);

	public void stop(final MEMain m, BukkitTask t, final String arena);

	public void destroy(final MEMain m, final Location l, final Location l2, String arena, int length2, int blockRatio);

	public boolean isWither(LivingEntity entity);
	
}
