package com.comze_instancelabs.mgmobescape.v1_7;

import java.util.ArrayList;

import net.minecraft.server.v1_7_R1.DamageSource;
import net.minecraft.server.v1_7_R1.EntityComplexPart;
import net.minecraft.server.v1_7_R1.EntityWither;
import net.minecraft.server.v1_7_R1.World;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgmobescape.AbstractMEWither;
import com.comze_instancelabs.mgmobescape.IArena;
import com.comze_instancelabs.mgmobescape.Main;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;

public class MEWither extends EntityWither implements AbstractMEWither{

	private boolean onGround = false;
	private ArrayList<Vector> points = new ArrayList();
	private int currentid;
	private double X;
	private double Y;
	private double Z;
	private Main m;
	private IArena arena;
	
	public MEWither(Main m, String arena, Location loc, World world, ArrayList<Vector> p) {
		super(world);
		this.m = m;
		this.arena = (IArena) MinigamesAPI.getAPI().pinstances.get(m).getArenaByName(arena);
		currentid = 0;
		this.points = p;
		setPosition(loc.getX(), loc.getY(), loc.getZ());
		yaw = loc.getYaw() + 180;
		while (yaw > 360) {
			yaw -= 360;
		}
		while (yaw < 0) {
			yaw += 360;
		}
		if (yaw < 45 || yaw > 315) {
			yaw = 0F;
		} else if (yaw < 135) {
			yaw = 90F;
		} else if (yaw < 225) {
			yaw = 180F;
		} else {
			yaw = 270F;
		}
		
		double disX = (this.locX - points.get(currentid).getX());
		double disY = (this.locY - points.get(currentid).getY());
		double disZ = (this.locZ - points.get(currentid).getZ());

		double tick = Math.sqrt(disX * disX + disY * disY + disZ * disZ) * 2 / m.mob_speed * Math.pow(0.98, currentid);

		this.X = (Math.abs(disX) / tick);
		this.Y = (Math.abs(disY) / tick);
		this.Z = (Math.abs(disZ) / tick);
	}

	@Override
	public void e() {
		return;
	}

	public boolean damageEntity(DamageSource damagesource, int i) {
		return false;
	}

	@Override
	public int getExpReward() {
		return 0;
	}

	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
		return false;
	}

	public Vector getCurrentPosition(){
		return points.get(currentid);
	}
	
	public Vector getCurrentPositionNext(){
		if(currentid + 1 < points.size() - 1){
			return points.get(currentid + 1);
		}
		return points.get(currentid);
	}
	
	public Vector getNextPosition() {
		
		double tempx = this.locX;
		double tempy = this.locY;
		double tempz = this.locZ;

		if (((Math.abs((int) tempx - points.get(currentid).getX()) == 0) && (Math.abs((int) tempz - points.get(currentid).getZ()) <= 3)) || ((Math.abs((int) tempz - points.get(currentid).getZ()) == 0) && (Math.abs((int) tempx - points.get(currentid).getX()) <= 3) && (Math.abs((int) tempy - points.get(currentid).getY()) <= 5))) {
			if (currentid < points.size() - 1) {
				currentid += 1;
			} else {
				// finish
				arena.stop();
			}
			
			ArrayList<String> temp = arena.getAllPlayers();
			for (String p : temp) {
				if (m.ppoint.containsKey(p)) {
					System.out.println("p:" + m.ppoint.get(p) + " d:" + currentid);
					if (m.ppoint.get(p) < currentid - 1) {
						// player fell behind mob
						arena.spectate(p);
					}
				}
			}

			double disX = (this.locX - points.get(currentid).getX());
			double disY = (this.locY - points.get(currentid).getY());
			double disZ = (this.locZ - points.get(currentid).getZ());
			
			double tick_ = Math.sqrt(disX * disX + disY * disY + disZ * disZ) * 2 / m.mob_speed * Math.pow(0.98, currentid);

			this.X = (Math.abs(disX) / tick_);
			this.Y = (Math.abs(disY) / tick_);
			this.Z = (Math.abs(disZ) / tick_);

			if ((int)this.locX <= points.get(currentid).getX()) {
				if ((int)this.locZ >= points.get(currentid).getZ()) {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 180F;
				} else {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 90F;
				}
			} else { // (this.locX > points.get(currentid).getX())
				if ((int)this.locZ >= points.get(currentid).getZ()) {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 90F;
				} else {
					this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z));
				}
			}

		}

		if ((int)this.locX <= points.get(currentid).getX()) {
			if ((int)this.locZ >= points.get(currentid).getZ()) {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 180F;
			} else {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 90F;
			}
		} else { // (this.locX > points.get(currentid).getX())
			if ((int)this.locZ >= points.get(currentid).getZ()) {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 90F;
			} else {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z));
			}
		}

		if (tempx < points.get(currentid).getX())
			tempx += this.X;
		else {
			tempx -= this.X;
		}

		if ((int) tempy < points.get(currentid).getY()) {
			tempy += this.Y;
		} else {
			tempy -= this.Y;
		}

		if (tempz < points.get(currentid).getZ())
			tempz += this.Z;
		else {
			tempz -= this.Z;
		}
		
		return new Vector(tempx, tempy, tempz);
	}
	
	
	public static float getLookAtYaw(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;

        if (dx != 0) {
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180 / Math.PI - 90);
    }

}