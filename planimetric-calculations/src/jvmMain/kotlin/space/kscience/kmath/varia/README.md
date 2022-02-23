## `:planimetric-calculations` : `planimetric-calculations`

The module contains implementation of objects and utilities to help in calculation and calculative analysis of
planimetric constructions.

The goal is to realize simple to use instruments. For example, [the euler line problem](https://en.wikipedia.org/wiki/Euler_line)
may be proven with such code:
```kotlin
// Define vertices of triangle △ABC:
val A by Point
val B by Point
val C by Point

// Compute △ABC's centroid, orthocenter and circumcenter:
val M = centroid(A, B, C)
val H = orthocenter(A, B, C)
val O = circumcenter(A, B, C)

// Check the collinearity of the points M, H and O:
collinearityCondition(A, B, C).isZero()
// Or in another way:
O.isLyingOn(lineThrough(M, H))
```