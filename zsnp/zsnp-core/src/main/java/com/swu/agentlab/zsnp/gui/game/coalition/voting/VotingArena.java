package com.swu.agentlab.zsnp.gui.game.coalition.voting;

import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import com.swu.agentlab.zsnp.entity.communicator.Sendable;
import com.swu.agentlab.zsnp.entity.communicator.pipeline.Pipe;
import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.message.result.Result;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.SessionBegin;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.cell.*;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.chart.ProposalPie;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.chart.UtilityBar;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.message.MessagePanel;
import com.swu.agentlab.zsnp.gui.player.Arena;
import com.swu.agentlab.zsnp.util.TimeUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class VotingArena extends Arena implements SelectedListener, CheckBoxCellHandler, SliderFieldCellHandler {

    private static final Logger log = Logger.getLogger(VotingDomain.class);

    private JPanel jp_main;
    private JPanel jp_header;
    private JPanel jp_message;
    private JPanel jp_handle;
    private JPanel jp_counterMsg;
    private JPanel jp_serverMsg;
    private JScrollPane jsp_counterMsg;
    private JScrollPane jsp_serverMsg;
    private JPanel jp_domain;
    private JPanel jp_coalitions;
    private JScrollPane jsp_coalitions;
    private JPanel jp_comments;
    private JPanel jp_buttons;
    private JPanel jp_offer;
    private JPanel jp_selection;
    private JPanel jp_reward;
    private JTextField tf_individual;
    private JTextField tf_coalitionReward;
    private JLabel lbl_coalitionReward;
    private JLabel lbl_individual;
    private JButton btn_reject;
    private JButton btn_accept;
    private JTable jt_coalitions;
    private JScrollPane jsp_selection;
    private JPanel jp_coalitionReward;
    private JPanel jp_individualReward;
    private JLabel lbl_playerName;
    private JLabel lbl_role;
    private JLabel lbl_roleName;
    private JPanel jp_player;
    private JPanel jp_room;
    private JPanel jp_playerAmount;
    private JLabel lbl_room;
    private JLabel lbl_roomName;
    private JLabel lbl_domain;
    private JLabel lbl_talent;
    private JLabel lbl_roomStatue;
    private JLabel lbl_requiredPlayers;
    private JTable jt_selection;
    private JPanel jp_jsp_counterMsg;
    private JScrollPane jsp_comments;
    private JPanel jp_jsp_comments;
    private JLabel lbl_statue;
    private JLabel lbl_players;
    private JLabel lbl_sessionNum;
    private JLabel lbl_session;
    private JLabel lbl_roundNum;
    private JLabel lbl_round;
    private JPanel jp_sessionRound;
    private JLabel lbl_discount;
    private JLabel lbl_discountName;
    private JTextArea ta_serverMsg;

    private VotingDomain votingDomain;

    private Coalition selectedCoalition;

    private Sendable sender;

    private PlayerInfo playerInfo;

    private double talent;

    private Set<PlayerInfo> playerInfos;

    private Map<PlayerInfo, Integer> proposerz;

    private Map<PlayerInfo, Toallyz> alliesz;

    private int rewards;

    private String roomId;

    private Set<Integer> selectedRowIndexs;

    private ProposalPie proposalPie;

    private UtilityBar utilityBar;

    private GUIBundle guiBundle;

    private int sessionRound;

    public VotingArena(Sendable sender, FrameCloseHandler closeHandler){
        this.guiBundle = GUIBundle.getInstance("arena");
        frame = new JFrame(guiBundle.getString("title"));
        frame.setContentPane(this.jp_main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeHandler.onClose(frame);
            }
        });
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        BoxLayout boxLayout = new BoxLayout(jp_jsp_counterMsg, BoxLayout.Y_AXIS);
        jp_jsp_counterMsg.setLayout(boxLayout);
        jp_jsp_counterMsg.setBackground(Color.white);
        //jp_jsp_comments.setLayout(new GridLayout(1,2,3,3));
        jp_jsp_comments.setLayout(new FlowLayout());
        this.sender = sender;
        this.selectedCoalition = new Coalition();
        this.selectedRowIndexs = new HashSet<>();
        this.proposerz = new HashMap<>();
        this.alliesz = new HashMap<>();
    }

    @Override
    public void init(PlayerInfo playerInfo, RoomInfo roomInfo, Domain domain) {
        System.out.println(playerInfo);
        System.out.println(roomInfo);
        System.out.println(domain);
        this.votingDomain = (VotingDomain) domain;
        this.playerInfo = playerInfo;
        this.talent = ((PartyInfo)playerInfo.getRoleInfo()).getTalent();
        ((PartyInfo)playerInfo.getRoleInfo()).setTalent(0.0);
        this.playerInfos = roomInfo.getPlayerInfos();
        this.roomId = roomInfo.getId();
        PartyInfo partyInfo = (PartyInfo) playerInfo.getRoleInfo();
        selectedCoalition.getPartyNums().add(partyInfo.getPartyNum());
        selectedCoalition.setResources(partyInfo.getResource());
        selectedRowIndexs.add(0);
        this.proposerz.put(playerInfo, 0);

        jp_coalitions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), guiBundle.getString("border_coalition_reward")));
        jp_offer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), guiBundle.getString("border_coalition_selection")));
        jp_comments.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), guiBundle.getString("border_vision")));
        jp_counterMsg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), guiBundle.getString("border_counter_message")));
        jp_serverMsg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), guiBundle.getString("border_server_message")));
        jsp_coalitions.getViewport().setBackground(Color.white);
        jt_coalitions.setBackground(Color.white);
        //jt_coalitions.getTableHeader().setBackground(Color.white);
        Vector vNames = new Vector();
        Vector vData = new Vector();
        String[] colNames = {
                guiBundle.getString("column1_name_coalition_reward"),
                guiBundle.getString("column2_name_coalition_reward"),
                guiBundle.getString("column3_name_coalition_reward")
        };
        for(int i = 0; i<3; i++){
            vNames.add(colNames[i]);
        }
        //Party party = (Party) playerInfo.getRole();
        Set<Coalition> coalitions = votingDomain.getCoalitions();
        /**
         * 这部分是联盟结构及收益的展示逻辑，移植到前端
         */
        for(Coalition item: coalitions){
            //
            StringBuilder namesBuilder = new StringBuilder();
            for(Integer num: item.getPartyNums()){
                if(!namesBuilder.toString().equals("")){
                    namesBuilder.append(", ");
                }
                namesBuilder.append(votingDomain.getPartyByNum(num).getRoleName());
            }
            Vector vRow = new Vector();
            vRow.add(namesBuilder.toString());
            vRow.add(item.getResources());
            vRow.add(item.getRewards());
            vData.add(vRow);
            //
            /*if(item.getPartyNums().contains(party.getPartyNum())){
                jp_jsp_selection.add(new CoalitionPanel(item, buttonGroup, this));
            }*/
        }
        DefaultTableModel coalitionModel = new DefaultTableModel(vData, vNames);
        jt_coalitions.setModel(coalitionModel);
        jt_coalitions.setEnabled(false);
        Vector vHeaders = new Vector();
        Vector vConts = new Vector();
        //String[] headers = {"Corporation", "Player", "Weight", "Ally", "Allocation", "Addition"};
        String[] headers = {
                guiBundle.getString("column1_name_coalition_selection"),
                guiBundle.getString("column2_name_coalition_selection"),
                guiBundle.getString("column3_name_coalition_selection"),
                guiBundle.getString("column4_name_coalition_selection"),
                guiBundle.getString("column5_name_coalition_selection"),
                guiBundle.getString("column6_name_coalition_selection"),
                guiBundle.getString("column7_name_coalition_selection"),
                guiBundle.getString("column8_name_coalition_selection")
        };
        for(int i = 0; i<headers.length; i++){
            vHeaders.add(headers[i]);
        }
        Vector vRow0 = new Vector();
        vRow0.add(playerInfo.getRoleInfo().getRoleName());
        vRow0.add(playerInfo.getName());
        vRow0.add(((PartyInfo)playerInfo.getRoleInfo()).getResource());
        CheckBoxCell.Check check0 = new CheckBoxCell.Check(false, true);
        vRow0.add(check0);
        vRow0.add(new Rewards(0, 0));
        vRow0.add(this.talent);
        vRow0.add("-");
        vRow0.add("-");
        vConts.add(vRow0);
        for(PartyInfo item: votingDomain.getParties()){
            Vector vRow = new Vector();
            if(item.getPartyNum()!=((PartyInfo) playerInfo.getRoleInfo()).getPartyNum()){
                int partyNum = item.getPartyNum();
                String name = "······";
                CheckBoxCell.Check check = new CheckBoxCell.Check(false, false);
                for(PlayerInfo info: roomInfo.getPlayerInfos()){
                    if(partyNum == ((PartyInfo)info.getRoleInfo()).getPartyNum()){
                        name = info.getName();
                        check.enable = true;
                        break;
                    }
                }
                //roomInfo.getPlayerInfos();
                vRow.add(item.getRoleName());
                vRow.add(name);
                vRow.add(item.getResource());
                vRow.add(check);
                vRow.add(new Rewards(0, 0));
                if(item.getTalent() == 0.0){
                    vRow.add("?");
                }else{
                    vRow.add(item.getTalent());
                }
                vRow.add("-");
                vRow.add("-");
                vConts.add(vRow);
            }else{
                /*vRow.add(playerInfo.getRoleInfo().getRoleName());
                vRow.add(playerInfo.getName());
                vRow.add(((PartyInfo) playerInfo.getRoleInfo()).getResource());
                vRow.add(new CheckBoxCell.Check(false, true));
                vRow.add(new Rewards(0, 0));
                vRow.add("-");
                vConts.add(vRow);*/
            }
        }
        DefaultTableModel partyModel = new DefaultTableModel(vConts, vHeaders);
        jt_selection.setModel(partyModel);
        jt_selection.getTableHeader().setReorderingAllowed(false);
        jt_selection.setRowHeight(28);
        int[] width = {50, 45, 30, 30, 180, 30, 40, 65};
        TableCellEditor[] cellEditors = {
                new DefaultCellEditor(new JTextField()),
                new DefaultCellEditor(new JTextField()),
                new DefaultCellEditor(new JTextField()),
                new CheckBoxCell(this),
                new SliderFieldCell(this),
                new DefaultCellEditor(new JTextField()),
                //new ButtonCell()
                new DefaultCellEditor(new JTextField()),
                new DefaultCellEditor(new JTextField())
        };
        TableCellRenderer[] cellRenderers = {new DefaultTableCellRenderer(),
                new DefaultTableCellRenderer(),
                new DefaultTableCellRenderer(),
                new CheckBoxCell(this),
                new SliderFieldCell(this),
                new DefaultTableCellRenderer(),
                //new ButtonCell()
                new DefaultTableCellRenderer(),
                new DefaultTableCellRenderer()
        };
        for(int i = 0; i<jt_selection.getColumnCount(); i++){
            TableColumn column = jt_selection.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
            column.setCellEditor(cellEditors[i]);
            column.setCellRenderer(cellRenderers[i]);
        }
        //jt_selection.setValueAt(new CheckBoxCell.Check(false, true), 0, 3);
        /*TableColumn checkboxColumn = jt_selection.getColumnModel().getColumn(3);
        //checkboxColumn.setCellEditor(new DefaultCellEditor(new CheckBoxCell()));
        checkboxColumn.setCellEditor(new CheckBoxCell(this));
        checkboxColumn.setCellRenderer(new CheckBoxCell(this));
        checkboxColumn.setWidth(50);

        TableColumn sliderFieldColumn = jt_selection.getColumnModel().getColumn(4);
        sliderFieldColumn.setCellEditor(new SliderFieldCell());
        sliderFieldColumn.setCellRenderer(new SliderFieldCell());*/
        //jp_jsp_selection.setBackground(Color.white);
        setProposer(false);
        ta_serverMsg = new JTextArea();
        ta_serverMsg.setLineWrap(true);
        ta_serverMsg.setWrapStyleWord(true);
        ta_serverMsg.setEditable(false);
        jsp_serverMsg.setViewportView(ta_serverMsg);
        jsp_counterMsg.setViewportView(jp_jsp_counterMsg);
        lbl_playerName.setText(playerInfo.getName());
        lbl_roleName.setText(playerInfo.getRoleInfo().getRoleName());
        //lbl_domainName.setText(domain.getName());
        lbl_talent.setText(this.talent+"");
        lbl_roomStatue.setText(roomInfo.getStatue()+"");
        lbl_requiredPlayers.setText(""+roomInfo.getAmountOfRoles());
        lbl_roomName.setText(roomInfo.getName());
        //添加统计图
        this.proposalPie = new ProposalPie(this.votingDomain);
        jp_jsp_comments.add(this.proposalPie.getPieChartPanel());
        this.utilityBar = new UtilityBar(this.votingDomain);
        jp_jsp_comments.add(this.utilityBar.getBarChartPanel());
        this.initComponentName();
        lbl_discount.setText(String.valueOf(votingDomain.getDiscountFactor()));
        lbl_session.setText(String.valueOf(votingDomain.getMaxSession()));
        lbl_round.setText(String.valueOf(votingDomain.getMaxRound()));
//        this.show();
    }

    private void initComponentName(){
        lbl_role.setText(guiBundle.getString("role"));
        lbl_room.setText(guiBundle.getString("room"));
        lbl_domain.setText(guiBundle.getString("cost"));
        lbl_discountName.setText(guiBundle.getString("discount"));
        lbl_sessionNum.setText(guiBundle.getString("session"));
        lbl_roundNum.setText(guiBundle.getString("round"));
        lbl_statue.setText(guiBundle.getString("status"));
        lbl_players.setText(guiBundle.getString("required_players"));
        lbl_coalitionReward.setText(guiBundle.getString("coalition_total_resource"));
        lbl_individual.setText(guiBundle.getString("individual_utility"));
    }

    /**
     * 更新当前房间的状态
     * 当有玩家进入或者退出房间是调用
     * @param statue
     */
    public void updateRoomStatue(String statue){
        lbl_roomStatue.setText(statue);
    }

    @Override
    public void selected(Coalition coalition) {
        this.selectedCoalition = coalition;
        tf_coalitionReward.setText(String.valueOf(coalition.getRewards()));
    }

    /**
     * 设置一个Party是否是联盟结构的提出人
     * 如果是，则可以操作选择联盟结构框
     * @param isProposer 是不是联盟结构提出人
     */
    public synchronized void setProposer(boolean isProposer){
        if(agent == null){
            //human玩家
            enableSelection(isProposer);
            if(btn_reject.getActionListeners().length == 0){
                btn_reject.setEnabled(false);
            }
            this.enableComment(false);
            if(isProposer){
                frame.toFront();
                btn_accept.setText(guiBundle.getString("button_accept_send"));
                btn_accept.addActionListener(proposalActionListener);
                btn_accept.setEnabled(true);
            }else{
                btn_accept.removeActionListener(proposalActionListener);
                if(btn_accept.getActionListeners().length == 0){
                    btn_accept.setEnabled(false);
                }
            }
        }else{
            //agent玩家
            if(isProposer){
                ((VotingAgent)agent).setRole(VotingAgent.type.PROPOSER);
                //agent.notifySender();
                agent.sendOffer();
            }
        }

    }

    /**
     * 设置玩家是联盟中的party
     * 如果是，则可以选择是否接受，并发送对这个联盟的评论
     * @param isAlly
     */
    public synchronized void setAlly(boolean isAlly){
        if(agent == null){
            //人类玩家
            enableSelection(false);
            if(isAlly){
                frame.toFront();
                enableComment(true);
                btn_accept.setEnabled(true);
                btn_accept.setText(guiBundle.getString("button_accept_agree"));
                btn_accept.addActionListener(acceptActionListener);
                btn_reject.setEnabled(true);
                btn_reject.setText(guiBundle.getString("button_reject_send"));
                btn_reject.addActionListener(rejectActionListener);
            }else{
                btn_accept.removeActionListener(acceptActionListener);
                if(btn_accept.getActionListeners().length == 0){
                    btn_accept.setEnabled(false);
                }
                btn_reject.removeActionListener(rejectActionListener);
                if(btn_reject.getActionListeners().length == 0){
                    btn_reject.setEnabled(false);
                }
                enableComment(false);
            }
        }else{
            //agent玩家
            if(isAlly){
                ((VotingAgent)agent).setRole(VotingAgent.type.RESPONSER);
                //agent.notifySender();
                agent.sendOffer();
            }
        }
    }

    ActionListener proposalActionListener = e -> {
        Proposal proposal = generateProposal();
        if(proposal != null){
            sender.sendMessage(new Message(null, null, null, new CounterBody(this.roomId, null, proposal), null));
            setProposer(false);
        }
    };

    ActionListener acceptActionListener = e -> {
        setAlly(false);
        sender.sendMessage(new Message(null, null,null, new CounterBody(this.roomId, null, new Response(this.playerInfo, true, null)), null));
    };

    ActionListener rejectActionListener = e -> {
        setAlly(false);
        sender.sendMessage(new Message(null, null, null, new CounterBody(this.roomId, null, new Response(this.playerInfo, false, null)), null));
    };

    /*private void enableSelectCoalition(boolean enable){
        Component[] components = jp_jsp_selection.getComponents();
        for(Component item: components){
            CoalitionPanel panel = (CoalitionPanel) item;
            panel.setEnable(enable);
        }
    }*/

    private void enableSelection(boolean enable){
        jt_selection.setEnabled(enable);
        /*for(int i = 0; i<jt_selection.getRowCount(); i++){
            CheckBoxCell cellEditor = (CheckBoxCell) jt_selection.getCellEditor(i, 3);
            ((JCheckBox)cellEditor.getComponent()).setEnabled(enable);
        }*/
    }

    /**
     * 开启/关闭评论（发送表情）功能
     * @param enable
     */
    private void enableComment(boolean enable){

    }

    @Override
    public void printServerMessage(String time, String cont){
        ta_serverMsg.append(time+"\n");
        ta_serverMsg.append(cont+"\n\n");
        Point p = new Point();
        p.setLocation(0, ta_serverMsg.getHeight());
        jsp_serverMsg.getViewport().setViewPosition(p);
        /*JScrollBar bar = jsp_serverMsg.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());*/
    }

    private String offerTime="";

    /**
     * 打印offer，到页面
     * @param offer
     * @param time
     */
    @Override
    public synchronized void printOffer(Offer offer, String time) {

        /**
         *
         */
        JLabel lbl_time = new JLabel();
        lbl_time.setForeground(Color.gray);
        lbl_time.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        /**
         * 如果offerTime没有赋值，那么将当前时间赋值给offertimen并显示在client上
         */
        if("".equals(offerTime)){
            //加入第一轮的水平线
            offerTime = time;
            lbl_time.setText(offerTime);
            jp_jsp_counterMsg.add(lbl_time);
        }else{
            /**
             * 如果已经赋值，并且时间超过1，那么将新的时间附给offerTime 并显示
             */
            if(TimeUtil.getMinutesDuration(offerTime, time)>=1){
                offerTime = time;
                lbl_time.setText(offerTime);
                jp_jsp_counterMsg.add(lbl_time);
            }
        }

        /**
         * 提取出offer，并进行显示
         */
        MessagePanel panel = new MessagePanel(offer);
        jp_jsp_counterMsg.add(panel);
        jp_jsp_counterMsg.updateUI();
        Point p = new Point();
        p.setLocation(0, jp_jsp_counterMsg.getHeight());
        jsp_counterMsg.getViewport().setViewPosition(p);
        /*JScrollBar bar = jsp_counterMsg.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());*/
        /**
         * 如果消息类型是开始
         */
        if(offer instanceof SessionBegin){
            SessionBegin begin = (SessionBegin) offer;
            String sessionText = begin.getSessionNum() + " / " + votingDomain.getMaxSession();
            lbl_session.setText(sessionText);
            this.sessionRound = 0;
            String roundText = 0 + " / " + votingDomain.getMaxRound();
            lbl_round.setText(roundText);
        }
        /**
         * 如果消息类型是方案
         */
        if(offer instanceof Proposal){
            Proposal proposal = (Proposal) offer;
            String roundText = proposal.getSessionRoundNum() + " / " + votingDomain.getMaxRound();
            lbl_round.setText(roundText);
            this.sessionRound = proposal.getSessionRoundNum();
        }
        /**
         * 如果不存在agent
         */
        if(agent == null){
            /**
             * 画图
             */
            ProposalPie.updateByOffer(proposalPie, offer);
            /**
             * 画图
             */
            UtilityBar.updateByOffer(utilityBar, offer);
        }
    }

    @Override
    public void printGameResult(Result gameResult) {

    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

    @Override
    public void hide() {
        frame.setVisible(false);
    }

    @Override
    public void close() {

    }

    @Override
    public void update(PlayerInfo playerInfo) {
        if(playerInfo !=null){
            playerInfos.add(playerInfo);
        }
        String roleName = playerInfo.getRoleInfo().getRoleName();
        for(int i = 0; i<jt_selection.getRowCount(); i++){
            String partyName = (String)jt_selection.getValueAt(i,0);
            if(partyName.equals(roleName)){
                jt_selection.setValueAt(playerInfo.getName(), i, 1);
                jt_selection.setValueAt(new CheckBoxCell.Check(true, false), i, 3);
                break;
            }
        }
    }

    @Override
    public void update(Statue roomStatue) {
        lbl_roomStatue.setText(roomStatue+"");
    }

    /**
     * 根据partyName 寻找playerinfo
     * @param partyName
     * @return
     */
    private PlayerInfo getByPartyName(String partyName){
        PlayerInfo info = null;
        for(PlayerInfo item: playerInfos){
            if(partyName.equals(item.getRoleInfo().getRoleName())){
                info = item;
                break;
            }
        }
        return info;
    }

    /**
     * 根据playerName寻找playerInfo
     * @param playerName
     * @return
     */
    private PlayerInfo getByPlayerName(String playerName){
        PlayerInfo info = null;
        for(PlayerInfo item: playerInfos){
            if(playerName.equals(item.getName())){
                info = item;
                break;
            }
        }
        return info;
    }


    /**
     * 提供提出议案的方法，组装message信息
     * @return
     */
    private Proposal generateProposal(){
        /*
       如果总的rewards 小于等于 0 ，那么说明没有选择合适的联盟结构
         */
        if(this.rewards<=0){
            JOptionPane.showMessageDialog(frame, guiBundle.getString("fault_proposal_msg_box_hint"),
                    guiBundle.getString("fault_proposal_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        /*
        创建一个新的proposal
         */
        Proposal proposal = new Proposal();
        /*
       将当前的提案的分配结构写入
        */
        proposal.setProposerz(proposerz);
        /*
        得到当前player的reward
         */
        System.out.println("????????????????");
        System.out.println(proposerz.get(this.playerInfo));
        System.out.println(alliesz);
//        这个是当前提议玩家的信息
        int rewards = proposerz.get(this.playerInfo);
//     alliesz中包含的信息是所有的其他玩家的reward和player信息   {PlayerInfo(id=5D4C9E4BE0684871, name=3, description=3, roleInfo=PartyInfo(partyNum=2, resource=2, talent=-12.0))=Toallyz(reward=60, comment=null)}
        for(Toallyz item: alliesz.values()){
            rewards += item.getReward();
        }
//如果rewards 的和比总reward小那么这个提案有效,把alliesz放入proposal中
        if(rewards<=this.rewards){
            proposal.setAlliesz(alliesz);
        }else{
//否则报错提示reward超出
            JOptionPane.showMessageDialog(frame, guiBundle.getString("invalid_proposal_msg_box_hint"),
                    guiBundle.getString("invalid_proposal_msg_box_title"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return proposal;
    }

    @Override
    public void handleRowInfo(RowInfo rowInfo) {
        String partyName = rowInfo.getPartyName();
        boolean isSelected = rowInfo.isSelected();
        int rowIndex = rowInfo.getRowIndex();
        int resource = rowInfo.getResource();
        int partyNum = votingDomain.getByPartyName(partyName).getPartyNum();
        PlayerInfo playerInfo = getByPartyName(partyName);
        if(isSelected){
            selectedCoalition.getPartyNums().add(partyNum);
            selectedCoalition.setResources(selectedCoalition.getResources() + resource);
            selectedRowIndexs.add(rowIndex);
            jt_selection.setValueAt("+", rowIndex, 6);
            Rewards rewards = (Rewards) jt_selection.getValueAt(rowIndex, 4);
            Toallyz toallyz = new Toallyz();
            toallyz.setReward(rewards.thisReward);
            alliesz.put(playerInfo, toallyz);
            System.out.println(alliesz);
        }else{
            selectedCoalition.getPartyNums().remove(partyNum);
            selectedCoalition.setResources(selectedCoalition.getResources() - resource);
            selectedRowIndexs.remove(rowIndex);
            jt_selection.setValueAt("-", rowIndex, 6);
            jt_selection.setValueAt("-", rowIndex, 7);
            alliesz.remove(playerInfo);
            jt_selection.setValueAt(new Rewards(0, 0), rowIndex, 4);
        }
        Coalition coalition = votingDomain.getByPartyNums(selectedCoalition.getPartyNums());
        if(coalition == null){
            rewards = 0;
            selectedCoalition.setRewards(0);
            tf_coalitionReward.setText(0+"");
            tf_individual.setText(0+"");
            for(int item: selectedRowIndexs){
                jt_selection.setValueAt(new Rewards(0, 0), item, 4);
                jt_selection.setValueAt("0", item, 6);
                jt_selection.setValueAt("0", item, 7);
            }
        }else{
            rewards = coalition.getRewards();
            int reward = rewards/selectedCoalition.getPartyNums().size();
            selectedCoalition.setRewards(rewards);
            tf_coalitionReward.setText(rewards+"");
            double real = reward + this.talent;
            tf_individual.setText(real+"");
            for(int item: selectedRowIndexs){
                jt_selection.setValueAt(new Rewards(rewards, reward), item, 4);
                String val = String.valueOf(jt_selection.getValueAt(item, 5));
                if("?".equals(val)){
                    jt_selection.setValueAt("?", item, 6);
                    jt_selection.setValueAt("?", item, 7);
                }else{
                    double tal = Double.parseDouble(val);
                    jt_selection.setValueAt(String.valueOf(reward + tal), item, 6);
                    double discountedU = reward
                            * Math.pow(votingDomain.getDiscountFactor(), ((double)(sessionRound + 1))/votingDomain.getMaxRound()) + tal;
                    jt_selection.setValueAt(String.valueOf(discountedU), item, 7);
                }
            }
            proposerz.put(this.playerInfo, reward);
            for(Toallyz toallyz: alliesz.values()){
                toallyz.setReward(reward);
            }
        }
    }

    @Override
    public void rewardCellChanged(RowInfo rowInfo) {
        String partyName = rowInfo.getPartyName();
        int reward = rowInfo.getReward();
        int rowIndex = rowInfo.getRowIndex();
        if(rowIndex == 0){
            proposerz.put(this.playerInfo, reward);
            double real = reward + this.talent;
            tf_individual.setText(real+"");
            jt_selection.setValueAt(real, 0, 6);
            return;
        }
        PlayerInfo playerInfo = getByPartyName(partyName);
        if(playerInfo!=null){
            Toallyz toallyz = alliesz.get(playerInfo);
            if(toallyz == null){
                return;
            }
            toallyz.setReward(reward);
            alliesz.put(playerInfo, toallyz);
            //updateUtilityCell(rowInfo, playerInfo);
            String val = String.valueOf(jt_selection.getValueAt(rowIndex, 5));
            if("?".equals(val)){
                jt_selection.setValueAt("?", rowIndex, 6);
            }else{
                double tal = Double.parseDouble(val);
                jt_selection.setValueAt(String.valueOf(reward + tal), rowIndex, 6);
            }
        }
    }

    private void updateUtilityCell(RowInfo rowInfo, PlayerInfo playerInfo){
        PartyInfo partyInfo = (PartyInfo) playerInfo.getRoleInfo();
        boolean isSelected = rowInfo.isSelected();
        int rowIndex = rowInfo.getRowIndex();
        String cont;
        if(!isSelected){
            cont = "0";
        }else{
            if(partyInfo.getTalent() == 0.0){
                cont = "?";
            }else{
                cont = String.valueOf(rowInfo.getReward()+partyInfo.getTalent());
            }
        }
        jt_selection.setValueAt(cont, rowIndex, 6);
    }

    @Override
    public void handleExit(Player player) {

    }
}
