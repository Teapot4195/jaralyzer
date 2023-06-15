package dev.teapot.jaralyzer.Util

enum class FlagType (val Type: String) {
    JarFile("Jar_File"), // Lists an issue found with the jar file (e.g. The overflowing of a read limit)
    StackMapTableAttr("Attr_StackMapTable"), // Lists issues found with the StackMapTable
}