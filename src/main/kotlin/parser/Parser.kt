package parser

import task.Task

interface Parser {
    fun parse(filePath: String) : Pair<List<Task>, Set<String>>
}