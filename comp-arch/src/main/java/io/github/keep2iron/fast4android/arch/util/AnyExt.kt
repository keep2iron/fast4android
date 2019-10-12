package io.github.keep2iron.fast4android.arch.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Deep copy
 * [https://rosettacode.org/wiki/Deepcopy#Kotlin]
 */
fun <T : Serializable> Any.deepCopy(): T {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(this)
    oos.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois = ObjectInputStream(bais)
    @Suppress("unchecked_cast")
    return ois.readObject() as T
}