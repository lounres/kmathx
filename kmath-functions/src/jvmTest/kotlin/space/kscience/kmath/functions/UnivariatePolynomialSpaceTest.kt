package space.kscience.kmath.functions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import space.kscience.kmath.misc.*
import space.kscience.kmath.operations.*

internal class UnivariatePolynomialSpaceTest {
    @Test
    @OptIn(UnstableKMathAPI::class)
    fun verySimpleTest() {
        val result =
            RationalField.univariatePolynomialSpace {
                val pol = x * x + 5 * x + 7
                pol(Rational(-3))
            }

        assertEquals(Rational(1), result)
    }
}