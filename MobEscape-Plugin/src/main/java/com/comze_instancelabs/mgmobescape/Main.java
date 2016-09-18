package com.comze_instancelabs.mgmobescape;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comze.instancelabs.mgmobescape.v1_10.V1_10Dragon;
import com.comze.instancelabs.mgmobescape.v1_10.V1_10Wither;
import com.comze.instancelabs.mgmobescape.v1_9.V1_9Dragon;
import com.comze.instancelabs.mgmobescape.v1_9.V1_9Wither;
import com.comze.instancelabs.mgmobescape.v1_9_R2.V1_9_4Dragon;
import com.comze.instancelabs.mgmobescape.v1_9_R2.V1_9_4Wither;
import com.comze_instancelabs.mgmobescape.v1_7.V1_7Dragon;
import com.comze_instancelabs.mgmobescape.v1_7.V1_7Wither;
import com.comze_instancelabs.mgmobescape.v1_7._R2.V1_7_5Dragon;
import com.comze_instancelabs.mgmobescape.v1_7._R2.V1_7_5Wither;
import com.comze_instancelabs.mgmobescape.v1_7._R3.V1_7_8Dragon;
import com.comze_instancelabs.mgmobescape.v1_7._R3.V1_7_8Wither;
import com.comze_instancelabs.mgmobescape.v1_7._R4.V1_7_10Dragon;
import com.comze_instancelabs.mgmobescape.v1_7._R4.V1_7_10Wither;
import com.comze_instancelabs.mgmobescape.v1_8._R1.V1_8Dragon;
import com.comze_instancelabs.mgmobescape.v1_8._R1.V1_8Wither;
import com.comze_instancelabs.mgmobescape.v1_8._R2.V1_8_3Dragon;
import com.comze_instancelabs.mgmobescape.v1_8._R2.V1_8_3Wither;
import com.comze_instancelabs.mgmobescape.v1_8._R2.V1_8_8Dragon;
import com.comze_instancelabs.mgmobescape.v1_8._R2.V1_8_8Wither;
import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Main extends JavaPlugin implements Listener, MEMain {

	MinigamesAPI api = null;
	public PluginInstance pli = null;
	static Main m = null;
	ICommandHandler cmdhandler;
	IArenaScoreboard scoreboard;

	public int destroy_radius = 10;
	public String dragon_name = "Dragon";
	public boolean spawn_falling_blocks = true;
	public boolean all_living_players_win = true;
	public boolean pvp = true;

	public double mob_speed = 1.0;
	
	public HashMap<String, Integer> ppoint = new HashMap<String, Integer>();
	public ArrayList<String> p_used_kit = new ArrayList<String>();

	public void onEnable() {
		cmdhandler = new ICommandHandler();
		m = this;
		getServer().getPluginManager().registerEvents(this, this);
		switch (MinigamesAPI.SERVER_VERSION)
		{
		default:
		case Unknown:
			getLogger().info("incompatible version " + MinigamesAPI.SERVER_VERSION);
			break;
		case V1_10:
		case V1_10_R1:
			getLogger().info("Turned on 1.10R1 mode.");
			break;
		case V1_7:
		case V1_7_R1:
			getLogger().info("Turned on 1.7R1 mode.");
			break;
		case V1_7_R2:
			getLogger().info("Turned on 1.7R2 mode.");
			break;
		case V1_7_R3:
			getLogger().info("Turned on 1.7R3 mode.");
			break;
		case V1_7_R4:
			getLogger().info("Turned on 1.7R4 mode.");
			break;
		case V1_8:
		case V1_8_R1:
			getLogger().info("Turned on 1.8R1 mode.");
			break;
		case V1_8_R2:
			getLogger().info("Turned on 1.8R2 mode.");
			break;
		case V1_8_R3:
			getLogger().info("Turned on 1.8R3 mode.");
			break;
		case V1_9:
		case V1_9_R1:
			getLogger().info("Turned on 1.9R1 mode.");
			break;
		case V1_9_R2:
			getLogger().info("Turned on 1.9R2 mode.");
			break;
		}
		registerEntities();

		api = MinigamesAPI.getAPI().setupAPI(this, "mobescape", IArena.class, new ArenasConfig(this), new MessagesConfig(this), new IClassesConfig(this), new StatsConfig(this, false), new DefaultConfig(this, false), true);
		PluginInstance pinstance = api.pinstances.get(this);
		pinstance.addLoadedArenas(loadArenas(this, pinstance.getArenasConfig()));
		pinstance.arenaSetup = new IArenaSetup();
		IArenaScoreboard score = new IArenaScoreboard(this);
		pinstance.scoreboardManager = score;
		scoreboard = score;
		IArenaListener listener = new IArenaListener(this, pinstance, "mobescape");
		pinstance.setArenaListener(listener);
		MinigamesAPI.getAPI().registerArenaListenerLater(this, listener);
		pli = pinstance;

		this.getConfig().addDefault("config.mob_name", "Dragon");
		this.getConfig().addDefault("config.mob_speed", 1);
		this.getConfig().addDefault("config.destroy_radius", destroy_radius);
		this.getConfig().addDefault("config.spawn_falling_blocks", spawn_falling_blocks);
		this.getConfig().addDefault("config.all_living_players_win", all_living_players_win);
		this.getConfig().addDefault("config.die_below_bedrock_level", 4);
		this.getConfig().addDefault("config.allow_player_pvp", true);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		this.dragon_name = this.getConfig().getString("config.mob_name");
		this.mob_speed = this.getConfig().getDouble("config.mob_speed");
		this.destroy_radius = this.getConfig().getInt("config.destroy_radius");
		this.spawn_falling_blocks = this.getConfig().getBoolean("config.spawn_falling_blocks");
		this.all_living_players_win = this.getConfig().getBoolean("config.all_living_players_win");
		this.pvp = this.getConfig().getBoolean("config.allow_player_pvp");

		if (this.getConfig().isBoolean("config.die_below_bedrock_level"))
		{
			boolean die_below_zero = this.getConfig().getBoolean("config.die_below_bedrock_level");
			if (die_below_zero) {
				pli.getArenaListener().loseY = 100;
			}
		}
		else
		{
			pli.getArenaListener().loseY = this.getConfig().getInt("config.die_below_bedrock_level");
		}

		pinstance.setAchievementGuiEnabled(true);

		boolean continue_ = false;
		for (Method m : pli.getArenaAchievements().getClass().getMethods()) {
			if (m.getName().equalsIgnoreCase("addDefaultAchievement")) {
				continue_ = true;
			}
		}
		if (continue_) {
			pli.getArenaAchievements().addDefaultAchievement("no_used_kit", "Haven't used any kit!", 50);
			pli.getAchievementsConfig().getConfig().options().copyDefaults(true);
			pli.getAchievementsConfig().saveConfig();
		}

	}

	private boolean registerEntities() {

		switch (MinigamesAPI.SERVER_VERSION)
		{
		default:
		case Unknown:
			return false;
		case V1_10:
		case V1_10_R1:
			return V1_10Dragon.registerEntities();
		case V1_7:
		case V1_7_R1:
			return V1_7Dragon.registerEntities();
		case V1_7_R2:
			return V1_7_5Dragon.registerEntities();
		case V1_7_R3:
			return V1_7_8Dragon.registerEntities();
		case V1_7_R4:
			return V1_7_10Dragon.registerEntities();
		case V1_8:
		case V1_8_R1:
			return V1_8Dragon.registerEntities();
		case V1_8_R2:
			return V1_8_3Dragon.registerEntities();
		case V1_8_R3:
			return V1_8_8Dragon.registerEntities();
		case V1_9:
		case V1_9_R1:
			return V1_9Dragon.registerEntities();
		case V1_9_R2:
			return V1_9_4Dragon.registerEntities();
		}
	}

	public static ArrayList<Arena> loadArenas(JavaPlugin plugin, ArenasConfig cf) {
		ArrayList<Arena> ret = new ArrayList<Arena>();
		FileConfiguration config = cf.getConfig();
		if (!config.isSet("arenas")) {
			return ret;
		}
		for (String arena : config.getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false)) {
			if (Validator.isArenaValid(plugin, arena, cf.getConfig())) {
				ret.add(initArena(arena));
			}
		}
		return ret;
	}

	public static IArena initArena(String arena) {
		IArena a = new IArena(m, arena);
		ArenaSetup s = MinigamesAPI.getAPI().pinstances.get(m).arenaSetup;
		a.init(Util.getSignLocationFromArena(m, arena), Util.getAllSpawns(m, arena), Util.getMainLobby(m), Util.getComponentForArena(m, arena, "lobby"), s.getPlayerCount(m, arena, true), s.getPlayerCount(m, arena, false), s.getArenaVIP(m, arena));
		return a;
	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		cmdhandler.handleArgs(this, MinigamesAPI.getAPI().getPermissionGamePrefix("mobescape"), "/" + cmd.getName(), sender, args);
		if (args.length > 0) {
			String action = args[0];
			if (action.equalsIgnoreCase("setmobspawn")) {
				if (args.length > 1) {
					String arena = args[1];

					if (!sender.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix("mobescape") + ".setup")) {
						sender.sendMessage(pli.getMessagesConfig().no_perm);
						return true;
					}
					if (sender instanceof Player) {
						Player p = (Player) sender;
						if (args.length > 1) {
							Util.saveComponentForArena(m, arena, "mobspawn", p.getLocation());
							sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "mobspawn"));
						} else {
							sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd.getName() + " " + action + " <arena>");
						}
					}
				}
			} else if (action.equalsIgnoreCase("setflypoint")) {
				if (args.length > 1) {
					String arena = args[1];

					if (!sender.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix("mobescape") + ".setup")) {
						sender.sendMessage(pli.getMessagesConfig().no_perm);
						return true;
					}
					if (sender instanceof Player) {
						Player p = (Player) sender;
						int count = getAllPoints(m, arena).size();
						Util.saveComponentForArena(m, arena, "flypoint.f" + Integer.toString(count), p.getLocation());
						sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "flypoint " + Integer.toString(count)));
					}
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd.getName() + " " + action + " <arena>");
				}
			} else if (action.equalsIgnoreCase("setmobtype")) {
				if (args.length > 2) {
					String arena = args[1];

					if (!sender.hasPermission(MinigamesAPI.getAPI().getPermissionGamePrefix("mobescape") + ".setup")) {
						sender.sendMessage(pli.getMessagesConfig().no_perm);
						return true;
					}
					if (sender instanceof Player) {
						if (args[2].equalsIgnoreCase("dragon") || args[2].equalsIgnoreCase("wither")) {
							Player p = (Player) sender;
							ArenasConfig config = pli.getArenasConfig();
							config.getConfig().set(ArenaConfigStrings.ARENAS_PREFIX + arena + ".mobtype", args[2]);
							config.saveConfig();
							IArena a = (IArena) pli.getArenaByName(arena);
							if (a != null) {
								a.mobtype = args[2];
							}
							sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "mobtype"));
						} else {
							sender.sendMessage(ChatColor.AQUA + "Mobtypes: wither, dragon");
						}
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "Mobtypes: wither, dragon");
					sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: /" + cmd.getName() + " " + action + " <arena> <mobtype>");
				}
			} else if (action.equalsIgnoreCase("help")) {
				Bukkit.getScheduler().runTaskLater(this, new Runnable() {
					public void run() {
						sender.sendMessage(ChatColor.RED + "Important commands: (Check project page for more information)");
						sender.sendMessage(ChatColor.DARK_AQUA + "/etm setflypoint <arena>" + ChatColor.GRAY + " - Set at least two of them!");
						sender.sendMessage(ChatColor.DARK_AQUA + "/etm setmobspawn <arena>");
					}
				}, 5L);
			}
		}
		return true;
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			IArena a = (IArena) pli.global_players.get(p.getName());
			if (a.getArenaState() == ArenaState.INGAME) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		final Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void EntityChangeBlockEvent(org.bukkit.event.entity.EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.FALLING_BLOCK) {
			if (event.getEntity().hasMetadata("1337")) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		try {
			final Player p = event.getPlayer();
			if (pli.global_players.containsKey(p.getName())) {
				final IArena a = (IArena) pli.global_players.get(p.getName());
				if (!pli.global_lost.containsKey(p.getName())) {
					if (a.getArenaState() == ArenaState.INGAME) {
						if (a.lowbounds != null && a.highbounds != null) {
							if (p.getLocation().getBlockY() + pli.getArenaListener().loseY < a.lowbounds.getBlockY()) {
								a.spectate(p.getName());
								return;
							}

							int index = getAllPoints(m, a.getName()).size() - 1;
							if (Math.abs(p.getLocation().getBlockX() - getAllPoints(m, a.getName()).get(index).getBlockX()) < 3 && Math.abs(p.getLocation().getBlockZ() - getAllPoints(m, a.getName()).get(index).getBlockZ()) < 3 && Math.abs(p.getLocation().getBlockY() - getAllPoints(m, a.getName()).get(index).getBlockY()) < 3) {
								if (!all_living_players_win) {
									for (String p_ : a.getAllPlayers()) {
										if (!p_.equalsIgnoreCase(p.getName())) {
											pli.global_lost.put(p_, a);
										}
									}
								}
								a.stop();
								return;
							}

							// TODO Die behind mob (experimental)
							if (!ppoint.containsKey(p.getName())) {
								ppoint.put(p.getName(), -1);
							}
							int i = ppoint.get(p.getName());

							int size = getAllPoints(m, a.getName()).size();
							if (i < size) {
								if (i > -1) {
									int defaultdelta = 5;

									if (i + 1 < size) {
										Location temp = getAllPoints(m, a.getName()).get(i + 1);

										if (Math.abs(p.getLocation().getBlockX() - temp.getBlockX()) < defaultdelta && Math.abs(p.getLocation().getBlockZ() - temp.getBlockZ()) < defaultdelta && Math.abs(p.getLocation().getBlockY() - temp.getBlockY()) < defaultdelta * 2) {
											i++;
											ppoint.put(p.getName(), i);
										}
									}
								} else {
									int defaultdelta = 5;
									Location temp = getAllPoints(m, a.getName()).get(0);
									if (Math.abs(p.getLocation().getBlockX() - temp.getBlockX()) < defaultdelta && Math.abs(p.getLocation().getBlockZ() - temp.getBlockZ()) < defaultdelta) {
										i++;
										ppoint.put(p.getName(), i);
									}
								}
							}
						} else {
							this.getLogger().severe("You forgot to set boundaries, this will cause bugs. Please fix your setup.");
						}
					}
				}
			}
		} catch (Exception e) {
			this.getLogger().log(Level.SEVERE, "Failed onMove", e);
		}

	}

	public static ArrayList<Location> getAllPoints(JavaPlugin plugin, String arena) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig();
		ArrayList<Location> ret = new ArrayList<Location>();
		if (!config.isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".flypoint")) {
			return ret;
		}
		for (String spawn : config.getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX + arena + ".flypoint.").getKeys(false)) {
			ret.add(Util.getComponentForArena(plugin, arena, "flypoint." + spawn));
		}
		return ret;
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity().getType() == EntityType.ENDER_PEARL) {
			if (event.getEntity().getShooter() instanceof Player) {
				final Player p = (Player) event.getEntity().getShooter();
				if (pli.global_players.containsKey(p.getName())) {
					p.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 2));
					p.updateInventory();
					for (final Entity t : p.getNearbyEntities(40, 40, 40)) {
						if (t instanceof Player) {
							if (t != p && pli.global_players.containsKey(((Player) t).getName())) {
								Bukkit.getScheduler().runTaskLater(this, new Runnable() {
									public void run() {
										p.teleport(t);
									}
								}, 5L);
							}
						}
					}
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			if (!event.hasItem()) {
				return;
			}
			final Arena arena = pli.global_players.get(p.getName());
			if (arena.getArenaState() != ArenaState.INGAME) {
				return;
			}
			int amount = event.getItem().getAmount();
			Material type = event.getItem().getType();
			ArrayList<ItemStack> temp = new ArrayList<ItemStack>();
			for (ItemStack i : p.getInventory().getContents()) {
				if (i != null) {
					if (i.getType() != type) {
						temp.add(i);
					}
				}
			}
			p.getInventory().clear();
			p.updateInventory();
			if (event.getItem().getTypeId() == 258) {
				double factor = 1.3D;
				if (this.getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena.getInternalName() + ".jumpFactor"))
				{
					factor = this.getConfig().getDouble(ArenaConfigStrings.ARENAS_PREFIX + arena.getInternalName() + ".jumpFactor");
				}
				Vector direction = p.getLocation().getDirection().multiply(factor);
				direction.setY(direction.getY() + 1.5);
				p.setVelocity(direction);
				event.setCancelled(true);
				p_used_kit.add(p.getName());
			} else if (event.getItem().getTypeId() == 368) {
				for (String p_ : pli.global_players.get(p.getName()).getAllPlayers()) {
					if (Validator.isPlayerOnline(p_) && !pli.global_lost.containsKey(p_)) {
						Util.teleportPlayerFixed(p, Bukkit.getPlayer(p_).getLocation());
					}
				}
				event.setCancelled(true);
				p_used_kit.add(p.getName());
			} else if (event.getItem().getTypeId() == 46) {
				p.getLocation().getWorld().dropItemNaturally(p.getLocation().add(0, 3, 0), new ItemStack(Material.TNT)).setVelocity(new Vector(0, 1, 0));
				event.setCancelled(true);
				p_used_kit.add(p.getName());
			}
			if (amount - 1 > 0) {
				p.getInventory().addItem(new ItemStack(type, amount - 1));
			}
			for (ItemStack i_ : temp) {
				p.getInventory().addItem(i_);
			}
			p.updateInventory();
			return;
		}

	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		if (pli.global_players.containsKey(event.getPlayer().getName())) {
			if (event.getItem().getItemStack().getType() == Material.TNT) {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
				event.getItem().remove();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Player attacker = null;
			if (event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				if (projectile.getShooter() instanceof Player) {
					attacker = (Player) projectile.getShooter();
				}
			} else if (event.getDamager() instanceof Player) {
				attacker = (Player) event.getDamager();
			} else {
				return;
			}

			if (pli.global_players.containsKey(p.getName()) && pli.global_players.containsKey(attacker.getName())) {
				Arena a = (Arena) pli.global_players.get(p.getName());
				if (a.getArenaState() == ArenaState.STARTING) {
					event.setCancelled(true);
				} else {
					if (pvp) {
						event.setCancelled(false);
						// TODO test if this affects other minigames
						// p.damage(1D);
						// p.setVelocity(p.getLocation().getDirection().add(new Vector(0.1, 0.1, 0.1)).multiply(1.5D));
					}
				}
			}
		}
	}

	@Override
	public Plugin getPlugin() {
		return this;
	}

	@Override
	public AbstractDragon createDragon() {
		switch (MinigamesAPI.SERVER_VERSION)
		{
		default:
		case Unknown:
			return null;
		case V1_10:
		case V1_10_R1:
			return new V1_10Dragon();
		case V1_7:
		case V1_7_R1:
			return new V1_7Dragon();
		case V1_7_R2:
			return new V1_7_5Dragon();
		case V1_7_R3:
			return new V1_7_8Dragon();
		case V1_7_R4:
			return new V1_7_10Dragon();
		case V1_8:
		case V1_8_R1:
			return new V1_8Dragon();
		case V1_8_R2:
			return new V1_8_3Dragon();
		case V1_8_R3:
			return new V1_8_8Dragon();
		case V1_9:
		case V1_9_R1:
			return new V1_9Dragon();
		case V1_9_R2:
			return new V1_9_4Dragon();
		}
	}

	@Override
	public AbstractWither createWither() {
		switch (MinigamesAPI.SERVER_VERSION)
		{
		default:
		case Unknown:
			return null;
		case V1_10:
		case V1_10_R1:
			return new V1_10Wither();
		case V1_7:
		case V1_7_R1:
			return new V1_7Wither();
		case V1_7_R2:
			return new V1_7_5Wither();
		case V1_7_R3:
			return new V1_7_8Wither();
		case V1_7_R4:
			return new V1_7_10Wither();
		case V1_8:
		case V1_8_R1:
			return new V1_8Wither();
		case V1_8_R2:
			return new V1_8_3Wither();
		case V1_8_R3:
			return new V1_8_8Wither();
		case V1_9:
		case V1_9_R1:
			return new V1_9Wither();
		case V1_9_R2:
			return new V1_9_4Wither();
		}
	}

	@Override
	public int getDestroyRadius() {
		return this.destroy_radius;
	}

	@Override
	public boolean isSpawnFallingBlocks() {
		return this.spawn_falling_blocks;
	}

	@Override
	public double getMobSpeed() {
		return this.mob_speed;
	}

	@Override
	public HashMap<String, Integer> getPPoint() {
		return this.ppoint;
	}

	@Override
	public String getDragonName() {
		return this.dragon_name;
	}

	@Override
	public PluginInstance getPluginInstance() {
		return this.pli;
	}

}
