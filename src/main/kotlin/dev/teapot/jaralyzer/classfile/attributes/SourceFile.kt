package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.classfile.ClassFileReader
import dev.teapot.jaralyzer.classfile.cp.ICPBase

class SourceFile(reader: ClassFileReader): IAttributeBase {
    var sourcefile: ICPBase?

    init {
        val index = reader.ReadU2()

        sourcefile = reader.cp[index]
    }
}