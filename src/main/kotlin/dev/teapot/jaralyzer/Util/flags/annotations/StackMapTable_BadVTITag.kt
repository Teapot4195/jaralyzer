package dev.teapot.jaralyzer.Util.flags.annotations

import dev.teapot.jaralyzer.Util.FlagType
import dev.teapot.jaralyzer.Util.IFlagBase

class StackMapTable_BadVTITag(Issue: String,
                              Additional: String
) : IFlagBase(FlagType.StackMapTableAttr, 5f, Issue, Additional) {}