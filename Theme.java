import javafx.scene.paint.Color;

/**
 * Abstract base class defining the color and font properties for a UI theme.
 * Concrete subclasses must provide specific color values and font choices.
 */
public abstract class Theme {
    
    /**
     * @return the background color used across the application screens
     */
    public abstract Color getBackgroundColor();
    
    
    /**
     * @return the primary button color used for clickable elements
     */
    public abstract Color getButtonColor();
    
    
    /**
     * @return the text color used for main labels and instructions
     */
    public abstract Color getTextColor();

    
    /**
     * @return the font family name to use for on-screen text (e.g., "Roboto Mono")
     */
    public abstract String getFontFamily();

    
    /**
     * @return the hover color applied when the user mouses over buttons
     */
    public abstract Color getHoverColor();
}
