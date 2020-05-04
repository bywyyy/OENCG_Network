package com.swu.agentlab.zsnp.entity.message.body;

/**
 * @author JJ.Wu
 */
public enum BodyType {


    /**
     *玩家启动，准备连接服务器
     */
    PLAYER_LAUNCH,
    /**
     *服务器对玩家启动，做出的响应
     */
    LAUNCH_RESPONSE,
    /**
     *玩家的登陆请求
     */
    LOGIN_REQUEST,

    /**
     * 玩家传送语言交流信息的请求
     */
    COMMUNICATE_BODY,

    /**
     * 服务器对登陆请求做出的响应
     */
    LOGIN_RESPONSE,

    /**
     *玩家进入房间
     */
    PLAYER_LOGIN,

    /**
     * 玩家退出房间
     */
    PLAYER_LOGOUT,

    /**
     * 服务器消息体
     */
    SERVER_BODY,

    /**
     * 游戏开始
     */
    GAME_BEGIN,

    /**
     * 回合消息体
     */
    COUNTER_BODY,

    /**
     * 游戏结束
     */
    GAME_END,

    /**
     *房间信息发生变化，如房间人数、房间游戏状态变化
     */
    ROOMINFO_CHANGE,

    /**
     * 服务器管理程序启动
     */
    ADMIN_LAUNCH,

    /**
     * (请求创建新的房间)
     * 创建房间的消息
     */
    ROOM_CREATE,

    /**
     * (新的房间已被创建，包含该房间的信息(RoomInfo)的消息)
     * 新房间的消息
     */
    NEW_ROOM,

    /**
     * 删除房间的命令
     */
    DELETE_ROOM_CMD,

    /**
     * 删除房间所有数据的命令
     */
    DELETE_ROOM_DATA_CMD,

    /**
     * 根据房间信息选择房间的消息
     */
    SELECT_ROOM_CMD,

    /**
     * 包含多个房间信息(Set<RoomInfo>  roomInfos)的消息体
     */
    ROOMS_BODY,

    /**
     * 玩家与服务器失去连接
     */
    PLAYER_DISCONNECT,
}
