# NavMark

### 使用

命令：
```text
/navmark <Player: 玩家名> <String: 标志物名>
```

实例：
```text
/navmark "Mcayear" 导师
```
可以 显示/隐藏 玩家的标志物

效果如图：

![截图1](./image/screenshot1.png)

### 配置

```yml
radius: 3 # 标志物离玩家的距离
enablePitch: true # 启用垂直跟随，若开启则玩家垂直视角移动也会跟着移动
target:
  装备精工师: # 目标点名称
    level: rpgx
    x: 22
    y: 32
    z: 61
  导师:
    level: rpgx
    x: -40
    y: 33
    z: -56
```