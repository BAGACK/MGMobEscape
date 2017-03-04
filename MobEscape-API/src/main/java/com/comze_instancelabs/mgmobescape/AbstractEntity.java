package com.comze_instancelabs.mgmobescape;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface AbstractEntity {

	public Block[] getLoc(MEMain m, final Location l, String arena, int i, int j, Location l2);

	public Block[] getSphereLoc(MEMain m, final Location l, String arena, Location l2, int x, int y, int z);

	public void playBlockBreakParticles(final Location loc, final Material m, final Player... players);

}
