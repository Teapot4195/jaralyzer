package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.classfile.ClassFileReader

class ExceptionEntry(reader: ClassFileReader) {
    var start_pc: Short
    var end_pc: Short
    var handler_pc: Short
    var catch_type: Short

    init {
        start_pc = reader.ReadU2()
        end_pc = reader.ReadU2()
        handler_pc = reader.ReadU2()
        catch_type = reader.ReadU2()
    }
}

class Code(reader: ClassFileReader): IAttributeBase {
    var max_stack: Short
    var max_locals: Short
    var code: Array<Byte>
    var exception_table: Array<ExceptionEntry>
    var attributes: Attributes

    init {
        max_stack = reader.ReadU2()
        max_locals = reader.ReadU2()
        val code_length = reader.ReadU4()
        code = Array<Byte>(code_length){reader.ReadU1()}
        val exception_table_length = reader.ReadU2()
        exception_table = Array<ExceptionEntry>(exception_table_length.toInt()){ExceptionEntry(reader)}
        attributes = Attributes(reader)
    }
}