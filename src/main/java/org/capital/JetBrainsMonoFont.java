package org.capital;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class JetBrainsMonoFont {
    public void useJetBrainsMonoFont() {
        try {
            File fontSansFile = new File("../../assets/JetBrainsMono-Regular.ttf");

            Font fontSans = Font.createFont(Font.TRUETYPE_FONT, fontSansFile);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fontSans);

        } catch (IOException | FontFormatException e) {
            e.getMessage();
        }
    }
}
