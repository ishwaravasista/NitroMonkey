import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;

/**
 * Displays the results of a completed typing test, including final WPM, accuracy,
 * and a WPM-over-time chart. Handles user input for restarting the test or returning
 * to the word-list chooser.
 */
public class Results {

    private Stage stage;
    private int finalWPM;
    private int finalAccuracy;
    private List<Integer> wpmOverTime;
    private String displayedWords;
    private int ghostWPM;
    private boolean userWon;
    private int timerLimit;
    private Scene chooserScene;
    private Theme currentTheme;
    private String displayName;
    

    /**
     * Constructs a Results screen with all necessary data from a completed test.
     *
     * @param stage          the primary Stage to display this scene on
     * @param finalWPM       the user’s final words-per-minute score
     * @param finalAccuracy  the user’s final accuracy percentage
     * @param wpmOverTime    list of WPM values recorded each second
     * @param displayedWords the text that was used during the test
     * @param ghostWPM       the AI opponent’s WPM 
     * @param userWon        true if the user beat the ghost, false otherwise
     * @param timerLimit     the duration of the test in seconds
     * @param chooserScene   the Scene to return to when pressing ESC
     * @param currentTheme   the UI theme currently applied
     * @param displayName    button name of the chosen word list
     */
    public Results(Stage stage, 
                    int finalWPM, 
                    int finalAccuracy, 
                    List<Integer> wpmOverTime, 
                    String displayedWords, 
                    int ghostWPM, 
                    boolean userWon, 
                    int timerLimit, 
                    Scene chooserScene, 
                    Theme currentTheme, 
                    String displayName) {
        this.stage = stage;
        this.finalWPM = Math.max(0, finalWPM);
        this.finalAccuracy = finalAccuracy;
        this.wpmOverTime = wpmOverTime;
        List<String> wordsList = new ArrayList<>(Arrays.asList(displayedWords.split("\\s+")));
        Collections.shuffle(wordsList);
        this.displayedWords = String.join(" ", wordsList);
        this.ghostWPM = ghostWPM;
        this.userWon = userWon;
        this.timerLimit = timerLimit;
        this.chooserScene = chooserScene;
        this.currentTheme = currentTheme;
        showResults();
    }
    
    
    /**
     * Builds and displays the results UI, including statistics labels,
     * the WPM-over-time LineChart, and key event handlers for ENTER and ESC.
     */
    private void showResults() {
        VBox statsBox = new VBox(20);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(20));
    
        Label wpmLabel = new Label("WPM: " + finalWPM);
        wpmLabel.setFont(Font.font("Roboto Mono", 36));
        wpmLabel.setTextFill(currentTheme.getButtonColor());
    
        Label accuracyLabel = new Label("Accuracy: " + finalAccuracy + "%");
        accuracyLabel.setFont(Font.font("Roboto Mono", 36));
        accuracyLabel.setTextFill(currentTheme.getButtonColor());
        
        Label escapeLabel = new Label("Press [ESC] to go Back");
        escapeLabel.setFont(Font.font("Roboto Mono", FontWeight.BOLD, 14));
        escapeLabel.setTextFill(currentTheme.getButtonColor());
        
        Label enterLabel = new Label("Press [ENTER] to Reset Test");
        enterLabel.setFont(Font.font("Roboto Mono", FontWeight.BOLD, 14));
        enterLabel.setTextFill(currentTheme.getButtonColor());
    
        statsBox.getChildren().addAll(wpmLabel, accuracyLabel);
        
        Label winnerlbl;
        if (userWon) 
        {
            winnerlbl = new Label("🎉 You win!");
            winnerlbl.setTextFill(Color.web("09b13c"));
            if(String.valueOf(currentTheme).contains("Strawberry"))
            {
                winnerlbl.setTextFill(Color.web("0d712c"));
            }
        } 
        else 
        {
            winnerlbl = new Label("💀 Ghost wins!");
            winnerlbl.setTextFill(Color.rgb(255, 113, 113));
        }
        winnerlbl.setFont(Font.font("Roboto Mono", 40));
        statsBox.getChildren().add(winnerlbl);
        
        statsBox.getChildren().add(escapeLabel);
        statsBox.getChildren().add(enterLabel);

        NumberAxis xAxis = new NumberAxis(0, timerLimit, timerLimit/10.0);
        xAxis.setLabel("Time (seconds)");
        xAxis.setTickLabelFill(currentTheme.getHoverColor());
        xAxis.setTickLabelFont(Font.font(14));
        xAxis.setMinorTickVisible(false);
    
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Words per Minute");
        yAxis.setTickLabelFill(currentTheme.getHoverColor());
        yAxis.setTickLabelFont(Font.font(14));
        yAxis.setMinorTickVisible(false);
    
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("");
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(true);
    
        lineChart.setPrefWidth(800); 
        lineChart.setPrefHeight(400);
    
        lineChart.setStyle("-fx-background-color: transparent;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        lineChart.lookup(".chart-vertical-grid-lines").setStyle("-fx-stroke: black; -fx-opacity: 0.2;");
        lineChart.lookup(".chart-horizontal-grid-lines").setStyle("-fx-stroke: black; -fx-opacity: 0.2;");
    
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < wpmOverTime.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, wpmOverTime.get(i)));
        }
        if (!wpmOverTime.isEmpty()) 
        {
            int lastWpm = wpmOverTime.get(wpmOverTime.size() - 1);
            series.getData().add(new XYChart.Data<>(timerLimit,lastWpm));
        }   
        lineChart.getData().add(series);
    
        HBox root = new HBox(50);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(50));
        root.setBackground(new Background(new BackgroundFill(currentTheme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(statsBox, lineChart);
    
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
        
        scene.setOnKeyPressed(e -> {
        if(e.getCode() == KeyCode.ENTER)
        {
            new GameScreen(stage, displayedWords, ghostWPM, timerLimit, chooserScene, currentTheme, displayName);
        }
        else if(e.getCode() == KeyCode.ESCAPE)
            {
                stage.setScene(chooserScene);
            }
        });
        
        
        Color axiscolor = currentTheme.getButtonColor();
        
        String hex = String.format
        (
            "#%02X%02X%02X",
            (int)(axiscolor.getRed()   * 255),
            (int)(axiscolor.getGreen() * 255),
            (int)(axiscolor.getBlue()  * 255)
        );
        
        String css = "-fx-text-fill: " + hex + ";";
        
        xAxis.lookup(".axis-label").setStyle(css);
        yAxis.lookup(".axis-label").setStyle(css);
    }

}