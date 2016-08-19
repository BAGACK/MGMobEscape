/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.comze_instancelabs.mgmobescape;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaListener;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Cuboid;

public class IArenaListener extends ArenaListener {

	private PluginInstance pli;

	public IArenaListener(JavaPlugin plugin, PluginInstance pinstance, String minigame) {
		super(plugin, pinstance, minigame);
		this.pli = pinstance; 
	}

	@EventHandler
	@Override
	public void onMobSpawn(CreatureSpawnEvent evt)
	{
		// allow spawn of our own dragons/withers
		final LivingEntity entity = evt.getEntity();
		System.out.println(entity);
		if (entity instanceof EnderDragon || entity instanceof Wither)
		{
			for (final Arena arena : this.pli.getArenas())
	        {
	            final Cuboid c = arena.getBoundaries();
	            if (c != null && c.containsLoc(evt.getLocation()))
	            {
	            	final IArena iarena = (IArena) arena;
	            	if (iarena.getDragonUtil() != null && iarena.getDragonUtil().isDragon(entity))
	            	{
	            		return;
	            	}
	            	if (iarena.getWitherUtil() != null && iarena.getWitherUtil().isWither(entity))
	            	{
	            		return;
	            	}
	                evt.setCancelled(true);
	            }
	        }
			return;
		}
		super.onMobSpawn(evt);
	}
	
	

}
