# bazik-build-system

## Features
* It is configurated by conf.json file and allows you to set fine-tuning.
* It is allowed to use any number of build files.
* It parse only necessary build files and includes them into graph.
* It has option to render the graph of dependencies.

## How to use

All configuration must be written to the "conf.json" file with the following structure:
```json
{
  "target_task" : "build_1.json:run_1", // Task target, format ["filePath":"task_name"]
  "graph" : {
    "render" : true,                    // Will graph be rendered
    "file" : "graph.txt"                // If "render" == true you must provide file for rendering
  },
  "logger" : { 
    "log" : true,                       // Will logger log something
    "level" : "debug",                  // If yes, provide log level: ["debug", "info", "warnign", "error"]
    "file" : "log.txt"                  // If yes, provide file for the logging
  }
}
```

You can create an arbitrary number of build files, with the following structure:
```json
{
  "tasks": [
    {
      "name" : "compile",          // Task name
      "task_type" : "compile",     // Task type: ["compile", "script", "test"] - unused yet
      "action" : "echo {srcs}",    // script which must be executed, [srcs] will be replaced with srcs and [deps] - with outputs of dependencies tasks
      "srcs" : ["hello", "world"], // srcs, which replace [srcs] in "action"
      "deps" : [],                 // list of dependencies, if they in different build file: use filePath:taskName
      "output" : ""                // output file, used by task, which depends on this one
    },
    {
      "name" : "run",
      "task_type" : "script",
      "action" : "mkdir bazik_build",
      "srcs" : [],
      "deps" : [":compile"],
      "output" : ""
    }
  ...
```

## Dependencies

|Dependency                                            |
|---------------------                                 |
|kotlin("stdlib-jdk8")                                 |
|kotlin("test")                                        |
|kotlin("reflect")                                     |
|org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10      |
|org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1|
|org.json:json:20230227                                |
