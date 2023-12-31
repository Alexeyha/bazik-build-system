package parser

import task.Task
import java.io.File
import java.lang.IllegalArgumentException
import org.json.JSONObject
import org.json.JSONArray
import task.handler.TaskHandler

class ParserJson : Parser {
    override fun parse(filePath: String) : Pair<List<Task>, Set<String>> {
        val tasksList = mutableListOf<Task>()
        val depsPathsSet = mutableSetOf<String>()

        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Build file does not exist in the path: $filePath")
        }

        val json = JSONObject(file.readText())
        val tasksJsonArray = json.getJSONArray("tasks")
        for (taskJsonAny in tasksJsonArray) {
            val taskJson = taskJsonAny as JSONObject
            val taskType = Task.getTaskTypeByString(taskJson.getString("task_type"))
            val name = taskJson.getString("name")
            val action = taskJson.getString("action")
            val srcs = parseJsonArrayOfStrings(taskJson.getJSONArray("srcs"))
            val deps = parseJsonArrayOfStrings(taskJson.getJSONArray("deps"))
            val output = taskJson.getString("output")

            val (task, depsPaths) =
                TaskHandler.buildTaskAndDepsPaths(name, taskType, action, srcs, deps, output)

            tasksList.add(task)
            depsPathsSet.addAll(depsPaths)
        }

        return Pair(tasksList, depsPathsSet)
    }

    private fun parseJsonArrayOfStrings(jsonArray: JSONArray) : List<String> {
        val stringList = mutableListOf<String>()
        for (i in 0 ..< jsonArray.length()) {
            stringList.add(jsonArray.getString(i))
        }
        return stringList
    }
}
