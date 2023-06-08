package classfile.attributes

import classfile.ClassFileReader

// Dummy reader that just drops everything it gets
class Attributes(reader: ClassFileReader) {


    init {
        //TODO: Make this not a dummy
        val count = reader.ReadU2()
        for (i in 0 until count) {
            reader.ReadU2() //Name
            val len = reader.ReadU4() //len

            for (y in 0 until len) {
                reader.ReadU1() //data
            }
        }
    }
}