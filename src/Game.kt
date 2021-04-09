class Game(var gridSize: Int) {
    //This is the Game class.  It hold the current state of the game
    //with the help of the Cell class.
    var finished = false
    var draw = false
    private val grid: Array<Cell?> = arrayOfNulls(gridSize * gridSize)

    //checks to see if a win condition has been met and
    //outputs the current game map to the console 
    fun output(): String {
        checkForTicTacToe()
        return drawMap()
    }

    //places an X or an O in a cell ont he game map
    fun setCell(index: Int): Boolean = if (grid[index]!!.empty) { grid[index]!!.placeMark()
            true } else false

    //checks to see if a win condition has been met
    private fun checkForTicTacToe(): Boolean {
        var gridFilled: Boolean
        var rowWin: Boolean
        var columnWin: Boolean
        var diagonalWin: Boolean
        val diagonals = Array(2) { arrayOfNulls<Cell>(gridSize) } //there are only ever two diagonals which complete a tictactoe in a square

        //if every cell is filled, end the game
        gridFilled = true

        (0 until gridSize * gridSize).forEach { if (grid[it]!!.empty) gridFilled = false }


        if (gridFilled) {
            finished = true
            draw = true
        }

        val rows = Array(gridSize) { i -> Array(gridSize){ j -> grid[gridSize * i + j] } }
        val columns = Array(gridSize) { i -> Array(gridSize){ j -> grid[i + gridSize * j] } }

//        for (i in 0 until gridSize) for (j in 0 until gridSize) rows[i][j] = grid[gridSize * i + j]
//        for (i in 0 until gridSize) {
//            for (j in 0 until gridSize) {
//                columns[i][j] = grid[i + gridSize * j]
//            }
//        }
        (0..1).forEach { i-> if (i == 0) (0 until gridSize).forEach { j -> diagonals[i][j] = grid[(gridSize + 1) * j] } else for (j in 0 until gridSize) diagonals[i][j] = grid[(gridSize - 1) * (j + 1)] }


        //if a row has all the same content and isn't empty & if a column has all the same content and isn't empty
        //then the game is over

        rows.forEachIndexed { index, row ->
            //if the row elements are all the same and not empty
            //set finished to true
            rowWin = true

            (0 until row.size - 1).forEach { i -> if (row[i]!!.output() !== row[i + 1]!!.output()) rowWin = false
                for (j in 0 until row.size - 1) if (row[i]!!.empty) rowWin = false
            }

            if (rowWin) {
                finished = true
                draw = false
            }

            //if the column elements are all the same and not empty
            //set finished to true
            columnWin = true

            (0 until columns[index].size - 1).forEach { i-> if (columns[index][i]!!.output() !== columns[index][i + 1]!!.output()) columnWin = false
                for (j in 0 until columns[index].size - 1) if (columns[index][i]!!.empty) columnWin = false
            }

            if (columnWin) {
                finished = true
                draw = false
            }
        }

        //if a diagonal has all the same content and isnt empty
        //then the game is over
        diagonals.forEach { diagonal ->

            //if the diagonal elements are all the same and not empty
            //set finished to true
            diagonalWin = true
            (0 until diagonal.size - 1).forEach { i -> if (diagonal[i]!!.output() !== diagonal[i + 1]!!.output()) diagonalWin = false
                for (j in 0 until diagonal.size - 1) if (diagonal[i]!!.empty) diagonalWin = false
            }
            if (diagonalWin) {
                finished = true
                draw = false
            }
        }
        return finished
    }

    //draws the current game state in perfect proportion
    private fun drawMap(): String {
        var top = "\t\t  "
        var fill = "\t\t    "
        var divider = "\t\t ---"
        var meat = "\t\t"
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var map = "\n"
        (1 until gridSize).forEach { i-> top += "$i  "
            if (i < 9) top += " "
            fill += "|   "
            divider += "+---"
        }
        top += "$gridSize \n"
        fill += "\n"
        divider += "\n"
        map += top + fill
        (1..1).forEach { row ->
            (1..1).forEach { column ->
                meat += alphabet.substring(row - 1, row) + " " + grid[3 * (row - 1) + (column - 1)]!!.output()
                (2 until gridSize + 1).forEach { i -> meat += " | " + grid[3 * (row - 1) + (i - 1)]!!.output() }
            }
            meat += "\n"
        }
        map += meat + fill
        (2 until gridSize + 1).forEach { row ->
            map += divider
            map += fill
            (1..1).forEach { column ->
                meat = "\t\t" + alphabet.substring(row - 1, row) + " " + grid[gridSize * (row - 1) + (column - 1)]!!.output()
                (column + 1 until gridSize + 1).forEach { i ->  meat += " | " + grid[gridSize * (row - 1) + (i - 1)]!!.output() }
            }
            map += meat + "\n" + fill
        }
        return map
    }

    //constructor.  takes integer and generates a new Game with given size
    init { for (i in grid.indices) grid[i] = Cell() }
}