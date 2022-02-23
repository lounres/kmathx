# kmathx
Extra (mathematical) objects and algorithms for KMath.

Mostly this is migration of my [simple math library](https://github.com/lounres/kotlin-test/tree/master/src/main/kotlin/math)
to KMath support and ideology

Also, the development mostly is going only for JVM platform because of context receivers feature of Kotlin

## Modules

---

* ### [help](help)
>
> **Features**
> - [common](help/src/commonMain/kotlin/space/kscience/kmath/common) : Common useful utilities
> - [operations](help/src/jvmMain/kotlin/space/kscience/kmath/operations) : Utilities for algebraic structures
> 
> **Status.**
> The module is used for containing the most contextless utilities (like operations with collection) that
> do not contain a lot of logic, but are very needed and surprisingly (or just unfortunately) are not implemented in
> correspondent library (Kotlin standard library, KMath, etc.).

---

* ### [kmath-discrete](help)
>
> **Features**
> - [number-theory](kmath-discrete/src/commonMain/kotlin/space/kscience/kmath/numberTheory) : Common number theoretical utilities
> - [number-theory-jvm](kmath-discrete/src/jvmMain/kotlin/space/kscience/kmath/numberTheory) : Common number theoretical utilities. JVM specific parts
> - [discrete-structures](kmath-discrete/src/jvmMain/kotlin/space/kscience/kmath/operations) : Some discrete algebraic structures
>
> **Status.**
> The module contains standard (or almost standard) utilities and objects from "discrete math" (number theory, etc.)
> that are not included in main KMath library. Main feature is rational numbers, which are already developed,
> so any other development is frozen.

---

* ### [kmath-functions](help)
>
> **Features**
> - [polynomials](kmath-functions/src/jvmMain/kotlin/space/kscience/kmath/functions) : Support for polynomial extensions
>   of rings and fields with some extra utilities.
>
> **Status.**
> For now basic polynomial support is developed. The extra polynomial utilities and rational functions support will be
> migrated soon.

---

* ### [planimetric-calculations](help)
>
> **Features**
> - planimetric-calculations : Objects and utilities for calculations on planimetric constructions
>
> **Status.**
> Right now no development is started because of waiting for polynomial support to be developed
> 
> **Reference.**
> [Original realization](https://github.com/lounres/kotlin-test/tree/master/src/main/kotlin/math/varia/planimetricsCalculation).
> But the main idea is greatly shown in [utilities](https://github.com/lounres/kotlin-test/blob/master/src/main/kotlin/math/varia/planimetricsCalculation/Util.kt).
> For example, [the euler line problem](https://en.wikipedia.org/wiki/Euler_line) may be proven with such code (in original style):
> ```kotlin
> // Define vertices of triangle △ABC:
> val A by Point
> val B by Point
> val C by Point
> 
> // Compute △ABC's centroid, orthocenter and circumcenter:
> val M = centroid(A, B, C)
> val H = orthocenter(A, B, C)
> val O = circumcenter(A, B, C)
> 
> // Check the collinearity of the points M, H and O:
> collinearityCondition(A, B, C).isZero()
> // Or in another way:
> O.isLyingOn(lineThrough(M, H))
> ```