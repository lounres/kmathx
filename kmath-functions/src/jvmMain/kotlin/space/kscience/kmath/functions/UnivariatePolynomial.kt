package space.kscience.kmath.functions

import space.kscience.kmath.operations.*
import kotlin.jvm.JvmName
import kotlin.math.*

/**
 * Represents multivariate polynomials with indexed variables.
 *
 * @param C Ring in which the polynomial is considered.
 * @param coefficients Coefficients of the instants.
 */
data class UnivariatePolynomial<C> (val coefficients: List<C>) : Polynomial<C> {
    /**
     * Represents the polynomial as a [String]. Consider that monomials are sorted in lexicographic order.
     */
    override fun toString(): String = "UnivariatePolynomial($coefficients)"

    companion object {
        /**
         * Default name of variables used in string representations.
         *
         * @see UnivariatePolynomial.toString
         */
        var defaultVariableName = "x"

        /**
         * Represents result of division with remainder.
         */
        data class DividingResult<C>(
            val quotient: UnivariatePolynomial<C>,
            val reminder: UnivariatePolynomial<C>
        )
    }
}

/**
 * Represents internal [UnivariatePolynomial] errors.
 */
internal class UnivariatePolynomialError(message: String): Error(message)

// region Constructors and converters

fun <T> UnivariatePolynomial(coefficients: List<T>, reverse: Boolean = false): UnivariatePolynomial<T> =
    UnivariatePolynomial(with(coefficients) { if (reverse) reversed() else this })

fun <T> UnivariatePolynomial(vararg coefficients: T, reverse: Boolean = false): UnivariatePolynomial<T> =
    UnivariatePolynomial(with(coefficients) { if (reverse) reversed() else toList() })

fun <T> T.asUnivariatePolynomial() : UnivariatePolynomial<T> = UnivariatePolynomial(listOf(this))

// endregion

/**
 * Space of polynomials.
 *
 * @param C the type of operated polynomials.
 * @param A the intersection of [Ring] of [C] and [ScaleOperations] of [C].
 * @param ring the [A] instance.
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "INAPPLICABLE_JVM_NAME")
class UnivariatePolynomialSpace<C, A : Ring<C>>(
    val ring: A,
) : PolynomialSpace<C, UnivariatePolynomial<C>> {

    // region Constant-integer relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    override operator fun C.plus(other: Int): C = ring { this@plus + other }
//    override operator fun C.minus(other: Int): C = ring { this@minus - other }
//    override operator fun C.times(other: Int): C = ring { this@times * other }
    // endregion

    // region Integer-constant relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    override operator fun Int.plus(other: C): C = ring { this@plus + other }
//    override operator fun Int.minus(other: C): C = ring { this@minus - other }
//    override operator fun Int.times(other: C): C = ring { this@times * other }
    // endregion

    // region Polynomial-integer relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    /**
//     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.plus(other: Int): UnivariatePolynomial<T> =
//        if (other == 0) this
//        else
//            UnivariatePolynomial(
//                coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero + other
//                        else this[0] += other
//                    }
//            )
//
//    /**
//     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.plus(other: Long): UnivariatePolynomial<T> =
//        if (other == 0L) this
//        else
//            UnivariatePolynomial(
//                coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero + other
//                        else this[0] += other
//                    }
//            )
//    /**
//     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.minus(other: Int): UnivariatePolynomial<T> =
//        if (other == 0) this
//        else
//            UnivariatePolynomial(
//                coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero - other
//                        else this[0] -= other
//                    }
//            )
//    /**
//     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun UnivariatePolynomial<T>.minus(other: Long): UnivariatePolynomial<T> =
//        if (other == 0L) this
//        else
//            UnivariatePolynomial(
//                coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero - other
//                        else this[0] -= other
//                    }
//            )
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    operator fun UnivariatePolynomial<T>.times(other: Int): UnivariatePolynomial<T> =
//        if (other == 0) zero
//        else UnivariatePolynomial(
//            coefficients
//                .subList(0, degree + 1)
//                .map { it * other }
//        )
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [Polynomial].
//     */
//    operator fun UnivariatePolynomial<T>.times(other: Long): UnivariatePolynomial<T> =
//        if (other == 0) zero
//        else UnivariatePolynomial(
//            coefficients
//                .subList(0, degree + 1)
//                .map { it * other }
//        )
    // endregion

    // region Integer-polynomial relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    override operator fun Int.plus(other: UnivariatePolynomial<C>): C = ring { this@plus + other }
//    override operator fun Int.minus(other: UnivariatePolynomial<C>): C = ring { this@minus - other }
//    override operator fun Int.times(other: UnivariatePolynomial<C>): C = ring { this@times * other }
//    /**
//     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun Int.plus(other: UnivariatePolynomial<T>): UnivariatePolynomial<T> =
//        if (this == 0) other
//        else
//            UnivariatePolynomial(
//                other.coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero + this@plus
//                        else this[0] += this@plus
//                    }
//            )
//
//    /**
//     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun Long.plus(other: UnivariatePolynomial<T>): UnivariatePolynomial<T> =
//        if (this == 0L) other
//        else
//            UnivariatePolynomial(
//                other.coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero + this@plus
//                        else this[0] += this@plus
//                    }
//            )
//    /**
//     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun Int.minus(other: UnivariatePolynomial<T>): UnivariatePolynomial<T> =
//        if (this == 0) other
//        else
//            UnivariatePolynomial(
//                other.coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero - this@minus
//                        else this[0] -= this@minus
//                    }
//            )
//    /**
//     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    override operator fun Long.minus(other: UnivariatePolynomial<T>): UnivariatePolynomial<T> =
//        if (this == 0L) other
//        else
//            UnivariatePolynomial(
//                other.coefficients
//                    .toMutableList()
//                    .apply {
//                        if (isEmpty()) this[0] = ring.zero - this@minus
//                        else this[0] -= this@minus
//                    }
//            )
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [UnivariatePolynomial].
//     */
//    operator fun Int.times(other: UnivariatePolynomial<T>): UnivariatePolynomial<T> =
//        if (this == 0) zero
//        else UnivariatePolynomial(
//            other.coefficients
//                .subList(0, degree + 1)
//                .map { it * this@times }
//        )
//    /**
//     * Returns product of the polynomials. [other] is interpreted as [Polynomial].
//     */
//    operator fun UnivariatePolynomial<T>.times(other: Long): UnivariatePolynomial<T> =
//        if (this == 0L) zero
//        else UnivariatePolynomial(
//            other.coefficients
//                .subList(0, degree + 1)
//                .map { it * this@times }
//        )
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
    override operator fun C.plus(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
        with(other.coefficients) {
            if (isEmpty()) UnivariatePolynomial(listOf(this@plus))
            else UnivariatePolynomial(
                toMutableList()
                    .apply {
                        this[0] += this@plus
                    }
            )
        }
//        if (degree == -1) UnivariatePolynomial(other) else UnivariatePolynomial(
//            listOf(coefficients[0] + other) + coefficients.subList(1, degree + 1)
//        )
    override operator fun C.minus(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
        with(other.coefficients) {
            if (isEmpty()) UnivariatePolynomial(listOf(-this@minus))
            else UnivariatePolynomial(
                toMutableList()
                    .apply {
                        this[0] -= this@minus
                    }
            )
        }
//        if (degree == -1) UnivariatePolynomial(other) else UnivariatePolynomial(
//            listOf(coefficients[0] + other) + coefficients.subList(1, degree + 1)
//        )
    override operator fun C.times(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
        UnivariatePolynomial(
            other.coefficients
    //                .subList(0, other.degree + 1)
                .map { it * this }
        )
    // endregion

    // region Polynomial-constant relation
    /**
     * Returns sum of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun UnivariatePolynomial<C>.plus(other: C): UnivariatePolynomial<C> =
        if (other.isZero()) this
        else with(coefficients) {
            if (isEmpty()) UnivariatePolynomial(listOf(other))
            else UnivariatePolynomial(
                toMutableList()
                    .apply {
                        this[0] += other
                    }
            )
        }
//        if (degree == -1) UnivariatePolynomial(other) else UnivariatePolynomial(
//            listOf(coefficients[0] + other) + coefficients.subList(1, degree + 1)
//        )
    /**
     * Returns difference of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun UnivariatePolynomial<C>.minus(other: C): UnivariatePolynomial<C> =
        with(coefficients) {
            if (isEmpty()) UnivariatePolynomial(listOf(other))
            else UnivariatePolynomial(
                toMutableList()
                    .apply {
                        this[0] -= other
                    }
            )
        }
//        if (degree == -1) UnivariatePolynomial(-other) else UnivariatePolynomial(
//            listOf(coefficients[0] - other) + coefficients.subList(1, degree + 1)
//        )
    /**
     * Returns product of the polynomials. [other] is interpreted as [UnivariatePolynomial].
     */
    override operator fun UnivariatePolynomial<C>.times(other: C): UnivariatePolynomial<C> =
        UnivariatePolynomial(
            coefficients
//                .subList(0, degree + 1)
                .map { it * other }
        )
    // endregion

    // region Polynomial-polynomial relation
    /**
     * Returns negation of the polynomial.
     */
    override fun UnivariatePolynomial<C>.unaryMinus(): UnivariatePolynomial<C> =
        UnivariatePolynomial(coefficients.map { -it })
    /**
     * Returns sum of the polynomials.
     */
    override operator fun UnivariatePolynomial<C>.plus(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
        UnivariatePolynomial(
            (0..max(degree, other.degree))
                .map {
                    when {
                        it > degree -> other.coefficients[it]
                        it > other.degree -> coefficients[it]
                        else -> coefficients[it] + other.coefficients[it]
                    }
                }
                .ifEmpty { listOf(ring.zero) }
        )
    /**
     * Returns difference of the polynomials.
     */
    override operator fun UnivariatePolynomial<C>.minus(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> =
        UnivariatePolynomial(
            (0..max(degree, other.degree))
                .map {
                    when {
                        it > degree -> -other.coefficients[it]
                        it > other.degree -> coefficients[it]
                        else -> coefficients[it] - other.coefficients[it]
                    }
                }
                .ifEmpty { listOf(ring.zero) }
        )
    /**
     * Returns product of the polynomials.
     */
    override operator fun UnivariatePolynomial<C>.times(other: UnivariatePolynomial<C>): UnivariatePolynomial<C> {
        val thisDegree = degree
        val otherDegree = other.degree
        return when {
            thisDegree == -1 -> this
            otherDegree == -1 -> other
            else ->
                UnivariatePolynomial(
                    (0..(thisDegree + otherDegree))
                        .map { d ->
                            (max(0, d - otherDegree)..(min(thisDegree, d)))
                                .map { coefficients[it] * other.coefficients[d - it] }
                                .reduce { acc, rational -> acc + rational }
                        }
                )
        }
    }

    override val zero: UnivariatePolynomial<C> = UnivariatePolynomial(emptyList())
    override val one: UnivariatePolynomial<C> = UnivariatePolynomial(listOf(ring.one))
    val x: UnivariatePolynomial<C> = UnivariatePolynomial(listOf(ring.zero, ring.one))

    // TODO: Docs
    @Suppress("EXTENSION_SHADOWED_BY_MEMBER", "CovariantEquals")
    override fun UnivariatePolynomial<C>.equals(other: UnivariatePolynomial<C>): Boolean =
        when {
            this === other -> true
            else -> {
                if (this.degree == other.degree)
                    (0..degree).all { coefficients[it] == other.coefficients[it] }
                else false
            }
        }
    // endregion

    // Not sure is it necessary...
    // region Polynomial properties
    /**
     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
     * zero, degree is -1.
     */
    override val UnivariatePolynomial<C>.degree: Int get() = coefficients.indexOfLast { it != ring.zero }

    override fun UnivariatePolynomial<C>.asConstantOrNull(): C? =
        with(coefficients) {
            when {
                isEmpty() -> ring.zero
                degree > 0 -> null
                else -> first()
            }
        }

    @Suppress("NOTHING_TO_INLINE")
    inline fun UnivariatePolynomial<C>.substitute(argument: C): C = this.substitute(ring, argument)
    @Suppress("NOTHING_TO_INLINE")
    inline fun UnivariatePolynomial<C>.substitute(argument: UnivariatePolynomial<C>): UnivariatePolynomial<C> = this.substitute(ring, argument)

    @Suppress("NOTHING_TO_INLINE")
    inline fun UnivariatePolynomial<C>.asFunction(): (C) -> C = { this.substitute(ring, it) }
    @Suppress("NOTHING_TO_INLINE")
    inline fun UnivariatePolynomial<C>.asFunctionOnConstants(): (C) -> C = { this.substitute(ring, it) }
    @Suppress("NOTHING_TO_INLINE")
    inline fun UnivariatePolynomial<C>.asFunctionOnPolynomials(): (UnivariatePolynomial<C>) -> UnivariatePolynomial<C> = { this.substitute(ring, it) }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun UnivariatePolynomial<C>.invoke(argument: C): C = this.substitute(ring, argument)
    @Suppress("NOTHING_TO_INLINE")
    inline operator fun UnivariatePolynomial<C>.invoke(argument: UnivariatePolynomial<C>): UnivariatePolynomial<C> = this.substitute(ring, argument)
    // endregion

    // region Legacy
    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun add(left: UnivariatePolynomial<C>, right: UnivariatePolynomial<C>): UnivariatePolynomial<C> = left + right
    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun multiply(left: UnivariatePolynomial<C>, right: UnivariatePolynomial<C>): UnivariatePolynomial<C> = left * right
    // endregion
}