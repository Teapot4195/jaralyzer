package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.Util.flags.attributes.Attributes_BadTag
import dev.teapot.jaralyzer.classfile.ClassFileReader
import dev.teapot.jaralyzer.classfile.cp.CPUTF8

// Dummy reader that just drops everything it gets
class Attributes(reader: ClassFileReader) {
    var attributes: Array<IAttributeBase?>

    init {
        //TODO: Make this not a dummy
        val count = reader.ReadU2().toInt()
        attributes = Array<IAttributeBase?>(count){null}
        for (i in 0 until count) {
            val name = (reader.cp.entries[reader.ReadU2().toInt()]?.data as CPUTF8).bytes
            val len = reader.ReadU4() //len

            val attr: IAttributeBase? = when (name) {
                "ConstantValue" -> ConstantValue(reader)
                "Code" -> Code(reader)
                "StackMapTable" -> StackMapTable(reader)
                "Exceptions" -> Exceptions(reader)
                "InnerClasses" -> InnerClasses(reader)
                "EnclosingMethod" -> EnclosingMethod(reader)
                "Synthetic" -> Synthetic()
                "Signature" -> Signature(reader)
                "SourceFile" -> SourceFile(reader)
                else -> {
                    reader.flags.add(Attributes_BadTag("Expected Valid Tag", "Got \"$name\" instead"))
                    for (y in 0 until len) {
                        reader.ReadU1() //data
                    }
                    null
                }
            }

            attributes[i] = attr
        }
    }
}