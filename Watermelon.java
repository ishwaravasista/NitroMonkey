import javafx.scene.paint.Color;

/**
 * Watermelon theme: a deep green background with pinkish-red accents.
 */
public class Watermelon extends Theme {
    /**
     * @return the background color for the Watermelon theme
     */
    @Override
    public Color getBackgroundColor() {
        return Color.web("#1f4437");
    }

    
    /**
     * @return the primary button color for the Watermelon theme
     */
    @Override
    public Color getButtonColor() {
        return Color.web("#d6686f");
    }

    
    /**
     * @return the default text color for the Watermelon theme
     */
    @Override
    public Color getTextColor() {
        return Color.web("#cdc6bc");
    }

    
    /**
     * @return the font family used by the Watermelon theme
     */
    @Override
    public String getFontFamily() {
        return "Roboto Mono";
    }
    
    
    /**
     * @return the hover color for buttons in the Watermelon theme
     */
    @Override
    public Color getHoverColor() {
        return Color.web("#3e7a65");
    }
}


