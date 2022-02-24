package space.kscience.kmath.varia

import space.kscience.kmath.functions.*
import space.kscience.kmath.misc.*
import space.kscience.kmath.operations.*
import kotlin.reflect.KProperty


@OptIn(UnstableKMathAPI::class)
data class Point<C> (
    val x: LabeledPolynomial<C>,
    val y: LabeledPolynomial<C>,
    val z: LabeledPolynomial<C>
) {

//    val rowVector
//        get() = RowVector(x, y, z)
//    val columnVector
//        get() = ColumnVector(x, y, z)

//    override fun equals(other: Any?): Boolean =
//        if (other !is Point) false
//        else RationalField.labeledPolynomialSpace { x * other.y == y * other.x && y * other.z == z * other.y && z * other.x == x * other.z }

    context(A)
    fun <A: Ring<C>> equals(other: Line<C>): Boolean =
        labeledPolynomialSpace { x * other.y == y * other.x && y * other.z == z * other.y && z * other.x == x * other.z }

    context(LabeledPolynomialSpace<C, A>)
    fun <A: Ring<C>> equals(other: Line<C>): Boolean =
        x * other.y == y * other.x && y * other.z == z * other.y && z * other.x == x * other.z

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    companion object {
        context(A)
        operator fun <C, A: Ring<C>> getValue(thisRef: Any?, property: KProperty<*>) : Point<C> = Point(property.name)
        context(LabeledPolynomialSpace<C, A>)
        operator fun <C, A: Ring<C>> getValue(thisRef: Any?, property: KProperty<*>) : Point<C> = Point(property.name)
    }
}

context(A)
fun <C, A: Ring<C>> Point(name: String) : Point<C> = Point<C>(
    Variable(name + "_x").asLabeledPolynomial(),
    Variable(name + "_y").asLabeledPolynomial(),
    Variable(name + "_z").asLabeledPolynomial(),
)

context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> Point(name: String) : Point<C> = Point<C>(
    Variable(name + "_x").asLabeledPolynomial(),
    Variable(name + "_y").asLabeledPolynomial(),
    Variable(name + "_z").asLabeledPolynomial(),
)

// TODO
//
//    constructor(rowVector: RowVector<LabeledPolynomial<Rational>>) : this(
//        rowVector[0],
//        rowVector[1],
//        rowVector[2]
//    )
//
//    constructor(columnVector: ColumnVector<LabeledPolynomial<Rational>>) : this(
//        columnVector[0],
//        columnVector[1],
//        columnVector[2]
//    )