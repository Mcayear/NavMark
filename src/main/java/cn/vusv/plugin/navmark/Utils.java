package cn.vusv.plugin.navmark;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import com.google.common.base.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class Utils {
    /**
     * 把base64转化为文件.
     *
     * @param base64   base64
     * @param filePath 目标文件路径
     * @return boolean isTrue
     */
    public static Boolean decryptByBase64(String base64, String filePath) {

        if (Strings.isNullOrEmpty(base64) && Strings.isNullOrEmpty(filePath)) {
            return Boolean.FALSE;
        }
        try {
            Files.write(Paths.get(filePath),
                    Base64.getDecoder().decode(base64.substring(base64.indexOf(",") + 1)), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    public static Position getMarkPos(Player player) {
        // 垂直 pitch, 水平 yaw
        if (NavMarkPlugin.ENABLE_PITCH) {
            // 获取 y, r
            double y = toFixed1(NavMarkPlugin.RADIUS * Math.cos(Math.toRadians(player.getPitch() + 90))),
                    r = toFixed1(NavMarkPlugin.RADIUS * Math.sin(Math.toRadians(player.getPitch() + 90)));
            // 获取 x, z 位置
            double x = toFixed1(r * Math.cos(Math.toRadians(player.getYaw() + 90))),
                    z = toFixed1(r * Math.sin(Math.toRadians(player.getYaw() + 90)));
            return player.getPosition().add(x, y - 1, z);
        } else {
            // 获取 x, z 位置
            double x = toFixed1(NavMarkPlugin.RADIUS * Math.cos(Math.toRadians(player.getYaw() + 90))),
                    z = toFixed1(NavMarkPlugin.RADIUS * Math.sin(Math.toRadians(player.getYaw() + 90)));
            return player.getPosition().add(x, -1, z);
        }
    }

    private static double toFixed1(double num) {
        return Math.round(num * 10) / 10.0;
    }
}
