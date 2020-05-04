package com.swu.agentlab.zsnp.gui.player;

import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import com.swu.agentlab.zsnp.entity.communicator.pipeline.Pipe;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.LoginRequest;
import com.swu.agentlab.zsnp.entity.message.body.SelectRoomCmd;
import com.swu.agentlab.zsnp.entity.player.Agent;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerType;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import com.swu.agentlab.zsnp.gui.Room.RoomInfoPanel;
import com.swu.agentlab.zsnp.gui.Room.RoomSelectedListener;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.util.ClassUtil;
import lombok.Getter;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Set;

public class LaunchForm extends Launch implements RoomSelectedListener {

    private static Logger log = Logger.getLogger(LaunchForm.class);

    private JPanel jp_main;
    private JTextField tf_name;
    private JTextArea ta_desc;
    private JRadioButton rb_human;
    private JRadioButton rb_agent;
    private JLabel lbl_name;
    private JLabel lbl_desc;
    private JScrollPane jsp_desc;
    private JPanel jp_player;
    private JPanel jp_name;
    private JPanel jp_desc;
    private JPanel jp_playerType;
    private JPanel jp_file;
    private JPanel jp_rooms;
    private JPanel jp_buttons;
    private JButton btn_reset;
    private JButton btn_ok;
    private JTextField tf_fileName;
    private JButton btn_fileDlg;
    private JLabel lbl_tips;
    //private JButton btn_createRoom;
    private JScrollPane jsp_rooms;
    private JPanel jp_tips;
    private JComboBox cbb_roomStatue;
    private RoomsPanel jp_existingRooms;

    private String selectRoomId;

    private Statue selectedRoomStatue;

    private Class agentClass;

    @Getter
    private JFrame frame;

    private Player player;

    private GUIBundle guiBundle;

    public LaunchForm(Player player) {
        this.guiBundle = GUIBundle.getInstance("launcher");
        this.player = player;
        frame = new JFrame(guiBundle.getString("title"));
        frame.setContentPane(this.jp_main);
        if(((RemotePlayer) player).getCommunicator() instanceof MySocket){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }else if(((RemotePlayer) player).getCommunicator() instanceof Pipe){
            Pipe pipe = (Pipe) ((RemotePlayer) player).getCommunicator();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    try {
                        if(pipe.getPos() != null){
                            pipe.getPos().close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    /**
     * 初始化启动窗体，需要房间信息
     */
    @Override
    public void init(Set<RoomInfo> roomInfos) {
        //
        jp_player.setBorder(BorderFactory.createTitledBorder(guiBundle.getString("border_player")));
        //
        jp_rooms.setBorder(BorderFactory.createTitledBorder(guiBundle.getString("border_room")));
        //Agent 目录选择不可见
        jp_file.setVisible(false);
        //添加Agent文件选择框
        btn_fileDlg.addActionListener(e -> {
            addFileDialog();
        });
        //初始化Human、Agent选择按钮
        ButtonGroup group = new ButtonGroup();
        group.add(rb_agent);
        group.add(rb_human);
        rb_human.setSelected(true);
        rb_human.addActionListener(e -> {
            jp_file.setVisible(false);
        });
        rb_agent.addActionListener(e -> {
            jp_file.setVisible(true);
        });
        //Reset按钮初始化
        btn_reset.addActionListener(e->{
            tf_name.setText("");
            ta_desc.setText("");
            rb_human.setSelected(true);
            tf_fileName.setText("");
            jp_file.setVisible(false);
            unselectedAllRoomPanel();
        });
        //OK按钮初始化
        btn_ok.addActionListener(e -> {
            LoginRequest request = generateLoginRequest();
            if(request==null){
                //JOptionPane.showMessageDialog(frame, "请重新检查你输入的信息! ", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Message message = new Message(null, null, null, request, null);
            player.receiveMessage(message);
        });
        //房间状态下拉框
        for(Statue item: Statue.values()){
            cbb_roomStatue.addItem(item);
        }
        cbb_roomStatue.setSelectedIndex(1);
        this.selectedRoomStatue = (Statue) cbb_roomStatue.getSelectedItem();
        cbb_roomStatue.addActionListener(e -> {
            Statue statue = (Statue) cbb_roomStatue.getSelectedItem();
            if(statue != this.selectedRoomStatue){
                Message message = new Message(null, null, null, new SelectRoomCmd(this.player.getId(), statue), null);
                player.receiveMessage(message);
                this.selectedRoomStatue = statue;
            }
        });
        //加载已有的房间
        jp_existingRooms = new RoomsPanel(roomInfos, this);
        //BoxLayout boxLayout = new BoxLayout(jp_existingRooms, BoxLayout.Y_AXIS);
        jp_existingRooms.setLayout(new GridLayout(0, 1, 5, 5));
        jsp_rooms.setViewportView(jp_existingRooms);
        /*for(RoomInfo info: roomInfos){
            RoomInfoPanel infoPanel = new RoomInfoPanel(info, this);
            jp_existingRooms.add(infoPanel);
        }*/
        this.initComponentName();
        //显示启动窗口
//        this.show();
        player.receiveMessage(new Message(null, null, null, new SelectRoomCmd(this.player.getId(), this.selectedRoomStatue), null));
    }

    private void initComponentName(){
        this.lbl_name.setText(guiBundle.getString("name"));
        this.lbl_desc.setText(guiBundle.getString("desc"));
        this.lbl_tips.setText(guiBundle.getString("room_status"));
        this.rb_agent.setText(guiBundle.getString("agent"));
        this.rb_human.setText(guiBundle.getString("human"));
        this.btn_ok.setText(guiBundle.getString("ok"));
        this.btn_reset.setText(guiBundle.getString("reset"));
        this.btn_fileDlg.setText(guiBundle.getString("open_file"));
    }

    @Override
    public void unselectedAllRoomPanel(){
        selectRoomId = "";
    }

    @Override
    public void setSelectedRoomId(String roomId) {
        this.selectRoomId = roomId;
    }
/**
* 这个函数是向web后台提供上传agent功能的button的函数
* */
    public void ButtonOk(String playerName,String desc,String roomId,String playerType){
        LoginRequest request = generateLoginRequest(playerName,desc,roomId,playerType);
        if(request==null){
            //JOptionPane.showMessageDialog(frame, "请重新检查你输入的信息! ", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Message message = new Message(null, null, null, request, null);
        player.receiveMessage(message);
    }



/**
* 这部分是重写，使agent名称和描述作为函数的输入，用来供web服务器调用
* */

    private  LoginRequest generateLoginRequest(String playerName,String desc,String roomId,String playerType){
        String path = tf_fileName.getText();
        if(rb_agent.isSelected()&&agentClass==null){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("missing_agent_msg_box_hint"),
                    guiBundle.getString("missing_agent_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return null;
        }

        LoginRequest request;

        if(playerType.equals("remote_human"))
        {
            request = new LoginRequest(
                    "",
                    playerName,
                    desc,
                    PlayerType.REMOTE_HUMAN,
                    path,
                    roomId);
        }
        else{
            request = new LoginRequest(
                    "",
                    playerName,
                    desc,
                    PlayerType.REMOTE_AGENT,
                    path,
                    roomId);

        }
        request.setAgentClass(this.agentClass);
        return request;
    }


    /**
     * 生成创建agent的请求
     * @return
     */
    private LoginRequest generateLoginRequest(){
        /**
         *
         */
        tf_name.setText(tf_name.getText().trim());
        ta_desc.setText(ta_desc.getText().trim());
        String playerName = tf_name.getText();
        String desc = ta_desc.getText();
        if(playerName==null||"".equals(playerName.trim())||"".equals(desc.trim())||selectRoomId==null||"".equals(selectRoomId.trim())){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("fault_info_msg_box_hint"),
                    guiBundle.getString("fault_info_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if(!validateStringLen(playerName, 8)||!validateStringLen(desc, 80)){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_length_msg_box_hint"),
                    guiBundle.getString("invalid_length_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String path = tf_fileName.getText();
        if(rb_agent.isSelected()&&agentClass==null){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("missing_agent_msg_box_hint"),
                    guiBundle.getString("missing_agent_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        System.out.println("----------path---------");
        System.out.println(path);
        LoginRequest request = new LoginRequest(
                "",
                tf_name.getText(),
                ta_desc.getText(),
                rb_human.isSelected()? PlayerType.REMOTE_HUMAN:PlayerType.REMOTE_AGENT,
                path,
                selectRoomId);
        System.out.println("-------------agentClass--------------");
        System.out.println(this.agentClass);

        request.setAgentClass(this.agentClass);
        return request;
    }

    private boolean validateStringLen(String str, int len){
        if(str == null || "".equals(str)){
            return false;
        }
        if(str.length()>len){
            return false;
        }
        return true;
    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

    @Override
    public void hide() {
        frame.setVisible(false);
        //frame.dispose();
    }

    @Override
    public void close() {
        frame = null;
    }

    @Override
    public void update(RoomInfo roomInfo) {
        if(roomInfo.getStatue() != this.selectedRoomStatue&&this.selectedRoomStatue != Statue.ALL){
            this.removeRoom(roomInfo.getId());
        }else{
            jp_existingRooms.update(roomInfo);
        }

    }

    @Override
    public void roomCreate(RoomInfo roomInfo) {
        jp_existingRooms.appendRoom(roomInfo);
    }

    @Override
    public void update(Set<RoomInfo> roomInfos) {
        jp_existingRooms.setRoomInfos(roomInfos);
    }

    @Override
    public void removeRoom(String roomId) {
        if(roomId.equals(this.selectRoomId)){
            selectRoomId = "";
        }
        jp_existingRooms.removeRoom(roomId);
    }

    public static void main(String[] args) {
       LaunchForm form = new LaunchForm(null);
    }

    private void addFileDialog(){
        JFileChooser chooser = new JFileChooser();
        File dir = new File("./");
        //chooser.setCurrentDirectory(new File("./zsnp-core/src/main/java/com/swu/agentlab/zsnp/entity/player/agent"));
        chooser.setCurrentDirectory(dir);
        chooser.setAcceptAllFileFilterUsed(false);
        String[] extensions = {"java", "class"};
        chooser.setFileFilter(new FileNameExtensionFilter("*.java; *.class", extensions));

        int val = chooser.showOpenDialog(frame);
        if(val == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            String fileName = file.getName();
            if(fileName.endsWith(".java")){
                this.findFullClassNameFromJavaFile(file);
            }else if(fileName.endsWith(".class")){
                this.findClassFormClassFile(file);
            }
        }
    }

    /**
     * 从Java源码文件(*.java文件)中获取该类的全类名，
     * 并将全类名设置到TextField中
     * @param file
     */
    private void findFullClassNameFromJavaFile(File file){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = "";
            while("".equals(line)||!line.contains("package")){
                String temp = br.readLine();
                if(temp == null){
                    line = "";
                    break;
                }else{
                    line = temp;
                }
            }
            if(br!=null){
                br.close();
            }
            String fullPackage = "";
            if(!"".equals(line)){
                String str1 = line.substring(line.indexOf("package"), line.indexOf(";"));
                String str2 = str1.replaceFirst("package", "");
                fullPackage = str2.trim()+".";
            }

            String fileName = file.getName();
            String name = fileName.replaceAll(".java","");
            String fullClass = fullPackage+name;
            if(this.isPathValid(fullClass)){
                tf_fileName.setText(fullClass);
                System.out.println(fullClass);
                agentClass = Class.forName(fullClass);
            }else{
            }
        } catch (IOException|ClassNotFoundException e) {
            //e.printStackTrace();
            tf_fileName.setText("");
            agentClass = null;
            JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_agent_type_msg_box_hint"));
        }
    }

    /**
     * 从类文件(*.class文件)中获取类对象
     * 并将类名设置到TextField中
     * @param file 类文件(*.class文件)
     * @return 类对象
     */
    private void findClassFormClassFile(File file){
        try {
            Class c = ClassUtil.loadClassFormFile(file);
            if(isClassValid(c)){
                agentClass = c;
                String fullClassName = c.getName();
                tf_fileName.setText(fullClassName);
            }else{
                JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_agent_type_msg_box_hint"),
                        guiBundle.getString("invalid_agent_type_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tf_fileName.setText("");
            agentClass = null;
            JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_agent_type_msg_box_hint"),
                    guiBundle.getString("invalid_agent_type_msg_box_title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isPathValid(String str){
        if(str==null||"".equals(str.trim())){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("missing_agent_msg_box_hint"),
                    guiBundle.getString("missing_agent_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }else{
            String fullClass = str.trim();
            try {
                if(!(Class.forName(fullClass).newInstance() instanceof Agent)){
                    JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_agent_type_msg_box_hint"),
                            guiBundle.getString("invalid_agent_type_msg_box_title"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }else{
                    return true;
                }
            } catch (ClassNotFoundException|IllegalAccessException|InstantiationException e) {
                //e.printStackTrace();
                JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_agent_type_msg_box_hint"),
                        guiBundle.getString("invalid_agent_type_msg_box_title"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    /**
     * 验证类是否为VotingAgent 的子类
     * @param agentClass 被验证的类
     * @return 是否为VotingAgent 的子类
     */
    private boolean isClassValid(Class agentClass){
        if(agentClass == null){
            return false;
        }
        Object obj = null;
        try {
            obj = agentClass.newInstance();
            if(obj instanceof VotingAgent){
                return true;
            }else{
                return false;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
