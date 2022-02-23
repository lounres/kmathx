package space.kscience.kmath.numberTheory

import java.math.*
import java.math.BigInteger.*

// region Java BigInteger

/**
 * Returns absolute value of the BigInteger.
 */
fun abs(a: BigInteger): BigInteger = a.abs()

/**
 * Computes [Greatest Common Divisor](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
 *
 * It's computed by [Euclidean algorithm](https://en.wikipedia.org/wiki/Greatest_common_divisor#Euclidean_algorithm).
 * Hence its time complexity is $$O(\log(a+b))$$ (see [Wolfram MathWorld](https://mathworld.wolfram.com/EuclideanAlgorithm.html)).
 */
tailrec fun gcd(a: BigInteger, b: BigInteger): BigInteger = if (a == ZERO) abs(b) else gcd(b % a, b)

/**
 * Computes [Greatest Common Divisor](https://en.wikipedia.org/wiki/Greatest_common_divisor) of the [values].
 */
fun gcd(vararg values: BigInteger): BigInteger = values.reduce(::gcd)
fun gcd(values: Iterable<BigInteger>): BigInteger = values.reduce(::gcd)

/**
 * Computes "the smallest" [Bézout coefficients](https://en.wikipedia.org/wiki/B%C3%A9zout%27s_identity) and
 * [GCD](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
 */
fun bezoutIdentityWithGCD(a: BigInteger, b: BigInteger): BezoutIdentityWithGCD<BigInteger> =
    when {
        a < ZERO && b < ZERO -> with(bezoutIdentityWithGCDInternalLogic(-a, -b, ONE, ZERO, ZERO, ONE)) { BezoutIdentityWithGCD(-first, -second, gcd) }
        a < ZERO -> with(bezoutIdentityWithGCDInternalLogic(-a, b, ONE, ZERO, ZERO, ONE)) { BezoutIdentityWithGCD(-first, second, gcd) }
        b < ZERO -> with(bezoutIdentityWithGCDInternalLogic(a, -b, ONE, ZERO, ZERO, ONE)) { BezoutIdentityWithGCD(first, -second, gcd) }
        else -> bezoutIdentityWithGCDInternalLogic(a, b, ONE, ZERO, ZERO, ONE)
    }

/**
 * Computes "the smallest" [Bézout coefficients](https://en.wikipedia.org/wiki/B%C3%A9zout%27s_identity) and
 * [GCD](https://en.wikipedia.org/wiki/Greatest_common_divisor) of [a] and [b].
 *
 * Also assumes that [a] and [b] are non-negative. TODO: Docs
 */
internal tailrec fun bezoutIdentityWithGCDInternalLogic(a: BigInteger, b: BigInteger, m1: BigInteger, m2: BigInteger, m3: BigInteger, m4: BigInteger): BezoutIdentityWithGCD<BigInteger> =
    if (b == ZERO) BezoutIdentityWithGCD(m1, m3, a)
    else {
        val (quotient, reminder) = a.divideAndRemainder(b)
        bezoutIdentityWithGCDInternalLogic(b, reminder, m2, m1 - quotient * m2, m4, m3 - quotient * m4)
    }

// TODO: It's not so simple to rewrite primality test for BigInteger...
///**
// * Trivial [primality test](https://en.wikipedia.org/wiki/Primality_test) by checking its divisibility by every integer
// * between its square root and 2.
// */
//fun BigInteger.isPrime(): Boolean {
//    val n = abs(this)
//
//    if (n <= ONE) return false
//
//    val sqRoot = n.sqrt()
//
//    val primeNumbers = MutableList(sqRoot + ONE) { true }
//
//    for (i in 2..sqRoot) {
//        if (primeNumbers[i]) {
//            if (n % i == 0) return false
//            for (j in 0..sqRoot / i) {
//                primeNumbers[i * j] = false
//            }
//        }
//    }
//    return true
//}
//
///**
// * Test for not primality of [this].
// */
//fun Int.isNotPrime(): Boolean = !isPrime()

/**
 * Computes [Kronecker symbol](https://en.wikipedia.org/wiki/Kronecker_symbol) of entries [a] and [b].
 */
@Suppress("NAME_SHADOWING")
fun kroneckerSymbol(a: BigInteger, b: BigInteger): Int {
    val mONE = BigInteger.valueOf(-1L)
    val THREE = BigInteger.valueOf(3L)
    val FOUR = BigInteger.valueOf(4L)
    val FIVE = BigInteger.valueOf(5L)
    val EIGHT = BigInteger.valueOf(8L)

    // Simple cases: when
    // `b` is `0`,
    if (b == ZERO) return if (a == ONE || a == mONE) 1 else 0
    // `a` is `0`,
    if (a == ZERO) return if (b == ONE || b == mONE) 1 else 0
    // `b` is `1`,
    if (b == ONE) return 1
    // or `b` is `-1`.
    if (b == mONE) return if (a >= ZERO) 1 else -1


    var a = a
    var b = b
    var result = 1

    // When 'b' is even
    if (b % TWO == ZERO) {
        // case of even 'a' is obvious,
        if (a % TWO == ZERO) return 0

        // otherwise eliminate all possible 4s (pairs of 2s in 'b')
        while (b % FOUR == ZERO) b /= FOUR

        // and if 'b' is still even
        if (b % TWO == ZERO) {
            // correct the result
            if ((abs(a) % EIGHT).let { it == THREE || it == FIVE }) result = -result
            // and divide 'b' by the only rest 2.
            b /= TWO
        }
    }

    // Now `a` is not zero, and `b` is not `0`, `1`, or `-1` and is odd.
    // We'll be updating the `result` preserving the conditions.

    // Each step is
    while (true) {
        // 1. Substitution of 'a' by its reminder of division of 'a' by 'b' preserving sign of 'a'. (Be aware that
        // trivial 'a % b' works only because of definition in kotlin standard library. See second paragraph in
        // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/rem.html .) We can do it because 'b' is odd.
        a %= b

        // 2. If 'a' is '0', then the result is obvious.
        if (a == ZERO) return 0
        // So now 'a' is still not '0', and 'b' is the same. But 'a' can be even.

        // 2. Removing 2s from 'a' in the same way as from 'b' before the cycle.
        if (a % TWO == ZERO) {
            while (a % FOUR == ZERO) a /= FOUR

            if (a % TWO == ZERO) {
                if ((abs(b) % EIGHT).let { it == THREE || it == FIVE }) result = -result
                a /= TWO
            }
        }
        // So now 'a' is not '0' and is odd, and 'b' is too.

        // 3. Now 'a' and 'b' are odd, so we can apply quadratic reciprocity law in a very simple way.
        a = b.also { b = a }
        if (a % FOUR == THREE && b % FOUR == THREE) result = -result
        if (a < ZERO && b < ZERO) result = -result
        // Now 'a' and 'b' are not '0' and are odd.

        // 4. If 'b' is '1' or '-1', the result is obvious and should be returned immediately.
        if (b == ONE) return result
        if (b == mONE) return result.let { if (a >= ZERO) it else -it }
        // Otherwise, we have that 'a' is not '0' and is odd, and 'b' is not '0', '1', or '-1' and is odd.
    }

    // With each step absolute value of 'a' drastically decreases, and it's swapped with 'b'. Thus, the process is
    // finite and fast. At least it's not slower than Euclidean algorithm, which time complexity is
    // $$O(\log(\max(a, b)))$$.
}

// endregion