package space.kscience.kmath.functions

import space.kscience.kmath.operations.*
import kotlin.math.max


/**
 * Represents multivariate polynomials with labeled variables.
 *
 * @param T Ring in which the polynomial is considered.
 * @param coefs Coefficients of the instant.
 * @param toCheckInput If it's `true` cleaning of [coefficients] is executed otherwise it is not.
 *
 * @constructor Gets the coefficients in format of [coefficients] field and cleans it: removes zero degrees from keys of
 * received maps, sums up proportional monomials, removes zero monomials, and if result is empty map adds only element
 * in it.
 *
 * @throws LabeledPolynomialError If no coefficient received or if any of degrees in any monomial is negative.
 */
class LabeledPolynomial<C>
internal constructor(
    /**
     * Map that collects coefficients of the polynomial. Every non-zero monomial
     * `a x_1^{d_1} ... x_n^{d_n}` is represented as pair "key-value" in the map, where value is coefficients `a` and
     * key is map that associates variables in the monomial with multiplicity of them occurring in the monomial.
     * For example polynomial
     * ```
     * 5 a^2 c^3 - 6 b + 0 b c
     * ```
     * has coefficients represented as
     * ```
     * mapOf(
     *      mapOf(
     *          a to 2,
     *          c to 3
     *      ) to 5,
     *      mapOf(
     *          b to 1
     *      ) to (-6)
     * )
     * ```
     * where `a`, `b` and `c` are corresponding [Variable] objects.
     */
    val coefficients: Map<Map<Variable, UInt>, C>
) : Polynomial<C> {
    companion object {
        /**
         * Comparator for lexicographic comparison of monomials.
         */
        val monomialComparator = Comparator<Map<Variable, UInt>> { o1: Map<Variable, UInt>?, o2: Map<Variable, UInt>? ->
            if (o1 == o2) return@Comparator 0
            if (o1 == null) return@Comparator 1
            if (o2 == null) return@Comparator -1

            val commonVariables = (o1.keys union o2.keys).sorted()

            for (variable in commonVariables) {
                if (o1.getOrDefault(variable, 0U) > o2.getOrDefault(variable, 0U)) return@Comparator -1
                if (o1.getOrDefault(variable, 0U) < o2.getOrDefault(variable, 0U)) return@Comparator 1
            }

            return@Comparator 0
        }

        /**
         * Represents result of division with remainder.
         */
        data class DividingResult<T : Field<T>>(
            val quotient: LabeledPolynomial<T>,
            val reminder: LabeledPolynomial<T>
        )
    }
}

/**
 * Represents internal [LabeledPolynomial] errors.
 */
internal class LabeledPolynomialError: Error {
    constructor() : super()
    constructor(message: String) : super(message)
}

/**
 * Returns the same degrees description of the monomial, but without zero degrees.
 */
internal fun Map<Variable, Int>.cleanUp() = filterValues { it > 0 }

/**
 * Space of polynomials.
 *
 * @param C the type of operated polynomials.
 * @param A the intersection of [Ring] of [C] and [ScaleOperations] of [C].
 * @param ring the [A] instance.
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "INAPPLICABLE_JVM_NAME")
class LabeledPolynomialSpace<C, A : Ring<C>>(
    val ring: A,
) : PolynomialSpace<C, LabeledPolynomial<C>> {

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
    override operator fun C.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (isZero()) other
        else
            LabeledPolynomial<C>(
                other.coefficients
                    .toMutableMap()
                    .apply {
                        val key = emptyMap<Variable, UInt>()
                        if (key in this) {
                            val res = this[key]!! + this@plus
                            if (res.isZero()) remove(key) else this[key] = res
                        } else {
                            this[key] = this@plus
                        }
                    }
            )
    override operator fun C.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (isZero()) other
        else
            LabeledPolynomial<C>(
                other.coefficients
                    .toMutableMap()
                    .apply {
                        val key = emptyMap<Variable, UInt>()
                        if (key in this) {
                            val res = this[key]!! - this@minus
                            if (res.isZero()) remove(key) else this[key] = res
                        } else {
                            this[key] = -this@minus
                        }
                    }
            )
    override operator fun C.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (isZero()) zero
        else
            LabeledPolynomial<C>(
                other.coefficients
                    .asSequence()
                    .map { (degs, c) -> degs to c * this@times }
                    .filter { (_, c) -> c.isNotZero() }
                    .toMap()
            )
    // endregion

    // region Polynomial-constant relation
    /**
     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun LabeledPolynomial<C>.plus(other: C): LabeledPolynomial<C> =
        if (other.isZero()) this
        else
            LabeledPolynomial<C>(
                coefficients
                    .toMutableMap()
                    .apply {
                        val key = emptyMap<Variable, UInt>()
                        if (key in this) {
                            val res = this[key]!! + other
                            if (res.isZero()) remove(key) else this[key] = res
                        } else {
                            this[key] = other
                        }
                    }
            )
    /**
     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun LabeledPolynomial<C>.minus(other: C): LabeledPolynomial<C> =
        if (other.isZero()) this
        else
            LabeledPolynomial<C>(
                coefficients
                    .toMutableMap()
                    .apply {
                        val key = emptyMap<Variable, UInt>()
                        if (key in this) {
                            val res = this[key]!! - other
                            if (res.isZero()) remove(key) else this[key] = res
                        } else {
                            this[key] = -other
                        }
                    }
            )
    /**
     * Returns product of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun LabeledPolynomial<C>.times(other: C): LabeledPolynomial<C> =
        if (other.isZero()) zero
        else
            LabeledPolynomial<C>(
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
    override fun LabeledPolynomial<C>.unaryMinus(): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
            coefficients.mapValues { -it.value }
        )
    /**
     * Returns sum of the polynomials.
     */
    override operator fun LabeledPolynomial<C>.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
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
//            LabeledPolynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyMap<Variable, Int>()
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
//            LabeledPolynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyMap<Variable, Int>()
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
    override operator fun LabeledPolynomial<C>.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
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
//            LabeledPolynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyMap<Variable, Int>()
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
//            LabeledPolynomial(
//                coefficients
//                    .toMutableMap()
//                    .apply {
//                        val key = emptyMap<Variable, Int>()
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
    override operator fun LabeledPolynomial<C>.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        when {
            isZero() -> this
            other.isZero() -> other
            else -> LabeledPolynomial<C>(
                buildMap<Map<Variable, UInt>, C> {
                    for ((degs1, c1) in coefficients) for ((degs2, c2) in other.coefficients) {
                        val degs = degs1.toMutableMap()
                        degs2.mapValuesTo(degs) { (variable, deg) -> degs.getOrDefault(variable, 0U) + deg }
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
//            LabeledPolynomial(
//                coefficients
//                    .mapValues { it.value * other }
//                    .filterValues { it.isNotZero() }
//                    .ifEmpty { mapOf(emptyMap<Variable, Int>() to ringZero) },
//                toCheckInput = false
//            )
//
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [Polynomial].
//     */
//    operator fun UnivariatePolynomial<T>.times(other: Long): UnivariatePolynomial<T> =
//        if ((ringOne * other).isZero()) getZero()
//        else
//            LabeledPolynomial(
//                coefficients
//                    .mapValues { it.value * other }
//                    .filterValues { it.isNotZero() }
//                    .ifEmpty { mapOf(emptyMap<Variable, Int>() to ringZero) },
//                toCheckInput = false
//            )
    override val zero: LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to ring.zero))
    override val one: LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(emptyMap<Variable, UInt>() to ring.one))

    // TODO: Docs
    @Suppress("EXTENSION_SHADOWED_BY_MEMBER", "CovariantEquals")
    override fun LabeledPolynomial<C>.equals(other: LabeledPolynomial<C>): Boolean =
        when {
            this === other -> true
            else -> coefficients.size == other.coefficients.size &&
                    coefficients.all { (key, value) -> with(other.coefficients) { key in this && this[key] == value } }
        }
    // endregion

    // Not sure is it necessary...
    // region Polynomial properties
    /**
     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
     * zero, degree is -1.
     */
    override val LabeledPolynomial<C>.degree: Int get() = coefficients.keys.maxOfOrNull { it.values.sum() }?.toInt() ?: -1
    /**
     * Map that associates variables (that appear in the polynomial in positive exponents) with their most exponents
     * in which they are appeared in the polynomial.
     *
     * As consequence all values in the map are positive integers. Also if the polynomial is constant, the map is empty.
     * And keys of the map is the same as in [variables].
     */
    val LabeledPolynomial<C>.degrees: Map<Variable, UInt>
        get() =
            buildMap {
                coefficients.keys.forEach { degs ->
                    degs.mapValuesTo(this) { (variable, deg) -> max(getOrDefault(variable, 0U), deg) }
                }
            }
    /**
     * Set of all variables that appear in the polynomial in positive exponents.
     */
    val LabeledPolynomial<C>.variables: Set<Variable>
        get() =
            buildSet {
                coefficients.keys.forEach { degs -> addAll(degs.keys) }
            }
    /**
     * Count of all variables that appear in the polynomial in positive exponents.
     */
    val LabeledPolynomial<C>.countOfVariables: Int get() = variables.size

    /**
     * Checks if the instant is constant polynomial (of degree no more than 0) over considered ring.
     */
    override fun LabeledPolynomial<C>.isConstant(): Boolean =
        with(coefficients) { isEmpty() || size == 1 && entries.first().let { (key, _) -> key.isEmpty() } }
    /**
     * Checks if the instant is constant non-zero polynomial (of degree no more than 0) over considered ring.
     */
    override fun LabeledPolynomial<C>.isNonZeroConstant(): Boolean =
        with(coefficients) { size == 1 && entries.first().let { (key, _) -> key.isEmpty() } }

    override fun LabeledPolynomial<C>.asConstantOrNull(): C? =
        with(coefficients) {
            when {
                isEmpty() -> ring.zero
                size > 1 -> null
                else -> with(entries.first()) { if(key.isEmpty()) value else null }
            }
        }

    // TODO: Перенести в реализацию
    @Suppress("NOTHING_TO_INLINE")
    inline fun LabeledPolynomial<C>.substitute(argument: Map<Variable, C>): LabeledPolynomial<C> = this.substitute(ring, argument)
    @Suppress("NOTHING_TO_INLINE")
    @JvmName("substitutePolynomial")
    inline fun LabeledPolynomial<C>.substitute(argument: Map<Variable, LabeledPolynomial<C>>): LabeledPolynomial<C> = this.substitute(ring, argument)

    @Suppress("NOTHING_TO_INLINE")
    inline fun LabeledPolynomial<C>.asFunction(): (Map<Variable, C>) -> LabeledPolynomial<C> = { this.substitute(ring, it) }
    @Suppress("NOTHING_TO_INLINE")
    inline fun LabeledPolynomial<C>.asFunctionOnConstants(): (Map<Variable, C>) -> LabeledPolynomial<C> = { this.substitute(ring, it) }
    @Suppress("NOTHING_TO_INLINE")
    inline fun LabeledPolynomial<C>.asFunctionOnPolynomials(): (Map<Variable, LabeledPolynomial<C>>) -> LabeledPolynomial<C> = { this.substitute(ring, it) }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun LabeledPolynomial<C>.invoke(argument: Map<Variable, C>): LabeledPolynomial<C> = this.substitute(ring, argument)
    @Suppress("NOTHING_TO_INLINE")
    @JvmName("invokePolynomial")
    inline operator fun LabeledPolynomial<C>.invoke(argument: Map<Variable, LabeledPolynomial<C>>): LabeledPolynomial<C> = this.substitute(ring, argument)
    // endregion

    // region Legacy
    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun add(left: LabeledPolynomial<C>, right: LabeledPolynomial<C>): LabeledPolynomial<C> = left + right
    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun multiply(left: LabeledPolynomial<C>, right: LabeledPolynomial<C>): LabeledPolynomial<C> = left * right
    // endregion
}