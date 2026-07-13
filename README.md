# Quax

A Hex/Havannah-style connection board game, built from scratch in Java with a JavaFX UI and a custom heuristic AI opponent.

Built collaboratively with two other developers as a team project.

Feel free to fork and tweak the bot or add any features as you wish. The scoring system in BotController.java is easy to modify and supports the addition/deletion of strategies. Happy coding!

<p align="center">
  <img src="src/assets/images/Screenshot%202026-07-12%20222628.png" alt="Mid-game board state showing Black's stones forming a diagonal chain toward a rhombus opportunity, with the Show Strategy panel explaining the bot's reasoning" width="500">
  <br>
  <sub><em>A game in progress: Black has built a diagonal chain of stones (E4–G3) toward the top-left, with the strategy panel explaining the bot's reasoning for its last move.</em></sub>
</p>

## About this project

Quax is played on an 11×11 grid of stone cells, with diamond ("rhombus") tiles that can be placed between four adjacent stones to bridge diagonal gaps, this changes the connectivity graph mid-game, which makes win detection more involved than a standard Hex clone. Notable pieces of the implementation:

- **Win detection** — a depth-first search over the stone/rhombus graph that walks both orthogonal neighbours and diagonal neighbours unlocked by rhombus placement, checking connectivity between a colour's two target edges.
- **Bot opponent** (`BotController.java`) — a heuristic move-scorer (immediate wins/blocks, diagonal connectivity, path improvement, pressure play) that also exposes a human-readable explanation of *why* it picked a move, surfaced in-app via "Show Strategy".
- **Pie rule** support for opening-move balance, and randomised bot colour assignment each game.
- **Architecture** — a clean separation between rules (`QuaxGame`), board state (`QuaxBoard`), and rendering/input (`BoardFx`), with JUnit 5 + TestFX covering both logic and UI interaction.

## Running it (Windows)

The JavaFX SDK is bundled in this repo (`javafx-sdk-21.0.10/`), no separate install is needed, just a JDK (17+) on your `PATH`.

**Easiest way:** double-click `run.bat`, or from PowerShell:

```powershell
.\run.ps1
```

This compiles the sources, copies the game assets, and launches the game in one step.

## Manual build/run

```powershell
javac --module-path javafx-sdk-21.0.10/lib --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing,javafx.web -d out/production/Quax src/*.java
Copy-Item src/assets out/production/Quax -Recurse -Force
Copy-Item src/META-INF out/production/Quax -Recurse -Force
java --module-path javafx-sdk-21.0.10/lib --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing,javafx.web -cp out/production/Quax Main
```

## How to play

You play against the built-in bot, which is randomly assigned Black or White each game (you get the other colour).

1. On launch, pick a board theme (Classic, Alternate, or "More" for Desert/Jungle).
2. **Goal:** connect your two sides of the board with an unbroken chain of your colour. **Black** connects top ↔ bottom, **White** connects left ↔ right.
3. **Your turn:** click an empty octagonal cell to place a stone.
4. Once you have two of your stones diagonally adjacent (forming a corner around an empty diamond slot), you can click that diamond slot to place a rhombus there — this links the two diagonal stones together as if they were directly connected, letting you route around gaps.
5. Turns alternate automatically with the bot after each move (stone or rhombus).
6. **Pie rule:** to offset first-move advantage, the second player can click "Activate Pie Rule" early on to swap colours with the bot instead of making a normal move.
7. Click "Show Strategy" at any time to see the reasoning behind the bot's last move; "Restart" starts a fresh game.

## Tests

Test sources are under `src/test/java`, using JUnit 5 and TestFX (see `.idea/libraries`). Run them from IntelliJ using the bundled run configurations, or via your own JUnit console launcher setup.
