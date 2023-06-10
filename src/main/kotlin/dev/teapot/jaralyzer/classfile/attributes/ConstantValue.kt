package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.Util.Warn
import dev.teapot.jaralyzer.classfile.ClassFileReader
import dev.teapot.jaralyzer.classfile.cp.ICPBase

class ConstantValue(reader: ClassFileReader): IAttributeBase {
    var constantvalue: ICPBase?

    init {
        val data = reader.cp.entries[reader.ReadU2().toInt()]
        if (data == null) {
            Warn("Tried to read a constant pool entry that is empty")
        }

        constantvalue = null

        if (data != null) {
            constantvalue = data.data!!
        }
    }
}