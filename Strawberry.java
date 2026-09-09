import javafx.scene.paint.Color;

/**
 * Lilac Mist theme: a light lavender theme with soft purple accents.
 */
public class Strawberry extends Theme {
    /**
     * @return the background color for the Lilac Mist theme
     */
    @Override
    public Color getBackgroundColor() {
        return Color.web("#f37f83");
    }

    
    /**
     * @return the primary button color for the Lilac Mist theme
     */
    @Override
    public Color getButtonColor() {
        return Color.web("#ec5e6c");
    }

    
    /**
     * @return the default text color for the Lilac Mist theme
     */
    @Override
    public Color getTextColor() {
        return Color.web("fcfcf8");
    }

    
    /**
     * @return the font family used by the Lilac Mist theme
     */
    @Override
    public String getFontFamily() {
        return "Roboto Mono";
    }
    
    
    /**
     * @return the hover color for buttons in the Lilac Mist theme
     */
    @Override
    public Color getHoverColor() {
        return Color.web("#e53c58");
    }
}


