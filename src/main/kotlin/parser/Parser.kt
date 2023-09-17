package parser

import task.Task

interface Parser {
    /**
     * Parse file.
     *
     * @return pair of list of tasks and set of strings - paths to files, which are needed to parse
     */
    fun parse(filePath: String) : Pair<List<Task>, Set<String>>
}
