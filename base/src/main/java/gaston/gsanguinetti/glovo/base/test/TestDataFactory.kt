package gaston.gsanguinetti.glovo.base.test

import java.util.concurrent.ThreadLocalRandom

/**
 * Factory functions for data instances
 */

fun randomString(): String = java.util.UUID.randomUUID().toString()

fun randomInt(start: Int = 1, end: Int = 1000): Int =
    ThreadLocalRandom.current().nextInt(start, end + 1)

fun randomDouble(): Double = ThreadLocalRandom.current().nextDouble()

fun randomLong(): Long = randomInt().toLong()

fun randomBoolean(): Boolean = Math.random() < 0.5

fun randomStringList(count: Int): List<String> {
    val items: MutableList<String> = mutableListOf()
    repeat(count) {
        items.add(randomString())
    }
    return items
}