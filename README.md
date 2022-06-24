## Java PDF Combiner

Combines multiple pdf files into one pdf file

### Usage

#### Package

Package the jar using `mvn package` and run the jar with `jar-with-dependencies` appended to the name.

#### Run with command line

Run using `java -jar [name of jar with dependencies].jar [space separated paths to pdfs] [final combined pdf name]`

Example
`java -jar java-pdf-combiner-1.0-SNAPSHOT-jar-with-dependencies.jar "pdf1.pdf" "pdf2.pdf" ... "final.pdf""`

#### Run with config json file

For more features & control, the program can be run with a json file with the format given below.

##### To run

`java -jar java-pdf-combiner-1.0-SNAPSHOT-jar-with-dependencies.jar config.json`

##### Sample json

```json
{
  "outputFile": "Path to the file/filename.pdf",
  "fileConfigs": [
    {
      "filePath": "Path to pdf to merge/file.pdf",
      "excludePages": [1, 2, 3]
    }
  ]
}
```

| Something here             | Another thing here                                                                                             |
|----------------------------|----------------------------------------------------------------------------------------------------------------|
| outputFile                 | Path to the output combined file. Should be unique as it'll replace any existing file and must end with `.pdf` |
| fileConfigs                | Array of file configurations of the pdf files to be combined with, in order of desired combination.            |
| filePath (fileConfigs)     | Path to the pdf file used in combination                                                                       |
| excludePages (fileConfigs) | Integer array of pages to be excluded. For the particular combined pdf.                                        |