package com.events.challenge.core

class FakeStringProvider : StringProvider {
    override fun getString(resId: Int): String = "FakeString_$resId"
    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return "FakeString_${resId}_${formatArgs.joinToString()}"
    }
}
