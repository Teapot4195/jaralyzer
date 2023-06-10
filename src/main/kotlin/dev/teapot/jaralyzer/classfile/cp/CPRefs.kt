package dev.teapot.jaralyzer.classfile.cp

class CPFieldref(c: ICPBase, n: ICPBase): ICPBase() {
    var clazz: ICPBase = c
    var name_and_type: ICPBase = n
}

class CPMethodref(c: ICPBase, n: ICPBase): ICPBase() {
    var clazz: ICPBase = c
    var name_and_type: ICPBase = n
}

class CPInterfaceMethodRef(c: ICPBase, n: ICPBase): ICPBase() {
    var clazz: ICPBase = c
    var name_and_type: ICPBase = n
}