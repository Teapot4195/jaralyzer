package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.classfile.ClassFileReader

class InnerClassesEntry(
    val inner_class_info_index: Short,
    val outer_class_info_index: Short,
    val inner_name_index: Short,
    val inner_class_access_flags: Short
)

class InnerClasses(reader: ClassFileReader) : IAttributeBase {
    var classes: Array<InnerClassesEntry>

    init {
        val count = reader.ReadU2()

        classes = Array<InnerClassesEntry>(count.toInt()) {InnerClassesEntry(
            reader.ReadU2(),
            reader.ReadU2(),
            reader.ReadU2(),
            reader.ReadU2()
        )}
    }
}