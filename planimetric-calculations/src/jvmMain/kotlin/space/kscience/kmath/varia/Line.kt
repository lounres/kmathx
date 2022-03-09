package space.kscience.kmath.varia

import space.kscience.kmath.functions.*
import space.kscience.kmath.misc.*
import space.kscience.kmath.operations.*
import kotlin.reflect.KProperty

/**
 * Stores Line on projective plane as triplet of coefficients of its equation. The coefficients are determined with
 * accuracy to multiplication all of them by any constant.
 *
 * @property x Coefficient before 'x' variable.
 * @property y Coefficient before 'y' variable.
 * @property z Coefficient before 'z' variable.
 *
 * @constructor Constructs line with given values. Just stores them and does nothing.
 * @param x Coefficient before 'x' variable.
 * @param y Coefficient before 'y' variable.
 * @param z Coefficient before 'z' variable.
 */
data class Line<C> (
    val x: LabeledPolynomial<C>,
    val y: LabeledPolynomial<C>,
    val z: LabeledPolynomial<C>
) {

//    /**
//     * Row vector representation.
//     */
//    val rowVector
//        get() = RowVector(x, y, z)
//    /**
//     * Column vector representation.
//     */
//    val columnVector
//        get() = ColumnVector(x, y, z)

    /**
     * Checks equality of the line to [other] object. True is returned if and only if [other] object is [Line] and has
     * proportional coefficients (i.e. [this] line and [other] line has equivalent equations).
     */
//    override fun equals(other: Any?): Boolean =
//        if (other !is Line<*>) false
//        else RationalField.labeledPolynomialSpace { x * other.y == y * other.x && y * other.z == z * other.y && z * other.x == x * other.z }

    /**
     * Checks equality of the line to [other] object. True is returned if and only if [other] object is [Line] and has
     * proportional coefficients (i.e. [this] line and [other] line has equivalent equations).
     */
    context(A)
    fun <A: Ring<C>> equals(other: Line<C>): Boolean =
        labeledPolynomialSpace { x * other.y == y * other.x && y * other.z == z * other.y && z * other.x == x * other.z }

    /**
     * Checks equality of the line to [other] object. True is returned if and only if [other] object is [Line] and has
     * proportional coefficients (i.e. [this] line and [other] line has equivalent equations).
     */
    context(LabeledPolynomialSpace<C, A>)
    fun <A: Ring<C>> equals(other: Line<C>): Boolean =
        x * other.y == y * other.x && y * other.z == z * other.y && z * other.x == x * other.z

    /**
     * Trivial hash code generator.
     */
    override fun hashCode(): Int = 1

    companion object {
        context(A)
        operator fun <C, A: Ring<C>> getValue(thisRef: Any?, property: KProperty<*>) : Line<C> = Line(property.name)
        context(LabeledPolynomialSpace<C, A>)
        operator fun <C, A: Ring<C>> getValue(thisRef: Any?, property: KProperty<*>) : Line<C> = Line(property.name)
    }
}

/**
 * Constructs arbitrary line with given [name]. Hence it's coefficients are just variables that get names after
 * [name]: coefficient before variable x will be variable [name]_x, and the same goes for y and z.
 *
 * @param name The name of the line.
 */
context(A)
fun <C, A: Ring<C>> Line(name: String) : Line<C> = Line<C>(
    Variable(name + "_x").asLabeledPolynomial(),
    Variable(name + "_y").asLabeledPolynomial(),
    Variable(name + "_z").asLabeledPolynomial(),
)

/**
 * Constructs arbitrary line with given [name]. Hence it's coefficients are just variables that get names after
 * [name]: coefficient before variable x will be variable [name]_x, and the same goes for y and z.
 *
 * @param name The name of the line.
 */
context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> Line(name: String) : Line<C> = Line<C>(
    Variable(name + "_x").asLabeledPolynomial(),
    Variable(name + "_y").asLabeledPolynomial(),
    Variable(name + "_z").asLabeledPolynomial(),
)

// TODO
//
//    /**
//     * Constructs line from the tuple. Hence the [rowVector] must contain exactly 3 elements (must have size 3⨯1).
//     *
//     * @param rowVector The input row vector.
////     * @throws
//     */
//    constructor(rowVector: RowVector<LabeledPolynomial<Rational>>) : this(
//        rowVector[0],
//        rowVector[1],
//        rowVector[2]
//    )
//
//    /**
//     * Constructs line from the tuple. Hence the [columnVector] must contain exactly 3 elements (must have size 1⨯3).
//     *
//     * @param columnVector The input column vector.
////     * @throws
//     */
//    constructor(columnVector: ColumnVector<LabeledPolynomial<Rational>>) : this(
//        columnVector[0],
//        columnVector[1],
//        columnVector[2]
//    )