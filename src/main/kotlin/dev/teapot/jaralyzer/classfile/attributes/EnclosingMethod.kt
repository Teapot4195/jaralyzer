package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.classfile.ClassFileReader

class EnclosingMethod(reader: ClassFileReader): IAttributeBase {
    val class_index: Short
    val method_index: Short

    init {
        class_index = reader.ReadU2()
        method_index = reader.ReadU2()
    }
}