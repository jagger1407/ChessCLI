# ChessCLI
Play chess via the command-line!
## Commands
Note: [arg] are required parameters, \<arg\> are optional

*italic* arguments are the default.
- **start**
  - Syntax: start <*pvp*|bot> [black|white]
  - Starts a game of chess.
  - If a bot match is selected, you must choose a side via the second argument.
- **board**
  - Syntax: board <square>
  - Prints the current state of the board to the console.
  - If a square is specified, it shows all the legal moves for the piece on that square.
- **moves**
  - Syntax: moves [square]
  - Prints all legal moves of the piece on this square.
- **move**
  - Syntax: move [source square] [destination square]
  - Moves a piece from the source square to the destination square.
  - It is preferred to use algebraic notation (example: Nc4).
- **load**
  - Syntax: load [FEN-position]
  - Loads the board using a FEN-position as the input.
- **export**
  - Syntax: export
  - Prints the current state of the board as a FEN-position.
- **material**
  - Syntax: material
  - Prints the current material value for each side.
- **resign**
  - Syntax: resign
  - The current player resigns and the opposing player wins the game.
- **exit**
  - syntax: exit|end|quit|stop
  - Quit the program.
## Todo
- Create more bots, and a selection of which one you want to play.
- Save a game after playing
- Export a game as a series of algebraic moves (e.g. "1. e4 e5")
- Add ability to add game variations (fog of war)
- Rewrite the code to be better
