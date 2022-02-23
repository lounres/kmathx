# kmathx
Extra (mathematical) objects and algorithms for KMath.

Mostly this is migration of my [simple math library](https://github.com/lounres/kotlin-test/tree/master/src/main/kotlin/math)
on KMath support and ideology

Also the development is going only for JVM platform because of context receivers feature of Kotlin

## Modules

---

* ### [help](help)
>
> **Features**
> - [common](help/src/commonMain/kotlin/space/kscience/kmath/common) : Common useful utilities
> - [operations](help/src/jvmMain/kotlin/space/kscience/kmath/operations) : Utilities for algebraic structures
> 
> **Status.**
> The module is used for containing the most contextless utilities (like operations with collection). Hence, it
> does not contain a lot of logic, and it's content may be changed any moment.

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