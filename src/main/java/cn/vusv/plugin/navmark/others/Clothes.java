package cn.vusv.plugin.navmark.others;

import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.utils.Config;
import cn.vusv.plugin.navmark.NavMarkPlugin;
import cn.vusv.plugin.navmark.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.UUID;

public class Clothes {
    public String skinname;
    public String playername;
    public Skin skin;
    public String gen = null;

    public Clothes(String skid, String p) {
        skinname = skid;
        playername = p;
        skin = Server.getInstance().getPlayer(playername).getSkin();
    }

    public Clothes(String skid) {
        skinname = skid;
        playername = "NoPlayerSkin";
        skin = new Skin();
        skin.setGeometryData(this.steveModal);
    }

    public Skin build() {
        skin.setSkinId(skinname);
        skin.setTrusted(true);
        try {
            File fileskin = new File(NavMarkPlugin.getInstance().getDataFolder() + "/skin/" + skinname + ".png");
            if (skinname.equals("Steve") && !fileskin.exists()) {
                Utils.decryptByBase64("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAAAV1BMVEUAAAD//fv///D++fj99fT+9PL99PT88vD+8O787Oj56+n84OH/47/y4+D/2eT6ztDmztD6x7a01s//q6iuwtbrlLHrk7G1m8SxjpGjepWRao2FbW55XmFhKOxcAAAAAXRSTlMAQObYZgAAAoRJREFUeNrtlttyozAMhn0gddEuTmtZpdvy/s+544N8AJIhdPZiZ/JPBiFif5ZlgyxEFhRN8SdWmqZ63RUYM2hjfgDg8bPEowIzaD0YgNfL5XJ5hROAqACQ8hSAp5AAZ6aQkngecCQCvNPxlup6TABYF7UsKxgwBkztwX62BQCTQYIKgPQwAoYVYMiAYFNDkYGEmGmTCdZMAgYwGgaAaxQUn63JgCEAMW9RAzAEO4AAHRpqgGW5XpcFip9tSZUWRgtd9/iQL2AmM0wGls/v6/X7c2GfrW5fhWq1TgAdASYAXt6kfHtZ2Gd7E5Ct4LeP15/9zVs5lQt35YsQiIhERGW/eOxUnisr5bi3wdYAvAFAdRhAtAtw+wCFbgWgcaQ9AG0BiKgCoFcD6PJB5JS1CjtAeMIAm4wqU/B+9g5VvHfOUSAo1wJsiMChc6GB9TZGVImIob9VCWAdkSNCXCUwpTDQOYLVlJJvrbexWQcYg2rTfOsQbejkbDI8wzSObQEy6VeUlDIPgWk06+OkHFITqfX2DuAr6E/UV3ufAvOeyLm9COJMRinle9DvqPf2Po7t59mHVNEdwK0I0uxDBO2WCwDVA+p+cGn185pSpzsRpP45dc0a9s0aQAf++MjZtuu9fRvw1FNPPaa2xCBu/hKIeBaAfMGzEfyL+fq+WhPiYx+Rdbn/zwD9p9Qjl7pU2Q4AfHuo8HMqJGjtYUA+VOQi7bmcxkJxCBBKn/P1SJPPDbFQKDqeg5HPDarkIBxcDgC4zHPZJyLFR6RDEWwB8Vh2fBW4yHLR9SECl1PhTwBmTy73Jz+vNkvxRPUqQKUIXFO9/R4A22/CNoLOH3cj2OyD7rywC2j0F+DPXhic88ufAAAAAElFTkSuQmCC", "./plugins/BlocklyNukkit/skin/Steve.png");
            }
            if (fileskin.exists()) {
                if (NavMarkPlugin.skinimagemap.get(skinname) != null) {
                    skin.setSkinData(NavMarkPlugin.skinimagemap.get(skinname));
                } else {
                    BufferedImage bi = ImageIO.read(fileskin);
                    skin.setSkinData(bi);
                    NavMarkPlugin.skinimagemap.put(skinname, bi);
                }
                File filegeo = new File(NavMarkPlugin.getInstance().getDataFolder() + "/skin/" + skinname + ".json");
                if (NavMarkPlugin.playergeonamemap.get(skinname) != null) {
                    skin.setGeometryName(NavMarkPlugin.playergeonamemap.get(skinname));
                    gen = NavMarkPlugin.playergeonamemap.get(skinname);
                } else {
                    if (filegeo.exists()) {
                        Map<String, Object> skinJson = (new Config(NavMarkPlugin.getInstance().getDataFolder() + "/skin/" + skinname + ".json", Config.JSON)).getAll();
                        String geometryName = null;
                        for (Map.Entry<String, Object> entry1 : skinJson.entrySet()) {
                            if (geometryName == null) {
                                geometryName = entry1.getKey();
                                gen = geometryName;
                            }
                        }
                        NavMarkPlugin.playergeonamemap.put(skinname, geometryName);
                        skin.setGeometryName(geometryName);
                    } else {
                        skin.setGeometryName("geometry.humanoid.customSlim");
                        gen = "geometry.humanoid.customSlim";
                    }
                }
                if (NavMarkPlugin.playergeojsonmap.get(skinname) != null) {
                    skin.setGeometryData(NavMarkPlugin.playergeojsonmap.get(skinname));
                } else {
                    if (filegeo.exists()) {
                        BufferedReader reader = new BufferedReader(new FileReader(filegeo));
                        String geotext = "";
                        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                            geotext += line;
                        }
                        reader.close();
                        NavMarkPlugin.playergeojsonmap.put(skinname, geotext);
                        skin.setGeometryData(geotext);
                    }
                }
//                byte[] data = Binary.appendBytes(skin.getSkinData().data, new byte[][]{skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8)});
//                skin.setSkinId(UUID.nameUUIDFromBytes(data) + "." + skinname);
                skin.setSkinId(UUID.randomUUID() + "." + skinname);
                return skin;
            } else {
                NavMarkPlugin.getInstance().getLogger().warning("没有该皮肤！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return skin;
    }

    public String steveModal = "{\n" +
            "   \"format_version\" : \"1.12.0\",\n" +
            "   \"minecraft:geometry\" : [\n" +
            "      {\n" +
            "         \"bones\" : [\n" +
            "            {\n" +
            "               \"name\" : \"body\",\n" +
            "               \"parent\" : \"waist\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"waist\",\n" +
            "               \"pivot\" : [ 0.0, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -5.0, 8.0, 3.0 ],\n" +
            "                     \"size\" : [ 10, 16, 1 ],\n" +
            "                     \"uv\" : [ 0, 0 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"cape\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 3.0 ],\n" +
            "               \"rotation\" : [ 0.0, 180.0, 0.0 ]\n" +
            "            }\n" +
            "         ],\n" +
            "         \"description\" : {\n" +
            "            \"identifier\" : \"geometry.cape\",\n" +
            "            \"texture_height\" : 32,\n" +
            "            \"texture_width\" : 64\n" +
            "         }\n" +
            "      },\n" +
            "      {\n" +
            "         \"bones\" : [\n" +
            "            {\n" +
            "               \"name\" : \"root\",\n" +
            "               \"pivot\" : [ 0.0, 0.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -4.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 8, 12, 4 ],\n" +
            "                     \"uv\" : [ 16, 16 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"body\",\n" +
            "               \"parent\" : \"waist\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"waist\",\n" +
            "               \"parent\" : \"root\",\n" +
            "               \"pivot\" : [ 0.0, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -4.0, 24.0, -4.0 ],\n" +
            "                     \"size\" : [ 8, 8, 8 ],\n" +
            "                     \"uv\" : [ 0, 0 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"head\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"cape\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24, 3.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.50,\n" +
            "                     \"origin\" : [ -4.0, 24.0, -4.0 ],\n" +
            "                     \"size\" : [ 8, 8, 8 ],\n" +
            "                     \"uv\" : [ 32, 0 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"hat\",\n" +
            "               \"parent\" : \"head\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ 4.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 32, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftArm\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 5.0, 22.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ 4.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 48, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftSleeve\",\n" +
            "               \"parent\" : \"leftArm\",\n" +
            "               \"pivot\" : [ 5.0, 22.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"leftItem\",\n" +
            "               \"parent\" : \"leftArm\",\n" +
            "               \"pivot\" : [ 6.0, 15.0, 1.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -8.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 40, 16 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightArm\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ -5.0, 22.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -8.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 40, 32 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightSleeve\",\n" +
            "               \"parent\" : \"rightArm\",\n" +
            "               \"pivot\" : [ -5.0, 22.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"locators\" : {\n" +
            "                  \"lead_hold\" : [ -6, 15, 1 ]\n" +
            "               },\n" +
            "               \"name\" : \"rightItem\",\n" +
            "               \"parent\" : \"rightArm\",\n" +
            "               \"pivot\" : [ -6, 15, 1 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -0.10, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 16, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftLeg\",\n" +
            "               \"parent\" : \"root\",\n" +
            "               \"pivot\" : [ 1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -0.10, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 0, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftPants\",\n" +
            "               \"parent\" : \"leftLeg\",\n" +
            "               \"pivot\" : [ 1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -3.90, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 0, 16 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightLeg\",\n" +
            "               \"parent\" : \"root\",\n" +
            "               \"pivot\" : [ -1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -3.90, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 0, 32 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightPants\",\n" +
            "               \"parent\" : \"rightLeg\",\n" +
            "               \"pivot\" : [ -1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -4.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 8, 12, 4 ],\n" +
            "                     \"uv\" : [ 16, 32 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"jacket\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            }\n" +
            "         ],\n" +
            "         \"description\" : {\n" +
            "            \"identifier\" : \"geometry.humanoid.custom\",\n" +
            "            \"texture_height\" : 64,\n" +
            "            \"texture_width\" : 64,\n" +
            "            \"visible_bounds_height\" : 2,\n" +
            "            \"visible_bounds_offset\" : [ 0, 1, 0 ],\n" +
            "            \"visible_bounds_width\" : 1\n" +
            "         }\n" +
            "      },\n" +
            "      {\n" +
            "         \"bones\" : [\n" +
            "            {\n" +
            "               \"name\" : \"root\",\n" +
            "               \"pivot\" : [ 0.0, 0.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"waist\",\n" +
            "               \"parent\" : \"root\",\n" +
            "               \"pivot\" : [ 0.0, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -4.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 8, 12, 4 ],\n" +
            "                     \"uv\" : [ 16, 16 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"body\",\n" +
            "               \"parent\" : \"waist\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -4.0, 24.0, -4.0 ],\n" +
            "                     \"size\" : [ 8, 8, 8 ],\n" +
            "                     \"uv\" : [ 0, 0 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"head\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.50,\n" +
            "                     \"origin\" : [ -4.0, 24.0, -4.0 ],\n" +
            "                     \"size\" : [ 8, 8, 8 ],\n" +
            "                     \"uv\" : [ 32, 0 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"hat\",\n" +
            "               \"parent\" : \"head\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -3.90, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 0, 16 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightLeg\",\n" +
            "               \"parent\" : \"root\",\n" +
            "               \"pivot\" : [ -1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -3.90, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 0, 32 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightPants\",\n" +
            "               \"parent\" : \"rightLeg\",\n" +
            "               \"pivot\" : [ -1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -0.10, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 16, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"mirror\" : true,\n" +
            "               \"name\" : \"leftLeg\",\n" +
            "               \"parent\" : \"root\",\n" +
            "               \"pivot\" : [ 1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -0.10, 0.0, -2.0 ],\n" +
            "                     \"size\" : [ 4, 12, 4 ],\n" +
            "                     \"uv\" : [ 0, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftPants\",\n" +
            "               \"parent\" : \"leftLeg\",\n" +
            "               \"pivot\" : [ 1.90, 12.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ 4.0, 11.50, -2.0 ],\n" +
            "                     \"size\" : [ 3, 12, 4 ],\n" +
            "                     \"uv\" : [ 32, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftArm\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 5.0, 21.50, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ 4.0, 11.50, -2.0 ],\n" +
            "                     \"size\" : [ 3, 12, 4 ],\n" +
            "                     \"uv\" : [ 48, 48 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"leftSleeve\",\n" +
            "               \"parent\" : \"leftArm\",\n" +
            "               \"pivot\" : [ 5.0, 21.50, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"leftItem\",\n" +
            "               \"parent\" : \"leftArm\",\n" +
            "               \"pivot\" : [ 6, 14.50, 1 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"origin\" : [ -7.0, 11.50, -2.0 ],\n" +
            "                     \"size\" : [ 3, 12, 4 ],\n" +
            "                     \"uv\" : [ 40, 16 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightArm\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ -5.0, 21.50, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -7.0, 11.50, -2.0 ],\n" +
            "                     \"size\" : [ 3, 12, 4 ],\n" +
            "                     \"uv\" : [ 40, 32 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"rightSleeve\",\n" +
            "               \"parent\" : \"rightArm\",\n" +
            "               \"pivot\" : [ -5.0, 21.50, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"locators\" : {\n" +
            "                  \"lead_hold\" : [ -6, 14.50, 1 ]\n" +
            "               },\n" +
            "               \"name\" : \"rightItem\",\n" +
            "               \"parent\" : \"rightArm\",\n" +
            "               \"pivot\" : [ -6, 14.50, 1 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"cubes\" : [\n" +
            "                  {\n" +
            "                     \"inflate\" : 0.250,\n" +
            "                     \"origin\" : [ -4.0, 12.0, -2.0 ],\n" +
            "                     \"size\" : [ 8, 12, 4 ],\n" +
            "                     \"uv\" : [ 16, 32 ]\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"name\" : \"jacket\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24.0, 0.0 ]\n" +
            "            },\n" +
            "            {\n" +
            "               \"name\" : \"cape\",\n" +
            "               \"parent\" : \"body\",\n" +
            "               \"pivot\" : [ 0.0, 24, -3.0 ]\n" +
            "            }\n" +
            "         ],\n" +
            "         \"description\" : {\n" +
            "            \"identifier\" : \"geometry.humanoid.customSlim\",\n" +
            "            \"texture_height\" : 64,\n" +
            "            \"texture_width\" : 64,\n" +
            "            \"visible_bounds_height\" : 2,\n" +
            "            \"visible_bounds_offset\" : [ 0, 1, 0 ],\n" +
            "            \"visible_bounds_width\" : 1\n" +
            "         }\n" +
            "      }\n" +
            "   ]\n" +
            "}\n";
}
