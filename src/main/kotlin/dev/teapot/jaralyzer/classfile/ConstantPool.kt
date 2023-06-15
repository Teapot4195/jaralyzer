package dev.teapot.jaralyzer.classfile

import dev.teapot.jaralyzer.Util.Warn
import dev.teapot.jaralyzer.classfile.cp.CPMethodHandleKind
import dev.teapot.jaralyzer.classfile.cp.*
import java.lang.StringBuilder

enum class ConstantPoolEntryType {
    CClass, CFieldRef, CMethodRef, CInterfaceMethodRef, CString, CInteger, CFloat, CLong, CDouble, CNameAndType,
    CUtf8, CMethodHandle, CMethodType, CDynamic, CInvokeDynamic, CModule, CPackage
}

class ConstantPoolEntry(t: ConstantPoolEntryType?, d: ICPBase?) {
    var type: ConstantPoolEntryType? = t
    var data: ICPBase? = d
}

class ConstantPool(reader: ClassFileReader) {
    var entries: Array<ConstantPoolEntry?>
    var size: Short

    fun Resolve() {
        //TODO: Resolve CPRefs to point to the correct entry
    }

    operator fun get(index: Short): ICPBase? {
        val v = entries[index.toInt()]
        if (v != null) {
            return v.data
        } else {
            throw Exception("Data was not valid")
        }
    }

    init {
        size = reader.ReadU2()

        entries = Array<ConstantPoolEntry?>(size.toInt()) { i -> null}

        var skip: Boolean = false

        for(i in 1 until size) {
            var type: ConstantPoolEntryType?
            var data: ICPBase?

            if (skip) {
                skip = false
                continue
            }

            val tag = reader.ReadU1().toInt()
            when(tag) {
                1 -> {
                    type = ConstantPoolEntryType.CUtf8
                    val s: StringBuilder = StringBuilder()
                    val size = reader.ReadU2()
                    for (x in 0 until size)
                        s.append(reader.ReadU1())
                    data = CPUTF8(s.toString())
                }
                3 -> {
                    type = ConstantPoolEntryType.CInteger
                    data = CPInt(reader.ReadU4())
                }
                4 -> {
                    type = ConstantPoolEntryType.CFloat
                    data = CPFloat(reader.ReadU4().toFloat())
                }
                5 -> {
                    type = ConstantPoolEntryType.CLong
                    data = CPLong((reader.ReadU4().toLong() shl 32) or (reader.ReadU4().toLong()))
                    skip = true
                }
                6 -> {
                    //TODO: I don't know if this works :(
                    type = ConstantPoolEntryType.CDouble
                    data = CPDouble(((reader.ReadU4().toLong() shl 32) or (reader.ReadU4().toLong())).toDouble())
                    skip = true
                }
                7 -> {
                    type = ConstantPoolEntryType.CClass
                    data = CPClass(CPRef(reader.ReadU2().toInt()))
                }
                8 -> {
                    type = ConstantPoolEntryType.CString
                    data = CPString(CPRef(reader.ReadU2().toInt()))
                }
                9 -> {
                    type = ConstantPoolEntryType.CFieldRef
                    data = CPFieldref(CPRef(reader.ReadU2().toInt()), CPRef(reader.ReadU2().toInt()))
                }
                10 -> {
                    type = ConstantPoolEntryType.CMethodRef
                    data = CPMethodref(CPRef(reader.ReadU2().toInt()), CPRef(reader.ReadU2().toInt()))
                }
                11 -> {
                    type = ConstantPoolEntryType.CInterfaceMethodRef
                    data = CPInterfaceMethodRef(CPRef(reader.ReadU2().toInt()), CPRef(reader.ReadU2().toInt()))
                }
                12 -> {
                    type = ConstantPoolEntryType.CNameAndType
                    data = CPNameAndType(CPRef(reader.ReadU2().toInt()), CPRef(reader.ReadU2().toInt()))
                }
                15 -> {
                    type = ConstantPoolEntryType.CMethodHandle
                    data = CPMethodHandle(CPMethodHandleKind.fromInt(reader.ReadU1()), CPRef(reader.ReadU2().toInt()))
                }
                16 -> {
                    type = ConstantPoolEntryType.CMethodType
                    data = CPMethodType(CPRef(reader.ReadU2().toInt()))
                }
                17 -> {
                    type = ConstantPoolEntryType.CDynamic
                    data = CPDynamic(CPRef(reader.ReadU2().toInt()), CPRef(reader.ReadU2().toInt()))
                }
                18 -> {
                    type = ConstantPoolEntryType.CInvokeDynamic
                    data = CPInvokeDynamic(CPRef(reader.ReadU2().toInt()), CPRef(reader.ReadU2().toInt()))
                }
                19 -> {
                    type = ConstantPoolEntryType.CModule
                    data = CPModule(CPRef(reader.ReadU2().toInt()))
                }
                20 -> {
                    type = ConstantPoolEntryType.CPackage
                    data = CPPackage(CPRef(reader.ReadU2().toInt()))
                }
                else -> {
                    Warn(String.format("Unknown tag: %d", tag))
                    type = null
                    data = null
                }
            }

            entries[i] = ConstantPoolEntry(type, data)
        }
    }
}