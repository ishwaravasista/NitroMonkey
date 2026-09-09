import javafx.scene.paint.Color;

/**
 * Lilac Mist theme: a light lavender theme with soft purple accents.
 */
public class LilacMist extends Theme {
    /**
     * @return the background color for the Lilac Mist theme
     */
    @Override
    public Color getBackgroundColor() {
        return Color.web("#e4dce4");
    }

    
    /**
     * @return the primary button color for the Lilac Mist theme
     */
    @Override
    public Color getButtonColor() {
        return Color.web("#e094c2");
    }

    
    /**
     * @return the default text color for the Lilac Mist theme
     */
    @Override
    public Color getTextColor() {
        return Color.web("#9c7c96");
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
        return Color.web("#db4ea2");
    }
}


