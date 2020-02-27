package io.github.keep2iron.fast4android.arch.util

import java.io.*

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