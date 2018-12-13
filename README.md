# MorphologicalAnalysis
For Developers
============

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called MorphologicalAnalysis will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/MorphologicalAnalysis.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `MorphologicalAnalysis/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 

See the snapshot of the project at the beginning:

// put the link of ss

![Main IDE page](https://github.com/master/dev/site/images/zemberek-ide-main.png)

## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run Morphological Analysis.

**From Console**

Go to `MorphologicalAnalysis` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `MorphologicalAnalysis` root module.

// add ss
![IDE Maven Package](https://github.com/master/dev/site/images/zemberek-maven-package.png)

**From Console**

Use below line to generate jar file:

     mvn install



------------------------------------------------

Morphological Analysis
============
* [Morphology](#morphology)
    + [Maven Usage](#maven-usage)
	+ [Creating FsmMorphologicalAnalyzer](#creating-fsmmorphologicalanalyzer)

## Morphology
Turkish is one of the morphologically rich languages due to its agglutinative nature. Morphological Analysis repository provides a two-level morphological analyzer for Turkish which consists of finite state transducer, rule engine for suffixation, and lexicon.

### Maven Usage

    <groupId>NlpToolkit</groupId>
    <artifactId>MorphologicalAnalysis</artifactId>
    <version>1.0.1</version>
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <repositories>
        <repository>
            <id>NlpToolkit</id>
            <url>http://haydut.isikun.edu.tr:8081/artifactory/NlpToolkit</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Corpus</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>DataStructure</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Dictionary</artifactId>
            <version>1.0.1</version>
        </dependency>
    </dependencies>

## Creating FsmMorphologicalAnalyzer 

FsmMorphologicalAnalyzer provides Turkish morphological analysis. Analysis can be done in word and sentence level. This class can be created as follows:

    FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
    
This generates a new `TxtDictionary` type dictionary from [`turkish_dictionary.txt`](https://github.com/olcaytaner/Dictionary/tree/master/src/main/resources) with fixed cache size 100000 and by using [`turkish_finite_state_machine.xml`](https://github.com/olcaytaner/MorphologicalAnalysis/tree/master/src/main/resources). 

Creating a morphological analyzer with different cache size, dictionary or finite state machine is also possible. 
* With different cache size, 

        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(50000)   

* Using a different dictionary,

        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("my_turkish_dictionary.txt")    

* Specifying both finite state machine and dictionary, 

        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("fsm.xml", "my_turkish_dictionary.txt")       
    
* Giving finite state machine and cache size with creating `TxtDictionary` object, 
        
        TxtDictionary dictionary = new TxtDictionary("my_turkish_dictionary.txt", new TurkishWordComparator())
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("fsm.xml", dictionary, 50000) 
    
* With different finite state machine and creating `TxtDictionary` object,
       
        TxtDictionary dictionary = new TxtDictionary("my_turkish_dictionary.txt", new TurkishWordComparator())
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("fsm.xml", dictionary)