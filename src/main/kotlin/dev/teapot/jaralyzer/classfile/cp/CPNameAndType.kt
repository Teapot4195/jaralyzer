package dev.teapot.jaralyzer.classfile.cp

class CPNameAndType(n: ICPBase, d: ICPBase): ICPBase() {
    var name: ICPBase = n
    var descriptor: ICPBase = d
}