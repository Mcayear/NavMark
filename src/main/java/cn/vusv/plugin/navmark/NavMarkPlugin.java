package cn.vusv.plugin.navmark;

import cn.lanink.gamecore.utils.NukkitTypeUtils;
import cn.nukkit.entity.Entity;
import cn.nukkit.lang.PluginI18n;
import cn.nukkit.lang.PluginI18nManager;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.Player;
import cn.vusv.plugin.navmark.command.NavMarkCommand;
import cn.vusv.plugin.navmark.config.PlayerData;
import cn.vusv.plugin.navmark.config.PluginConfig;
import cn.vusv.plugin.navmark.entity.NavMarkNPC;
import cn.vusv.plugin.navmark.others.Clothes;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class NavMarkPlugin extends PluginBase {

    @Getter
    public static NavMarkPlugin instance;

    @Getter
    public static PluginI18n i18n;

    public static HashMap<String, String> playergeonamemap = new HashMap<>();
    public static HashMap<String, String> playergeojsonmap = new HashMap<>();
    public static HashMap<String, BufferedImage> skinimagemap = new HashMap<>();

    public static double RADIUS = 3.0;
    public static boolean ENABLE_PITCH = true;

    public static final Map<String, Position> targetMap = new HashMap();

    public static final Map<String, PlayerData> playerDataMap = new HashMap<>();
    public static final Map<Long, Player> navMarkNpcData = new HashMap<>();

    @Override
    public void onLoad(){
        //save Plugin Instance
        instance = this;
        //register the plugin i18n
        i18n = PluginI18nManager.register(this);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new Events(), this);
        this.getServer().getCommandMap().register("navmark", new NavMarkCommand());
        init();
    }

    public void init() {
        this.saveResource("config.yml");
        this.saveResource("skin/arrow.json", "skin/arrow.json", false);
        this.saveResource("skin/arrow.png", "skin/arrow.png", false);
        PluginConfig.init();
    }

    public static Entity createNavMarkNPC(Player player, String targetName) {
        Position position = player.getPosition();
        // 创建并配置 NPC 实体
        NavMarkNPC npc = new NavMarkNPC(position.getChunk(), Entity.getDefaultNBT(position), targetName, new Clothes("arrow"));
        npc.setMaster(player);
        npc.setTargetName(targetName);
        npc.setTargetPosition(targetMap.get(targetName));
        if (NukkitTypeUtils.getNukkitType() == NukkitTypeUtils.NukkitType.MOT) {
            npc.setCanBeSavedWithChunk(false);
        }
        npc.spawnTo(player);
        // 省略代码示例
        return npc;
    }
}
