package space.kscience.kmath.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.*


/**
 * Checks if the collection's size is at least the given size.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Iterable<T>.atLeast(bound: Int): Boolean {
    if (bound <= 0) return true
    if (this is Collection) return size >= bound
    var rest = bound
    for (element in this) {
        rest -= 1
        if (rest == 0) return true
    }
    return false
}

/**
 * Checks if the collection's size is at most the given size.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Iterable<T>.atMost(bound: Int): Boolean {
    if (bound < 0) return false
    if (this is Collection) return size <= bound
    var rest = bound
    for (element in this) {
        if (rest == 0) return false
        rest -= 1
    }
    return true
}

/**
 * Checks if the collection's size is at most the given size.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Iterable<T>.exactly(bound: Int): Boolean {
    if (bound < 0) return false
    if (this is Collection) return size == bound
    var rest = bound
    with (this.iterator()) {
        for (element in this) {
            rest -= 1
            if (rest == 0) return !hasNext()
        }
    }
    return false
}

/**
 * Checks if the number of elements matching the given [predicate] is at least the given bound.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> Iterable<T>.atLeast(bound: Int, predicate: (T) -> Boolean): Boolean {
    contract { callsInPlace(predicate, InvocationKind.EXACTLY_ONCE) }
    if (bound <= 0) return true
    if (this is Collection && size < bound) return false
    var rest = bound
    for (element in this) if (predicate(element)) {
        rest -= 1
        if (rest == 0) return true
    }
    return false
}

/**
 * Checks if the number of elements matching the given [predicate] is at most the given bound.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> Iterable<T>.atMost(bound: Int, predicate: (T) -> Boolean): Boolean {
    contract { callsInPlace(predicate, InvocationKind.EXACTLY_ONCE) }
    if (bound < 0) return false
    if (this is Collection && size <= bound) return true
    var rest = bound
    for (element in this) if (predicate(element)) {
        if (rest == 0) return false
        rest -= 1
    }
    return true
}

/**
 * Checks if the collection's size is at most the given size.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> Iterable<T>.exactly(bound: Int, predicate: (T) -> Boolean): Boolean {
    contract { callsInPlace(predicate, InvocationKind.EXACTLY_ONCE) }
    if (bound < 0) return false
    if (this is Collection && size < bound) return false
    var rest = bound
    for (element in this) if (predicate(element)) {
        if (rest == 0) return false
        rest -= 1
    }
    return rest == 0
}

/**
 * Returns `true` if all elements with correspondent indices match the given [predicate].
 */
inline fun <T> Iterable<T>.allIndexed(predicate: (index: Int, T) -> Boolean): Boolean {
    if (this is Collection && isEmpty()) return true
    forEachIndexed { index, element -> if (!predicate(index, element)) return false }
    return true
}

/**
 * Returns `true` if any element with correspondent index matches the given [predicate].
 */
inline fun <T> Iterable<T>.anyIndexed(predicate: (index: Int, T) -> Boolean): Boolean {
    if (this is Collection && isEmpty()) return false
    forEachIndexed { index, element -> if (predicate(index, element)) return true }
    return false
}