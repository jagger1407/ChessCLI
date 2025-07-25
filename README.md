# ChessCLI
Play chess via the command-line!
## How to run
Note: This program was programmed in Java 21, so an installation of [java](https://www.oracle.com/java/technologies/downloads/#java21) is required.

### **Compiling**
If you want to compile this program, open a terminal inside the project directory and type:
```bash
mkdir bin
javac -d bin src/jagger/Chess/*.java src/jagger/Chess/bots/*.java
```
After this, you can run the game by using `java -cp bin jagger.Chess.Game`.
## Commands
Note: [arg] are required parameters, \<arg\> are optional

*italic* arguments are the default.
- **start**
  - Syntax: start \<*pvp*|bot\> [black|white]
  - Starts a game of chess.
  - If a bot match is selected, you must choose a side via the second argument.
- **show board**
  - Syntax: show board
  - Prints the current state of the board to the console.
- **show moves**
  - Syntax: show moves [square]
  - Prints the board to the console, highlighting all legal moves of the piece on the specified square.
- **reverse**
  - Syntax: reverse
  - Reverses the board view accessed via the `show` commands.
- **moves**
  - Syntax: moves [square]
  - Prints all legal moves of the piece on this square.
- **move**
  - Syntax: move [source square] [destination square]
  - Moves a piece from the source square to the destination square.
  - It is preferred to use algebraic notation (example: Nc4).
- **load**
  - Syntax: load [*fen*|pgn] [position|filename]
  - Loads the specified format.
  - If `fen` is selected, the board is set up to match the position.
  - If `pgn` is selected, the board plays through all the contained moves.
  - Note: For PGN loads, each move must be separated with spaces, not newlines.
  - If the format is `pgn` and a filename is specified, the file will be read and the PGN will be loaded.
- **export**
  - Syntax: export [fen|*pgn*] \<filepath\>
  - Prints the specified format to the console.
  - If `fen` is selected, the current state of the board is printed.
  - If `pgn` is selected, the game up to this point is printed.
  - If `filepath` is specified, the output will be redirected to the file.
- **material**
  - Syntax: material
  - Prints the current material value for each side.
- **bot**
  - Syntax: bot [white|black]
  - Makes a bot take over a side.
  - Note: Only 1 bot can play at a time.
- **resign**
  - Syntax: resign
  - The current player resigns and the opposing player wins the game.
- **exit**
  - syntax: exit|end|quit|stop
  - Quit the program.
## Todo
- En Passant (IMPORTANT)
- Create more bots, and a selection of which one you want to play.
- Add ability to add game variations (fog of war)
- Rewrite the code to be better
- Create runnable .jar or .exe or smth
