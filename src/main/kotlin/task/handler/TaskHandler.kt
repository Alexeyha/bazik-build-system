package task.handler

import task.Task

class TaskHandler {

    companion object {

        /**
         * Build task and find all files in dependencies.
         *
         * @param name
         * @param taskType
         * @param action
         * @param srcs
         * @param deps
         * @return pair of task and list of file paths to the files which must be parsed due to dependencies
         */
        fun buildTaskAndDepsPaths(
            name: String,
            taskType: Task.Companion.TaskType,
            action: String,
            srcs: List<String> = emptyList(),
            deps: List<String>,
            output: String
        ) : Pair<Task, List<String>> {
            val depsList = mutableListOf<Task.Dependency>()
            val depsPathList = mutableListOf<String>()
            for (dependency in deps) {
                val parsedDependency = parseDependency(dependency)
                depsList.add(parsedDependency)
                if (parsedDependency.path != "") depsPathList.add(parsedDependency.path)
            }
            return Pair(Task(name, taskType, action, srcs, depsList, output), depsPathList)
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
