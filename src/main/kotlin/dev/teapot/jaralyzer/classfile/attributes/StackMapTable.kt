package dev.teapot.jaralyzer.classfile.attributes

import dev.teapot.jaralyzer.Util.flags.annotations.StackMapTable_BadVTITag
import dev.teapot.jaralyzer.classfile.ClassFileReader
import dev.teapot.jaralyzer.classfile.cp.CPRef
import dev.teapot.jaralyzer.classfile.cp.ICPBase

interface IStack_map_frame_base
open class IVTI_Base(t: UByte) {
    var tag: UByte = t
}

enum class VTI_Tag(val i: UByte) {
    Integer(1u),
    Float(2u),
    Double(3u),
    Long(4u),
    Null(5u),
    UninitializedThis(6u),
    Object(7u),
    Uninitialized(8u),
    Unknown(0u);

    companion object {
        private val VALUES = values()
        fun getByValue(value: UByte): VTI_Tag {
            return VALUES.firstOrNull() { it.i == value } ?: Unknown
        }
    }
}

class VTI_NOINFO(t: UByte): IVTI_Base(t)

class VTI_object(t: UByte, c: ICPBase): IVTI_Base(t) {
    var cpool: ICPBase = c
}

class VTI_uninitialized(t: UByte, o: Short): IVTI_Base(t) {
    var offset = o
}

class VTI_unknown(t: UByte) : IVTI_Base(t) {}

fun VTIReader(reader: ClassFileReader): IVTI_Base {
    val tag: UByte = reader.ReadU1().toUByte()
    val x: VTI_Tag = VTI_Tag.getByValue(tag)

    return when(x) {
        in VTI_Tag.Integer..VTI_Tag.UninitializedThis -> VTI_NOINFO(tag)
        //TODO: Resolve CPOOL references instead
        VTI_Tag.Object -> VTI_object(tag, CPRef(reader.ReadU2().toInt()))
        VTI_Tag.Uninitialized -> VTI_uninitialized(tag, reader.ReadU2())
        else -> {
            reader.flags.add(StackMapTable_BadVTITag("Invalid VTI Tag", "Verification was expected to be 0 < x < 9 was $tag"))
            VTI_unknown(tag)
        }
    }
}

class same_frame: IStack_map_frame_base

class same_locals_1_stack_item_frame(val stack: IVTI_Base): IStack_map_frame_base

class same_locals_1_stack_item_frame_extended(val offset_delta: Short,
                                              val stack: IVTI_Base): IStack_map_frame_base

class chop_frame(val offset_delta: Short, val k: Short): IStack_map_frame_base

class same_frame_extended(val offset_delta: Short): IStack_map_frame_base

class append_frame(val offset_delta: Short,
                   val locals: Array<IVTI_Base>): IStack_map_frame_base

class full_frame(val offset_delta: Short,
                 val locals: Array<IVTI_Base>,
                 val stack: Array<IVTI_Base>): IStack_map_frame_base

class stack_map_frame(val frame_type: UByte,
                      val offset_delta: Short,
                      val data: IStack_map_frame_base)

class StackMapTable(reader: ClassFileReader): IAttributeBase {
    var entries: Array<stack_map_frame?>

    init {
        val count = reader.ReadU1()
        entries = Array<stack_map_frame?>(count.toInt()){ null }
        for (i in 0 until count) {
            val frame_type: UByte = reader.ReadU1().toUByte()

            var frame: stack_map_frame? = when (frame_type) {
                in 0u..63u -> /*same frame*/ stack_map_frame(frame_type, frame_type.toShort(), same_frame())
                in 64u .. 127u -> /*same locals 1 stack item frame*/
                    stack_map_frame(frame_type, (frame_type - 64u).toShort(), same_locals_1_stack_item_frame(VTIReader(reader)))
                247u.toUByte() -> {/*same locals 1 stack item frame extended*/
                    val delta = reader.ReadU2()
                    stack_map_frame(frame_type, delta, same_locals_1_stack_item_frame_extended(delta, VTIReader(reader)))
                }
                in 248u..250u -> {/*chop frame*/
                    val delta = reader.ReadU2()
                    stack_map_frame(frame_type, delta, chop_frame(delta, (251u - frame_type).toShort()))
                }
                251u.toUByte() -> {/*same frame extended*/
                    val delta = reader.ReadU2()
                    stack_map_frame(frame_type, delta, same_frame_extended(delta))
                }
                in 252u..254u -> {/*append frame*/
                    val delta = reader.ReadU2()
                    val k = frame_type - 251u
                    stack_map_frame(frame_type, delta, append_frame(delta, Array<IVTI_Base>(k.toInt()){VTIReader(reader)}))
                }
                255u.toUByte() -> {/*full frame*/
                    val delta = reader.ReadU2()
                    stack_map_frame(frame_type, delta, full_frame(delta,
                        Array<IVTI_Base>(reader.ReadU2().toInt()){VTIReader(reader)},
                        Array<IVTI_Base>(reader.ReadU2().toInt()){VTIReader(reader)}
                    ))
                }
                else -> {null}
            }
        }
    }
}