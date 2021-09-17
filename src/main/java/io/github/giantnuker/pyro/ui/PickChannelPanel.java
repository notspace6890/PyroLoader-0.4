package io.github.giantnuker.pyro.ui;

import io.github.giantnuker.pyro.ClassStorage;
import io.github.giantnuker.pyro.Config;
import io.github.giantnuker.pyro.ServerConnection;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PickChannelPanel extends JPanel {
    JComboBox channels;
    JComboBox versions;

    public PickChannelPanel(ServerConnection connection) {
        this.setLayout(new BoxLayout(this, 1));
        if (ClassStorage.channels.isEmpty()) {
            this.add(new JLabel("You dont have access to any release channels"));
        } else {
            this.channels = new JComboBox();
            Iterator var2 = ClassStorage.channels.keySet().iterator();

            while(var2.hasNext()) {
                String channel = (String)var2.next();
                this.channels.addItem(channel);
            }

            if (Config.INSTANCE.channel != null && ClassStorage.channels.containsKey(Config.INSTANCE.channel)) {
                this.channels.setSelectedItem(Config.INSTANCE.channel);
            }

            this.channels.addItemListener((e) -> {
                this.repackVersionList();
            });
            this.add(new JLabel("Release channel"));
            this.add(this.channels);
            this.versions = new JComboBox();
            this.repackVersionList();
            this.add(new JLabel("Version"));
            this.add(this.versions);
            JButton select = new JButton("Launch");
            select.addActionListener((e) -> {
                this.channels.setEnabled(false);
                this.versions.setEnabled(false);
                Config.INSTANCE.channel = (String)this.channels.getSelectedItem();
                select.setEnabled(false);

                try {
                    connection.write((String)this.channels.getSelectedItem());
                    connection.write((String)this.versions.getSelectedItem());
                } catch (IOException var5) {
                    var5.printStackTrace();
                }

            });
            this.add(select);
        }

    }

    public void repackVersionList() {
        this.versions.removeAllItems();
        Iterator var1 = ((List)ClassStorage.channels.get(this.channels.getSelectedItem())).iterator();

        while(var1.hasNext()) {
            String version = (String)var1.next();
            this.versions.addItem(version);
        }

    }
}
