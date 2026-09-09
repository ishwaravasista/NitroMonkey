import javafx.scene.paint.Color;

/**
 * Tiramisu theme: a warm, neutral theme with earthy buttons.
 */
public class Tiramisu extends Theme {
    /**
     * @return the background color for the Tiramisu theme
     */
    @Override
    public Color getBackgroundColor() {
        return Color.web("#cfc6b9");
    }

    
    /**
     * @return the primary button color for the Tiramisu theme
     */
    @Override
    public Color getButtonColor() {
        return Color.web("#c0976f");
    }

    
    /**
     * @return the default text color for the Tiramisu theme
     */
    @Override
    public Color getTextColor() {
        return Color.web("#7e564e");
    }

    
    /**
     * @return the font family used by the Tiramisu theme
     */
    @Override
    public String getFontFamily() {
        return "Roboto Mono";
    }
    
    
    /**
     * @return the hover color for buttons in the Tiramisu theme
     */
    @Override
    public Color getHoverColor() {
        return Color.web("#a48c7c");
    }
}



