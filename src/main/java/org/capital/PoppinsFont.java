package org.capital;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class PoppinsFont {
    public void usePoppinsFont() {
        try {
            File fontFile = new File("../../assets/Poppins-Regular.ttf");

            Font fontSans = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fontSans);

        } catch (IOException | FontFormatException e) {
            e.getMessage();
        }
    }
}
