import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.geometry.*;
import java.util.*;

/**
 * Main application class that sets up and manages the different UI screens
 * of the NitroMonkey typing game.
 */
public class Main extends Application {
    private Stage primaryStage;
    private Scene titleScene;
    private Scene chooserScene;
    private Theme currentTheme = new Tiramisu(); 
    
    
    /**
     * Entry point of the JavaFX application. Stores primary stage and
     * shows the title screen.
     *
     * @param primaryStage the main application window
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createTitleScreen();
    }
    
    
    
    /**
     * Builds and displays the title screen with buttons for starting
     * the race, opening options, or exiting the game.
     */    
    private void createTitleScreen() {
        // Create main container
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(currentTheme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Create VBox for vertical layout
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(20));
        
        // Create title text
        Text title = new Text("NITROMONKEY");
        title.setFont(Font.font("Roboto Mono", FontWeight.BOLD, 72));
        title.setFill(currentTheme.getButtonColor());
        
        // Create subtitle
        Text subtitle = new Text("by ishy and saky 🙉");
        subtitle.setFont(Font.font("Roboto Mono", FontWeight.NORMAL, 36));
        subtitle.setFill(currentTheme.getTextColor());
        
        // Create buttons
        Button startButton = createStyledButton("START RACE");
        Button optionsButton = createStyledButton("OPTIONS");
        Button exitButton = createStyledButton("EXIT");
        
        // Add button actions
        startButton.setOnAction(e -> wordListChooser());
        exitButton.setOnAction(e -> primaryStage.close());
        optionsButton.setOnAction(e -> openOptionsScreen());
        
        // Add all elements to the VBox
        menuBox.getChildren().addAll(title, subtitle, startButton, optionsButton, exitButton);
        
        // Add VBox to root
        root.getChildren().add(menuBox);
        
        
        
        // Create scene
        titleScene = new Scene(root, 1280, 720);
        
        // Set up stage
        primaryStage.setTitle("Nitro Monkeys");
        primaryStage.setScene(titleScene);
        primaryStage.show();
        
        titleScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });
    }
    
    
    /**
     * Presents a word-list chooser screen where the user selects
     * which set of words to use for the typing test.
     */
    private void wordListChooser() {
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
    
        Text infoText = new Text("Choose a set of words you would like to type");
        infoText.setFont(Font.font(currentTheme.getFontFamily(), FontWeight.BOLD, 28));
        infoText.setFill(currentTheme.getTextColor());
    
        HBox chooser = new HBox(20);
        chooser.setAlignment(Pos.CENTER);
    
        Button englishButton = createStyledButton("Common Words");
        Button misspelledButton = createStyledButton("Medical Terms");
        Button keywordButton = createStyledButton("Java Keywords");
    
        chooser.getChildren().addAll(englishButton, misspelledButton, keywordButton);
    
        englishButton.setOnAction(e -> launchGameWith("english.txt", "Common Words"));
        misspelledButton.setOnAction(e -> launchGameWith("english_medical.txt", "Medical Terms"));
        keywordButton.setOnAction(e -> launchGameWith("java_keywords.txt", "Java Keywords"));
    
        layout.getChildren().addAll(infoText, chooser);
    
        StackPane chooserRoot = new StackPane(layout);
        chooserRoot.setBackground(new Background(new BackgroundFill(currentTheme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        
    
        chooserScene = new Scene(chooserRoot, 1280, 720);
        Label escHint = new Label("Press [ESC] to go Back");
        escHint.setFont(Font.font("Roboto Mono", FontWeight.BOLD, 21));
        escHint.setTextFill(currentTheme.getTextColor());
        
        chooserRoot.getChildren().add(escHint);
        StackPane.setAlignment(escHint, Pos.TOP_LEFT);
        
        
        chooserScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                primaryStage.setScene(titleScene);
            }
        });
    
        primaryStage.setScene(chooserScene);
        primaryStage.setTitle("Choose Your Word List");
    }

    
    /**
     * Loads the selected word list, prompts the user for opponent WPM
     * and test time limit, then launches the main GameScreen.
     *
     * @param fileName    the name of the word file to load
     * @param displayName the button name of the chosen list
     */
    private void launchGameWith(String fileName, String displayName)
    {
        List<String> words = WordListLoader.load(fileName);
        
        Collections.shuffle(words);
        
        int ghostWPM;
        while (true) 
        {
            TextInputDialog wpmDialog = new TextInputDialog("70");
            wpmDialog.setHeaderText("Enter Opponent Speed (WPM)");
            Optional<String> wpmResult = wpmDialog.showAndWait();
            if (!wpmResult.isPresent()) 
                return;  
            try 
            {
                ghostWPM = Integer.parseInt(wpmResult.get());
            } catch (NumberFormatException ex) 
            {
                showError("Please enter a valid number for WPM.");
                continue;
            }
            if (ghostWPM > 0) 
            {
                break;  
            }
            showError("WPM must be greater than zero.");
        }
        
        int timeLimit;
        while(true) 
        {
            TextInputDialog secDialog = new TextInputDialog("15");
            secDialog.setHeaderText("Enter Test Time Limit (seconds)");
            Optional<String> rslt = secDialog.showAndWait();
            if (!rslt.isPresent()) 
                return;                  
            try 
            {
                timeLimit = Integer.parseInt(rslt.get());
            } catch (NumberFormatException ex) 
            {
                showError("Please enter a valid number.");   
                continue;
            }
            break;
        }
                
        int wordCount = 80;
        if(timeLimit >= 30)
        {
            wordCount = 150;
        }
        
        if(fileName.equals("java_keywords.txt")){wordCount = 68;}
        if(fileName.equals("english_medical.txt")){wordCount = 100;}
        
        String displayedWords = String.join(" ", words.subList(0, wordCount));
        
        new GameScreen(primaryStage, displayedWords, ghostWPM, timeLimit, chooserScene, currentTheme, displayName);
        
    }
    
    
    /**
     * Displays an error alert with the given message.
     * Blocks until the user dismisses it.
     *
     * @param message text to show in the error dialog
     */
    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    
    /**
     * Opens the options screen allowing the user to change theme settings
     * and view instructions.
     */
    private void openOptionsScreen() {
        // Left: Theme selection buttons
        VBox optionsBox = new VBox(20);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        optionsBox.setPadding(new Insets(50));
    
        Text title = new Text("Select Theme:");
        title.setFont(Font.font(currentTheme.getFontFamily(), FontWeight.BOLD, 36));
        title.setFill(currentTheme.getTextColor());
    
        Button watermelonButton = createStyledButton("Watermelon");
        Button suiseiButton = createStyledButton("Suisei");
        Button tiramisuButton = createStyledButton("Tiramisu");
        Button lilacMistButton = createStyledButton("Lilac Mist");
        Button strawberryButton = createStyledButton("Strawberry");
    
    
        watermelonButton.setOnAction(e -> {
            currentTheme = new Watermelon();
            createTitleScreen();
        });
        
        suiseiButton.setOnAction(e -> {
            currentTheme = new Suisei();
            createTitleScreen();
        });
    
        tiramisuButton.setOnAction(e -> {
            currentTheme = new Tiramisu();
            createTitleScreen();
        });
    
        lilacMistButton.setOnAction(e -> {
            currentTheme = new LilacMist();
            createTitleScreen();
        });
        
        strawberryButton.setOnAction(e -> {
            currentTheme = new Strawberry();
            createTitleScreen();
        });
    
        Button back = createStyledButton("← Back");
        back.setOnAction(e -> primaryStage.setScene(titleScene));
    
        optionsBox.getChildren().addAll(title, tiramisuButton, suiseiButton, watermelonButton, lilacMistButton, strawberryButton, back);
    
        // Right: Instructions box
        VBox instructionsBox = new VBox(10);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsBox.setPadding(new Insets(50, 50, 50, 0)); // right padding
    
        Text instructionsTitle = new Text("Instructions:");
        instructionsTitle.setFont(Font.font(currentTheme.getFontFamily(), FontWeight.BOLD, 28));
        instructionsTitle.setFill(currentTheme.getTextColor());
    
        Text instructions = new Text(
            "• Press ESC to go back to the main menu\n" +
            "• Press ENTER to restart the typing test\n" +
            "• Select a theme to change the game’s appearance\n"
        );
        instructions.setFont(Font.font(currentTheme.getFontFamily(), 20));
        instructions.setFill(currentTheme.getTextColor());
    
        instructionsBox.getChildren().addAll(instructionsTitle, instructions);
    
        // Combine both boxes in an HBox
        HBox rootLayout = new HBox(100, optionsBox, instructionsBox); // 100px spacing
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setBackground(new Background(new BackgroundFill(currentTheme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    
        Scene optionsScene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(optionsScene);
        primaryStage.setTitle("Options");
        
        optionsScene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE)
            {
                primaryStage.setScene(titleScene);
            }
        });
    }


    /**
     * Creates a styled button using the current theme colors.
     * Provides hover effects.
     *
     * @param text the label text for the button
     * @return a fully styled JavaFX Button
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        
        String baseColor = toHex(currentTheme.getButtonColor());
        String hoverColor = toHex(currentTheme.getHoverColor());
        String baseStyle = 
            "-fx-background-color: " + baseColor + ";" +
            "-fx-text-fill: " + toHex(currentTheme.getTextColor()) + ";" +
            "-fx-font-family: " + currentTheme.getFontFamily() + ";" +
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;";
    
        String hoverStyle = baseStyle.replace(baseColor, hoverColor);
    
        button.setStyle(baseStyle);
        button.setMinWidth(400);
        
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    
        return button;
    }


    /**
     * Converts a JavaFX Color into a hex string for CSS styling.
     *
     * @param color the JavaFX Color to convert
     * @return a string in the format "#RRGGBB"
     */
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
