package space.kscience.kmath.functions

import space.kscience.kmath.operations.*
import space.kscience.kmath.common.*
import space.kscience.kmath.numberTheory.BezoutIdentityWithGCD
import kotlin.contracts.*
import kotlin.math.max

// TODO: Docs

// region Sort of legacy

// region Constants

// TODO: Reuse underlying ring extensions

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> C.isZero() : Boolean = ring { this@isZero.isZero<C>() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> C.isNotZero() : Boolean = ring { this@isNotZero.isNotZero<C>() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> C.isOne() : Boolean = ring { this@isOne.isOne<C>() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> C.isNotOne() : Boolean = ring { this@isNotOne.isNotOne<C>() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> C.isMinusOne() : Boolean = ring { this@isMinusOne.isMinusOne<C>() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> C.isNotMinusOne() : Boolean = ring { this@isNotMinusOne.isNotMinusOne<C>() }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> C.times(other: Int): C = ring { this@times.times<C>(other) }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> Int.times(other: C): C = ring { this@times.times<C>(other) }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> C.plus(other: Int): C = ring { this@plus.plus<C>(other) }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> Int.plus(other: C): C = ring { this@plus.plus<C>(other) }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> C.minus(other: Int): C = ring { this@minus.minus<C>(other) }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> Int.minus(other: C): C = ring { this@minus.minus<C>(other) }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> numberConstant(value: Int): C = ring { number<C>(value) }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> multiplyWithPower(base: C, arg: C, pow: UInt): C = ring { multiplyWithPower<C>(base, arg, pow) }

// endregion

// region Polynomials

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.isZero() : Boolean = coefficients.all { it.isZero() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.isNotZero() : Boolean = coefficients.any { it.isNotZero() }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.isOne() : Boolean =
    with(coefficients) { isNotEmpty() && anyIndexed { index, c -> if (index == 0) c.isOne() else c.isZero() } }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.isNotOne() : Boolean = !isOne()

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.isMinusOne() : Boolean =
    with(coefficients) { isNotEmpty() && anyIndexed { index, c -> if (index == 0) c.isMinusOne() else c.isZero() } }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.isNotMinusOne() : Boolean = !isMinusOne()

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> UnivariatePolynomial<C>.times(other: Int): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        coefficients
            .map { it * other }
    )

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> Int.times(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        other.coefficients
            .map { this * it }
    )

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> UnivariatePolynomial<C>.plus(other: Int): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        with(coefficients) {
            if (isEmpty()) listOf(ring.zero + other)
            else
                toMutableList()
                    .apply { this[0] = this[0] + other } // TODO: Strange bug of compiler
        }
    )

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> Int.plus(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        with(other.coefficients) {
            if (isEmpty()) listOf(ring.zero + this@plus)
            else
                toMutableList()
                    .apply { this[0] = this[0] + this@plus } // TODO: Strange bug of compiler
        }
    )

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> UnivariatePolynomial<C>.minus(other: Int): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        with(coefficients) {
            if (isEmpty()) listOf(ring.zero + other)
            else
                toMutableList()
                    .apply { this[0] = this[0] - other }
        }
    )

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Ring<C>> Int.minus(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        with(other.coefficients) {
            if (isEmpty()) listOf(ring.zero + this@minus)
            else
                toMutableList()
                    .apply { this[0] = this[0] - this@minus }
        }
    )

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> number(value: Int): UnivariatePolynomial<C> = ring { UnivariatePolynomial(listOf(number<C>(value))) }

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> multiplyWithPower(base: UnivariatePolynomial<C>, arg: UnivariatePolynomial<C>, pow: UInt): UnivariatePolynomial<C> =
    when {
        arg.isZero() && pow > 0U -> base
        arg.isOne() -> base
        arg.isMinusOne() -> if (pow % 2U == 0U) base else -base
        else -> multiplyWithPowerInternalLogic(base, arg, pow)
    }

// Trivial but slow as duck
context(UnivariatePolynomialSpace<C, A>)
internal tailrec fun <C, A: Ring<C>> multiplyWithPowerInternalLogic(base: UnivariatePolynomial<C>, arg: UnivariatePolynomial<C>, exponent: UInt): UnivariatePolynomial<C> =
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
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.removeZeros() : UnivariatePolynomial<C> =
    if (degree > -1) UnivariatePolynomial(coefficients.subList(0, degree + 1)) else zero

@OptIn(ExperimentalContracts::class)
inline fun <C, A : Ring<C>, R> A.univariatePolynomialSpace(block: UnivariatePolynomialSpace<C, A>.() -> R): R {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return UnivariatePolynomialSpace(this).block()
}

// endregion

// region String representations

/**
 * Represents the polynomial as a [String] where name of the variable is [withVariableName].
 * Consider that monomials are sorted in lexicographic order.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.represent(withVariableName: String = UnivariatePolynomial.defaultVariableName): String =
    coefficients
        .asSequence()
        .withIndex()
        .filter { it.value != ring.zero }
        .map { (index, t) ->
            when (index) {
                0 -> "$t"
                1 -> when (t) {
                    ring.one -> withVariableName
                    -ring.one -> "-$withVariableName"
                    else -> "$t $withVariableName"
                }
                else -> when (t) {
                    ring.one -> "$withVariableName^$index"
                    -ring.one -> "-$withVariableName^$index"
                    else -> "$t $withVariableName^$index"
                }
            }
        }
        .joinToString(separator = " + ") { it }
        .ifEmpty { "0" }

/**
 * Represents the polynomial as a [String] where name of the variable is [withVariableName]
 * and with brackets around the string if needed (i.e. when there are at least two addends in the representation).
 * Consider that monomials are sorted in lexicographic order.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.representWithBrackets(withVariableName: String = UnivariatePolynomial.defaultVariableName): String =
    with(represent(withVariableName)) { if (coefficients.atMost(1) { it != ring.zero }) this else "($this)" }

/**
 * Represents the polynomial as a [String] where name of the variable is [withVariableName].
 * Consider that monomials are sorted in **reversed** lexicographic order.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.representReversed(withVariableName: String = UnivariatePolynomial.defaultVariableName): String =
    coefficients
        .asSequence()
        .withIndex()
        .filter { it.value != ring.zero }
        .toList()
        .asReversed()
        .asSequence()
        .map { (index, t) ->
            when (index) {
                0 -> "$t"
                1 -> when (t) {
                    ring.one -> withVariableName
                    -ring.one -> "-$withVariableName"
                    else -> "$t $withVariableName"
                }
                else -> when (t) {
                    ring.one -> "$withVariableName^$index"
                    -ring.one -> "-$withVariableName^$index"
                    else -> "$t $withVariableName^$index"
                }
            }
        }
        .joinToString(separator = " + ") { it }
        .ifEmpty { "0" }

/**
 * Represents the polynomial as a [String] where name of the variable is [withVariableName]
 * and with brackets around the string if needed (i.e. when there are at least two addends in the representation).
 * Consider that monomials are sorted in **reversed** lexicographic order.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.representReversedWithBrackets(withVariableName: String = UnivariatePolynomial.defaultVariableName): String =
    with(representReversed(withVariableName)) { if (coefficients.exactly(1) { it != ring.zero }) this else "($this)" }

// endregion

// region Polynomial substitution and functional representation

fun <C> UnivariatePolynomial<C>.substitute(ring: Ring<C>, arg: C): C = ring {
    if (coefficients.isEmpty()) return zero
    var result: C = coefficients.last()
    for (j in coefficients.size - 2 downTo 0) {
        result = (arg * result) + coefficients[j]
    }
    return result
}

// TODO: Replace with optimisation: the [result] may be unboxed, and all operations may be performed as soon as
//  possible on it
fun <C> UnivariatePolynomial<C>.substitute(ring: Ring<C>, arg: UnivariatePolynomial<C>) : UnivariatePolynomial<C> = ring.univariatePolynomialSpace {
    if (coefficients.isEmpty()) return zero
    var result: UnivariatePolynomial<C> = coefficients.last().asUnivariatePolynomial()
    for (j in coefficients.size - 2 downTo 0) {
        result = (arg * result) + coefficients[j]
    }
    return result
}

// TODO: Substitute rational function

fun <C, A : Ring<C>> UnivariatePolynomial<C>.asFunctionOver(ring: A): (C) -> C = { substitute(ring, it) }

fun <C, A : Ring<C>> UnivariatePolynomial<C>.asPolynomialFunctionOver(ring: A): (UnivariatePolynomial<C>) -> UnivariatePolynomial<C> = { substitute(ring, it) }

// endregion

// region Operator extensions

// region Field case

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Field<C>> C.div(other: C) : C = ring { this@div / other }

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Field<C>> UnivariatePolynomial<C>.div(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> {
    if (other.isZero()) throw ArithmeticException("/ by zero")

    val thisCoefficients = coefficients/*.subList(0, degree + 1)*/.toMutableList()
    val otherCoefficients = other.coefficients.subList(0, other.degree + 1)
    val quotientCoefficients = ArrayList<C>()

    for (i in (thisCoefficients.size - otherCoefficients.size) downTo  0) {
        val quotient = thisCoefficients[otherCoefficients.lastIndex + i] / otherCoefficients.last()
        quotientCoefficients.add(quotient)
        otherCoefficients.forEachIndexed { index, t -> thisCoefficients[index + i] -= t * quotient }
    }

    return UnivariatePolynomial(quotientCoefficients, reverse = true)
}

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Field<C>> UnivariatePolynomial<C>.div(other: C): UnivariatePolynomial<C> =
    UnivariatePolynomial(
        coefficients
//            .subList(0, degree + 1)
            .map { it / other }
    )

context(UnivariatePolynomialSpace<C, A>)
operator fun <C, A: Field<C>> UnivariatePolynomial<C>.rem(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> {
    if (other.isZero()) throw ArithmeticException("/ by zero")

    val thisCoefficients = coefficients/*.subList(0, degree + 1)*/.toMutableList()
    val otherCoefficients = other.coefficients.subList(0, other.degree + 1)

    for (i in (thisCoefficients.size - otherCoefficients.size) downTo 0) {
        val quotient = thisCoefficients[otherCoefficients.lastIndex + i] / otherCoefficients.last()
        otherCoefficients.forEachIndexed { index, t -> thisCoefficients[index + i] -= t * quotient }
    }

    return with(thisCoefficients) { UnivariatePolynomial(subList(0, max(this.indexOfLast { it.isNotZero() }, 0) + 1)) }
}

context(UnivariatePolynomialSpace<C, A>)
infix fun <C, A: Field<C>> UnivariatePolynomial<C>.divrem(other: UnivariatePolynomial<C>): UnivariatePolynomial.Companion.DividingResult<C> {
    if (other.isZero()) throw ArithmeticException("/ by zero")

    val thisCoefficients = coefficients.subList(0, degree + 1).toMutableList()
    val otherCoefficients = other.coefficients.subList(0, other.degree + 1)
    val quotientCoefficients = ArrayList<C>()

    for (i in (thisCoefficients.size - otherCoefficients.size) downTo  0) {
        val quotient = thisCoefficients[otherCoefficients.lastIndex + i] / otherCoefficients.last()
        quotientCoefficients.add(quotient)
        otherCoefficients.forEachIndexed { index, t -> thisCoefficients[index + i] -= t * quotient }
    }

    return UnivariatePolynomial.Companion.DividingResult(
        UnivariatePolynomial(quotientCoefficients, reverse = true),
        with(thisCoefficients) { UnivariatePolynomial(subList(0, max(this.indexOfLast { it.isNotZero() }, 0) + 1)) }
    )
}

context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Field<C>> UnivariatePolynomial<C>.toMonicPolynomial() = with(coefficients[degree]) {
    UnivariatePolynomial(coefficients.map { it / this })
}

// endregion

//// region Polynomials
//
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.plus(other: UnivariateRationalFunction<T>) =
//    UnivariateRationalFunction(
//        this * other.denominator + other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.plus(other: Polynomial<T>) = this.toPolynomial() + other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.plus(other: RationalFunction<T>) =
//    RationalFunction(
//        this.toPolynomial() * other.denominator + other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.plus(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() + other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.plus(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.denominator + other.numerator,
//        other.denominator
//    )
//
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.minus(other: UnivariateRationalFunction<T>) =
//    UnivariateRationalFunction(
//        this * other.denominator - other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.minus(other: Polynomial<T>) = this.toPolynomial() - other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.minus(other: RationalFunction<T>) =
//    RationalFunction(
//        this.toPolynomial() * other.denominator - other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.minus(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() - other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.minus(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.denominator - other.numerator,
//        other.denominator
//    )
//
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.times(other: UnivariateRationalFunction<T>) =
//    UnivariateRationalFunction(
//        this * other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.times(other: Polynomial<T>) = this.toPolynomial() * other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.times(other: RationalFunction<T>) =
//    RationalFunction(
//        this.toPolynomial() * other.numerator,
//        other.denominator
//    )
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.times(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() * other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.times(other: LabeledRationalFunction<T>) =
//    LabeledRationalFunction(
//        this.toLabeledPolynomial() * other.numerator,
//        other.denominator
//    )
//
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.div(other: UnivariateRationalFunction<T>) =
//    UnivariateRationalFunction(
//        this * other.denominator,
//        other.numerator
//    )
//operator fun <T: Field<T>> UnivariatePolynomial<T>.div(other: Polynomial<T>) = this.toPolynomial() / (other)
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.div(other: RationalFunction<T>) =
//    RationalFunction(
//        this.toPolynomial() * other.denominator,
//        other.numerator
//    )
//operator fun <T: Field<T>> UnivariatePolynomial<T>.div(other: LabeledPolynomial<T>) = this.toLabeledPolynomial() / other
//operator fun <T: Ring<T>> UnivariatePolynomial<T>.div(other: LabeledRationalFunction<T>) =
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
context(UnivariatePolynomialSpace<C, A>)
tailrec fun <C, A: Field<C>> polynomialBinGCD(P: UnivariatePolynomial<C>, Q: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
    if (Q.isZero()) P else polynomialBinGCD(Q, P % Q)

/**
 * Returns GCD (greatest common divider) of polynomials in [pols].
 *
 * Special cases:
 * - If [pols] is empty throws an exception.
 * - If any polynomial is 0 it is ignored. For example `gcd(P, 0) == P`.
 * - If [pols] is contains only zero polynomials) zero polynomial is returned.
 *
 * @param C Field where we are working now.
 * @param pols List of polynomials which GCD is asked.
 * @return GCD of the polynomials.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Field<C>> polynomialGCD(pols: List<UnivariatePolynomial<C>>): UnivariatePolynomial<C> =
    pols.reduce { acc, polynomial ->  polynomialBinGCD(acc, polynomial) } // TODO: Хочу написать красиво, с рефлексией
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Field<C>> polynomialGCD(vararg pols: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
    pols.reduce { acc, polynomial ->  polynomialBinGCD(acc, polynomial) } // TODO: Хочу написать красиво, с рефлексией

/**
 * Computes "the smallest" [Bézout coefficients](https://en.wikipedia.org/wiki/B%C3%A9zout%27s_identity) and
 * [GCD](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Field<C>> bezoutIdentityWithGCD(a: UnivariatePolynomial<C>, b: UnivariatePolynomial<C>): BezoutIdentityWithGCD<UnivariatePolynomial<C>> =
    bezoutIdentityWithGCDInternalLogic(a, b, one, zero, zero, one)

/**
 * Computes "the smallest" [Bézout coefficients](https://en.wikipedia.org/wiki/B%C3%A9zout%27s_identity) and
 * [GCD](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
 *
 * Also assumes that [a] and [b] are non-negative. TODO: Docs
 */
context(UnivariatePolynomialSpace<C, A>)
internal tailrec fun <C, A: Field<C>> bezoutIdentityWithGCDInternalLogic(
    a: UnivariatePolynomial<C>,
    b: UnivariatePolynomial<C>,
    m1: UnivariatePolynomial<C>,
    m2: UnivariatePolynomial<C>,
    m3: UnivariatePolynomial<C>,
    m4: UnivariatePolynomial<C>
): BezoutIdentityWithGCD<UnivariatePolynomial<C>> =
    if (b.isZero()) BezoutIdentityWithGCD(m1, m3, a)
    else {
        val (quotient, reminder) = a divrem b
        bezoutIdentityWithGCDInternalLogic(b, reminder, m2, m1 - quotient * m2, m4, m3 - quotient * m4)
    }
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

// region Derivatives
/**
 * Returns result of applying formal derivative to the polynomial.
 *
 * @param C Field where we are working now.
 * @return Result of the operator.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.derivative() = UnivariatePolynomial(coefficients.mapIndexed { index, t -> t * index }.subList(1, coefficients.size))

/**
 * Returns result of applying discrete derivative to the polynomial with specified [delta].
 *
 * @param C Field where we are working now.
 * @param delta Argument shift in applying of the operator
 * @return Result of the operator.
 */
context(UnivariatePolynomialSpace<C, A>)
fun <C, A: Ring<C>> UnivariatePolynomial<C>.discreteDerivative(delta: C) = this(UnivariatePolynomial(delta, ring.one)) - this
// endregion

// region Resultant

//context(UnivariatePolynomialSpace<T, A>)
//fun <T, A: Ring<T>> resultant(P: UnivariatePolynomial<T>, Q: UnivariatePolynomial<T>) : T {
//    val zero = P.ringZero
//    val pCoefficients = P.coefficients.subList(0, P.degree + 1).reversed()
//    val qCoefficients = Q.coefficients.subList(0, Q.degree + 1).reversed()
//    val pDeg = P.degree
//    val qDeg = Q.degree
//
//    return SquareMatrix(
//        List(qDeg) { List(it) { zero } + pCoefficients + List(qDeg - 1 - it) { zero } } +
//                List(pDeg) { List(it) { zero } + qCoefficients + List(pDeg - 1 - it) { zero } }
//    ).det
//}

// endregion