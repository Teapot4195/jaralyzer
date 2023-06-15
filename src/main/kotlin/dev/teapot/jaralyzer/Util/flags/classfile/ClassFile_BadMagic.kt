package dev.teapot.jaralyzer.Util.flags.classfile

import dev.teapot.jaralyzer.Util.FlagType
import dev.teapot.jaralyzer.Util.IFlagBase

class ClassFile_BadMagic(Issue: String,
                         Additional: String
) : IFlagBase(FlagType.ClassFile, 50f, Issue, Additional){
}