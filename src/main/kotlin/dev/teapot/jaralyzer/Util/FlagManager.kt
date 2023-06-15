package dev.teapot.jaralyzer.Util

class FlagManager {
    var Flags: ArrayList<IFlagBase> = ArrayList<IFlagBase>()

    fun add(flag: IFlagBase) {
        Flags.add(flag)
    }

    fun print(): Unit {
        for (flag in Flags) {
            flag.print()
        }
    }
}