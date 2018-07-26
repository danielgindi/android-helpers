package com.dg.helpers

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

/**
 * Utilities for handling Files
 */
object FileHelper
{
    /**
     * Move a file, even across volumes.
     * Does not throw on failure.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @return true if successful
     */
    @JvmOverloads
    fun move(from: File, to: File, overwrite: Boolean = true): Boolean
    {
        try
        {
            moveWithExceptions(from, to, overwrite)
            return true
        }
        catch (ignore: IOException)
        {

        }

        return false
    }

    /**
     * Move a file, even across volumes.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmOverloads
    fun moveWithExceptions(from: File, to: File, overwrite: Boolean = true)
    {
        if (!from.exists())
        {
            throw FileNotFoundException(String.format("'from' file was not found (%s).", from.toString()))
        }

        if (overwrite && to.exists())
        {
            if (!to.delete())
            {
                throw IOException(String.format("'to' file was cannot be overwritten (%s).", to.toString()))
            }
        }
        if (from.renameTo(to))
        {
            return
        }
        if (to.createNewFile())
        {
            var source: FileChannel? = null
            var destination: FileChannel? = null
            try
            {
                source = FileInputStream(from).channel
                destination = FileOutputStream(to).channel
                destination!!.transferFrom(source, 0, source!!.size())
            }
            catch (ex: IOException)
            {

                to.delete()
                throw ex
            }
            finally
            {
                source?.close()
                destination?.close()

                from.delete()
            }
        }
        else
        {
            throw IOException(String.format("'to' file was cannot be created (%s).", to.toString()))
        }
    }

    /**
     * Copy a file, even across volumes.
     * Does not throw on failure.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @return true if successful
     */
    @JvmOverloads
    fun copy(from: File, to: File, overwrite: Boolean = true): Boolean
    {
        try
        {
            copyWithExceptions(from, to, overwrite)
            return true
        }
        catch (ignore: IOException)
        {

        }

        return false
    }

    /**
     * Copy a file, even across volumes.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmOverloads
    fun copyWithExceptions(from: File, to: File, overwrite: Boolean = true)
    {
        if (!from.exists())
        {
            throw FileNotFoundException(String.format("'from' file was not found (%s).", from.toString()))
        }

        if (overwrite && to.exists())
        {
            if (!to.delete())
            {
                throw IOException(String.format("'to' file was cannot be overwritten (%s).", to.toString()))
            }
        }
        if (to.createNewFile())
        {
            var source: FileChannel? = null
            var destination: FileChannel? = null
            try
            {
                source = FileInputStream(from).channel
                destination = FileOutputStream(to).channel
                destination!!.transferFrom(source, 0, source!!.size())
            }
            catch (ex: IOException)
            {

                to.delete()
                throw ex
            }
            finally
            {
                source?.close()
                destination?.close()
            }
        }
        else
        {
            throw IOException(String.format("'to' file was cannot be created (%s).", to.toString()))
        }
    }

    /**
     * Return the extension of the path, from the last '.' to end of string in the last portion of the path.
     * If there is no '.' in the last portion of the path or the first character of it is '.', then it returns an empty string.
     * @param filePath File path to retrieve extension from
     * @return i.e. ".jpg", ".gz", ""
     */
    fun getExtension(filePath: String): String
    {
        val i = filePath.lastIndexOf('.')
        val p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'))

        return if (i > p)
        {
            filePath.substring(i)
        }
        else ""

    }

    /**
     * Return the extension of the path, from the last '.' to end of string in the last portion of the path.
     * If there is no '.' in the last portion of the path or the first character of it is '.', then it returns an empty string.
     * @param file File path to retrieve extension from
     * @return i.e. ".jpg", ".gz", ""
     */
    fun getExtension(file: File): String
    {
        return getExtension(file.name)
    }
}
