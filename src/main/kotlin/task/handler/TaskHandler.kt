package task.handler

import task.Task

class TaskHandler {

    companion object {

        fun buildTaskAndDepsPaths(
            name: String,
            taskType: Task.Companion.TaskType,
            action: String,
            srcs: List<String> = emptyList(),
            deps: List<String>
        ) : Pair<Task, List<String>> {
            val depsList = mutableListOf<Task.Dependency>()
            val depsPathList = mutableListOf<String>()
            for (dependency in deps) {
                val parsedDependency = parseDependency(dependency)
                depsList.add(parsedDependency)
                if (parsedDependency.path != "") depsPathList.add(parsedDependency.path)
            }
            return Pair(Task(name, taskType, action, srcs, depsList), depsPathList)
        }

        private fun parseDependency(dependency: String) : Task.Dependency {
            if (!dependency.contains(":")) {
                throw IllegalArgumentException("Task's dependency must contain ':' symbol")
            }
            val (pathName, taskName) = dependency.split(":")
            return Task.Dependency(taskName, pathName)
        }

    }
}
