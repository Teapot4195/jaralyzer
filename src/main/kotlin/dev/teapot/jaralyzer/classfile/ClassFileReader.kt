package dev.teapot.jaralyzer.classfile

import dev.teapot.jaralyzer.Util.FlagManager
import dev.teapot.jaralyzer.Util.Info
import dev.teapot.jaralyzer.classfile.attributes.Attributes
import dev.teapot.jaralyzer.classfile.attributes.Exceptions
import java.io.IOException
import java.io.InputStream

class IException(): Exception() {}

class ClassFileReader(f: InputStream) {
    var flags: FlagManager = FlagManager()

    var bytes = f

    var minor_version: Short
    var major_version: Short

    var cp: ConstantPool

    var access_flags: Short
    var this_class: Short
    var super_class: Short

    var interfaces: Array<Short>
    var fields: Array<field_info>
    var methods: Array<method_info>
    var attributes: Attributes

    init {
        //Magic
        val magic = ReadU4()
        if (magic.toLong() != 0xCAFEBABE) {
            throw IException()
        }

        minor_version = ReadU2()
        major_version = ReadU2()

        Info(String.format("class version: 0x%04.0x%04", major_version, minor_version))

        cp = ConstantPool(this)

        access_flags = ReadU2()
        this_class = ReadU2()
        super_class = ReadU2()

        val interface_count: Int = ReadU2().toInt()
        interfaces = Array<Short>(interface_count){ReadU2()}

        val field_count: Int = ReadU2().toInt()
        fields = Array<field_info>(field_count){field_info(this)}

        val method_count: Int = ReadU2().toInt()
        methods = Array<method_info>(method_count){method_info(this)}

        attributes = Attributes(this)
    }

    fun ReadU4(): Int { //TODO: This isn't spec compliant
        try {
            val buffer = bytes.readNBytes(4)
            if (buffer.size != 4) {
                throw IOException("Reached EOF before finishing read")
            }
            return ((buffer[0].toInt() and 0xff) or
                    (buffer[1].toInt() and 0xff shl 8) or
                    (buffer[2].toInt() and 0xff shl 16) or
                    (buffer[3].toInt() and 0xff shl 24))
        } catch (e: IOException) {
            System.err.print("Exception for FileInputStream.readNBytes caught")
            throw IOException(e)
        }
    }

    fun ReadU2(): Short { //TODO: This isn't spec compliant
        try {
            val buffer = bytes.readNBytes(2)
            if (buffer.size != 2) {
                throw IOException("Reached EOF before finishing read")
            }
            return ((buffer[0].toInt() and 0xff) or
                    (buffer[1].toInt() and 0xff shl 8)).toShort()
        } catch (e: IOException) {
            System.err.print("Exception for FileInputStream.readNBytes caught")
            throw IOException(e)
        }
    }

    fun ReadU1(): Byte { //TODO: This isn't spec compliant
        try {
            val buffer = bytes.readNBytes(1)
            if (buffer.size != 1) {
                throw IOException("Reached EOF before finishing read")
            }
            return buffer[0]
        } catch (e: IOException) {
            System.err.print("Exception for FileInputStream.readNBytes caught")
            throw IOException(e)
        }
    }
}
