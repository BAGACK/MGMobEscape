package com.comze_instancelabs.mgmobescape;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.comze_instancelabs.minigamesapi.PluginInstance;

public interface MEMain {

	Plugin getPlugin();

	AbstractDragon createDragon();

	AbstractWither createWither();

	int getDestroyRadius();

	boolean isSpawnFallingBlocks();

	double getMobSpeed();

	HashMap<String, Integer> getPPoint();

	Logger getLogger();

	String getDragonName();

	PluginInstance getPluginInstance();

	boolean isSphereDestroy();
	
	boolean hasLiquidParticles();

	Set<MaterialType> getBlockBlacklist();
	
	public static final class MaterialType
	{
		private final int minecraftId;
		private final int variantId;
		private final int hash;
		public MaterialType(int minecraftId, int variantId) {
			super();
			this.minecraftId = minecraftId;
			this.variantId = variantId;
			this.hash = calcHash();
		}
		@Override
		public int hashCode() {
			return this.hash;
		}
		private int calcHash() {
			final int prime = 31;
			int result = 1;
			result = prime * result + minecraftId;
			result = prime * result + variantId;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MaterialType other = (MaterialType) obj;
			if (minecraftId != other.minecraftId)
				return false;
			if (variantId != other.variantId)
				return false;
			return true;
		}
	}

	boolean isDebug();

}
