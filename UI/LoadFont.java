package UI;

import java.awt.*;
import java.io.File;

public class LoadFont {

    public static Font loadPoppins(float size) {

        try {

            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("src/fonts/Poppins-Regular.ttf")
            );

            return font.deriveFont(size);

        } catch (Exception e) {

            System.out.println("Font load failed: " + e.getMessage());

            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    public static void applyFont(Component comp, Font font) {

        comp.setFont(font);

        if (comp instanceof Container) {

            for (Component child : ((Container) comp).getComponents()) {

                applyFont(child, font);

            }
        }
    }
}