package classfile.cp

class CPString(s: ICPBase) : ICPBase() {
    var string: ICPBase = s
}

class CPUTF8(b: String) : ICPBase() {
    var bytes: String = b
}