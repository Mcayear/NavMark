package cn.vusv.plugin.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.vusv.plugin.NavMarkPlugin;
import cn.vusv.plugin.config.PlayerData;
import cn.vusv.plugin.config.PluginConfig;

public class NavMarkCommand extends Command {

    public NavMarkCommand() {
        super("navmark", "导航标志物", "/navmark <玩家名> [标志物名]");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§7------ NavMark Help ------§a");
            sender.sendMessage("§2/navmark:§f 导航标志物");
            sender.sendMessage("§2/navmark reload:§f 重载配置");
            sender.sendMessage("§2/navmark <玩家名> [标志物名]:§f 打开或关闭某标志物");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§7------ NavMark Help ------§a");
                sender.sendMessage("§2/navmark:§f 导航标志物");
                sender.sendMessage("§2/navmark reload:§f 重载配置");
                sender.sendMessage("§2/navmark <玩家名> [标志物名]:§f 打开或关闭某标志物");
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                PluginConfig.init();
                sender.sendMessage("§a重载成功");
                return true;
            }
        }

        Player player = sender.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("玩家 " + args[0] + " 未在线");
            return true;
        }

        if (args.length < 2 || !NavMarkPlugin.targetMap.containsKey(args[1])) {
            sender.sendMessage("目标 " + args[1] + " 不存在");
            return true;
        }

        String playerName = player.getName();
        if (NavMarkPlugin.playerDataMap.containsKey(playerName)) {
            PlayerData existingData = NavMarkPlugin.playerDataMap.get(playerName);
            Entity npc = sender.getServer().getLevelByName(existingData.levelName).getEntity(existingData.entityId);
            if (npc != null) {
                npc.close();
            }
            NavMarkPlugin.navMarkNpcData.remove(existingData.entityId);
            NavMarkPlugin.playerDataMap.remove(playerName);
            if (existingData.targetName.equals(args[1])) {
                sender.sendMessage("标志 " + args[1] + " 已关闭");
                return true;
            }
        }

        Position targetPos = NavMarkPlugin.targetMap.get(args[1]);
        Entity npc = NavMarkPlugin.createNavMarkNPC(player, args[1]);
        long npcId = npc.getId();
        NavMarkPlugin.playerDataMap.put(playerName, new PlayerData(npcId, targetPos.add(0, -1.5), args[1], targetPos.getLevel().getName()));
        NavMarkPlugin.navMarkNpcData.put(npcId, player);
        npc.setNameTag(args[1] + " §e" + targetPos.distance(player.getPosition()) + "§rm");

        return true;
    }
}
