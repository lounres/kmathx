package space.kscience.kmath.operations

import space.kscience.kmath.misc.UnstableKMathAPI
import space.kscience.kmath.numberTheory.*
import java.math.BigInteger as JBInt
import java.math.BigInteger.ZERO as JBIZero
import java.math.BigInteger.ONE as JBIOne
import kotlin.toBigInteger as toJBInt
import kotlin.text.toBigInteger as toJBInt

/**
 * The class represents rational numbers.
 *
 * Instances contain [numerator] and [denominator] represented as [Long].
 *
 * Also [numerator] and [denominator] are coprime and [denominator] is positive.
 *
 * @author [Gleb Minaev](https://github.com/lounres)
 * [Origin](https://github.com/lounres/kotlin-test/blob/master/src/main/kotlin/math/ringsAndFields/Rational.kt#L12)
 */
class Rational: Comparable<Rational> {
    companion object {
        /**
         * Constant containing the zero (the additive identity) of the [Rational] field.
         */
        val ZERO = Rational(JBIZero)
        /**
         * Constant containing the one (the multiplicative identity) of the [Rational] field.
         */
        val ONE = Rational(JBIOne)
    }

    /**
     * Numerator of the fraction. It's stored as non-negative coprime with [denominator] integer.
     */
    val numerator: JBInt
    /**
     * Denominator of the fraction. It's stored as non-zero coprime with [numerator] integer.
     */
    val denominator: JBInt

    /**
     * If [toCheckInput] is `true` before assigning values to [Rational.numerator] and [Rational.denominator] makes them coprime and makes
     * denominator positive. Otherwise, just assigns the values.
     *
     * @throws ArithmeticException If denominator is zero.
     */
    internal constructor(numerator: JBInt, denominator: JBInt, toCheckInput: Boolean = true) {
        if (toCheckInput) {
            if (denominator == JBIZero) throw ArithmeticException("/ by zero")

            val greatestCommonDivider = gcd(numerator, denominator).let { if (denominator < JBIZero) -it else it }

            this.numerator = numerator / greatestCommonDivider
            this.denominator = denominator / greatestCommonDivider
        } else {
            this.numerator = numerator
            this.denominator = denominator
        }
    }

    /**
     * Before assigning values to [Rational.numerator] and [Rational.denominator] makes them coprime and makes
     * denominator positive.
     *
     * @throws ArithmeticException If denominator is zero.
     */
    constructor(numerator: JBInt, denominator: JBInt) : this(numerator, denominator, true)
    constructor(numerator: Int, denominator: JBInt) : this(numerator.toJBInt(), denominator, true)
    constructor(numerator: Long, denominator: JBInt) : this(numerator.toJBInt(), denominator, true)
    constructor(numerator: JBInt, denominator: Int) : this(numerator, denominator.toJBInt(), true)
    constructor(numerator: JBInt, denominator: Long) : this(numerator, denominator.toJBInt(), true)
    constructor(numerator: Int, denominator: Int) : this(numerator.toJBInt(), denominator.toJBInt(), true)
    constructor(numerator: Int, denominator: Long) : this(numerator.toJBInt(), denominator.toJBInt(), true)
    constructor(numerator: Long, denominator: Int) : this(numerator.toJBInt(), denominator.toJBInt(), true)
    constructor(numerator: Long, denominator: Long) : this(numerator.toJBInt(), denominator.toJBInt(), true)
    constructor(numerator: JBInt) : this(numerator, JBIOne, false)
    constructor(numerator: Int) : this(numerator.toJBInt(), JBIOne, false)
    constructor(numerator: Long) : this(numerator.toJBInt(), JBIOne, false)

    /**
     * Parses simple string to [Rational] value.
     *
     * Possible inputs:
     * - `"a"` is converted to `Rational(a)`
     * - `"a/b"` is converted to `Rational(a, b)`
     *
     * @throws NumberFormatException
     */
    constructor(str: String) {
        val result = str.split("/")
        when (result.size) {
            1 -> try {
                numerator = result[0].toJBInt()
                denominator = JBIOne
            } catch (e: NumberFormatException) {
                throw NumberFormatException("For input string: \"$str\"")
            }

            2 -> try {
                val numerator = result[0].toJBInt()
                val denominator = result[1].toJBInt()

                val greatestCommonDivider = gcd(numerator, denominator).let { if (denominator < JBIZero) -it else it }

                this.numerator = numerator / greatestCommonDivider
                this.denominator = denominator / greatestCommonDivider
            } catch (e: NumberFormatException) {
                throw NumberFormatException("For input string: \"$str\"")
            }

            else -> throw NumberFormatException("For input string: \"$str\"")
        }
    }

    /**
     * Returns the same instant.
     */
    operator fun unaryPlus(): Rational = this

    /**
     * Returns negation of the instant of [Rational] field.
     */
    operator fun unaryMinus(): Rational = Rational(-this.numerator, this.denominator)

    /**
     * Returns sum of the instants of [Rational] field.
     */
    operator fun plus(other: Rational): Rational =
        Rational(
            numerator * other.denominator + denominator * other.numerator,
            denominator * other.denominator
        )

    /**
     * Returns sum of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun plus(other: JBInt): Rational =
        Rational(
            numerator + denominator * other,
            denominator
        )

    /**
     * Returns sum of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun plus(other: Int): Rational =
        Rational(
            numerator + denominator * other.toJBInt(),
            denominator
        )

    /**
     * Returns sum of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun plus(other: Long): Rational =
        Rational(
            numerator + denominator * other.toJBInt(),
            denominator
        )

    /**
     * Returns difference of the instants of [Rational] field.
     */
    operator fun minus(other: Rational): Rational =
        Rational(
            numerator * other.denominator - denominator * other.numerator,
            denominator * other.denominator
        )

    /**
     * Returns difference of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun minus(other: JBInt): Rational =
        Rational(
            numerator - denominator * other,
            denominator
        )

    /**
     * Returns difference of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun minus(other: Int): Rational =
        Rational(
            numerator - denominator * other.toJBInt(),
            denominator
        )

    /**
     * Returns difference of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun minus(other: Long): Rational =
        Rational(
            numerator - denominator * other.toJBInt(),
            denominator
        )

    /**
     * Returns product of the instants of [Rational] field.
     */
    operator fun times(other: Rational): Rational =
        Rational(
            numerator * other.numerator,
            denominator * other.denominator
        )

    /**
     * Returns product of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun times(other: JBInt): Rational =
        Rational(
            numerator * other,
            denominator
        )

    /**
     * Returns product of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun times(other: Int): Rational =
        Rational(
            numerator * other.toJBInt(),
            denominator
        )

    /**
     * Returns product of the instants of [Rational] field. [other] is represented as [Rational].
     */
    operator fun times(other: Long): Rational =
        Rational(
            numerator * other.toJBInt(),
            denominator
        )

    /**
     * Returns quotient of the instants of [Rational] field.
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun div(other: Rational): Rational =
        Rational(
            numerator * other.denominator,
            denominator * other.numerator
        )

    /**
     * Returns quotient of the instants of [Rational] field. [other] is represented as [Rational].
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun div(other: JBInt): Rational =
        Rational(
            numerator,
            denominator * other
        )

    /**
     * Returns quotient of the instants of [Rational] field. [other] is represented as [Rational].
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun div(other: Int): Rational =
        Rational(
            numerator,
            denominator * other.toJBInt()
        )

    /**
     * Returns quotient of the instants of [Rational] field. [other] is represented as [Rational].
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun div(other: Long): Rational =
        Rational(
            numerator,
            denominator * other.toJBInt()
        )

    /**
     * Returns reminder from integral division.
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun rem(other: Rational): Rational =
        Rational(
            (numerator * other.denominator) % (denominator * other.numerator),
            denominator * other.denominator
        )

    /**
     * Returns reminder from integral division.
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun rem(other: JBInt): Rational =
        Rational(
            numerator % denominator * other,
            denominator * other
        )

    /**
     * Returns reminder from integral division.
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun rem(other: Int): Rational =
        Rational(
            numerator % denominator * other.toJBInt(),
            denominator * other.toJBInt()
        )

    /**
     * Returns reminder from integral division.
     *
     * @throws ArithmeticException if [other] is the zero of the field it can't be a divisor.
     */
    operator fun rem(other: Long): Rational =
        Rational(
            numerator % denominator * other.toJBInt(),
            denominator * other.toJBInt()
        )

    /**
     * Checks equality of the instance to [other].
     *
     * [Integer], [Int] and [Long] values are also checked as Rational ones.
     */
    override fun equals(other: Any?): Boolean =
        when (other) {
            is Rational -> numerator == other.numerator && denominator == other.denominator
            is JBInt -> numerator == other && denominator == JBIOne
            is Int -> numerator == other && denominator == JBIOne
            is Long -> numerator == other && denominator == JBIOne
            else -> false
        }

    /**
     * Compares the instance to [other] as [Comparable.compareTo].
     *
     * @see Comparable.compareTo
     */
    override operator fun compareTo(other: Rational): Int = (numerator * other.denominator).compareTo(other.numerator * denominator)

    /**
     * Compares the instance to [other] as [Comparable.compareTo].
     *
     * [Integer] values are also checked as Rational ones.
     *
     * @see Comparable.compareTo
     */
    operator fun compareTo(other: JBInt): Int = (numerator).compareTo(denominator * other)

    /**
     * Compares the instance to [other] as [Comparable.compareTo].
     *
     * [Int] values are also checked as Rational ones.
     *
     * @see Comparable.compareTo
     */
    operator fun compareTo(other: Int): Int = (numerator).compareTo(denominator * other.toJBInt())

    /**
     * Compares the instance to [other] as [Comparable.compareTo].
     *
     * [Long] values are also checked as Rational ones.
     *
     * @see Comparable.compareTo
     */
    operator fun compareTo(other: Long): Int = (numerator).compareTo(denominator * other.toJBInt())

    override fun hashCode(): Int = 31 * numerator.hashCode() + denominator.hashCode()

//    /** Creates a range from this value to the specified [other] value. */
//    operator fun rangeTo(other: JBInt) = ClosedRationalRange(this, other.toRational())
//    /** Creates a range from this value to the specified [other] value. */
//    operator fun rangeTo(other: Rational) = ClosedRationalRange(this, other)
//    /** Creates a range from this value to the specified [other] value. */
//    operator fun rangeTo(other: Int) = ClosedRationalRange(this, other.toRational())
//    /** Creates a range from this value to the specified [other] value. */
//    operator fun rangeTo(other: Long) = ClosedRationalRange(this, other.toRational())

    fun toRational(): Rational = this

    fun toInteger(): JBInt = numerator / denominator

    fun toInt(): Int = (numerator / denominator).toInt()

    fun toLong(): Long = (numerator / denominator).toLong()

    fun toDouble(): Double = (numerator.toDouble() / denominator.toDouble())

    fun toFloat(): Float = (numerator.toFloat() / denominator.toFloat())

    override fun toString(): String = if (denominator == JBIOne) "$numerator" else "$numerator/$denominator"
}


/**
 * Algebraic structure for rational numbers.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
@UnstableKMathAPI
object RationalField : Field<Rational>, NumbersAddOps<Rational> {
    override inline val zero: Rational get() = Rational.ZERO
    override inline val one: Rational get() = Rational.ONE

    override inline fun number(value: Number): Rational = Rational(value.toLong())

    override inline fun add(left: Rational, right: Rational): Rational = left + right
    override inline fun multiply(left: Rational, right: Rational): Rational = left * right
    override inline fun divide(left: Rational, right: Rational): Rational = left / right
    override inline fun scale(a: Rational, value: Double): Rational = a * number(value)

    override inline fun Rational.unaryMinus(): Rational = -this
    override inline fun Rational.plus(arg: Rational): Rational = this + arg
    override inline fun Rational.minus(arg: Rational): Rational = this - arg
    override inline fun Rational.times(arg: Rational): Rational = this * arg
    override inline fun Rational.div(arg: Rational): Rational = this / arg
}