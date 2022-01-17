package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Represents a splashscreen that disappears upon user click
public class SplashScreen extends JWindow {

    // REQUIRES: valid file location string
    // EFFECTS: constructs a splashscreen using provided image that will disappear upon user click
    public SplashScreen(String imageFile, Frame frame) {
        super(frame);
        JLabel label = new JLabel(new ImageIcon(imageFile));
        getContentPane().add(label, BorderLayout.CENTER);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = label.getPreferredSize();
        setLocation(screenSize.width / 2 - (labelSize.width / 2), screenSize.height / 2 - (labelSize.height / 2));
        setVisible(true);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });

    }
}
