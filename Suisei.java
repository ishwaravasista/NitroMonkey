import javafx.scene.paint.Color;

/**
 * Suisei theme: a dark-blue, cool-toned theme.
 */
public class Suisei extends Theme {
    
    
    /**
     * @return the background color for the Suisei theme
     */
    @Override
    public Color getBackgroundColor() {
        return Color.rgb(59, 74, 98);
    }

    
    /**
     * @return the primary button color for the Suisei theme
     */
    @Override
    public Color getButtonColor() {
        return Color.web("#ffb06c");
    }

    
    /**
     * @return the default text color for the Suisei theme
     */
    @Override
    public Color getTextColor() {
        return Color.WHITE;
    }

    
    /**
     * @return the font family used by the Suisei theme
     */
    @Override
    public String getFontFamily() {
        return "Roboto Mono";
    }
    
    
    /**
     * @return the hover color for buttons in the Suisei theme
     */
    @Override
    public Color getHoverColor() {
        return Color.web("#bef0ff");
    }
}