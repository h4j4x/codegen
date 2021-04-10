# CodeGen
![Gradle build](https://github.com/h4j4x/codegen/actions/workflows/gradle.yml/badge.svg)
![Pre release](https://github.com/h4j4x/codegen/actions/workflows/pre-release.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.h4j4x.codegen/cli?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.h4j4x.codegen)
[![javadoc](https://javadoc.io/badge2/io.github.h4j4x.codegen/lib/Lib%20javadoc.svg)](https://javadoc.io/doc/io.github.h4j4x.codegen/lib)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/7e7ed58d5ed846d0aa15e415da4443cc)](https://www.codacy.com/gh/h4j4x/codegen/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=h4j4x/codegen&amp;utm_campaign=Badge_Grade)

Generate code from [Freemarker](https://freemarker.apache.org/docs/dgui_quickstart.html) templates and [JSON](https://www.json.org/json-en.html) data with [CSV](https://en.wikipedia.org/wiki/Comma-separated_values) support.

## How to use it?
### From terminal:
Download cli [latest version](https://github.com/h4j4x/codegen/releases) and unzip it. From the root folder of application, you should see a folder structure similar to the following:
```
├── bin
│   └── cli
│   └── cli.bat
├── lib
│   └── ...
```
Execute according your operating system.
```
$ bin/cli \
    --verbose \
    --overwrite \
    --read-recursive \
    -d DATA_FOLDER \
    -t TEMPLATES_FOLDER \
    -o OUTPUT_FOLDER
```
### From [gradle](https://gradle.org/) script:
Create `configuration`:
```groovy
configurations {
    codegen
}
```
Declare `dependencies`:
```groovy
dependencies {
    codegen (
        "io.github.h4j4x.codegen:cli:${latestVersion}",
        "io.github.h4j4x.codegen:lib:${latestVersion}",
    )
}
```
Create gradle task:
```groovy
task codegen {
    description = 'Generate code'
    javaexec {
        classpath = configurations.codegen
        main = 'io.github.h4j4x.codegen.cli.CliApp'
        args = [
            '-d', "$projectDir/src/main/resources/data",
            '-t', "$projectDir/src/main/resources/template",
            '-o', "$projectDir/src/main/java",
        ]
    }
}
```

## Configuration
### `--verbose`
Verbose mode (info, warning and error logs).
### `--overwrite`
Overwrite mode (output files will replace existing ones).
### `--read-recursive`
Data folder and sub-folders will be read for JSON files.
### `-d`
Data folder for generation.
#### How to structure data?
Data must be specified in JSON files ***(.json)*** with following structure:
- With CSV (***test.csv*** inside data folder).
```json
{
  "templates": [{
    "template": "test.ftl",
    "file": "folder/TestClass.java"
  }],
  "csvData": {
    "csvKey": "items",
    "dataKey": "data",
    "fields": ["key", "description"],
    "filePath": "test.csv",
    "data": {
      "generalConfig": "Some config"
    }
  }
}
```
- Without CSV.
```json
{
  "templates": [{
    "template": "test.ftl",
    "file": "folder/TestClass.java"
  }],
  "data": {
    "key": "value"
  }
}
```
### `-t`
Templates folder for generation. Templates must be specified in FTL files ***(.ftl)***.
### `-o`
Output folder for generation. Must be writable.
## License
[![License](https://img.shields.io/github/license/h4j4x/codegen)](https://raw.githubusercontent.com/h4j4x/codegen/main/LICENSE)
