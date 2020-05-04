package com.swu.agentlab.zsnp.entity.player;

import com.swu.agentlab.zsnp.entity.communicator.Sendable;
import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.communicator.Receivable;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.gui.player.Launch;
import lombok.Data;

import javax.websocket.Session;

/**
 * @author JJ.Wu
 */
@Data
public abstract class Player implements Receivable, Sendable {

    /**
     * 玩家的id
     * (一般由服务器生成)
     */
    private String id;

    /**
     * 玩家名称
     * （由玩家自己指定）
     */
    private String name;

    /**
     * 玩家的描述
     * （由玩家指定）
     */
    private String description;

    /**
     * 玩家的类型
     */
    private PlayerType type;

    /**
     * agent的类路径
     * （如果玩家为Agent玩家，此字段有效；如果玩家为Human玩家，此字段为null）
     */
    private String path;

    /**
     * 玩家与服务器的连接状态
     * （是否能够正常收发消息）
     */
    private boolean connected;

    /**
     * 启动界面，填写player信息、选择房间
     */
    private Launch launch;

    /**
     * 玩家在游戏中的角色
     * 如preference或者联盟游戏中该玩家的resource
     */
    private Role role;

    /**
     * 游戏域
     */
    private Domain domain;


    /**
     * websession
     */
    private Session session;

    /**
     * 处理玩家离开房间
     */
    protected PlayerExitHandler exitHandler;





    public Player() {
    }

    public Player(String id, String name, String description, boolean connected, Launch launch, PlayerType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.connected = connected;
        this.launch = launch;
        this.type = type;
    }

    /**
     *接收消息
     * @param message
     */
    @Override
    public abstract void receiveMessage(Message message);

    /**
     *发送消息
     * @param message
     */
    @Override
    public abstract void sendMessage(Message message);

    /**
     * 放回玩家的基本信息
     * 用于传输通信
     * @return id, name, role
     */
    public PlayerInfo generatePlayerInfo(){
        return new PlayerInfo(this.getId(), this.getName(), this.getDescription(), this.getRole() == null?null:this.getRole().generateRoleInfo());
    }

    /**
     * 判断是否是同一个玩家
     * 非空-->玩家类型-->玩家id-->玩家名称
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;

        if(this.getType() != player.getType()){
            return false;
        }

        if (id != null ? !id.equals(player.id) : player.id != null) {
            return false;
        }
        return name != null ? name.equals(player.name) : player.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
