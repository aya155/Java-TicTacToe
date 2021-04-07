import java.lang.InterruptedException
import kotlin.math.roundToInt

class Player     //constructor.  requires string to set player type
    (//player makes moves and can be human or AI
    private val type // whether the player is human or AI
    : String
) {
    private var index = 0
    private var column = 0
    private var row = 0
    private var turn //whether or not it's the player's turn
            = false

    //player "goes" while it's their turn
    fun go() {
        turn = true

        // if AI, do computery things
        if (type === "AI") {

            //let user know that AI is going
            print("\tThe computer will now make a move..")
            TicTacToe.game?.let { delay(1000, it.gridSize) } //take a second to go to make it appear as if computer is thinking
            while (turn) {
                //AI selects a random empty cell and places corrosponding mark
                index = ((TicTacToe.game!!.gridSize * TicTacToe.game!!.gridSize - 1) * Math.random()).roundToInt()
                    .toInt()
                move(index, TicTacToe.game!!)
            }
        } else {
            //if human, do human stuff
            println("\tPlease place an X on the grid.  You can")
            TicTacToe.user_input = TicTacToe.getInput("\tdo this by typing 1A, 1B, 1C, 2A, etc.: ")

            //while it's the player's turn...
            while (turn) {

                //validate user input
                if (valid_input(TicTacToe.user_input!!)) {
                    if (TicTacToe.user_input!!.length == 2) {
                        column = TicTacToe.user_input!!.substring(0, 1).toInt()
                        row = letterToNumber(TicTacToe.user_input!!.substring(1, 2))
                    } else {
                        column = TicTacToe.user_input!!.substring(0, 2).toInt()
                        row = letterToNumber(TicTacToe.user_input!!.substring(2, 3))
                    }
                    index = TicTacToe.game!!.gridSize * (row - 1) + (column - 1)
                    if (index > TicTacToe.game!!.gridSize * TicTacToe.game!!.gridSize - 1 || index < 0) {
                        TicTacToe.user_input =
                            TicTacToe.getInput("That's not a valid spot!  Please choose another spot: ")
                    } else {

                        //if valid input, and cell isn't taken already,
                        //place mark in selected cell and end turn
                        TicTacToe.game?.let { move(index, it) }
                        if (turn) {
                            TicTacToe.user_input =
                                TicTacToe.getInput("That space is already in play!  Please choose another spot: ")
                        }
                    }
                } else {
                    TicTacToe.user_input = TicTacToe.getInput("That's not valid input.  Please choose another spot: ")
                }
            }
        }
    }

    //player places mark
    private fun move(index: Int, game: Game) {
        if (TicTacToe.game!!.setCell(index)) {
            turn = false
        }
    }

    companion object {
        //encapsulated code for user input validation
        //it checks to make sure the input was two or three characters long,
        //and that it contained one or two digits, followed by one lower
        //case or upper case letter
        private fun valid_input(user_input: String): Boolean {
            var output = false
            if (user_input.length == 2) {
                output = user_input.substring(0, 1).matches(Regex("[0-9]")) && user_input.substring(1, 2).matches(Regex("[a-zA-Z]"))
            } else if (user_input.length == 3) {
                output =
                    user_input.substring(0, 2).matches(Regex("[1-2][0-9]")) && user_input.substring(2, 3).matches(Regex("[a-zA-Z]"))
                if (user_input.substring(0, 2).toInt() > TicTacToe.game!!.gridSize) {
                    output = false
                }
            }
            return output
        }

        //encapsulated code for AI delay behavior
        private fun delay(amount: Int, gameSize: Int) {
            try {
                Thread.sleep((amount * 3 / (gameSize * gameSize)).toLong())
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }

        //converts the letter input for row/column selection into a usable number
        private fun letterToNumber(str: String): Int {
            return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(str) % 26 + 1
        }
    }
}