package space.kscience.kmath.functions

import space.kscience.kmath.numberTheory.BezoutIdentityWithGCD
import space.kscience.kmath.operations.*
import kotlin.contracts.*


// TODO: Docs

// region Sort of legacy

// region Constants

// TODO: Reuse underlying ring extensions

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> C.isZero() : Boolean = ring { this@isZero.isZero<C>() }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> C.isNotZero() : Boolean = ring { this@isNotZero.isNotZero<C>() }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> C.isOne() : Boolean = ring { this@isOne.isOne<C>() }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> C.isNotOne() : Boolean = ring { this@isNotOne.isNotOne<C>() }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> C.isMinusOne() : Boolean = ring { this@isMinusOne.isMinusOne<C>() }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> C.isNotMinusOne() : Boolean = ring { this@isNotMinusOne.isNotMinusOne<C>() }

context(LabeledPolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> C.times(other: Int): C = ring { this@times.times<C>(other) }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> Int.times(other: C): C = ring { this@times.times<C>(other) }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> C.plus(other: Int): C = ring { this@plus.plus<C>(other) }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> Int.plus(other: C): C = ring { this@plus.plus<C>(other) }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> C.minus(other: Int): C = ring { this@minus.minus<C>(other) }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> Int.minus(other: C): C = ring { this@minus.minus<C>(other) }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
fun <C, A: Ring<C>> numberConstant(value: Int): C = ring { number<C>(value) }

context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> multiplyWithPower(base: C, arg: C, pow: UInt): C = ring { multiplyWithPower<C>(base, arg, pow) }

// endregion

// region Polynomials

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> LabeledPolynomial<C>.isZero() : Boolean = coefficients.isEmpty()

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> LabeledPolynomial<C>.isNotZero() : Boolean = coefficients.isNotEmpty()

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> LabeledPolynomial<C>.isOne() : Boolean =
    with(coefficients) { size == 1 && entries.first().let { (key, value) -> key.isEmpty() && value.isOne() } }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> LabeledPolynomial<C>.isNotOne() : Boolean = !isOne<C, A>()

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> LabeledPolynomial<C>.isMinusOne() : Boolean =
    with(coefficients) { size == 1 && entries.first().let { (key, value) -> key.isEmpty() && value.isMinusOne() } }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
inline fun <C, A: Ring<C>> LabeledPolynomial<C>.isNotMinusOne() : Boolean = !isMinusOne<C, A>()

context(LabeledPolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> LabeledPolynomial<C>.times(other: Int): LabeledPolynomial<C> =
    if (other == 0) zero
    else LabeledPolynomial<C>(
        coefficients
            .asSequence()
            .map { (degs, c) -> degs to c * other }
            .filter { (_, c) -> c.isNotZero() }
            .toMap()
    )

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> Int.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
    if (this == 0) zero
    else LabeledPolynomial<C>(
        other.coefficients
            .asSequence()
            .map { (degs, c) -> degs to this@times * c }
            .filter { (_, c) -> c.isNotZero() }
            .toMap()
    )

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> LabeledPolynomial<C>.plus(other: Int): LabeledPolynomial<C> =
    if (other == 0) this
    else with(coefficients) {
        if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to numberConstant(other)))
        else LabeledPolynomial<C>(
            toMutableMap()
                .apply {
                    if (emptyMap() !in this) this[emptyMap()] = numberConstant(other)
                    else {
                        val res = this[emptyMap()]!! + other
                        if (res.isZero()) remove(emptyMap()) else this[emptyMap()] = res
                    }
                }
        )
    }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> Int.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
    if (this == 0) other
    else with(other.coefficients) {
        if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to numberConstant(this@plus)))
        else LabeledPolynomial<C>(
            toMutableMap()
                .apply {
                    if (emptyMap() !in this) this[emptyMap()] = numberConstant(this@plus)
                    else {
                        val res = this@plus + this[emptyMap()]!!
                        if (res.isZero()) remove(emptyMap()) else this[emptyMap()] = res
                    }
                }
        )
    }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> LabeledPolynomial<C>.minus(other: Int): LabeledPolynomial<C> =
    if (other == 0) this
    else with(coefficients) {
        if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to numberConstant(other)))
        else LabeledPolynomial<C>(
            toMutableMap()
                .apply {
                    if (emptyMap() !in this) this[emptyMap()] = numberConstant(-other)
                    else {
                        val res = this[emptyMap()]!! - other
                        if (res.isZero()) remove(emptyMap()) else this[emptyMap()] = res
                    }
                }
        )
    }

context(LabeledPolynomialSpace<C, A>)
@Suppress("NOTHING_TO_INLINE")
operator fun <C, A: Ring<C>> Int.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
    if (this == 0) -other
    else with(other.coefficients) {
        if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to numberConstant(this@minus)))
        else LabeledPolynomial<C>(
            toMutableMap()
                .apply {
                    forEach { (degs, c) -> if(degs.isNotEmpty()) this[degs] = -c }
                    if (emptyMap() !in this) this[emptyMap()] = numberConstant(this@minus)
                    else {
                        val res = this@minus - this[emptyMap()]!!
                        if (res.isZero()) remove(emptyMap()) else this[emptyMap()] = res
                    }
                }
        )
    }

context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> number(value: Int): LabeledPolynomial<C> = ring { LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to number<C>(value))) }

context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> multiplyWithPower(base: LabeledPolynomial<C>, arg: LabeledPolynomial<C>, pow: UInt): LabeledPolynomial<C> =
    when {
        arg.isZero() && pow > 0U -> base
        arg.isOne() -> base
        arg.isMinusOne() -> if (pow % 2U == 0U) base else -base
        else -> multiplyWithPowerInternalLogic(base, arg, pow)
    }

// Trivial but slow as duck
context(LabeledPolynomialSpace<C, A>)
internal tailrec fun <C, A: Ring<C>> multiplyWithPowerInternalLogic(base: LabeledPolynomial<C>, arg: LabeledPolynomial<C>, exponent: UInt): LabeledPolynomial<C> =
    when {
        exponent == 0U -> base
        exponent == 1U -> base * arg
        exponent % 2U == 0U -> multiplyWithPowerInternalLogic(base, arg * arg, exponent / 2U)
        exponent % 2U == 1U -> multiplyWithPowerInternalLogic(base * arg, arg * arg, exponent / 2U)
        else -> error("Error in raising ring instant by unsigned integer: got reminder by division by 2 different from 0 and 1")
    }

// endregion

// endregion

// region Utilities

// TODO: Docs
@OptIn(ExperimentalContracts::class)
inline fun <C, A : Ring<C>, R> A.labeledPolynomialSpace(block: LabeledPolynomialSpace<C, A>.() -> R): R {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return LabeledPolynomialSpace(this).block()
}

// endregion

// region String representations


/**
 * Represents the polynomial as a [String] with names of variables substituted with names from [names].
 * Consider that monomials are sorted in lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.represent(names: Map<Variable, String> = emptyMap()): String =
    coefficients.entries
        .sortedWith { o1, o2 -> LabeledPolynomial.monomialComparator.compare(o1.key, o2.key) }
        .asSequence()
        .map { (degs, t) ->
            if (degs.isEmpty()) "$t"
            else {
                when {
                    t.isOne() -> ""
                    t.isMinusOne() -> "-"
                    else -> "$t "
                } +
                        degs
                            .toSortedMap()
                            .filter { it.value > 0U }
                            .map { (variable, deg) ->
                                val variableName = names.getOrDefault(variable, variable.toString())
                                when (deg) {
                                    1U -> variableName
                                    else -> "$variableName^$deg"
                                }
                            }
                            .joinToString(separator = " ") { it }
            }
        }
        .joinToString(separator = " + ") { it }
        .ifEmpty { "0" }

/**
 * Represents the polynomial as a [String] naming variables by [namer].
 * Consider that monomials are sorted in lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.represent(namer: (Variable) -> String): String =
    coefficients.entries
        .sortedWith { o1, o2 -> LabeledPolynomial.monomialComparator.compare(o1.key, o2.key) }
        .asSequence()
        .map { (degs, t) ->
            if (degs.isEmpty()) "$t"
            else {
                when {
                    t.isOne() -> ""
                    t.isMinusOne() -> "-"
                    else -> "$t "
                } +
                        degs
                            .toSortedMap()
                            .filter { it.value > 0U }
                            .map { (variable, deg) ->
                                when (deg) {
                                    1U -> namer(variable)
                                    else -> "${namer(variable)}^$deg"
                                }
                            }
                            .joinToString(separator = " ") { it }
            }
        }
        .joinToString(separator = " + ") { it }
        .ifEmpty { "0" }

/**
 * Represents the polynomial as a [String] with names of variables substituted with names from [names] and with
 * brackets around the string if needed (i.e. when there are at least two addends in the representation).
 * Consider that monomials are sorted in lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.representWithBrackets(names: Map<Variable, String> = emptyMap()): String =
    with(represent(names)) { if (coefficients.count() == 1) this else "($this)" }

/**
 * Represents the polynomial as a [String] naming variables by [namer] and with brackets around the string if needed
 * (i.e. when there are at least two addends in the representation).
 * Consider that monomials are sorted in lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.representWithBrackets(namer: (Variable) -> String): String =
    with(represent(namer)) { if (coefficients.count() == 1) this else "($this)" }

/**
 * Represents the polynomial as a [String] with names of variables substituted with names from [names].
 * Consider that monomials are sorted in **reversed** lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.representReversed(names: Map<Variable, String> = emptyMap()): String =
    coefficients.entries
        .sortedWith { o1, o2 -> -LabeledPolynomial.monomialComparator.compare(o1.key, o2.key) }
        .asSequence()
        .map { (degs, t) ->
            if (degs.isEmpty()) "$t"
            else {
                when {
                    t.isOne() -> ""
                    t.isMinusOne() -> "-"
                    else -> "$t "
                } +
                        degs
                            .toSortedMap()
                            .filter { it.value > 0U }
                            .map { (variable, deg) ->
                                val variableName = names.getOrDefault(variable, variable.toString())
                                when (deg) {
                                    1U -> variableName
                                    else -> "$variableName^$deg"
                                }
                            }
                            .joinToString(separator = " ") { it }
            }
        }
        .joinToString(separator = " + ") { it }
        .ifEmpty { "0" }

/**
 * Represents the polynomial as a [String] naming variables by [namer].
 * Consider that monomials are sorted in **reversed** lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.representReversed(namer: (Variable) -> String): String =
    coefficients.entries
        .sortedWith { o1, o2 -> -LabeledPolynomial.monomialComparator.compare(o1.key, o2.key) }
        .asSequence()
        .map { (degs, t) ->
            if (degs.isEmpty()) "$t"
            else {
                when {
                    t.isOne() -> ""
                    t.isMinusOne() -> "-"
                    else -> "$t "
                } +
                        degs
                            .toSortedMap()
                            .filter { it.value > 0U }
                            .map { (variable, deg) ->
                                when (deg) {
                                    1U -> namer(variable)
                                    else -> "${namer(variable)}^$deg"
                                }
                            }
                            .joinToString(separator = " ") { it }
            }
        }
        .joinToString(separator = " + ") { it }
        .ifEmpty { "0" }

/**
 * Represents the polynomial as a [String] with names of variables substituted with names from [names] and with
 * brackets around the string if needed (i.e. when there are at least two addends in the representation).
 * Consider that monomials are sorted in **reversed** lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.representReversedWithBrackets(names: Map<Variable, String> = emptyMap()): String =
    with(representReversed(names)) { if (coefficients.count() == 1) this else "($this)" }

/**
 * Represents the polynomial as a [String] naming variables by [namer] and with brackets around the string if needed
 * (i.e. when there are at least two addends in the representation).
 * Consider that monomials are sorted in **reversed** lexicographic order.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> LabeledPolynomial<C>.representReversedWithBrackets(namer: (Variable) -> String): String =
    with(representReversed(namer)) { if (coefficients.count() == 1) this else "($this)" }

// endregion

// region Polynomial substitution and functional representation

fun <C> LabeledPolynomial<C>.substitute(ring: Ring<C>, args: Map<Variable, C>): LabeledPolynomial<C> = ring {
    if (coefficients.isEmpty()) return this@substitute
    LabeledPolynomial<C>(
        buildMap {
            coefficients.forEach { (degs, c) ->
                val newDegs = degs.filterKeys { it !in args }
                val newC = degs.entries.asSequence().filter { it.key in args }.fold(c) { acc, (variable, deg) ->
                    multiplyWithPower(acc, args[variable]!!, deg)
                }
                this[newDegs] = if (newDegs in this) this[newDegs]!! + newC else newC
            }
        }
    )
}

// TODO: Replace with optimisation: the [result] may be unboxed, and all operations may be performed as soon as
//  possible on it
@JvmName("substitutePolynomial")
fun <C> LabeledPolynomial<C>.substitute(ring: Ring<C>, arg: Map<Variable, LabeledPolynomial<C>>) : LabeledPolynomial<C> =
    ring.labeledPolynomialSpace {
        if (coefficients.isEmpty()) return zero
        coefficients
            .asSequence()
            .map { (degs, c) ->
                degs.entries
                    .asSequence()
                    .filter { it.key in arg }
                    .fold(LabeledPolynomial(mapOf(degs.filterKeys { it !in arg } to c))) { acc, (index, deg) ->
                        multiplyWithPower(acc, arg[index]!!, deg)
                    }
            }
            .reduce { acc, polynomial -> acc + polynomial } // TODO: Rewrite. Might be slow.
    }

// TODO: Substitute rational function

fun <C, A : Ring<C>> LabeledPolynomial<C>.asFunctionOver(ring: A): (Map<Variable, C>) -> LabeledPolynomial<C> =
    { substitute(ring, it) }

fun <C, A : Ring<C>> LabeledPolynomial<C>.asPolynomialFunctionOver(ring: A): (Map<Variable, LabeledPolynomial<C>>) -> LabeledPolynomial<C> =
    { substitute(ring, it) }

// endregion

// region Operator extensions

//// region Field case
//
//operator fun <T: Field<T>> Polynomial<T>.div(other: Polynomial<T>): Polynomial<T> {
//    if (other.isZero()) throw ArithmeticException("/ by zero")
//    if (isZero()) return this
//
//    fun Map<List<Int>, T>.leadingTerm() =
//        this
//            .asSequence()
//            .map { Pair(it.key, it.value) }
//            .reduce { (accDegs, accC), (listDegs, listC) ->
//                for (i in 0..accDegs.lastIndex) {
//                    if (accDegs[i] > listDegs.getOrElse(i) { 0 }) return@reduce accDegs to accC
//                    if (accDegs[i] < listDegs.getOrElse(i) { 0 }) return@reduce listDegs to listC
//                }
//                if (accDegs.size < listDegs.size) listDegs to listC else accDegs to accC
//            }
//
//    var thisCoefficients = coefficients.toMutableMap()
//    val otherCoefficients = other.coefficients
//    val quotientCoefficients = HashMap<List<Int>, T>()
//
//    var (thisLeadingTermDegs, thisLeadingTermC) = thisCoefficients.leadingTerm()
//    val (otherLeadingTermDegs, otherLeadingTermC) = otherCoefficients.leadingTerm()
//
//    while (
//        thisLeadingTermDegs.size >= otherLeadingTermDegs.size &&
//        (0..otherLeadingTermDegs.lastIndex).all { thisLeadingTermDegs[it] >= otherLeadingTermDegs[it] }
//    ) {
//        val multiplierDegs =
//            thisLeadingTermDegs
//                .mapIndexed { index, deg -> deg - otherLeadingTermDegs.getOrElse(index) { 0 } }
//                .cleanUp()
//        val multiplierC = thisLeadingTermC / otherLeadingTermC
//
//        quotientCoefficients[multiplierDegs] = multiplierC
//
//        for ((degs, t) in otherCoefficients) {
//            val productDegs =
//                (0..max(degs.lastIndex, multiplierDegs.lastIndex))
//                    .map { degs.getOrElse(it) { 0 } + multiplierDegs.getOrElse(it) { 0 } }
//                    .cleanUp()
//            val productC = t * multiplierC
//            thisCoefficients[productDegs] =
//                if (productDegs in thisCoefficients) thisCoefficients[productDegs]!! - productC else -productC
//        }
//
//        thisCoefficients = thisCoefficients.filterValues { it.isNotZero() }.toMutableMap()
//
//        if (thisCoefficients.isEmpty())
//            return Polynomial(quotientCoefficients, toCheckInput = false)
//
//        val t = thisCoefficients.leadingTerm()
//        thisLeadingTermDegs = t.first
//        thisLeadingTermC = t.second
//    }
//
//    return Polynomial(quotientCoefficients, toCheckInput = false)
//}
//
//operator fun <T: Field<T>> Polynomial<T>.div(other: T): Polynomial<T> =
//    if (other.isZero()) throw ArithmeticException("/ by zero")
//    else
//        Polynomial(
//            coefficients
//                .mapValues { it.value / other },
//            toCheckInput = false
//        )
//
//operator fun <T: Field<T>> Polynomial<T>.rem(other: Polynomial<T>): Polynomial<T> {
//    if (other.isZero()) throw ArithmeticException("/ by zero")
//    if (isZero()) return this
//
//    fun Map<List<Int>, T>.leadingTerm() =
//        this
//            .asSequence()
//            .map { Pair(it.key, it.value) }
//            .reduce { (accDegs, accC), (listDegs, listC) ->
//                for (i in 0..accDegs.lastIndex) {
//                    if (accDegs[i] > listDegs.getOrElse(i) { 0 }) return@reduce accDegs to accC
//                    if (accDegs[i] < listDegs.getOrElse(i) { 0 }) return@reduce listDegs to listC
//                }
//                if (accDegs.size < listDegs.size) listDegs to listC else accDegs to accC
//            }
//
//    var thisCoefficients = coefficients.toMutableMap()
//    val otherCoefficients = other.coefficients
//
//    var (thisLeadingTermDegs, thisLeadingTermC) = thisCoefficients.leadingTerm()
//    val (otherLeadingTermDegs, otherLeadingTermC) = otherCoefficients.leadingTerm()
//
//    while (
//        thisLeadingTermDegs.size >= otherLeadingTermDegs.size &&
//        (0..otherLeadingTermDegs.lastIndex).all { thisLeadingTermDegs[it] >= otherLeadingTermDegs[it] }
//    ) {
//        val multiplierDegs =
//            thisLeadingTermDegs
//                .mapIndexed { index, deg -> deg - otherLeadingTermDegs.getOrElse(index) { 0 } }
//                .cleanUp()
//        val multiplierC = thisLeadingTermC / otherLeadingTermC
//
//        for ((degs, t) in otherCoefficients) {
//            val productDegs =
//                (0..max(degs.lastIndex, multiplierDegs.lastIndex))
//                    .map { degs.getOrElse(it) { 0 } + multiplierDegs.getOrElse(it) { 0 } }
//                    .cleanUp()
//            val productC = t * multiplierC
//            thisCoefficients[productDegs] =
//                if (productDegs in thisCoefficients) thisCoefficients[productDegs]!! - productC else -productC
//        }
//
//        thisCoefficients = thisCoefficients.filterValues { it.isNotZero() }.toMutableMap()
//
//        if (thisCoefficients.isEmpty())
//            return Polynomial(thisCoefficients, toCheckInput = false)
//
//        val t = thisCoefficients.leadingTerm()
//        thisLeadingTermDegs = t.first
//        thisLeadingTermC = t.second
//    }
//
//    return Polynomial(thisCoefficients, toCheckInput = false)
//}
//
//infix fun <T: Field<T>> Polynomial<T>.divrem(other: Polynomial<T>): Polynomial.Companion.DividingResult<T> {
//    if (other.isZero()) throw ArithmeticException("/ by zero")
//    if (isZero()) return Polynomial.Companion.DividingResult(this, this)
//
//    fun Map<List<Int>, T>.leadingTerm() =
//        this
//            .asSequence()
//            .map { Pair(it.key, it.value) }
//            .reduce { (accDegs, accC), (listDegs, listC) ->
//                for (i in 0..accDegs.lastIndex) {
//                    if (accDegs[i] > listDegs.getOrElse(i) { 0 }) return@reduce accDegs to accC
//                    if (accDegs[i] < listDegs.getOrElse(i) { 0 }) return@reduce listDegs to listC
//                }
//                if (accDegs.size < listDegs.size) listDegs to listC else accDegs to accC
//            }
//
//    var thisCoefficients = coefficients.toMutableMap()
//    val otherCoefficients = other.coefficients
//    val quotientCoefficients = HashMap<List<Int>, T>()
//
//    var (thisLeadingTermDegs, thisLeadingTermC) = thisCoefficients.leadingTerm()
//    val (otherLeadingTermDegs, otherLeadingTermC) = otherCoefficients.leadingTerm()
//
//    while (
//        thisLeadingTermDegs.size >= otherLeadingTermDegs.size &&
//        (0..otherLeadingTermDegs.lastIndex).all { thisLeadingTermDegs[it] >= otherLeadingTermDegs[it] }
//    ) {
//        val multiplierDegs =
//            thisLeadingTermDegs
//                .mapIndexed { index, deg -> deg - otherLeadingTermDegs.getOrElse(index) { 0 } }
//                .cleanUp()
//        val multiplierC = thisLeadingTermC / otherLeadingTermC
//
//        quotientCoefficients[multiplierDegs] = multiplierC
//
//        for ((degs, t) in otherCoefficients) {
//            val productDegs =
//                (0..max(degs.lastIndex, multiplierDegs.lastIndex))
//                    .map { degs.getOrElse(it) { 0 } + multiplierDegs.getOrElse(it) { 0 } }
//                    .cleanUp()
//            val productC = t * multiplierC
//            thisCoefficients[productDegs] =
//                if (productDegs in thisCoefficients) thisCoefficients[productDegs]!! - productC else -productC
//        }
//
//        thisCoefficients = thisCoefficients.filterValues { it.isNotZero() }.toMutableMap()
//
//        if (thisCoefficients.isEmpty())
//            return Polynomial.Companion.DividingResult(
//                Polynomial(quotientCoefficients, toCheckInput = false),
//                Polynomial(thisCoefficients, toCheckInput = false)
//            )
//
//        val t = thisCoefficients.leadingTerm()
//        thisLeadingTermDegs = t.first
//        thisLeadingTermC = t.second
//    }
//
//    return Polynomial.Companion.DividingResult(
//        Polynomial(quotientCoefficients, toCheckInput = false),
//        Polynomial(thisCoefficients, toCheckInput = false)
//    )
//}
//
//// endregion

//// region Polynomials
//
//operator fun <T: Ring<T>> Polynomial<T>.plus(other: UnivariatePolynomial<T>) = this + other.toPolynomial()
//operator fun <T: Ring<T>> Polynomial<T>.plus(other: UnivariateRationalFunction<T>) =
//    RationalFunction(
//        this * other.denominator.toPolynomial() + other.numerator.toPolynomial(),
//        other.denominator.toPolynomial()
//    )
//operator fun <T: Ring<T>> Polynomial<T>.plus(other: RationalFunction<T>) =
//    RationalFunction(
//        this * other.denominator + other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> Polynomial<T>.plus(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() + other
//operator fun <T: Ring<T>> Polynomial<T>.plus(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.denominator + other.numerator,
//        other.denominator
//    )
//
//operator fun <T: Ring<T>> Polynomial<T>.minus(other: UnivariatePolynomial<T>) = this - other.toPolynomial()
//operator fun <T: Ring<T>> Polynomial<T>.minus(other: UnivariateRationalFunction<T>) =
//    RationalFunction(
//        this * other.denominator.toPolynomial() - other.numerator.toPolynomial(),
//        other.denominator.toPolynomial()
//    )
//operator fun <T: Ring<T>> Polynomial<T>.minus(other: RationalFunction<T>) =
//    RationalFunction(
//        this * other.denominator - other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> Polynomial<T>.minus(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() - other
//operator fun <T: Ring<T>> Polynomial<T>.minus(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.denominator - other.numerator,
//        other.denominator
//    )
//
//operator fun <T: Ring<T>> Polynomial<T>.times(other: UnivariatePolynomial<T>) = this * other.toPolynomial()
//operator fun <T: Ring<T>> Polynomial<T>.times(other: UnivariateRationalFunction<T>) =
//    RationalFunction(
//        this * other.numerator.toPolynomial(),
//        other.denominator.toPolynomial()
//    )
//operator fun <T: Ring<T>> Polynomial<T>.times(other: RationalFunction<T>) =
//    RationalFunction(
//        this * other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> Polynomial<T>.times(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() * other
//operator fun <T: Ring<T>> Polynomial<T>.times(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.numerator,
//        other.denominator
//    )
//
//operator fun <T: Field<T>> Polynomial<T>.div(other: UnivariatePolynomial<T>) = this / other.toPolynomial()
//operator fun <T: Ring<T>> Polynomial<T>.div(other: UnivariateRationalFunction<T>) =
//    RationalFunction(
//        this * other.denominator.toPolynomial(),
//        other.numerator.toPolynomial()
//    )
//operator fun <T: Ring<T>> Polynomial<T>.div(other: RationalFunction<T>) =
//    RationalFunction(
//        this * other.denominator,
//        other.numerator
//    )
//operator fun <T: Field<T>> Polynomial<T>.div(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() / other
//operator fun <T: Ring<T>> Polynomial<T>.div(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.denominator,
//        other.numerator
//    )
//
//// endregion

// endregion

//// region Invocation
///**
// * Returns numerator (polynomial) of rational function gotten by substitution rational function [arg] to the polynomial instance.
// * More concrete, if [arg] is `f(x)/g(x)` and the receiving instance is `p(x)`, then
// * ```
// * p(f/g) * g^deg(p)
// * ```
// * is returned.
// *
// * Used in [UnivariatePolynomial.invoke] and [UnivariateRationalFunction.invoke] for performance optimisation.
// */
//context(UnivariatePolynomialSpace<T>)
//internal infix fun <T: Ring<T>> UnivariatePolynomial<T>.invokeRFTakeNumerator (arg: UnivariateRationalFunction<T>): UnivariatePolynomial<T> =
//    if (isZero()) this
//    else
//        coefficients
//            .asSequence()
//            .withIndex()
//            .filter { it.value.isNotZero() }
//            .map { (index, t) -> multiplyByPower(multiplyByPower(UnivariatePolynomial(t), arg.numerator, index), arg.denominator, degree - index) }
//            .reduce { acc, res -> acc + res }
//// endregion

// region Greatest Common Divisor (GCD) computation
//context(UnivariatePolynomialSpace<C, A>)
//        tailrec fun <C, A: Field<C>> polynomialBinGCD(P: UnivariatePolynomial<C>, Q: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
//    if (Q.isZero()) P else polynomialBinGCD(Q, P % Q)
//
///**
// * Returns GCD (greatest common divider) of polynomials in [pols].
// *
// * Special cases:
// * - If [pols] is empty throws an exception.
// * - If any polynomial is 0 it is ignored. For example `gcd(P, 0) == P`.
// * - If [pols] is contains only zero polynomials) zero polynomial is returned.
// *
// * @param C Field where we are working now.
// * @param pols List of polynomials which GCD is asked.
// * @return GCD of the polynomials.
// */
//context(UnivariatePolynomialSpace<C, A>)
//fun <C, A: Field<C>> polynomialGCD(pols: List<UnivariatePolynomial<C>>): UnivariatePolynomial<C> =
//    pols.reduce { acc, polynomial ->  polynomialBinGCD(acc, polynomial) } // TODO: Хочу написать красиво, с рефлексией
//context(UnivariatePolynomialSpace<C, A>)
//fun <C, A: Field<C>> polynomialGCD(vararg pols: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
//    pols.reduce { acc, polynomial ->  polynomialBinGCD(acc, polynomial) } // TODO: Хочу написать красиво, с рефлексией

// TODO: Rewrite for multivariate polynomials
///**
// * Computes "the smallest" [Bézout coefficients](https://en.wikipedia.org/wiki/B%C3%A9zout%27s_identity) and
// * [GCD](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
// */
//context(UnivariatePolynomialSpace<C, A>)
//fun <C, A: Field<C>> bezoutIdentityWithGCD(a: UnivariatePolynomial<C>, b: UnivariatePolynomial<C>): BezoutIdentityWithGCD<UnivariatePolynomial<C>> =
//    bezoutIdentityWithGCDInternalLogic(a, b, one, zero, zero, one)
//
///**
// * Computes "the smallest" [Bézout coefficients](https://en.wikipedia.org/wiki/B%C3%A9zout%27s_identity) and
// * [GCD](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
// *
// * Also assumes that [a] and [b] are non-negative. TODO: Docs
// */
//context(UnivariatePolynomialSpace<C, A>)
//        internal tailrec fun <C, A: Field<C>> bezoutIdentityWithGCDInternalLogic(
//    a: UnivariatePolynomial<C>,
//    b: UnivariatePolynomial<C>,
//    m1: UnivariatePolynomial<C>,
//    m2: UnivariatePolynomial<C>,
//    m3: UnivariatePolynomial<C>,
//    m4: UnivariatePolynomial<C>
//): BezoutIdentityWithGCD<UnivariatePolynomial<C>> =
//    if (b.isZero()) BezoutIdentityWithGCD(m1, m3, a)
//    else {
//        val (quotient, reminder) = a divrem b
//        bezoutIdentityWithGCDInternalLogic(b, reminder, m2, m1 - quotient * m2, m4, m3 - quotient * m4)
//    }
// endregion

//// region Interpolation
// TODO: This is legacy. Rewrite with new
///**
// * Returns polynomial `P` of minimal degree such that `P(e.key) = e.value` for every `e` in entries of [results].
// *
// * @param T Field where we are working now.
// * @param results Describes values in specified points.
// * @return Interpolated polynomial.
// */
//fun <T: Field<T>> interpolationPolynomial(results: Map<T, T>) =
//    results
//        .let { entries ->
//            if (entries.isEmpty()) UnivariatePolynomial()
//            else entries
//                .map {
//                    (results.keys - it.key)
//                        .run {
//                            if (this.isEmpty()) UnivariatePolynomial(it.value)
//                            else this
//                                .map { p: T -> UnivariatePolynomial(-p, p.getOne()) / (it.key - p) }
//                                .reduce { acc, polynomial -> acc * polynomial } * it.value
//                        }
//                }
//                .reduce { acc, polynomial -> acc + polynomial }
//        }
//fun <T: Field<T>> interpolationPolynomial(vararg results: Pair<T, T>) = interpolationPolynomial(mapOf(*results))
//// endregion