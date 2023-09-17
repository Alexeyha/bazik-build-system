package task

import java.lang.IllegalArgumentException

class Task(val name: String,
           val taskType: TaskType,
           val action: String,
           val srcs: List<String> = emptyList(),
           val deps: List<Dependency> = emptyList(),
           val output: String
) {

    companion object {

        enum class TaskType {
            BINARY, TEST, SCRIPT
        }

        fun getTaskTypeByString(type: String) : TaskType {
            val taskType = when (type) {
                "compile" -> TaskType.BINARY
                "test" -> TaskType.TEST
                "script" -> TaskType.SCRIPT
                else -> throw IllegalArgumentException("TaskType does not meet requirements")
            }
            return taskType
        }
    }

    data class Dependency(val taskName: String, val path: String)
}
