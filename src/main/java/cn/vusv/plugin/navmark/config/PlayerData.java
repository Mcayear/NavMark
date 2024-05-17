package cn.vusv.plugin.navmark.config;


import cn.nukkit.level.Position;

public class PlayerData {
    public long entityId;
    public Position targetPos;
    public String targetName;
    public String levelName;

    public PlayerData(long entityId, Position targetPos, String targetName, String levelName) {
        this.entityId = entityId;
        this.targetPos = targetPos;
        this.targetName = targetName;
        this.levelName = levelName;
    }
}