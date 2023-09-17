package parser

import task.Task
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.io.path.createTempFile

class ParserJsonTest {
    private val parserJson = ParserJson()

    private val jsonData = """
        {
          "tasks": [
            {
              "name" : "compile_1",
              "task_type" : "compile",
              "action" : "cd bazik_build",
              "srcs" : [],
              "deps" : ["build.json:compile", "build.json:run"]
            },
            {
              "name" : "run_1",
              "task_type" : "script",
              "action" : "mkdir bazik_build_1",
              "srcs" : ["Class.kt"],
              "deps" : [":compile_1"]
            }
          ]
        }
    """.trimIndent()

    @Test
    fun testParse() {
        val jsonFile = createTempFile("test_json_file", ".json").toFile()
        jsonFile.writeText(jsonData)

        try {
            val expectedTasks = listOf(
                Task("compile_1", Task.Companion.TaskType.BINARY, "cd bazik_build", emptyList(),
                    listOf(Task.Dependency("compile", "build.json"),
                        Task.Dependency("run", "build.json"))),
                Task("run_1", Task.Companion.TaskType.SCRIPT, "mkdir bazik_build_1", listOf("Class.kt"),
                    listOf(Task.Dependency("compile_1", ""))
                )
            )
            val expectedDeps = setOf("build.json")
            val (outputTasks, outputsDeps) = parserJson.parse(jsonFile.absolutePath)

            assertEquals(expectedTasks.size, outputTasks.size)
            for (i in outputTasks.indices) {
                assertEquals(expectedTasks[i].name, outputTasks[i].name)
                assertEquals(expectedTasks[i].taskType, outputTasks[i].taskType)
                assertEquals(expectedTasks[i].action, outputTasks[i].action)
                assertEquals(expectedTasks[i].srcs, outputTasks[i].srcs)
                assertEquals(expectedTasks[i].deps, outputTasks[i].deps)
            }
            assertEquals(expectedDeps, outputsDeps)

        } finally {
            jsonFile.delete()
        }
    }
}