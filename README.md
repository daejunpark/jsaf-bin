# JavaScript Program Normalizer for [KJS](https://github.com/kframework/javascript-semantics)

For a given JavaScript program,
this tool carries out several normalizations such as:
* Automatic semicolon insertion
* Floating point representation normalization
* Empty statement normalization
* Empty array normalization

This tool is built upon [SAFE framework](https://github.com/sukyoung/safe),
and highly customized for [KJS](https://github.com/kframework/javascript-semantics),
an executable formal semantics of JavaScript.

To reproduce this binary:
```
$ git clone https://github.com/daejunpark/jsaf.git
$ cd jsaf
$ wget http://cs.nyu.edu/rgrimm/xtc/xtc.jar
$ mv xtc.jar bin/
$ export JS_HOME=`pwd`
$ export JAVA_HOME=<PATH-TO-JDK-1.6-OR-1.7>
$ ./ant clean compile
```
