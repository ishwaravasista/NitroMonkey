# NitroMonkey

NitroMonkey is a JavaFX typing game created for CSIII Project 2 by ishy and saky. Race against a ghost caret at a speed you choose, then review your words per minute (WPM), accuracy, and a chart of your speed over time.

## Features

- Three word lists: Common Words, Medical Terms, and Java Keywords.
- Configurable opponent speed and test duration (defaults: 70 WPM and 15 seconds).
- Live countdown, WPM display, and character feedback, with mistakes shown in red.
- Five color themes: Tiramisu (default), Suisei, Watermelon, Lilac Mist, and Strawberry.
- Results showing final WPM, accuracy, a speed chart, and the race winner.

## Requirements

- A Java Development Kit (JDK) with `java` and `javac` available on your PATH.
- A JavaFX SDK compatible with your JDK and operating system, including `javafx.controls`.
- A desktop environment to display the game window.

The project includes BlueJ metadata, but does not use Maven or Gradle. JavaFX must be available through your IDE or supplied explicitly when compiling and running. The interface requests Roboto Mono; installing that font is optional.

## Build and run

Open PowerShell in the project directory. Set the path below to the `lib` folder of your extracted JavaFX SDK, then compile and launch:

```powershell
$javaFxLib = "C:\path\to\javafx-sdk\lib"
javac -encoding UTF-8 --module-path "$javaFxLib" --add-modules javafx.controls *.java
java --module-path "$javaFxLib" --add-modules javafx.controls Main
```

Run the game from the project directory so it can find the `wordlists` folder. Launch `Main`, which sets up the menu and game settings.

For BlueJ, open `package.bluej`, ensure JavaFX is available in your BlueJ environment, compile the project, and run `Main.main(String[] args)` with an empty argument array (`{}`).

## How to play

1. Select **START RACE** from the main menu.
2. Choose a word list.
3. Enter a positive whole number for the opponent's WPM and for the test duration in seconds.
4. Begin typing the displayed text to start the countdown and ghost caret.
5. Keep typing until the timer ends, then review your results. Your final WPM must exceed the ghost's WPM to win; ties count as a ghost win.

Choose **OPTIONS** on the main menu to change the theme.

| Control | Action |
| --- | --- |
| Typing keys | Attempt the next character, including spaces |
| Backspace | Return to the previous character |
| Enter during a race | Shuffle the current words and restart the test |
| Enter on the results screen | Start another test with shuffled words and the same settings |
| Escape during a race or on results | Return to the word-list chooser |
| Escape in the chooser or options | Return to the main menu |
| Escape on the main menu | Exit the game |

## Project structure

| File or folder | Purpose |
| --- | --- |
| `Main.java` | Application entry point, menus, word-list selection, and race settings |
| `GameScreen.java` | Typing input, countdown, ghost animation, and live statistics |
| `Results.java` | Final statistics, WPM chart, and replay controls |
| `WordListLoader.java` | Reads word lists from disk |
| `Theme.java` | Interface defining theme colors and fonts |
| `Tiramisu.java`, `Suisei.java`, `Watermelon.java`, `LilacMist.java`, `Strawberry.java` | Theme implementations |
| `wordlists/` | Plain-text word lists, with one entry per line |
| `doc/` | Generated API documentation |
| `package.bluej`, `*.ctxt` | BlueJ project metadata |

## Word lists and scoring notes

You can edit the existing word-list files to change the typing material. Keep at least 150 entries in `english.txt`, 100 in `english_medical.txt`, and 68 in `java_keywords.txt`; the game selects fixed-size subsets from these files. Adding a new selectable list also requires updating `Main.java`.

WPM uses the game's space-based word counter divided by elapsed minutes, rather than a standardized five-character word calculation. Accuracy uses the game's correct-character counter divided by attempted characters. Backspace handling can affect these counters, so the results should be treated as game statistics.

The duration input currently checks for an integer but does not reject zero or negative values. Use a positive duration so the countdown can finish normally.

## Troubleshooting

- **JavaFX packages or modules cannot be found:** Check that `$javaFxLib` points to the SDK's `lib` directory and that the SDK is compatible with your JDK.
- **Could not load word list:** Run from the project root and make sure the three files in `wordlists/` are present.
- **Word-list selection fails after editing a file:** Check that the file still contains the minimum number of entries listed above.
