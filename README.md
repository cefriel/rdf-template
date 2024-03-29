⚠️ **REPOSITORY ARCHIVED** Current implementation available in the repository https://github.com/cefriel/mapping-template

# rdf-template

A template-based component exploiting [Apache Velocity](https://velocity.apache.org/) to define declarative mappings.

#### Usage as a Library

The  `mapping-template`  can be used via command line but also as a library through the `TemplateExecutor` class. It allows to execute templates through the filesystem or through `InputStream`s. Configuration examples can be found in the `Main` class and in the `test` folder.

### Documentation
This section contains the documentation to use the tool and to produce compliant templates.

#### Template Default Variables
The `mapping-template` component offers a set of already bound variables that can be used in the template.

##### `$reader` 

The `$reader` variable offers methods to extract data frames accordingly to the provided input.

- If RDF input files are provided, it is bound to `RDFReader` accepting SPARQL queries.
- If CSV input files are provided, it is bound to `CSVReader` automatically generating a data frame from the CSV.
- If XML input files are provided, it is bound to `XMLReader` accepting XQuery queries.
- If JSON input files are provided, it is bound to `JsonReader` accepting JsonPath queries.

##### `$functions`

The `$functions` variable offers a set of utility methods that can be extended defining sub-classes (see the section below). 

Functions to define `Reader`s are:
- `getRDFReaderFromFile(String filename)` and `getRDFReaderFromString(String s)`: returns dynamically a RDFReader from a RDF file or string
- `getXMLReaderFromFile(String filename)` and `getXMLReaderFromString(String s)`: returns dynamically a XMLReader from a XML file or string
- `getJSONReaderFromFile(String filename)` and `getJSONReaderFromString(String s)`: returns dynamically a JSONReader from a JSON file or string
- `getCSVReaderFromFile(String filename)` and `getCSVReaderFromString(String s)`: returns dynamically a CSVReader from a CSV file or string

Utility functions (e.g., for data transformation) are:
- `rp(String s)`: if a prefix is set, removes it from the parameter string. If a prefix is not set, or the prefix is not contained in the given string it returns the string as it is.
- `setPrefix(String prefix)`: set a prefix for the `rp` method.
- `sp(String s, String substring)`: returns the substring of the parameter string after the first occurrence of the parameter substring.
- `p(String s, String substring)`: returns the substring of the parameter string before the first occurrence of the parameter substring.
- `replace(String s, String regex, String replacement)`: returns a string replacing all the occurrences of the regex with the replacement provided.
- `newline()`: returns a newline string.
- `hash(String s)`: returns a string representing the hash of the parameter.
- `checkString(String s)`: returns `true` if the string is not null and not an empty string.

Functions to optimise the access to data frames are:
- `getMap(List<Map<String, String>> results, String key)`: creates a support data structure to access data frames faster. Builds a map associating a single row with its value w.r.t a specified column (key parameter). The assumption is for each row the value for the given column is unique, otherwise, the result will be incomplete.
- `getListMap(List<Map<String, String>> results, String key)`: creates a support data structure to access data frames faster. Builds a map associating a value with all rows having that as value for a specified column (key parameter).
- `checkList(List<T> l)`: returns `true` if the list is not null and not empty.
- `checkList(List<T> l, T o)`: returns `true` if the list is not null, not empty and contains `o`.
- `checkMap(Map<K,V> m)`: returns `true` if the map is not null and not empty.
- `checkMap(Map<K, V> m, K key)`: returns `true` if the map is not null, not empty and contains the key `key`.
- `getMapValue(Map<K, V> map, K key)`: if `checkMap(map, key)` is `true` returns the value for `key` in `map`, otherwise returns `null`. 
- `getListMapValue(Map<K, List<V>> listMap, K key)`: if `checkMap(listMap, key)` is `true` returns the value for `key` in `listMap`, otherwise returns an empty list.
- `mergeResults(List<Map<String,String>> results, List<Map<String,String>> otherResults)`: merge two data frames

##### `$map`
The `$map` variable contains all key-value pairs specified with both `-kv` and `-kvc` options.

#### TemplateUtils subclasses
Subclasses of `TemplateUtils` may be defined and set to modify the utility functions available in processing the template.

#### `mapping-template.jar` ####
This is the intended usage of the `mapping-template.jar`.

```
usage: java -jar mapping-template.jar <options>
options:
  -b, --basepath <arg>            Base path for files (input, template, output). Default value is './'.
  -c, --contextIRI <arg>          IRI identifies the named graph for context-aware querying of the repository. 
                                  Default behaviour: the entire repository is considered for querying.
  -dq, --debug-query              Saves in the output file the result of the query provided with -q option.
  -f, --format <arg>              Activate procedures for specific output formats. Supported values: 'xml' 
                                  (XML parsing to check structure, indentation), 'turtle', 'rdfxml', 'nt'.
  -rdf, --rdf <arg>               Path(s) of input RDF file(s), if no remote repository is specified an 
                                  in-memory repository is initialized and triples are made available for querying.
  -xml, --xml <arg>               Path of input XML file.
  -json, --json <arg>             Path of input JSON file.
  -csv, --csv <arg>               Path of input CSV file.
  -kv, --key-value <arg>          Path for a file containing a key:value pair for each line. These pairs
                                  are made available as a map in the template.
  -kvc, --key-value-csv <arg>     Path for a csv file with one line interpreted as a set of key[column]-value[line] pairs. 
                                  These pairs are made available as a map in the template.
  -o, --output <arg>              Path of output file. Default: output.txt
  -r, --repository <arg>          Repository Id related to the triples store.
  -t, --template <arg>            Path of template file. Default: template.vm
  -tm, --time <arg>               Path of file reporting template execution time. Default: timing not saved. 
  -tr, --trim                     Trim newlines from the template before executing it to reduce memory usage.
  -ts, --ts-address <arg>         Triples store address.
  -v, --verbose                   Debug information are logged.
```
Only one between `-xml`, `-csv`, `-json` and `-rdf` option can be used at once, the `$reader` is initialised accordingly. Additional `Reader`s should be defined in the template using the available functions.

If `-ts` and `-r` options are set a remote repository is used for queries and the `-rdf` option is ignored, if they are not set `-rdf` option is mandatory. Assumptions to use a remote repository are: the triples store is up and running, and triples are already in there.

#### Tips ####
- If it is feasible for the specific case, splitting templates into multiple files and then combining them improves performances. 
- It is better to avoid nested cycles in the template without using support data structures.
- The component can be used as an external library to launch multiple template executions in parallel.
