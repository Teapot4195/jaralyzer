package classfile.cp

class CPDynamic(b: ICPBase, n: ICPBase): ICPBase() {
    var bootstrap_method_attr: ICPBase = b
    var name_and_type: ICPBase = n
}

class CPInvokeDynamic(b: ICPBase, n: ICPBase): ICPBase() {
    var bootstrap_method_attr: ICPBase = b
    var name_and_type: ICPBase = n
}