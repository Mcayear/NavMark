package cn.vusv.plugin.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.vusv.plugin.NavMarkPlugin;
import cn.vusv.plugin.others.Clothes;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

import static cn.vusv.plugin.Utils.getMarkPos;

public class NavMarkNPC extends EntityHuman {
    @Setter
    @Getter
    public Player master;

    @Setter
    @Getter
    public String targetName;

    @Setter
    @Getter
    public Position targetPosition;

    public NavMarkNPC(FullChunk chunk, CompoundTag nbt, String name, Clothes clothes) {
        super(chunk, nbt.putString("NameTag", name).putString("name", "BNNPC")
                .putCompound("Skin", new CompoundTag()).putBoolean("ishuman", true).putBoolean("npc", true)
                .putFloat("scale", 1));
        Skin sk = clothes.build();
        nbt.putByteArray("Data", sk.getSkinData().data);
        nbt.putString("ModelID", sk.getSkinId())
                .putString("GeometryName", clothes.gen)
                .putByteArray("GeometryData", sk.getGeometryData().getBytes(StandardCharsets.UTF_8));
        nbt.putString("SkinResourcePatch", "{\"geometry\" : {\"default\" : \"" + clothes.gen + "\"}}\n");
        this.setSkin(clothes.build());
        this.setNameTag(name);
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.setScale(1.0f);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.closed) {
            return false;
        }
        boolean hasUpdate = super.entityBaseTick(tickDiff);
        Vector3 vec3 = getMarkPos(this.master);
        this.move(this.getX() - vec3.x, this.getY() - vec3.y, this.getZ() - vec3.z);
        this.updateMovement();
        double dist = targetPosition.distance(master.getPosition());

        if (dist < 2) {
            master.sendMessage("目标 " + targetName + " 在您附近，导航标志物已自动关闭。");
            NavMarkPlugin.playerDataMap.remove(master.getName());
            NavMarkPlugin.navMarkNpcData.remove(this.getId());
            this.close();
            return false;
        }
        this.setNameTag(targetName + " §e" + dist + "§rm");
        return hasUpdate;
    }

}
