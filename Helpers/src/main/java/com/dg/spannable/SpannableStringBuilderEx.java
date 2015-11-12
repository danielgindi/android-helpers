package com.dg.spannable;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class SpannableStringBuilderEx extends SpannableStringBuilder
{
    /**
     * Create a new SpannableStringBuilderEx with empty contents
     */
    public SpannableStringBuilderEx() {
        super();
    }

    /**
     * Create a new SpannableStringBuilderEx containing a copy of the
     * specified text, including its spans if any.
     */
    public SpannableStringBuilderEx(CharSequence text) {
        super(text);
    }

    /**
     * Create a new SpannableStringBuilderEx containing a copy of the
     * specified slice of the specified text, including its spans if any.
     */
    public SpannableStringBuilderEx(CharSequence text, int start, int end) {
        super(text, start, end);
    }

    /**
     * Appends the character sequence {@code text} and spans {@code what} over the appended part.
     * See {@link Spanned} for an explanation of what the flags mean.
     * @param text the character sequence to append.
     * @param what the object to be spanned over the appended text.
     * @param flags see {@link Spanned}.
     * @return this {@code SpannableStringBuilderEx}.
     */
    public SpannableStringBuilder append(CharSequence text, Object what, int flags)
    {
        int start = length();
        append(text);
        setSpan(what, start, length(), flags);
        return this;
    }

    /**
     * Appends the character sequence {@code text} and spans {@code spannables} over the appended part.
     * @param text the character sequence to append.
     * @param spannables a collection of spannables and their flags to be applied to this text.
     * @return this {@code SpannableStringBuilderEx}.
     */
    public SpannableStringBuilder append(CharSequence text, SpannableWithFlags[] spannables)
    {
        int start = length();
        append(text);
        if (spannables != null)
        {
            for (SpannableWithFlags spannable : spannables)
            {
                setSpan(spannable.getSpannable(), start, length(), spannable.getFlags());
            }
        }
        return this;
    }

    /**
     * This class wraps a pair of a Spannable and Flags for spanning.
     */
    public static class SpannableWithFlags
    {
        private Object mSpannable;
        private int mFlags;

        /**
         * Constructs a pair of a Spannable with SPAN_INCLUSIVE_INCLUSIVE flags.
         * @param spannable the object to be spanned over the appended text.
         */
        public SpannableWithFlags(Object spannable)
        {
            setSpannable(spannable);
            setFlags(Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        /**
         * Constructs a pair of a Spannable with flags.
         * @param spannable the object to be spanned over the appended text.
         * @param flags see {@link Spanned}.
         */
        public SpannableWithFlags(Object spannable, int flags)
        {
            setSpannable(spannable);
            setFlags(flags);
        }

        public Object getSpannable()
        {
            return mSpannable;
        }

        public void setSpannable(Object spannable)
        {
            this.mSpannable = spannable;
        }

        public int getFlags()
        {
            return mFlags;
        }

        public void setFlags(int flags)
        {
            this.mFlags = flags;
        }
    }
}
