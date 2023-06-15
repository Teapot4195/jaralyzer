package dev.teapot.jaralyzer.Util.flags.attributes

import dev.teapot.jaralyzer.Util.FlagType
import dev.teapot.jaralyzer.Util.IFlagBase

class Attributes_BadTag(Issue: String,
                              Additional: String
) : IFlagBase(FlagType.ClassFile, 10f, Issue, Additional) {}