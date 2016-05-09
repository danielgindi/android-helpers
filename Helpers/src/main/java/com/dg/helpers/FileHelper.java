package com.dg.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 *
 * Utilities for handling Files
 */
public class FileHelper
{
    /**
     * Move a file, even across volumes.
     * Overwrites existing files.
     * Does not throw on failure.
     * @param from origin path
     * @param to destination path
     * @return true if successful
     */
    public static boolean move(File from, File to)
    {
        return move(from, to, true);
    }

    /**
     * Move a file, even across volumes.
     * Does not throw on failure.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @return true if successful
     */
    public static boolean move(File from, File to, boolean overwrite)
    {
        try
        {
            moveWithExceptions(from, to, overwrite);
            return true;
        }
        catch (IOException ignore)
        {

        }
        return false;
    }

    /**
     * Move a file, even across volumes.
     * Overwrites existing files.
     * @param from origin path
     * @param to destination path
     * @throws IOException
     */
    public static void moveWithExceptions(File from, File to) throws IOException
    {
        moveWithExceptions(from, to, true);
    }

    /**
     * Move a file, even across volumes.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @throws IOException
     */
    public static void moveWithExceptions(File from, File to, boolean overwrite) throws IOException
    {
        if (!from.exists())
        {
            throw new FileNotFoundException(String.format("'from' file was not found (%s).", from.toString()));
        }

        if (overwrite && to.exists())
        {
            if (!to.delete())
            {
                throw new IOException(String.format("'to' file was cannot be overwritten (%s).", to.toString()));
            }
        }
        if (from.renameTo(to))
        {
            return;
        }
        if (to.createNewFile())
        {
            FileChannel source = null;
            FileChannel destination = null;
            try
            {
                source = new FileInputStream(from).getChannel();
                destination = new FileOutputStream(to).getChannel();
                destination.transferFrom(source, 0, source.size());
            }
            catch (IOException ex)
            {
                //noinspection ResultOfMethodCallIgnored
                to.delete();
                throw ex;
            }
            finally
            {
                if (source != null)
                {
                    source.close();
                }
                if (destination != null)
                {
                    destination.close();
                }
                //noinspection ResultOfMethodCallIgnored
                from.delete();
            }
        }
        else
        {
            throw new IOException(String.format("'to' file was cannot be created (%s).", to.toString()));
        }
    }

    /**
     * Copy a file, even across volumes.
     * Overwrites existing files.
     * Does not throw on failure.
     * @param from origin path
     * @param to destination path
     * @return true if successful
     */
    public static boolean copy(File from, File to)
    {
        return copy(from, to, true);
    }

    /**
     * Copy a file, even across volumes.
     * Does not throw on failure.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @return true if successful
     */
    public static boolean copy(File from, File to, boolean overwrite)
    {
        try
        {
            copyWithExceptions(from, to, overwrite);
            return true;
        }
        catch (IOException ignore)
        {

        }
        return false;
    }

    /**
     * Copy a file, even across volumes.
     * Overwrites existing files.
     * @param from origin path
     * @param to destination path
     * @throws IOException
     */
    public static void copyWithExceptions(File from, File to) throws IOException
    {
        copyWithExceptions(from, to, true);
    }

    /**
     * Copy a file, even across volumes.
     * @param from origin path
     * @param to destination path
     * @param overwrite should we overwrite existing files?
     * @throws IOException
     */
    public static void copyWithExceptions(File from, File to, boolean overwrite) throws IOException
    {
        if (!from.exists())
        {
            throw new FileNotFoundException(String.format("'from' file was not found (%s).", from.toString()));
        }

        if (overwrite && to.exists())
        {
            if (!to.delete())
            {
                throw new IOException(String.format("'to' file was cannot be overwritten (%s).", to.toString()));
            }
        }
        if (to.createNewFile())
        {
            FileChannel source = null;
            FileChannel destination = null;
            try
            {
                source = new FileInputStream(from).getChannel();
                destination = new FileOutputStream(to).getChannel();
                destination.transferFrom(source, 0, source.size());
            }
            catch (IOException ex)
            {
                //noinspection ResultOfMethodCallIgnored
                to.delete();
                throw ex;
            }
            finally
            {
                if (source != null)
                {
                    source.close();
                }
                if (destination != null)
                {
                    destination.close();
                }
            }
        }
        else
        {
            throw new IOException(String.format("'to' file was cannot be created (%s).", to.toString()));
        }
    }
}
