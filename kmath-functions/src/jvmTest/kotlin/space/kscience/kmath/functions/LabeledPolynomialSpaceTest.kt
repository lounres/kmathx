package space.kscience.kmath.functions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import space.kscience.kmath.misc.*
import space.kscience.kmath.operations.*

internal class LabeledPolynomialSpaceTest {
    @Test
    @OptIn(UnstableKMathAPI::class)
    fun verySimpleTest() {
        val result =
            RationalField.labeledPolynomialSpace {
                val x by Variable
                val y by Variable
                val pol = LabeledPolynomial(mapOf( // 5 x^2 y + 3 y^3
                    mapOf(
                        x to 2U,
                        y to 1U
                    ) to Rational(5),
                    mapOf(
                        y to 3U
                    ) to Rational(3),
                ))
                pol(mapOf(
                    x to Rational(-2), // x -> -2
                    y to Rational(7), // y -> 7
                )).asConstant()
            }

        assertEquals(Rational(1169), result)
    }
}