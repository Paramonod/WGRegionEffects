package com.paramonod.WGRegionEffects.flags;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static org.bukkit.Bukkit.getServer;


public class EffectFlag extends Handler {

    public static final Factory FACTORY = new Factory();

    protected EffectFlag(Session session) {
        super(session);
    }

    public static class Factory extends Handler.Factory<EffectFlag> {
        @Override
        public EffectFlag create(Session session) {
            return new EffectFlag(session);
        }
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, com.sk89q.worldedit.util.Location from,
                                   com.sk89q.worldedit.util.Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        String effects = toSet.queryValue(player, CustomFlags.EFFECT_FLAG);
        ArrayList<String[]> ApplySet = new ArrayList<>();
        if (effects != null)
            ApplySet = getEffectsSet(effects);
        ArrayList<PotionEffectType> RevokeSet = new ArrayList<>();
        for (ProtectedRegion rg : exited) {
            String EffectFlag = rg.getFlag(CustomFlags.EFFECT_FLAG);
            ArrayList<String[]> tmpSet;
            if (EffectFlag != null) {
                tmpSet = getEffectsSet(EffectFlag);
                for (String[] effect : tmpSet)
                    RevokeSet.add(castToEffect(effect[0]));
            }
        }
        revokeEffects(RevokeSet, player);
        applyEffects(ApplySet, player);
        return true;
    }

    private PotionEffectType castToEffect(String effect) {
        return PotionEffectType.getByName(effect);
    }

    private void applyEffects(ArrayList<String[]> EffectsSet, LocalPlayer player) {
        for (String[] effectmas : EffectsSet)
            try {
                Bukkit.getPlayer(player.getUniqueId()).addPotionEffect(new PotionEffect(castToEffect(effectmas[0].trim()), Integer.MAX_VALUE,
                        Integer.parseInt(effectmas[1].trim())));
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
    }

    private void revokeEffects(ArrayList<PotionEffectType> EffectTypeSet, LocalPlayer player) {
        for (PotionEffectType effect : EffectTypeSet)
            try {
                Bukkit.getPlayer(player.getUniqueId()).removePotionEffect(effect);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
    }

    private ArrayList<String[]> getEffectsSet(String effects) {
        ArrayList<String[]> EffectsSet = new ArrayList<>();
        String[] tmpEffectsSet;
        if (effects.contains(","))
            tmpEffectsSet = effects.split(",");
        else {
            tmpEffectsSet = new String[1];
            tmpEffectsSet[0] = effects.trim();
        }
        for (String effect : tmpEffectsSet) {
            effect = effect.trim();
            String effectmas[] = effect.split(" ");
            if (effectmas.length != 2 || castToEffect(effectmas[0].trim()) == null) {
                getServer().getLogger().warning("Bad Effect: " + effect);
            } else {
                EffectsSet.add(effectmas);
            }
        }
        return EffectsSet;
    }
}

