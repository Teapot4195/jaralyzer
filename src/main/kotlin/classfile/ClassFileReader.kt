package classfile

import Util.Info
import Util.Warn
import classfile.attributes.Attributes
import java.io.File
import java.io.IOException

class ClassFileReader(path: String) {
    var file: File = File(path)

    var bytes = file.inputStream()

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
            Warn(String.format("Detected an unusual magic of 0x%08X", magic))
        }

        minor_version = ReadU2()
        major_version = ReadU2()

        Info(String.format("class version: 0x%04.0x%04", major_version, minor_version))

        cp = ConstantPool(this)

        access_flags = ReadU2()
        this_class = ReadU2()
        super_class = ReadU2()

        val interface_count: Int = ReadU2().toInt()
        interfaces = Array<Short>(interface_count){i -> ReadU2()}

        val field_count: Int = ReadU2().toInt()
        fields = Array<field_info>(field_count){i -> field_info(this) }

        val method_count: Int = ReadU2().toInt()
        methods = Array<method_info>(method_count){i -> method_info(this)}

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
