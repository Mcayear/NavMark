package cn.vusv.plugin.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector2;
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

    public void lookAt(Position pos) {
        double xdiff = pos.x - this.x;
        double zdiff = pos.z - this.z;
        double angle = Math.atan2(zdiff, xdiff);
        double yaw = ((angle * 180) / Math.PI) - 90;
        double ydiff = pos.y - this.y;
        Vector2 v = new Vector2(this.x, this.z);
        double dist = v.distance(pos.x, pos.z);
        angle = Math.atan2(dist, ydiff);
        double pitch = ((angle * 180) / Math.PI) - 90;
        this.yaw = yaw;
        this.headYaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return true;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.closed) {
            return false;
        }
        Position vec3 = getMarkPos(this.master);
        this.teleport(vec3);
        lookAt(targetPosition);
        double dist = targetPosition.distance(master.getPosition());

        AxisAlignedBB bb = this.getBoundingBox();
        final double x = this.getX() + 100, y = this.getY() + 100, z = this.getZ() + 100;
        final float dy = this.getHeight();
        bb.setMaxX(x);
        bb.setMinX(x);
        bb.setMaxZ(z);
        bb.setMinZ(z);
        bb.setMaxY(y + dy);
        bb.setMinY(y);

        if (dist < NavMarkPlugin.RADIUS) {
            master.sendMessage("目标 " + targetName + " 在您附近，导航标志物已自动关闭。");
            NavMarkPlugin.playerDataMap.remove(master.getName());
            NavMarkPlugin.navMarkNpcData.remove(this.getId());
            this.close();
            return false;
        }
        this.setNameTag(targetName + " §e" + String.format("%.1f", dist) + "§rm");
        return true;
    }

}
