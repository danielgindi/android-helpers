package com.dg.spannable

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned

@Suppress("unused")
class SpannableStringBuilderEx : SpannableStringBuilder
{
    /**
     * Create a new SpannableStringBuilderEx with empty contents
     */
    constructor() : super()

    /**
     * Create a new SpannableStringBuilderEx containing a copy of the
     * specified text, including its spans if any.
     */
    constructor(text: CharSequence) : super(text)

    /**
     * Create a new SpannableStringBuilderEx containing a copy of the
     * specified slice of the specified text, including its spans if any.
     */
    constructor(text: CharSequence, start: Int, end: Int) : super(text, start, end)

    /**
     * Appends the character sequence `text` and spans `what` over the appended part.
     * See [Spanned] for an explanation of what the flags mean.
     * @param text the character sequence to append.
     * @param what the object to be spanned over the appended text.
     * @param flags see [Spanned].
     * @return this `SpannableStringBuilderEx`.
     */
    override fun append(text: CharSequence, what: Any, flags: Int): SpannableStringBuilder
    {
        val start = length
        append(text)
        setSpan(what, start, length, flags)
        return this
    }

    /**
     * Appends the character sequence `text` and spans `spannables` over the appended part.
     * @param text the character sequence to append.
     * @param spannables a collection of spannables and their flags to be applied to this text.
     * @return this `SpannableStringBuilderEx`.
     */
    fun append(text: CharSequence, spannables: Array<SpannableWithFlags>?): SpannableStringBuilder
    {
        val start = length
        append(text)
        if (spannables != null)
        {
            for (spannable in spannables)
            {
                setSpan(spannable.spannable, start, length, spannable.flags)
            }
        }
        return this
    }

    /**
     * This class wraps a pair of a Spannable and Flags for spanning.
     */
    class SpannableWithFlags
    {
        var spannable: Any? = null
        var flags: Int = 0

        /**
         * Constructs a pair of a Spannable with SPAN_INCLUSIVE_INCLUSIVE flags.
         * @param spannable the object to be spanned over the appended text.
         */
        constructor(spannable: Any)
        {
            this.spannable = spannable
            this.flags = Spannable.SPAN_INCLUSIVE_INCLUSIVE
        }

        /**
         * Constructs a pair of a Spannable with flags.
         * @param spannable the object to be spanned over the appended text.
         * @param flags see [Spanned].
         */
        constructor(spannable: Any, flags: Int)
        {
            this.spannable = spannable
            this.flags = flags
        }
    }
}
