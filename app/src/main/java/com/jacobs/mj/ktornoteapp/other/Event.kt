package com.jacobs.mj.ktornoteapp.other

/**
 * Created by mj on 2021/03/06 at 9:35 PM
 *
 * Generic class for all projects
 */
open class Event<out T>(private val content: T) {
    //  Has the event been consumed
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() = if (hasBeenHandled) {
        //  If the event has already been handled
        null
    } else {
        //  If the event has not been handled yet
        hasBeenHandled = true
        content
    }

    fun peekContent() = content
}