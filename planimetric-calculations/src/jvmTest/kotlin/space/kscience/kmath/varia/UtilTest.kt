package space.kscience.kmath.varia

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import space.kscience.kmath.functions.*
import space.kscience.kmath.misc.*
import space.kscience.kmath.operations.*

internal class UtilTest {
    @Test
    @OptIn(UnstableKMathAPI::class)
    fun eulerLineCheck() {
        RationalField.labeledPolynomialSpace {
            val A by Point
            val B by Point
            val C by Point

            val M = centroid(A, B, C)
            val H = orthocenter(A, B, C)
            val O = circumcenter(A, B, C)

            assertTrue(collinearityCondition(M, H, O).isZero())
        }
    }
}