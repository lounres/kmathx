package space.kscience.kmath.functions

import space.kscience.kmath.operations.*
import space.kscience.kmath.common.*
import kotlin.math.max
import kotlin.math.min


/**
 * Represents multivariate polynomials with indexed variables.
 *
 * @param C Ring in which the polynomial is considered.
 * @param coefs Coefficients of the instant.
 * @param toCheckInput If it's `true` cleaning of [coefficients] is executed otherwise it is not.
 *
 * @constructor Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of
 * received lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element
 * in it.
 *
 * @throws EnumeratedPolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
class EnumeratedPolynomial<C>
internal constructor(
    /**
     * Map that collects coefficients of the polynomial. Every non-zero monomial
     * `a x_1^{d_1} ... x_n^{d_n}` is represented as pair "key-value" in the map, where value is coefficients `a` and
     * key is list that associates index of every variable in the monomial with multiplicity of the variable occurring
     * in the monomial. For example polynomial
     * ```
     * 5 x_1^2 x_3^3 - 6 x_2 + 0 x_2 x_3
     * ```
     * has coefficients represented as
     * ```
     * mapOf(
     *      listOf(2, 0, 3) to 5,
     *      listOf(0, 1) to (-6)
     * )
     * ```
     *
     * There is only one special case: if the polynomial is zero, the list contains only one monomial:
     * ```
     * listOf<Int>() to ZERO
     * ```
     * where `ZERO` is the ring zero.
     */
    val coefficients: Map<List<UInt>, C>
) : Polynomial<C> {
    companion object {
        /**
         * Default name of variables used in string representations.
         *
         * @see EnumeratedPolynomial.toString
         */
        var defaultVariableName = "x"

        /**
         * Comparator for lexicographic comparison of monomials.
         */
        val monomialComparator = Comparator<List<UInt>> { o1: List<UInt>?, o2: List<UInt>? ->
                if (o1 == o2) return@Comparator 0
                if (o1 == null) return@Comparator 1
                if (o2 == null) return@Comparator -1

                val countOfVariables = max(o1.size, o2.size)

                for (variable in 0 until countOfVariables) {
                    if (o1.getOrElse(variable) { 0U } > o2.getOrElse(variable) { 0U }) return@Comparator -1
                    if (o1.getOrElse(variable) { 0U } < o2.getOrElse(variable) { 0U }) return@Comparator 1
                }

                return@Comparator 0
            }

        /**
         * Represents result of division with remainder.
         */
        data class DividingResult<C>(
            val quotient: EnumeratedPolynomial<C>,
            val reminder: EnumeratedPolynomial<C>
        )
    }
}

/**
 * Represents internal [EnumeratedPolynomial] errors.
 */
internal class EnumeratedPolynomialError(message: String): Error(message)

/**
 * Returns the same degrees description of the monomial, but without extra zero degrees on the end.
 */
internal fun List<UInt>.cleanUp() = subList(0, indexOfLast { it != 0U } + 1)

context(A)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> EnumeratedPolynomial(coefs: Map<List<UInt>, C>, toCheckInput: Boolean): EnumeratedPolynomial<C> {
    if (!toCheckInput) return EnumeratedPolynomial<C>(coefs)

    val fixedCoefs = mutableMapOf<List<UInt>, C>()

    for (entry in coefs) {
        val key = entry.key.cleanUp()
        val value = entry.value
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return EnumeratedPolynomial<C>(
        fixedCoefs
            .filter { it.value.isNotZero() }
    )
}
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 * @param toCheckInput If it's `true` cleaning of [coefficients] is executed otherwise it is not.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(A)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> EnumeratedPolynomial(pairs: Collection<Pair<List<UInt>, C>>, toCheckInput: Boolean): EnumeratedPolynomial<C> {
    if (!toCheckInput) return EnumeratedPolynomial(pairs.toMap())

    val fixedCoefs = mutableMapOf<List<UInt>, C>()

    for (entry in pairs) {
        val key = entry.first.cleanUp()
        val value = entry.second
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return EnumeratedPolynomial<C>(
        fixedCoefs
            .filter { it.value.isNotZero() }
    )
}
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 * @param toCheckInput If it's `true` cleaning of [coefficients] is executed otherwise it is not.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(A)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> EnumeratedPolynomial(vararg pairs: Pair<List<UInt>, C>, toCheckInput: Boolean): EnumeratedPolynomial<C> =
    EnumeratedPolynomial(pairs.toMap(), toCheckInput)
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param coefs Coefficients of the instants.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(A)
fun <C, A: Ring<C>> EnumeratedPolynomial(coefs: Map<List<UInt>, C>) = EnumeratedPolynomial(coefs, toCheckInput = true)
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(A)
fun <C, A: Ring<C>> EnumeratedPolynomial(pairs: Collection<Pair<List<UInt>, C>>) = EnumeratedPolynomial(pairs, toCheckInput = true)
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(A)
fun <C, A: Ring<C>> EnumeratedPolynomial(vararg pairs: Pair<List<UInt>, C>) = EnumeratedPolynomial(*pairs, toCheckInput = true)

context(EnumeratedPolynomialSpace<C, A>)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> EnumeratedPolynomial(coefs: Map<List<UInt>, C>, toCheckInput: Boolean): EnumeratedPolynomial<C> {
    if (!toCheckInput) return EnumeratedPolynomial(coefs)

    val fixedCoefs = mutableMapOf<List<UInt>, C>()

    for (entry in coefs) {
        val key = entry.key.cleanUp()
        val value = entry.value
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return EnumeratedPolynomial<C>(
        fixedCoefs
            .filter { it.value.isNotZero() }
    )
}
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 * @param toCheckInput If it's `true` cleaning of [coefficients] is executed otherwise it is not.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(EnumeratedPolynomialSpace<C, A>)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> EnumeratedPolynomial(pairs: Collection<Pair<List<UInt>, C>>, toCheckInput: Boolean): EnumeratedPolynomial<C> {
    if (!toCheckInput) return EnumeratedPolynomial(pairs.toMap())

    val fixedCoefs = mutableMapOf<List<UInt>, C>()

    for (entry in pairs) {
        val key = entry.first.cleanUp()
        val value = entry.second
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return EnumeratedPolynomial<C>(
        fixedCoefs
            .filter { it.value.isNotZero() }
    )
}
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 * @param toCheckInput If it's `true` cleaning of [coefficients] is executed otherwise it is not.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(EnumeratedPolynomialSpace<C, A>)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> EnumeratedPolynomial(vararg pairs: Pair<List<UInt>, C>, toCheckInput: Boolean): EnumeratedPolynomial<C> =
    EnumeratedPolynomial(pairs.toList(), toCheckInput)
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param coefs Coefficients of the instants.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(EnumeratedPolynomialSpace<C, A>)
fun <C, A: Ring<C>> EnumeratedPolynomial(coefs: Map<List<UInt>, C>) = EnumeratedPolynomial(coefs, toCheckInput = true)
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(EnumeratedPolynomialSpace<C, A>)
fun <C, A: Ring<C>> EnumeratedPolynomial(pairs: Collection<Pair<List<UInt>, C>>) = EnumeratedPolynomial(pairs, toCheckInput = true)
/**
 * Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from end of received
 * lists, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element in it.
 *
 * @param pairs Collection of pairs that represent monomials.
 *
 * @throws PolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
context(EnumeratedPolynomialSpace<C, A>)
fun <C, A: Ring<C>> EnumeratedPolynomial(vararg pairs: Pair<List<UInt>, C>) = EnumeratedPolynomial(*pairs, toCheckInput = true)

/**
 * Space of polynomials.
 *
 * @param C the type of operated polynomials.
 * @param A the intersection of [Ring] of [C] and [ScaleOperations] of [C].
 * @param ring the [A] instance.
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "INAPPLICABLE_JVM_NAME")
class EnumeratedPolynomialSpace<C, A : Ring<C>>(
    val ring: A,
) : PolynomialSpace<C, EnumeratedPolynomial<C>> {

    // region Constant-integer relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    // из Z в любое другое кольцо.
//    override operator fun C.plus(other: Int): C = ring { this@plus + other }
//    override operator fun C.minus(other: Int): C = ring { this@minus - other }
//    override operator fun C.times(other: Int): C = ring { this@times * other }
    // endregion

    // region Integer-constant relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    // из Z в любое другое кольцо.
//    override operator fun Int.plus(other: C): C = ring { this@plus + other }
//    override operator fun Int.minus(other: C): C = ring { this@minus - other }
//    override operator fun Int.times(other: C): C = ring { this@times * other }
    // endregion

    // region Polynomial-integer relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    // из Z в любое другое кольцо.
//    override operator fun UnivariatePolynomial<C>.plus(other: Int): C = ring { this@plus + other }
//    override operator fun UnivariatePolynomial<C>.minus(other: Int): C = ring { this@minus - other }
//    override operator fun UnivariatePolynomial<C>.times(other: Int): C = ring { this@times * other }
    // endregion

    // region Integer-polynomial relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    // из Z в любое другое кольцо.
//    override operator fun Int.plus(other: UnivariatePolynomial<C>): C = ring { this@plus + other }
//    override operator fun Int.minus(other: UnivariatePolynomial<C>): C = ring { this@minus - other }
//    override operator fun Int.times(other: UnivariatePolynomial<C>): C = ring { this@times * other }
    // endregion

    // region Constant-constant relation
    @JvmName("constantUnaryMinus")
    override operator fun C.unaryMinus(): C = ring { -this@unaryMinus }
    @JvmName("constantPlus")
    override operator fun C.plus(other: C): C = ring { this@plus + other }
    @JvmName("constantMinus")
    override operator fun C.minus(other: C): C = ring { this@minus - other }
    @JvmName("constantTimes")
    override operator fun C.times(other: C): C = ring { this@times * other }
    // endregion

    // region Constant-polynomial relation
    override operator fun C.plus(other: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> =
        if (this.isZero()) other
        else with(other.coefficients) {
            if (isEmpty()) EnumeratedPolynomial<C>(mapOf(listOf<UInt>() to this@plus))
            else EnumeratedPolynomial<C>(
                toMutableMap()
                    .apply {
                        if (listOf() !in this) this[listOf()] = this@plus
                        else {
                            val res = this@plus + this[listOf()]!!
                            if (res.isZero()) remove(listOf()) else this[listOf()] = res
                        }
                    }
            )
        }
    override operator fun C.minus(other: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> =
        if (this.isZero()) -other
        else with(other.coefficients) {
            if (isEmpty()) EnumeratedPolynomial<C>(mapOf(listOf<UInt>() to this@minus))
            else EnumeratedPolynomial<C>(
                toMutableMap()
                    .apply {
                        forEach { (degs, c) -> if(degs.isNotEmpty()) this[degs] = -c }
                        if (listOf() !in this) this[listOf()] = this@minus
                        else {
                            val res = this@minus - this[listOf()]!!
                            if (res.isZero()) remove(listOf()) else this[listOf()] = res
                        }
                    }
            )
        }
    override operator fun C.times(other: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> =
        if (this.isZero()) zero
        else EnumeratedPolynomial<C>(
            other.coefficients
                .asSequence()
                .map { (degs, c) -> degs to this@times * c }
                .filter { (_, c) -> c.isNotZero() }
                .toMap()
        )
    // endregion

    // region Polynomial-constant relation
    /**
     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun EnumeratedPolynomial<C>.plus(other: C): EnumeratedPolynomial<C> =
        if (other.isZero()) this
        else with(coefficients) {
            if (isEmpty()) EnumeratedPolynomial<C>(mapOf(listOf<UInt>() to other))
            else EnumeratedPolynomial<C>(
                toMutableMap()
                    .apply {
                        if (listOf() !in this) this[listOf()] = other
                        else {
                            val res = this[listOf()]!! + other
                            if (res.isZero()) remove(listOf()) else this[listOf()] = res
                        }
                    }
            )
        }
    /**
     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun EnumeratedPolynomial<C>.minus(other: C): EnumeratedPolynomial<C> =
        if (other.isZero()) this
        else with(coefficients) {
            if (isEmpty()) EnumeratedPolynomial<C>(mapOf(listOf<UInt>() to other))
            else EnumeratedPolynomial<C>(
                toMutableMap()
                    .apply {
                        if (listOf() !in this) this[listOf()] = -other
                        else {
                            val res = this[listOf()]!! - other
                            if (res.isZero()) remove(listOf()) else this[listOf()] = res
                        }
                    }
            )
        }
    /**
     * Returns product of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun EnumeratedPolynomial<C>.times(other: C): EnumeratedPolynomial<C> =
        if (other.isZero()) zero
        else EnumeratedPolynomial<C>(
            coefficients
                .asSequence()
                .map { (degs, c) -> degs to c * other }
                .filter { (_, c) -> c.isNotZero() }
                .toMap()
        )
    // endregion

    // region Polynomial-polynomial relation
    /**
     * Returns negation of the polynomial.
     */
    override fun EnumeratedPolynomial<C>.unaryMinus(): EnumeratedPolynomial<C> =
        EnumeratedPolynomial<C>(
            coefficients.mapValues { -it.value }
        )
    /**
     * Returns sum of the polynomials.
     */
    override operator fun EnumeratedPolynomial<C>.plus(other: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> =
        EnumeratedPolynomial<C>(
            coefficients
                .toMutableMap()
                .apply {
                    other.coefficients
                        .mapValuesTo(this) { (key, value) -> if (key in this) this[key]!! + value else value }
                }
                .filterValues { it.isNotZero() }
        )
//    /**
//     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.plus(other: Int): UnivariatePolynomial<T> =
//        if (other == 0) this
//        else
//            Polynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyList<Int>()
//                        if (key in this) {
//                            val res = this[key]!! + other
//                            if (res.isZero() && size == 1) {
//                                this[key] = res
//                            } else {
//                                this.remove(key)
//                            }
//                        } else {
//                            this[key] = ringOne * other
//                        }
//                    },
//                toCheckInput = false
//            )
//
//    /**
//     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.plus(other: Long): UnivariatePolynomial<T> =
//        if (other == 0L) this
//        else
//            Polynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyList<Int>()
//                        if (key in this) {
//                            val res = this[key]!! + other
//                            if (res.isZero() && size == 1) {
//                                this[key] = res
//                            } else {
//                                this.remove(key)
//                            }
//                        } else {
//                            this[key] = ringOne * other
//                        }
//                    },
//                toCheckInput = false
//            )
    /**
     * Returns difference of the polynomials.
     */
    override operator fun EnumeratedPolynomial<C>.minus(other: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> =
        EnumeratedPolynomial<C>(
            coefficients
                .toMutableMap()
                .apply {
                    other.coefficients
                        .mapValuesTo(this) { (key, value) -> if (key in this) this[key]!! - value else -value }
                }
                .filterValues { it.isNotZero() }
        )
//    /**
//     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.minus(other: Int): UnivariatePolynomial<T> =
//        if (other == 0) this
//        else
//            Polynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyList<Int>()
//                        if (key in this) {
//                            val res = this[key]!! - other
//                            if (res.isZero() && size == 1) {
//                                this[key] = res
//                            } else {
//                                this.remove(key)
//                            }
//                        } else {
//                            this[key] = -ringOne * other
//                        }
//                    },
//                toCheckInput = false
//            )
//
//    /**
//     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.minus(other: Long): UnivariatePolynomial<T> =
//        if (other == 0L) this
//        else
//            Polynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyList<Int>()
//                        if (key in this) {
//                            val res = this[key]!! - other
//                            if (res.isZero() && size == 1) {
//                                this[key] = res
//                            } else {
//                                this.remove(key)
//                            }
//                        } else {
//                            this[key] = -ringOne * other
//                        }
//                    },
//                toCheckInput = false
//            )
    /**
     * Returns product of the polynomials.
     */
    override operator fun EnumeratedPolynomial<C>.times(other: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> =
        when {
            isZero() -> this
            other.isZero() -> other
            else ->
                EnumeratedPolynomial<C>(
                    buildMap<List<UInt>, C> {
                        for ((degs1, c1) in coefficients) for ((degs2, c2) in other.coefficients) {
                            val degs =
                                (0..max(degs1.lastIndex, degs2.lastIndex))
                                    .map { degs1.getOrElse(it) { 0U } + degs2.getOrElse(it) { 0U } }
                            val c = c1 * c2
                            this[degs] = if (degs in this) this[degs]!! + c else c
                        }
                    }
                        .filterValues { it.isNotZero() }
                )
        }
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    operator fun UnivariatePolynomial<T>.times(other: Int): UnivariatePolynomial<T> =
//        if ((ringOne * other).isZero()) getZero()
//        else
//            Polynomial(
//                coefficients
//                    .mapValues { it.value * other }
//                    .filterValues { it.isNotZero() }
//                    .ifEmpty { mapOf(emptyList<Int>() to ringZero) },
//                toCheckInput = false
//            )
//
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [Polynomial].
//     */
//    operator fun UnivariatePolynomial<T>.times(other: Long): UnivariatePolynomial<T> =
//        if ((ringOne * other).isZero()) getZero()
//        else
//            Polynomial(
//                coefficients
//                    .mapValues { it.value * other }
//                    .filterValues { it.isNotZero() }
//                    .ifEmpty { mapOf(emptyList<Int>() to ringZero) },
//                toCheckInput = false
//            )

    override val zero: EnumeratedPolynomial<C> = EnumeratedPolynomial<C>(emptyMap())
    override val one: EnumeratedPolynomial<C> =
        EnumeratedPolynomial<C>(
            mapOf(
                listOf<UInt>() to ring.one // 1 * x_1^0 * x_2^0 * ...
            )
        )

    // TODO: Docs
    @Suppress("EXTENSION_SHADOWED_BY_MEMBER", "CovariantEquals")
    override fun EnumeratedPolynomial<C>.equals(other: EnumeratedPolynomial<C>): Boolean =
        when {
            this === other -> true
            else -> coefficients.size == other.coefficients.size &&
                    coefficients.all { (key, value) -> with(other.coefficients) { key in this && this[key] == value } }
        }
    // endregion

    // Not sure is it necessary...
    // region Polynomial properties
    /**
     * Count of all variables that appear in the polynomial in positive exponents.
     */
    val EnumeratedPolynomial<C>.countOfVariables: Int get() = coefficients.keys.maxOfOrNull { it.size } ?: 0
    /**
     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
     * zero, degree is -1.
     */
    override val EnumeratedPolynomial<C>.degree: Int get() = coefficients.keys.maxOfOrNull { it.sum() }?.toInt() ?: -1
    /**
     * List that associates indices of variables (that appear in the polynomial in positive exponents) with their most
     * exponents in which the variables are appeared in the polynomial.
     *
     * As consequence all values in the list are non-negative integers. Also if the polynomial is constant, the list is empty.
     * And size of the list is [countOfVariables].
     */
    val EnumeratedPolynomial<C>.degrees: List<UInt>
        get() =
            buildList(countOfVariables) {
                repeat(size) { add(0U) }
                coefficients.keys.forEach {
                    it.forEachIndexed { index, deg ->
                        this[index] = max(this[index], deg)
                    }
                }
            }

    /**
     * Checks if the instant is constant polynomial (of degree no more than 0) over considered ring.
     */
    override fun EnumeratedPolynomial<C>.isConstant(): Boolean =
        with(coefficients) { isEmpty() || size == 1 && entries.first().let { (key, _) -> key.isEmpty() } }
    /**
     * Checks if the instant is constant non-zero polynomial (of degree no more than 0) over considered ring.
     */
    override fun EnumeratedPolynomial<C>.isNonZeroConstant(): Boolean =
        with(coefficients) { size == 1 && entries.first().let { (key, _) -> key.isEmpty() } }

    override fun EnumeratedPolynomial<C>.asConstantOrNull(): C? =
        with(coefficients) {
            when {
                isEmpty() -> ring.zero
                size > 1 -> null
                else -> with(entries.first()) { if(key.isEmpty()) value else null }
            }
        }

    // TODO: Перенести в реализацию
    @Suppress("NOTHING_TO_INLINE")
    inline fun EnumeratedPolynomial<C>.substitute(argument: Map<Int, C>): EnumeratedPolynomial<C> = this.substitute(ring, argument)
    @Suppress("NOTHING_TO_INLINE")
    @JvmName("substitutePolynomial")
    inline fun EnumeratedPolynomial<C>.substitute(argument: Map<Int, EnumeratedPolynomial<C>>): EnumeratedPolynomial<C> = this.substitute(ring, argument)

    @Suppress("NOTHING_TO_INLINE")
    inline fun EnumeratedPolynomial<C>.asFunction(): (Map<Int, C>) -> EnumeratedPolynomial<C> = { this.substitute(ring, it) }
    @Suppress("NOTHING_TO_INLINE")
    inline fun EnumeratedPolynomial<C>.asFunctionOnConstants(): (Map<Int, C>) -> EnumeratedPolynomial<C> = { this.substitute(ring, it) }
    @Suppress("NOTHING_TO_INLINE")
    inline fun EnumeratedPolynomial<C>.asFunctionOnPolynomials(): (Map<Int, EnumeratedPolynomial<C>>) -> EnumeratedPolynomial<C> = { this.substitute(ring, it) }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun EnumeratedPolynomial<C>.invoke(argument: Map<Int, C>): EnumeratedPolynomial<C> = this.substitute(ring, argument)
    @Suppress("NOTHING_TO_INLINE")
    @JvmName("invokePolynomial")
    inline operator fun EnumeratedPolynomial<C>.invoke(argument: Map<Int, EnumeratedPolynomial<C>>): EnumeratedPolynomial<C> = this.substitute(ring, argument)
    // endregion

    // region Legacy
    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun add(left: EnumeratedPolynomial<C>, right: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> = left + right
    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun multiply(left: EnumeratedPolynomial<C>, right: EnumeratedPolynomial<C>): EnumeratedPolynomial<C> = left * right
    // endregion
}