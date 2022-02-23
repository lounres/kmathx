@file:Suppress("NOTHING_TO_INLINE")
package space.kscience.kmath.operations


// region Sort of legacy

context(Group<C>)
inline fun <C> C.isZero() : Boolean = this == zero

context(Group<C>)
inline fun <C> C.isNotZero() : Boolean = this != zero

context(Ring<C>)
inline fun <C> C.isOne() : Boolean = this == one

context(Ring<C>)
inline fun <C> C.isNotOne() : Boolean = this != one

context(Ring<C>)
inline fun <C> C.isMinusOne() : Boolean = this == -one

context(Ring<C>)
inline fun <C> C.isNotMinusOne() : Boolean = this != -one

context(Group<C>)
operator fun <C> C.times(other: Int): C =
    when {
        other == 0 -> zero
        other == 1 -> this
        other == -1 -> -this
        other % 2 == 0 -> (this + this) * (other / 2)
        other % 2 == 1 -> addMultiplied(this, this + this, other / 2)
        other % 2 == -1 -> addMultiplied(-this, this + this, other / 2)
        else -> error("Error in multiplication group instant by integer: got reminder by division by 2 different from 0, 1 and -1")
    }

context(Group<C>)
internal tailrec fun <C> addMultiplied(base: C, arg: C, multiplier: Int): C =
    when {
        multiplier == 0 -> base
        multiplier == 1 -> base + arg
        multiplier == -1 -> base - arg
        multiplier % 2 == 0 -> addMultiplied(base, arg + arg, multiplier / 2)
        multiplier % 2 == 1 -> addMultiplied(base + arg, arg + arg, multiplier / 2)
        multiplier % 2 == -1 -> addMultiplied(base + arg, arg + arg, multiplier / 2)
        else -> error("Error in multiplication group instant by integer: got reminder by division by 2 different from 0, 1 and -1")
    }

context(Group<C>)
operator fun <C> Int.times(other: C): C = other * this

context(Ring<C>)
operator fun <C> C.plus(other: Int): C = this + one * other

context(Ring<C>)
operator fun <C> Int.plus(other: C): C = one * this + other

context(Ring<C>)
operator fun <C> C.minus(other: Int): C = this - one * other

context(Ring<C>)
operator fun <C> Int.minus(other: C): C = one * this - other

context(Ring<C>)
fun <C> number(value: Int): C = zero + value

context(Ring<C>)
fun <C> multiplyWithPower(base: C, arg: C, pow: UInt): C =
    when {
        arg.isZero() && pow > 0U -> base
        arg.isOne() -> base
        arg.isMinusOne() -> if (pow % 2U == 0U) base else -base
        else -> multiplyWithPowerInternalLogic(base, arg, pow)
    }

context(Ring<C>)
internal tailrec fun <C> multiplyWithPowerInternalLogic(base: C, arg: C, exponent: UInt): C =
    when {
        exponent == 0U -> base
        exponent == 1U -> base * arg
        exponent % 2U == 0U -> multiplyWithPowerInternalLogic(base, arg * arg, exponent / 2U)
        exponent % 2U == 1U -> multiplyWithPowerInternalLogic(base * arg, arg * arg, exponent / 2U)
        else -> error("Error in raising ring instant by unsigned integer: got reminder by division by 2 different from 0 and 1")
    }

// endregion