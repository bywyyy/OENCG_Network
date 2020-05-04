package com.swu.agentlab.zsnp.gui.player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AwtTest {

    public static void main(String[] args) {
        Frame f = new Frame("FileDialog Test");

        FileDialog d1 = new FileDialog(f, "Open File", FileDialog.LOAD);
        FileDialog d2 = new FileDialog(f, "Save File", FileDialog.SAVE);

        Button b1 = new Button("Open");
        Button b2 = new Button("Save");

        b1.addActionListener((e) -> {
            d1.setVisible(true);
            System.out.println(d1.getDirectory()+d1.getFile());
        });

        b2.addActionListener(e -> {
            d2.setVisible(true);
            System.out.println(d2.getDirectory() + d2.getFile());
        });

        f.add(b1);
        f.add(b2, BorderLayout.SOUTH);

        f.pack();
        f.setVisible(true);
    }

}
