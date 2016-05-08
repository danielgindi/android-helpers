package com.dg.helpers;

import java.io.File;
import java.io.FileInputStream;
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean moveTo(File from, File to)
    {
        if (from.exists())
        {
            if (from.renameTo(to))
            {
                return true;
            }

            try
            {
                to.delete();
                to.createNewFile();

                FileChannel source = null;
                FileChannel destination = null;
                try
                {
                    source = new FileInputStream(from).getChannel();
                    destination = new FileOutputStream(to).getChannel();
                    destination.transferFrom(source, 0, source.size());
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
            catch (IOException e)
            {
                // Could not copy the file over.
                to.delete();
                return false;
            }
        }

        return true;
    }
}
