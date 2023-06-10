package dev.teapot.jaralyzer.classfile.cp

class CPInt(d: Int): ICPBase() {
    var bytes: Int = d
}

class CPFloat(d: Float): ICPBase() {
    var bytes: Float = d
}

class CPLong(d: Long): ICPBase() {
    var bytes: Long = d
}

class CPDouble(d: Double): ICPBase() {
    var bytes: Double = d
}
