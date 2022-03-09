package space.kscience.kmath.varia

import space.kscience.kmath.functions.*
import space.kscience.kmath.operations.*
import kotlin.reflect.KProperty


data class Quadric<C> (
    val xx: LabeledPolynomial<C>,
    val yy: LabeledPolynomial<C>,
    val zz: LabeledPolynomial<C>,
    val xy: LabeledPolynomial<C>,
    val xz: LabeledPolynomial<C>,
    val yz: LabeledPolynomial<C>
) {
//    constructor(matrix: SquareMatrix<LabeledPolynomial<Rational>>) : this(
//        xx = matrix[0, 0].also { if (matrix.countOfRows != 3) throw IllegalArgumentException("Defining matrix should have sizes 3×3.")},
//        yy = matrix[1, 1],
//        zz = matrix[2, 2],
//        xy = 2 * matrix[0, 1].also { if (matrix[1, 0] != it) throw IllegalArgumentException("Defining matrix should be symmetric.") },
//        xz = 2 * matrix[0, 2].also { if (matrix[2, 0] != it) throw IllegalArgumentException("Defining matrix should be symmetric.") },
//        yz = 2 * matrix[1, 2].also { if (matrix[2, 1] != it) throw IllegalArgumentException("Defining matrix should be symmetric.") }
//    )
//
//    val matrix
//        get() =
//            SquareMatrix(
//                listOf(2 * xx, xy, xz),
//                listOf(xy, 2 * yy, yz),
//                listOf(xz, yz, 2 * zz)
//            )

//    override fun equals(other: Any?): Boolean =
//        if (other !is Quadric) false
//        else
//            xx * other.yy == yy * other.xx &&
//                    xx * other.zz == zz * other.xx &&
//                    xx * other.xy == xy * other.xx &&
//                    xx * other.xz == xz * other.xx &&
//                    xx * other.yz == yz * other.xx &&
//                    yy * other.zz == zz * other.yy &&
//                    yy * other.xy == xy * other.yy &&
//                    yy * other.xz == xz * other.yy &&
//                    yy * other.yz == yz * other.yy &&
//                    zz * other.xy == xy * other.zz &&
//                    zz * other.xz == xz * other.zz &&
//                    zz * other.yz == yz * other.zz &&
//                    xy * other.xz == xz * other.xy &&
//                    xy * other.yz == yz * other.xy &&
//                    xz * other.yz == yz * other.xz

    context(A)
    fun <A: Ring<C>> equals(other: Quadric<C>): Boolean = labeledPolynomialSpace {
        xx * other.yy == yy * other.xx &&
                xx * other.zz == zz * other.xx &&
                xx * other.xy == xy * other.xx &&
                xx * other.xz == xz * other.xx &&
                xx * other.yz == yz * other.xx &&
                yy * other.zz == zz * other.yy &&
                yy * other.xy == xy * other.yy &&
                yy * other.xz == xz * other.yy &&
                yy * other.yz == yz * other.yy &&
                zz * other.xy == xy * other.zz &&
                zz * other.xz == xz * other.zz &&
                zz * other.yz == yz * other.zz &&
                xy * other.xz == xz * other.xy &&
                xy * other.yz == yz * other.xy &&
                xz * other.yz == yz * other.xz
    }

    context(LabeledPolynomialSpace<C, A>)
    fun <A: Ring<C>> equals(other: Quadric<C>): Boolean =
        xx * other.yy == yy * other.xx &&
                xx * other.zz == zz * other.xx &&
                xx * other.xy == xy * other.xx &&
                xx * other.xz == xz * other.xx &&
                xx * other.yz == yz * other.xx &&
                yy * other.zz == zz * other.yy &&
                yy * other.xy == xy * other.yy &&
                yy * other.xz == xz * other.yy &&
                yy * other.yz == yz * other.yy &&
                zz * other.xy == xy * other.zz &&
                zz * other.xz == xz * other.zz &&
                zz * other.yz == yz * other.zz &&
                xy * other.xz == xz * other.xy &&
                xy * other.yz == yz * other.xy &&
                xz * other.yz == yz * other.xz

    override fun hashCode(): Int {
        var result = xx.hashCode()
        result = 31 * result + yy.hashCode()
        result = 31 * result + zz.hashCode()
        result = 31 * result + xy.hashCode()
        result = 31 * result + xz.hashCode()
        result = 31 * result + yz.hashCode()
        return result
    }

    companion object {
        context(A)
        operator fun <C, A: Ring<C>> getValue(thisRef: Any?, property: KProperty<*>) : Quadric<C> = Quadric(property.name)
        context(LabeledPolynomialSpace<C, A>)
        operator fun <C, A: Ring<C>> getValue(thisRef: Any?, property: KProperty<*>) : Quadric<C> = Quadric(property.name)
    }
}

context(A)
fun <C, A: Ring<C>> Quadric(name: String) : Quadric<C> = Quadric(
    Variable(name + "_xx").asLabeledPolynomial(),
    Variable(name + "_yy").asLabeledPolynomial(),
    Variable(name + "_zz").asLabeledPolynomial(),
    Variable(name + "_xy").asLabeledPolynomial(),
    Variable(name + "_xz").asLabeledPolynomial(),
    Variable(name + "_yz").asLabeledPolynomial(),
)

context(LabeledPolynomialSpace<C, A>)
fun <C, A: Ring<C>> Quadric(name: String) : Quadric<C> = Quadric(
    Variable(name + "_xx").asLabeledPolynomial(),
    Variable(name + "_yy").asLabeledPolynomial(),
    Variable(name + "_zz").asLabeledPolynomial(),
    Variable(name + "_xy").asLabeledPolynomial(),
    Variable(name + "_xz").asLabeledPolynomial(),
    Variable(name + "_yz").asLabeledPolynomial(),
)