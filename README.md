Following are the commands and the instructions to run ANT on the project:
#### Note: build.xml is present in src folder.

To clean: ant -buildfile src/build.xml clean

Description: It cleans up all the .class files that are generated when we
compile the code.

To compile the code: ant -buildfile src/build.xml all

Description: Compiles your code and generates .class files inside the BUILD folder.

To run the code: ant -buildfile src/build.xml run  -Darg0="sample_input.txt" -Darg1="output.txt" 

Note: Arguments accept the absolute path of the files.

Algorithm:

The observers update their state i.e. their instance variables when the subject make some change and notify them all. Trie was used to store ServiceName and ServiceInstance pair.




