/*
 * Copyright (C) 2012 mewin <mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.paramonod.WGRegionEffects;

import com.paramonod.WGRegionEffects.flags.CustomFlags;
import com.paramonod.WGRegionEffects.flags.EffectFlag;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;

import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WGRegionEffectsPlugin extends JavaPlugin {
    static final Flag EFFECT_FLAG = CustomFlags.EFFECT_FLAG;
    private WorldGuardPlugin plug;

    @Override
    public void onLoad() {
        plug = WGBukkit.getPlugin();
        FlagRegistry registry = plug.getFlagRegistry();
        try {
            // register our flag with the registry
            registry.register(EFFECT_FLAG);
            getServer().getLogger().info("Plugin registered");
        } catch (FlagConflictException e) {
            getServer().getLogger().warning("Plugin not registered");
        }

    }

    @Override
    public void onEnable() {


        getLogger().info("HIII");

        plug = WGBukkit.getPlugin();
        if (plug == null || !plug.isEnabled()) {
            getLogger().warning("Could not load WorldGuard Plugin, disabling");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            getLogger().info("WG Loaded");
        }

        SessionManager sessionManager = plug.getSessionManager();
        sessionManager.registerHandler(EffectFlag.FACTORY, null);

    }

    @Override
    public void onDisable() {
    }
}