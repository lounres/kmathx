package space.kscience.kmath.functions

import space.kscience.kmath.operations.*
import kotlin.jvm.JvmName

interface Polynomial<C>

@Suppress("INAPPLICABLE_JVM_NAME")
interface PolynomialSpace<C, P: Polynomial<C>> : Ring<P> {
    // region Constant-integer relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    operator fun C.plus(other: Int): C
//    operator fun C.minus(other: Int): C
//    operator fun C.times(other: Int): C
    // endregion

    // region Integer-constant relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    operator fun Int.plus(other: C): C
//    operator fun Int.minus(other: C): C
//    operator fun Int.times(other: C): C
    // endregion

    // region Polynomial-integer relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    operator fun P.plus(other: Int): C
//    operator fun P.minus(other: Int): C
//    operator fun P.times(other: Int): C
    // endregion

    // region Integer-polynomial relation
    // TODO: Очень хочется прибавлять/вычитать и умножать на целые числа, так как есть единственный (естественный) гомоморфизм
    //  из Z в любое другое кольцо.
//    operator fun Int.plus(other: P): C
//    operator fun Int.minus(other: P): C
//    operator fun Int.times(other: P): C
    // endregion

    // region Constant-constant relation
    @JvmName("constantUnaryPlus")
    operator fun C.unaryPlus(): C = this
    @JvmName("constantUnaryMinus")
    operator fun C.unaryMinus(): C
    @JvmName("constantPlus")
    operator fun C.plus(other: C): C
    @JvmName("constantMinus")
    operator fun C.minus(other: C): C
    @JvmName("constantTimes")
    operator fun C.times(other: C): C
    // endregion

    // region Constant-polynomial relation
    operator fun C.plus(other: P): P
    operator fun C.minus(other: P): P
    operator fun C.times(other: P): P
    // endregion

    // region Polynomial-constant relation
    operator fun P.plus(other: C): P
    operator fun P.minus(other: C): P
    operator fun P.times(other: C): P
    // endregion

    // region Polynomial-polynomial relation
    override operator fun P.unaryPlus(): P = this
    override operator fun P.unaryMinus(): P
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override operator fun P.plus(other: P): P
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override operator fun P.minus(other: P): P
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override operator fun P.times(other: P): P

    override val zero: P
    override val one: P

    @Suppress("EXTENSION_SHADOWED_BY_MEMBER", "CovariantEquals")
    fun P.equals(other: P): Boolean
    // endregion

    // Not sure is it necessary...
    // region Polynomial properties
    /**
     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
     * zero, degree is -1.
     */
    val P.degree: Int

    /**
     * Checks if the instant is constant polynomial (of degree no more than 0) over considered ring.
     */
    fun P.isConstant(): Boolean = degree <= 0
    /**
     * Checks if the instant is **not** constant polynomial (of degree no more than 0) over considered ring.
     */
    fun P.isNotConstant(): Boolean = !isConstant()
    /**
     * Checks if the instant is constant non-zero polynomial (of degree no more than 0) over considered ring.
     */
    fun P.isNonZeroConstant(): Boolean = degree == 0
    /**
     * Checks if the instant is **not** constant non-zero polynomial (of degree no more than 0) over considered ring.
     */
    fun P.isNotNonZeroConstant(): Boolean = !isNonZeroConstant()

    fun P.asConstantOrNull(): C?

    fun P.asConstant(): C = asConstantOrNull() ?: error("Can not represent non-constant polynomial as a constant")

    // TODO: Перенести в реализацию
//    fun P.substitute(argument: C): C
//    fun P.substitute(argument: P): P
//
//    fun P.asFunction(): (C) -> C = /*this::substitute*/ { this.substitute(it) }
//    fun P.asFunctionOnConstants(): (C) -> C = /*this::substitute*/ { this.substitute(it) }
//    fun P.asFunctionOnPolynomials(): (P) -> P = /*this::substitute*/ { this.substitute(it) }
//
//    operator fun P.invoke(argument: C): C = this.substitute(argument)
//    operator fun P.invoke(argument: P): P = this.substitute(argument)
    // endregion

    // region Legacy
    override fun add(left: P, right: P): P = left + right
    override fun multiply(left: P, right: P): P = left * right
    // endregion
}

interface RationalFunction<C, P: Polynomial<C>>

//interface RationalFunctionalSpace<C, P: Polynomial<C>, R: RationalFunction<C, P>> : Ring<R> {
//    // region Constant-constant relation
//    operator fun C.unaryPlus(): C = this
//    operator fun C.unaryMinus(): C
//    operator fun C.plus(other: C): C
//    operator fun C.minus(other: C): C
//    operator fun C.times(other: C): C
//    // endregion
//
//    // region Constant-polynomial relation
//    operator fun C.plus(other: P): P
//    operator fun C.minus(other: P): P
//    operator fun C.times(other: P): P
//    // endregion
//
//    // region Polynomial-constant relation
//    operator fun P.plus(other: C): P
//    operator fun P.minus(other: C): P
//    operator fun P.times(other: C): P
//    // endregion
//
//    // region Polynomial-polynomial relation
//    operator fun P.unaryPlus(): P = this
//    operator fun P.unaryMinus(): P
//    operator fun P.plus(other: P): P
//    operator fun P.minus(other: P): P
//    operator fun P.times(other: P): P
//    // endregion
//
//    // region Constant-rational relation
//    operator fun C.plus(other: R): R
//    operator fun C.minus(other: R): R
//    operator fun C.times(other: R): R
//    // endregion
//
//    // region Rational-constant relation
//    operator fun R.plus(other: C): R
//    operator fun R.minus(other: C): R
//    operator fun R.times(other: C): R
//    // endregion
//
//    // region Polynomial-rational relation
//    operator fun P.plus(other: R): R
//    operator fun P.minus(other: R): R
//    operator fun P.times(other: R): R
//    // endregion
//
//    // region Rational-polynomial relation
//    operator fun R.plus(other: P): R
//    operator fun R.minus(other: P): R
//    operator fun R.times(other: P): R
//    // endregion
//
//    // region Rational-rational relation
//    override operator fun R.unaryPlus(): R = this
//    override operator fun R.unaryMinus(): R
//    override operator fun R.plus(other: R): R
//    override operator fun R.minus(other: R): R
//    override operator fun R.times(other: R): R
//
//    override val one: R
//    override val zero: R
//
//    fun R.equals(other: R): Boolean
//    // endregion
//
//    // Not sure is it necessary...
//    // region Polynomial properties
//    /**
//     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
//     * zero, degree is -1.
//     */
//    val P.degree: Int
//
//    /**
//     * Checks if the instant is constant polynomial (of degree no more than 0) over considered ring.
//     */
//    fun P.isConstant(): Boolean = degree <= 0
//    /**
//     * Checks if the instant is **not** constant polynomial (of degree no more than 0) over considered ring.
//     */
//    fun P.isNotConstant(): Boolean = !isConstant()
//    /**
//     * Checks if the instant is constant non-zero polynomial (of degree no more than 0) over considered ring.
//     */
//    fun P.isNonZeroConstant(): Boolean = degree == 0
//    /**
//     * Checks if the instant is **not** constant non-zero polynomial (of degree no more than 0) over considered ring.
//     */
//    fun P.isNotNonZeroConstant(): Boolean = !isNonZeroConstant()
//
//    fun P.asConstantOrNull(): C?
//
//    fun P.asConstant(): C = asConstantOrNull() ?: error("Can not represent non-constant polynomial as a constant")
//
//    // TODO: Перенести в реализацию
////    fun P.substitute(argument: C): C
////    fun P.substitute(argument: P): P
////
////    fun P.asFunction(): (C) -> C = /*this::substitute*/ { this.substitute(it) }
////    fun P.asFunctionOnConstants(): (C) -> C = /*this::substitute*/ { this.substitute(it) }
////    fun P.asFunctionOnPolynomials(): (P) -> P = /*this::substitute*/ { this.substitute(it) }
////
////    operator fun P.invoke(argument: C): C = this.substitute(argument)
////    operator fun P.invoke(argument: P): P = this.substitute(argument)
//    // endregion
//
//    // Not sure is it necessary...
//    // region Polynomial properties
//    /**
//     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
//     * zero, degree is -1.
//     *
//     * // TODO: Determine "best definition" of degre
//     */
//    val R.degree: Int
//
//    /**
//     * Checks if the instant is constant polynomial (of degree no more than 0) over considered ring.
//     */
//    fun R.isConstant(): Boolean
//    /**
//     * Checks if the instant is **not** constant polynomial (of degree no more than 0) over considered ring.
//     */
//    fun R.isNotConstant(): Boolean = !isConstant()
//    /**
//     * Checks if the instant is constant non-zero polynomial (of degree no more than 0) over considered ring.
//     */
//    fun R.isNonZeroConstant(): Boolean
//    /**
//     * Checks if the instant is **not** constant non-zero polynomial (of degree no more than 0) over considered ring.
//     */
//    fun R.isNotNonZeroConstant(): Boolean = !isNonZeroConstant()
//
//    fun R.asConstantOrNull(): C?
//
//    fun R.asConstant(): C = asConstantOrNull() ?: error("Can not represent non-constant polynomial as a constant")
//
//    // TODO: Перенести в реализацию
////    fun R.substitute(argument: C): C
////    fun R.substitute(argument: P): R
////    fun R.substitute(argument: R): R
////
////    fun R.asFunction(): (C) -> C = /*this::substitute*/ { this.substitute(it) }
////    fun R.asFunctionOnConstants(): (C) -> C = /*this::substitute*/ { this.substitute(it) }
////    fun P.asFunctionOnPolynomials(): (P) -> R = /*this::substitute*/ { this.substitute(it) }
////    fun R.asFunctionOnRationalFunctions(): (R) -> R = /*this::substitute*/ { this.substitute(it) }
////
////    operator fun R.invoke(argument: C): C = this.substitute(argument)
////    operator fun R.invoke(argument: P): R = this.substitute(argument)
////    operator fun R.invoke(argument: R): R = this.substitute(argument)
//    // endregion
//
//    // region Legacy
//    override fun add(left: R, right: R): R = left + right
//    override fun multiply(left: R, right: R): R = left * right
//    // endregion
//}