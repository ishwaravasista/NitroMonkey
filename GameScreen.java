import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;
import java.util.*;
import java.util.stream.*;

/**
 * Represents the main game screen where the user types and
 * races against a "ghost" caret at a chosen WPM.
 */
public class GameScreen extends Application {
    private Scene chooserScene;
    private TextFlow textFlow = new TextFlow();
    private Label timerLabel = new Label("15");
    private Label wpmLabel = new Label("0");
    private double elapsed;
    // textqueue -> what needs to be pressed
    private Deque<Text> textQueue = new ArrayDeque<>();
    // pressed -> what user has already attempted
    private Deque<Text> pressed = new ArrayDeque<>();
    private boolean tabPressed = false;
    private int correctlyTypedChar;
    private int charTyped;
    private int wordsTyped;
    private double caretOffsetY = 6;
    private double startTime;
    private AnchorPane typingPane;
    private Stage stage;
    private String displayedWords;
    private int ghostWPM;
    private int timerLimit;
    private Line userCaret;
    private List<Integer> wpmOverTime = new ArrayList<>();
    private int timeRemaining;
    private Timeline countdown;
    private boolean timerStarted = false;
    private boolean ghostStarted = false;
    private SequentialTransition ghostSeq;
    private Theme currentTheme;
    private String displayName;
    
    
    /**
     * Constructs a new GameScreen using the following parameters.
     *
     * @param stage          primary application Stage
     * @param displayedWords the text that will be typed
     * @param ghostWPM       the opponent's WPM
     * @param timerLimit     duration of the test (sec)
     * @param chooserScene   Scene to return to on ESC
     * @param currentTheme   UI theme to apply
     * @param displayName    name of the current word list
     */
    public GameScreen(Stage stage,
                        String displayedWords, 
                        int ghostWPM, 
                        int timerLimit, 
                        Scene chooserScene, 
                        Theme currentTheme, 
                        String displayName) 
     {
        this.stage = stage;
        this.displayedWords = displayedWords;
        this.ghostWPM = ghostWPM;
        this.timerLimit = timerLimit;
        this.timerLabel = new Label(String.valueOf(timerLimit));
        this.chooserScene = chooserScene;
        this.currentTheme = currentTheme;
        this.displayName = displayName;
        setupUI(stage);
    }
    
    
    /**
     * JavaFX entry point when this Application is launched.
     * Goes to setupUI().
     *
     * @param primaryStage main Stage container
     */
    @Override
    public void start(Stage primaryStage) {
        setupUI(primaryStage);
    }
    
    
    /**
     * Builds and lays out all UI elements: stats, text flow, carets, and
     * binds event handlers.
     *
     * @param stage the Stage to display the game UI on
     */
    private void setupUI(Stage stage) {
        
        Label nameLabel = new Label(displayName);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        root.setBackground(new Background(new BackgroundFill(currentTheme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.TOP_LEFT);
        stats.getChildren().addAll(timerLabel, wpmLabel, nameLabel);
        
        Label instructLabel = new Label("[ESC] Back | [ENTER] Restart");
        instructLabel.setFont(Font.font("Roboto Mono", FontWeight.BOLD, 14));
        instructLabel.setTextFill(currentTheme.getTextColor());
        instructLabel.setAlignment(Pos.CENTER_RIGHT);
        
        textFlow.setLineSpacing(10);
        
        Font font = Font.font("Roboto Mono", 24);
        timerLabel.setFont(Font.font(30));
        timerLabel.setTextFill(currentTheme.getButtonColor());
        
        
        timeRemaining = Integer.parseInt(timerLabel.getText());
        
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            timerLabel.setText(String.valueOf(timeRemaining));
            elapsed = (System.currentTimeMillis() - startTime) / 60000;
            if (elapsed > 0)
            {
                int wpm = (int) Math.round(wordsTyped/elapsed);
                wpm = Math.max(0, wpm);
                wpmLabel.setText(String.valueOf(wpm));
                wpmOverTime.add(wpm);
            }
            if (timeRemaining == 0) 
            {
                countdown.stop();
                int accuracy = 0;
                if(charTyped > 0)
                {
                    accuracy = (int)Math.round((double)correctlyTypedChar / charTyped * 100);
                }
                int finalWPM = wpmOverTime.isEmpty() ? 0 : wpmOverTime.get(wpmOverTime.size() - 1);
                //System.out.println(accuracy);
                boolean userWon = finalWPM > ghostWPM;
                new Results(stage, finalWPM, accuracy, wpmOverTime, displayedWords, ghostWPM, userWon, timerLimit, chooserScene, currentTheme, displayName);
            }
        }));
        
        countdown.setCycleCount(Timeline.INDEFINITE);
        
        
        wpmLabel.setFont(Font.font(30));
        wpmLabel.setTextFill(currentTheme.getButtonColor());
        
        nameLabel.setFont(Font.font(30));
        nameLabel.setTextFill(currentTheme.getTextColor());
        
        
        Label inputLabel = new Label();
        inputLabel.setVisible(false);
        
        
        
        typingPane = new AnchorPane();
        typingPane.setPrefHeight(320);
        
        typingPane.getChildren().add(textFlow);
        AnchorPane.setTopAnchor(textFlow, 0.0);
        AnchorPane.setLeftAnchor(textFlow, 0.0);
        AnchorPane.setRightAnchor(textFlow, 0.0);
        
        root.getChildren().addAll(stats, typingPane, inputLabel, instructLabel);
        
        Scene scene = new Scene(root, 1280,720);
        stage.setTitle("NitroMonkey");
        
        
        textFlow.prefWidthProperty().bind(scene.widthProperty().subtract(40)); 
             
        stage.setScene(scene);
        stage.show();
        root.requestFocus();
        
        scene.setOnKeyPressed(this::handleKeyPress);
        String words = this.displayedWords;
        for (char c : words.toCharArray()) {
            Text t = new Text(String.valueOf(c));
            t.setFont(Font.font("Roboto Mono", 24));
            t.setFill(currentTheme.getHoverColor());
            textFlow.getChildren().add(t);
            textQueue.add(t);
        }
        
        userCaret = new Line(0, 0, 0, Font.font("Roboto Mono", 24).getSize());
        userCaret.setStroke(currentTheme.getButtonColor());
        userCaret.setStrokeWidth(2);
        typingPane.getChildren().add(userCaret);
        updateUserCaretPosition();

    }
    
    
    /**
     * Begins the ghost caret animation, sliding it across each wrapped
     * line of text at the speed corresponding to ghostWPM.
     */
    private void startGhostCaret() {
        if (ghostStarted) return;
        ghostStarted = true;
    
        Platform.runLater(() -> 
        {
            List<Text> letters = textFlow.getChildren().stream()
                .filter(n -> n instanceof Text)
                .map(n -> (Text)n)
                .collect(Collectors.toList());
    
            Map<Double, List<Text>> lines = new LinkedHashMap<>();
            for (Text t : letters) 
            {
                double y = t.getBoundsInParent().getMinY();
                lines.computeIfAbsent(y, yy -> new ArrayList<>()).add(t);
            }
    
            List<double[]> segments = new ArrayList<>();
            for (Map.Entry<Double, List<Text>> entry : lines.entrySet()) 
            {
                List<Text> lineChars = entry.getValue();
                Bounds first = lineChars.get(0).getBoundsInParent();
                Bounds last= lineChars.get(lineChars.size()-1).getBoundsInParent();
                segments.add(new double[]{ first.getMinX(), last.getMaxX(), entry.getKey() });
            }
            
            
            
            int wordCount = displayedWords.split("\\s+").length;
            double totalTimeS = wordCount / (double)ghostWPM *60.0;
            double totalDistance = segments.stream().mapToDouble(s -> s[1] - s[0]).sum();
            double velocityPxPerS = totalDistance / totalTimeS;
    
            Line enemyCaret = new Line(0, 0, 0, Font.font("Roboto Mono",24).getSize());
            enemyCaret.setStroke(Color.RED);
            enemyCaret.setStrokeWidth(3);
            typingPane.getChildren().add(enemyCaret);
    
            SequentialTransition seq = new SequentialTransition();
            for (int i = 0; i < segments.size(); i++) 
            {
                double[] s = segments.get(i);
                double segWidth = s[1] - s[0];
                double segTime = segWidth / velocityPxPerS;
                TranslateTransition move = new TranslateTransition(Duration.seconds(segTime), enemyCaret);
                move.setFromX(s[0]); 
                move.setToX(s[1]);
                move.setFromY(s[2] +caretOffsetY); 
                move.setInterpolator(Interpolator.LINEAR);
                seq.getChildren().add(move);
    
                if (i+1 < segments.size()) 
                {
                    double[] next = segments.get(i +1);
                    PauseTransition jump = new PauseTransition(Duration.ZERO);
                    jump.setOnFinished(e -> 
                    {
                        enemyCaret.setTranslateX(next[0]);
                        enemyCaret.setTranslateY(next[2] +caretOffsetY);
                    });
                    seq.getChildren().add(jump);
                }
            }
            seq.play();
        });
    }
    
    
    /**
     * Stops the current test, clears all state and UI nodes, and
     * re-initializes the screen to allow a new test.
     */
    private void restartTest() {
        countdown.stop();
        if (ghostSeq != null)
            ghostSeq.stop();
    
        // reset all state
        timerStarted      = false;
        ghostStarted      = false;
        charTyped         = 0;
        correctlyTypedChar= 0;
        wordsTyped        = 0;
        wpmOverTime.clear();
    
        textFlow.getChildren().clear();
        textQueue.clear();
        pressed.clear();
    
        timeRemaining = timerLimit;
        timerLabel.setText(String.valueOf(timeRemaining));
        wpmLabel.setText("0");
    
        setupUI(stage);
    }

    
    /**
     * Moves the user's caret to the position of the next char
     * in the textQueue.
     */
    private void updateUserCaretPosition() 
    {
        if (textQueue.isEmpty()) return;
          Text nextChar = textQueue.peek();             
          Bounds b = nextChar.getBoundsInParent();      
          userCaret.setTranslateX(b.getMinX());
          userCaret.setTranslateY(b.getMinY() + caretOffsetY);
    }
    
    
    /**
     * Handles all key presses: starting timers, processing backspace,
     * accepting correct/incorrect keystrokes, and handling ESC/ENTER.
     *
     * @param event the KeyEvent from the scene
     */
    public void handleKeyPress(KeyEvent event){
        
        if(event.getCode() == KeyCode.SHIFT){
            return;
        }
        
        if(event.getCode() == KeyCode.ESCAPE)
        {
            restartTest();
            stage.setScene(chooserScene);
            return;
        }
       
        if(event.getCode() == KeyCode.ENTER)
        {
            List<String> wordsList = new ArrayList<>(Arrays.asList(displayedWords.split("\\s+")));
            Collections.shuffle(wordsList);
            displayedWords = String.join(" ", wordsList);
           restartTest();
           return;
        }
        
        if(!timerStarted)
        {
            timerStarted = true;
            startTime = System.currentTimeMillis();
            countdown.play();
            
        }
        
        if(!ghostStarted) {startGhostCaret();}
        
        
        //handles backspace
        if(event.getCode() == KeyCode.BACK_SPACE)
        {
            if(!pressed.isEmpty())
            {
                Text last = pressed.removeLast();
                last.setFill(Color.rgb(255, 254, 166));
                correctlyTypedChar--;
                textQueue.addFirst(last);
                updateUserCaretPosition();
            }
            return;
        }
        
        if(textQueue.isEmpty()){
            return; 
        }
        
        String typed = event.getText();
        // if valid keystroke
        if(typed.isEmpty()){
            return;
        }
        charTyped++;
        
        char typedLetter = typed.charAt(0);
        Text currentLetter = textQueue.peek();
        char expectedLetter = currentLetter.getText().charAt(0);
        
        if(typedLetter == expectedLetter){
            currentLetter.setFill(currentTheme.getTextColor());
            if(expectedLetter == ' ')
            {
                wordsTyped++;
            }

            
            correctlyTypedChar++;
        }
        else{
            currentLetter.setFill(Color.RED);
            if(expectedLetter == ' ')
            {
                wordsTyped--;
            }
        }
        
        pressed.add(currentLetter);
        textQueue.poll();
        
        updateUserCaretPosition();

        
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