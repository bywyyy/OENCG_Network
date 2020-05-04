package com.swu.agentlab.zsnp.gui.admin;

import com.swu.agentlab.zsnp.entity.player.admin.LaunchHandler;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingProtocol;
import com.swu.agentlab.zsnp.game.coalition.voting.room.MessageManager;
import com.swu.agentlab.zsnp.gui.Room.RoomSelectedListener;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.gui.player.RoomsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class AdminForm extends BaseAdminForm implements RoomSelectedListener {


    private JPanel jp_main;
    private JPanel jp_operations;
    private JButton btn_createRoom;
    private JButton btn_deleteRoom;
    private JButton btn_deleteRoomData;
    private JScrollPane jsp_rooms;
    private JComboBox cbb_roomStatue;
    private JButton btn_launchClient;

    private RoomsPanel jp_rooms;

    private JFrame frame;

    private RoomHandler roomHandler;

    private LaunchHandler launchHandler;

    private String roomId;

    private Statue selectedStatue;

    private GUIBundle guiBundle;

    private Set<RoomInfo> adminFormRoomInfos;





    public AdminForm(RoomHandler roomHandler, LaunchHandler launchHandler){
        this.guiBundle = GUIBundle.getInstance("admin");
        frame = new JFrame(guiBundle.getString("title"));
        frame.setContentPane(this.jp_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        this.roomHandler = roomHandler;
        this.launchHandler = launchHandler;
        this.roomId = "";
    }

    @Override
    public void init(Set<RoomInfo> roomInfos) {
        adminFormRoomInfos = roomInfos;
        jsp_rooms.setBorder(BorderFactory.createTitledBorder(guiBundle.getString("border_room")));
        jp_rooms = new RoomsPanel(roomInfos, this);
        jp_rooms.setLayout(new GridLayout(0, 2, 5, 5));
        jsp_rooms.setViewportView(jp_rooms);
        jp_operations.setBorder(BorderFactory.createTitledBorder(guiBundle.getString("border_operation")));
        //cbb_roomStatue.addItem("ALL");
        for(Statue statue: Statue.values()){
            cbb_roomStatue.addItem(statue);
        }
        cbb_roomStatue.setSelectedIndex(0);
        this.selectedStatue = Statue.ALL;
        cbb_roomStatue.addActionListener(e -> {
            Statue statue = (Statue) cbb_roomStatue.getSelectedItem();
            if(statue == selectedStatue){
                return;
            }
            this.selectedStatue = statue;
            roomHandler.selectRoomByStatue(this.selectedStatue);
            setSelectedRoomId("");
        });
        frame.setVisible(false);
        btn_createRoom.addActionListener(e -> {
            System.out.println("this is button listener");
            roomHandler.createRoom(guiBundle.formatString("new_room_name_prefix", roomInfos.size()),
                    guiBundle.formatString("new_room_desc_prefix", roomInfos.size()),
                    VotingDomain.class.getName(),
                    //TripleDomain.class.getName(),
                    VotingProtocol.class.getName(),
                    MessageManager.class.getName(),
                    -1,
                    -1,
                    "3Players_weight1-2-2");
        });
        btn_deleteRoom.addActionListener(e -> {
            System.out.println(this.roomId);
            if("".equals(this.roomId)){
                JOptionPane.showMessageDialog(frame, guiBundle.getString("delete_room_msg_box_hint"),
                        guiBundle.getString("delete_room_msg_box_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            RoomInfo roomInfo = this.jp_rooms.getById(this.roomId);
            if(roomInfo.getAmountOfPlayers() != 0){
                int confirm = JOptionPane.showConfirmDialog(frame, guiBundle.getString("force_player_exit_msg_box_hint"),
                        guiBundle.getString("force_player_exit_msg_box_title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(confirm != JOptionPane.YES_OPTION){
                    return;
                }
            }
            roomHandler.deleteRoom(this.roomId);
        });
        btn_deleteRoomData.addActionListener(e -> {
            if("".equals(this.roomId)){
                JOptionPane.showMessageDialog(frame, guiBundle.getString("delete_room_msg_box_hint"),
                        guiBundle.getString("delete_room_msg_box_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(frame, guiBundle.getString("delete_room_data_confirm_msg_box_hint"),
                    guiBundle.getString("delete_room_data_confirm_msg_box_title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(confirm == JOptionPane.YES_OPTION){
                roomHandler.deleteRoomData(this.roomId);
            }else if(confirm == JOptionPane.NO_OPTION){
                return;
            }
        });
        btn_launchClient.addActionListener(e -> {
            launchHandler.launchClient();
        });
        this.initComponentName();
    }

    public void adminCreatRoom(){
        roomHandler.createRoom(guiBundle.formatString("new_room_name_prefix", adminFormRoomInfos.size()),
                guiBundle.formatString("new_room_desc_prefix", adminFormRoomInfos.size()),
                VotingDomain.class.getName(),
                //TripleDomain.class.getName(),
                VotingProtocol.class.getName(),
                MessageManager.class.getName(),
                -1,
                -1,
                "3Players_weight1-2-2");
    }

    public void adminLaunchClient(){
        System.out.println("this is adminform okbutton");
        launchHandler.launchClient();
    }


    public void adminDeleteRoom(String roomId){
        if("".equals(roomId)){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("delete_room_msg_box_hint"),
                    guiBundle.getString("delete_room_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        RoomInfo roomInfo = this.jp_rooms.getById(roomId);
        if(roomInfo.getAmountOfPlayers() != 0){
            int confirm = JOptionPane.showConfirmDialog(frame, guiBundle.getString("force_player_exit_msg_box_hint"),
                    guiBundle.getString("force_player_exit_msg_box_title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(confirm != JOptionPane.YES_OPTION){
                return;
            }
        }
        roomHandler.deleteRoom(roomId);
    }

    public void adminDeleteRoomData(String roomId){
        if("".equals(roomId)){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("delete_room_msg_box_hint"),
                    guiBundle.getString("delete_room_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame, guiBundle.getString("delete_room_data_confirm_msg_box_hint"),
                guiBundle.getString("delete_room_data_confirm_msg_box_title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(confirm == JOptionPane.YES_OPTION){
            roomHandler.deleteRoomData(roomId);
        }else if(confirm == JOptionPane.NO_OPTION){
            return;
        }
    }



    private void initComponentName(){
        this.btn_createRoom.setText(guiBundle.getString("create_room"));
        this.btn_deleteRoom.setText(guiBundle.getString("delete_room"));
        this.btn_deleteRoomData.setText(guiBundle.getString("delete_room_data"));
        this.btn_launchClient.setText(guiBundle.getString("launch_player"));
    }

    @Override
    public void appendRoom(RoomInfo roomInfo) {
        jp_rooms.appendRoom(roomInfo);
    }

    @Override
    public void updateAllRooms(Set<RoomInfo> roomInfos) {
        jp_rooms.setRoomInfos(roomInfos);
    }

    @Override
    public void removeRoom(String roomId) {
        if(roomId.equals(this.roomId)){
            this.roomId = "";
        }
        jp_rooms.removeRoom(roomId);
    }

    @Override
    public void updateRoomInfo(RoomInfo roomInfo) {
        if(roomInfo.getStatue() != this.selectedStatue&&this.selectedStatue != Statue.ALL){
            this.removeRoom(roomInfo.getId());
        }else {
            jp_rooms.update(roomInfo);
        }
    }

    @Override
    public void unselectedAllRoomPanel() {
        /*Component[] components = jp_rooms.getComponents();
        for(Component item: components){
            RoomInfoPanel panel = (RoomInfoPanel) item;
            panel.setUnselected();
        }*/
        roomId = "";
    }

    @Override
    public void setSelectedRoomId(String roomId) {
        this.roomId = roomId;
    }
}
