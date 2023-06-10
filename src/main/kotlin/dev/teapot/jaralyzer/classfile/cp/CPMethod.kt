package dev.teapot.jaralyzer.classfile.cp

enum class CPMethodHandleKind(var kind: Byte) {
    getField(1),
    getStatic(2),
    putField(3),
    putStatic(4),
    invokeVirtual(5),
    invokeStatic(6),
    invokeSpecial(7),
    newInvokeSpecial(8),
    invokeInterface(9);

    companion object {
        fun fromInt(value: Byte) = CPMethodHandleKind.values().first { it.kind == value }
    }
}

class CPMethodHandle(rk: CPMethodHandleKind, ri: ICPBase): ICPBase() {
    var kind: CPMethodHandleKind = rk
    var index: ICPBase = ri
}

class CPMethodType(d: ICPBase): ICPBase() {
    var descriptor: ICPBase = d
}