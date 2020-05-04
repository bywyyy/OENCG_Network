package com.swu.agentlab.zsnp.gui.Room;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoomInfoPanel extends JPanel {

    @Getter
    private String roomId;

    private JLabel lbl_name;

    private JLabel lbl_players;

    private JLabel lbl_domainName;

    private JLabel lbl_protocolDesc;

    private JLabel lbl_roomStatue;

    private boolean selected;

    private GUIBundle guiBundle;

    public RoomInfoPanel(RoomInfo info, RoomSelectedListener selectedListener) {
        this.guiBundle = GUIBundle.getInstance("admin");
        this.roomId = info.getId();
        this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        GridLayout layout = new GridLayout(0, 2);
        layout.setHgap(5);
        layout.setVgap(2);
        this.setLayout(layout);
        this.add(new JLabel(guiBundle.getString("name")));
        lbl_name = new JLabel(info.getName());
        this.add(lbl_name);
        this.add(new JLabel(guiBundle.getString("players")));
        lbl_players = new JLabel(info.getAmountOfPlayers() + "/" + info.getAmountOfRoles());
        this.add(lbl_players);
        this.add(new JLabel(guiBundle.getString("domain")));
        lbl_domainName = new JLabel(info.getDomainName());
        this.add(lbl_domainName);
        this.add(new JLabel(guiBundle.getString("desc")));
        lbl_protocolDesc = new JLabel(info.getDescription());
        this.add(lbl_protocolDesc);
        this.add(new JLabel(guiBundle.getString("status")));
        lbl_roomStatue = new JLabel(info.getStatue().toString());
        this.add(lbl_roomStatue);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedListener.unselectedAllRoomPanel();
                setSelected();
                selectedListener.setSelectedRoomId(roomId);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void setSelected(){
        selected = true;
        this.setBorder(BorderFactory.createMatteBorder(2,1,2,1, Color.black));
    }

    public void setUnselected(){
        selected = false;
        this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }

    public void updateInfo(RoomInfo roomInfo){

        lbl_name.setText(roomInfo.getName());
        lbl_players.setText(roomInfo.getAmountOfPlayers()+"/"+roomInfo.getAmountOfRoles());
        lbl_roomStatue.setText(roomInfo.getStatue().toString());
        lbl_domainName.setText(roomInfo.getDomainName());
        lbl_protocolDesc.setText(roomInfo.getDescription());

    }
}
