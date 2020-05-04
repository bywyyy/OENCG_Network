package com.swu.agentlab.zsnp.entity.room;

import java.util.HashSet;
import java.util.Set;

/**
 * Room 集合，多个房间
 * @author JJ.Wu
 */
public class RoomSet extends HashSet<Room> {

    /**
     * 根据id获取Room
     * @param id
     * @return
     */
    public Room get(String id){
        if(id==null||"".equals(id)){
            return null;
        }
        Room room = null;
        for(Room item: this){
            if(id.equals(item.getId())){
                room = item;
                break;
            }
        }
        return room;
    }

    /**
     * 根据房间的状态，获取在该状态下所有房间的集合
     * @param statue 房间的状态
     * @return 该状态下的所有房间的集合
     */
    public RoomSet getRoomsWithStatue(Statue statue){
        if(statue == Statue.ALL){
            return this;
        }
        RoomSet set = new RoomSet();
        for(Room item: this){
            if(item.getStatue() == statue){
                set.add(item);
            }
        }
        return set;
    }

    /**
     * 根据房间的状态，在HashSet中搜索房间，并返回搜索到的在HashSet中第一个该状态的房间
     * @param statue 房间的状态
     * @return 该状态下的一个房间
     */
    public Room getRoomWithStatue(Statue statue){
        Room room = null;
        for(Room item: this){
            if(item.getStatue() == statue){
                room = item;
                break;
            }
        }
        return room;
    }

    /**
     * 根据房间id，删除房间
     * @param id
     */
    public void removeById(String id){
        if(id == null||"".equals(id)){
            return;
        }
        Room room = get(id);
        remove(room);
    }

    /**
     * 根据房间的状态，删除该状态下所有的房间
     * @param statue
     */
    public void removeByStatue(Statue statue){
        RoomSet set = getRoomsWithStatue(statue);
        remove(set);
    }

    /**
     * 生成RoomInfo集合
     * @return Set<RoomInfo>
     */
    public Set<RoomInfo> generateRoomInfos(){
        Set<RoomInfo> roomInfos = new HashSet<>();
        for(Room item: this){
            roomInfos.add(item.generateInfo());
        }
        return roomInfos;
    }

    /**
     * 根据房间名查找房间（模糊查询）
     * @param name 房间名
     * @return 房间集合RoomSet
     */
    public RoomSet getRoomsWithName(String name){
        RoomSet rooms = new RoomSet();
        for(Room item: this){
            if(item.getName() == null||"".equals(item.getName())){
                continue;
            }
            if(item.getName().contains(name)){
                rooms.add(item);
            }
        }
        return rooms;
    }
}
