package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.classfile.ClassFileReader
import dev.teapot.jaralyzer.classfile.cp.ICPBase

class Signature(reader: ClassFileReader): IAttributeBase {
    var signature: ICPBase?

    init {
        val index = reader.ReadU2()
        signature = reader.cp[index]
    }
}