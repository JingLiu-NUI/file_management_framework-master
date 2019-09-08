package com.studentportal.ui;

import javax.swing.*;

public abstract class Ui {

    private JFrame frame;

    public Ui() {
        this.frame = new JFrame();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void show() { frame.setVisible(true); }
    public void close() {
        frame.dispose();
    }
    public void hide() {
        frame.setVisible(false);
    }
}
