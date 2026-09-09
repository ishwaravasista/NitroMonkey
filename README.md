# NitroMonkey

NitroMonkey is a JavaFX typing game created for CSIII Project 2 by ishy and saky. Race against a ghost caret at a speed you choose, then review your words per minute (WPM), accuracy, and a chart of your speed over time.

## Features

- Three word lists: Common Words, Medical Terms, and Java Keywords.
- Configurable opponent speed and test duration (defaults: 70 WPM and 15 seconds).
- Live countdown, WPM display, and character feedback, with mistakes shown in red.
- Five color themes: Tiramisu (default), Suisei, Watermelon, Lilac Mist, and Strawberry.
- Results showing final WPM, accuracy, a speed chart, and the race winner.


## Build and run

For BlueJ, open `package.bluej`, ensure JavaFX is available in your BlueJ environment, compile the project, and run `Main.main(String[] args)` with an empty argument array (`{}`).

## How to play

1. Select **START RACE** from the main menu.
2. Choose a word list.
3. Enter a positive whole number for the opponent's WPM and for the test duration in seconds.
4. Begin typing the displayed text to start the countdown and ghost caret.
5. Keep typing until the timer ends, then review your results. Your final WPM must exceed the ghost's WPM to win; ties count as a ghost win.

Choose **OPTIONS** on the main menu to change the theme.

## Word lists and scoring notes

You can edit the existing word-list files to change the typing material. Keep at least 150 entries in `english.txt`, 100 in `english_medical.txt`, and 68 in `java_keywords.txt`; the game selects fixed-size subsets from these files. Adding a new selectable list also requires updating `Main.java`.

WPM uses the game's space-based word counter divided by elapsed minutes, rather than a standardized five-character word calculation. Accuracy uses the game's correct-character counter divided by attempted characters. Backspace handling can affect these counters, so the results should be treated as game statistics.

The duration input currently checks for an integer but does not reject zero or negative values. Use a positive duration so the countdown can finish normally.

