package dev.teapot.jaralyzer.classfile

import dev.teapot.jaralyzer.classfile.attributes.Attributes

class method_info(reader: ClassFileReader) {
    var access_flags: Short
    var name_index: Short
    var descriptor_index: Short
    var attributes: Attributes

    init {
        access_flags = reader.ReadU2()
        name_index = reader.ReadU2()
        descriptor_index = reader.ReadU2()
        attributes = Attributes(reader)
    }
}