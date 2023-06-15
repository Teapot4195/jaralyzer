package dev.teapot.jaralyzer.Util

open class IFlagBase(
    var Type: FlagType,
    var Severity: Float,
    var Issue: String,
    var Additional: String
) {
    fun print(): Unit {
        Info("$Type($Issue, $Additional, Severity: $Severity)")
    }
}