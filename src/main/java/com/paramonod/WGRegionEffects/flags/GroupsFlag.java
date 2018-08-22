package com.paramonod.WGRegionEffects.flags;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sun.reflect.generics.tree.Tree;

import javax.swing.border.EtchedBorder;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static org.bukkit.Bukkit.getServer;


public class GroupsFlag extends Handler {

    public static final Factory FACTORY = new Factory();

    private GroupsFlag(Session session) {
        super(session);
    }

    public static class Factory extends Handler.Factory<GroupsFlag> {
        @Override
        public GroupsFlag create(Session session) {
            return new GroupsFlag(session);
        }
    }


    @Override
    public boolean onCrossBoundary(Player player, Location from, Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        TreeSet<String> EnterGroups = new TreeSet<>();
        TreeSet<String> ExitGroups = new TreeSet<>();
        for (ProtectedRegion rg : entered) {
            String groups = rg.getFlag(CustomFlags.GROUPS_FLAG);
            if (groups != null)
                EnterGroups = addGroups(EnterGroups, groups);
        }
        for (ProtectedRegion rg : exited) {
            String groups = rg.getFlag(CustomFlags.GROUPS_FLAG);
            if (groups != null)
                ExitGroups = addGroups(ExitGroups, groups);
        }
        for (String group : EnterGroups){
            if (!ExitGroups.contains(group)){
                doCommands(player, group);
            }
        }
        return true;

    }
    private TreeSet<String> addGroups(TreeSet<String> res, String groups){
        groups = groups.trim().toLowerCase();
        String[] tmp = groups.split(",");
        for (String q : tmp) {
            res.add(q.trim());
        }
        return res;
    }

    private void doCommands(Player player, String group){
        //TODO: make groups file
    }
}

