package io.github.giantnuker.pyro.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import io.github.giantnuker.pyro.*;

public class LoginPanel extends JPanel
{
    JLabel failLabel;
    JTextField username;
    JPasswordField password;
    JButton submit;
    JCheckBox remeber;
    ServerConnection connection;
    boolean logging;
    
    public LoginPanel(final ServerConnection connection) {
        this.logging = false;
        this.connection = connection;
        this.setLayout(new BoxLayout(this, 1));
        this.add(new JLabel("Username"));
        this.username = new JTextField();
        final KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }
            
            @Override
            public void keyPressed(final KeyEvent e) {
            }
            
            @Override
            public void keyReleased(final KeyEvent e) {
                LoginPanel.this.onFieldChange();
            }
        };
        this.username.addKeyListener(listener);
        this.add(this.username);
        this.add(new JLabel("Password"));
        (this.password = new JPasswordField()).addKeyListener(listener);
        this.add(this.password);
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.failLabel = new JLabel(" ");
        this.remeber = new JCheckBox("Remember login");
        (this.submit = new JButton("Log in")).addActionListener(this::login);
        this.submit.setEnabled(false);
        panel.add(this.failLabel, "West");
        panel.add(this.remeber, "East");
        this.add(panel);
        this.add(this.submit);
        this.loadLogin();
    }
    
    private void login(final ActionEvent actionEvent) {
        try {
            this.connection.login(this.username.getText(), this.password.getText());
            this.logging = true;
            this.submit.setText("Logging in...");
            this.submit.setEnabled(false);
            this.username.setEnabled(false);
            this.password.setEnabled(false);
            this.failLabel.setText(" ");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveLogin() {
        if (this.remeber.isSelected()) {
            Config.INSTANCE.username = this.username.getText();
            Config.INSTANCE.password = this.password.getText();
        }
        else {
            Config.INSTANCE.username = null;
            Config.INSTANCE.password = null;
        }
    }
    
    public void loadLogin() {
        if (Config.INSTANCE.username != null && Config.INSTANCE.password != null) {
            this.username.setText(Config.INSTANCE.username);
            this.password.setText(Config.INSTANCE.password);
            this.remeber.setSelected(true);
            this.submit.setEnabled(true);
        }
    }
    
    private void onFieldChange() {
        if (!this.logging) {
            this.submit.setEnabled(!this.username.getText().isEmpty() && !this.password.getText().isEmpty());
        }
    }
    
    public void fail() {
        this.logging = false;
        this.submit.setText("Log in");
        this.username.setEnabled(true);
        this.password.setEnabled(true);
        this.password.setText("");
        this.failLabel.setText("Invalid login. Check your credentials and contact GiantNuker#8194 if you need help");
    }
}
