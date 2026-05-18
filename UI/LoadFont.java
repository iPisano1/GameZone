package UI;

import java.awt.*;
import java.io.File;

public class LoadFont {

    public static Font loadPoppins(float size) {
        return loadFont("src/fonts/Poppins-Regular.ttf", size);
    }

    public static Font loadPoppinsBold(float size) {
        return loadFont("src/fonts/Poppins-Bold.ttf", size);
    }

    private static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(path));

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