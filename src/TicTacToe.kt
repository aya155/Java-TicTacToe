import kotlin.jvm.JvmStatic
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

object TicTacToe {
    var game: Game? = null
    var count = 0
    var userInput: String? = null
    private var gameMode = 0
    private var valid_input = false
    @JvmStatic
    fun main(args: Array<String>) {
        val minimumGameSize = 1
        val maximumGameSize = 26

        //When program starts, user is met with a welcome message
        println("\n\tWelcome to this wonderful and lovely game of TicTacToe. \n \n" + "\tPlease select your Game mode. \n \n" + "\t    (1) Human vs. Computer \n \n" + "\t    (2) Computer vs. Computer")
        userInput = getInput("\n\tWhich mode would you like to play? (1/2): ")

        //Keep asking for an answer from the user until we get a 1 or a 2
        gameMode(userInput) //gameMode() is defines below
        println("\n\tHow large of a grid would you like to use? ")
        userInput = getInput("\n\tPlease enter an integer between $minimumGameSize and $maximumGameSize: ")

        //validate user input for game size
        valid_input = false
        while (!valid_input) if (userInput!!.isNotEmpty() && userInput!!.substring(0, 1).matches(Regex("[1-9]")) && minimumGameSize <= userInput!!.toInt() && userInput!!.toInt() <= maximumGameSize) valid_input = true else userInput = getInput("\n\tYou must enter a number between $minimumGameSize and $maximumGameSize: ")

        //issue warning for game sizes larger than 15
        if (userInput!!.toInt() > 15) {
            println("\n\t!!WARNING!!\n\t!!WARNING!!  Games large than 15 will not display correctly if console width is restricted to 80 col (neither will this message)\n\t!!WARNING!!")
            getInput("")
        }
        val gameSize: Int = userInput!!.toInt()

        //Create a new Game instance
        game = Game(gameSize)

        //create an array of two players
        val players = arrayOfNulls<Player>(2)

        //set players to AI or Human depending on game mode
        if (gameMode == 1) {
            players[0] = Player("Human")
            players[1] = Player("AI")
        } else {
            players[0] = Player("AI")
            players[1] = Player("AI")
        }

        //Draw the blank board initially to show user which columns and rows to choose from
        println(game!!.output())

        //until the game is over, go back and forth between players in players array
        //output the game map to the screen after each move
        while (!game!!.finished) {
            for (player in players) {
                player!!.go()
                println("\n${game!!.output()}")
                count += 1
                if (game!!.finished) break
            }
        }

        //output an ending message to the game
        if (game!!.draw) println("\n\tCat's game!")

        //count variable from earlier is used to decide who went last and therefore won.
        else { if (count % 2 == 1) println("\n\tX's win!") else println("\n\tO's win!") }
    }

    //encapsulated code for input stream buffer
    fun getInput(prompt: String?): String {
        val stdin = BufferedReader(InputStreamReader(System.`in`))
        print(prompt)
        System.out.flush()
        return try {
            stdin.readLine()
        } catch (e: Exception) {
            "Error: " + e.message
        }
    }

    //validates user input and sets the game mode
    private fun gameMode(userInput: String?) {
        var userInputVariable = userInput
        valid_input = false
        while (!valid_input) if (userInputVariable!!.length == 1 && userInputVariable.substring(0, 1).matches(Regex("[1-2]"))) valid_input = true else userInputVariable = getInput("\n\tYou must enter '1' or '2' for the game mode: ")

        //Set user input to gameMode for use later
        gameMode = userInputVariable!!.toInt()
    }
}