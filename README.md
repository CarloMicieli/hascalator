![Logo](img/logo.png)  
(Logo design by *Sandro Stucky* - retrivied from the Martin Odersky's talk _The Evolution of Scala_, 2015).

[![Build Status](https://travis-ci.org/CarloMicieli/hascalator.png?branch=master)](https://travis-ci.org/CarloMicieli/hascalator)
[![Coverage Status](https://coveralls.io/repos/github/CarloMicieli/hascalator/badge.svg?branch=master)](https://coveralls.io/github/CarloMicieli/hascalator?branch=master)
[![Dependencies](https://app.updateimpact.com/badge/763721648812724224/hascalator.svg?config=compile)](https://app.updateimpact.com/latest/763721648812724224/hascalator)

This project is just "for fun". There are way better implementations for immutable data structures and typeclasses.

Given the differences between Scala and Haskell, this is not complete nor an exact copy of the Haskell prelude. Almost everywhere the _dot notation_ has been preferred (ie `type.function`). Moreover, in order to help the Scala type inference the order of function parameters has been changed to improve the API usage.

The library is currently implementing the following types:

* `Data.List`
* `Data.Maybe`
* `Data.Either`

and the following typeclasses:

* `Show`
* `Eq`
* `Ord`
* `Num`
* `Integral`
* `Fractional`
* `Enum`

## Layout ##

The project has the following layout:

```
+ src
--+ main
----+ scala
------+ io
---------+ hascalator
------------+ Prelude.scala   // the entry point
------------+ data            // basic data types (ie Maybe, Either, List...)
------------+ dst             // immutable, purely function data structures
------------+ math            // math types
------------+ typeclasses     // typeclasses
```

## References ##

* Paul Chiusano, Rúnar Bjarnason. 2014. __Functional Programming in Scala__. Manning Publications
* Martin Odersky, Lex Spoon, and Bill Venners. 2016. __Programming in Scala, Third Edition__. Artima
* Larry LIU Xinyu. 2014. __Elementary Algorithms__. Retrived from [here](https://github.com/liuxinyu95/AlgoXY/releases/download/v0.618033/elementary-algorithms.pdf)

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License ##

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
