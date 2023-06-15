package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.classfile.ClassFileReader

class Exceptions(reader: ClassFileReader): IAttributeBase {
    var exception_index_table: Array<Short>

    init {
        val count = reader.ReadU2().toInt()
        exception_index_table = Array<Short>(count){reader.ReadU2()}
    }
}