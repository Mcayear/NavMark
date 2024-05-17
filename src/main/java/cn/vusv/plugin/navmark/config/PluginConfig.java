package cn.vusv.plugin.navmark.config;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.vusv.plugin.navmark.NavMarkPlugin;

public class PluginConfig {
    public static void init() {
        Config cfg = new Config(NavMarkPlugin.getInstance().getDataFolder()+"/config.yml", Config.YAML);
        NavMarkPlugin.RADIUS = cfg.getInt("radius");
        NavMarkPlugin.ENABLE_PITCH = cfg.getBoolean("enablePitch");

        NavMarkPlugin.targetMap.clear();
        ConfigSection targets = cfg.getSection("target");
        for (String key : targets.getKeys(false)) {
            ConfigSection target = targets.getSection(key);
            String levelName = target.getString("level");
            int x = target.getInt("x");
            int y = target.getInt("y");
            int z = target.getInt("z");
            Level level = Server.getInstance().getLevelByName(levelName);
            if (level == null) {
                NavMarkPlugin.getInstance().getLogger().warning("level "+levelName+" not found!");
                continue;
            }
            NavMarkPlugin.targetMap.put(key, new Position(x, y, z, level));
        }
    }
}
