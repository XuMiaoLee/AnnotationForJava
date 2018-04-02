package utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by admin on 2018/3/30.
 */
public class IOUtils
{
    public static void close(Closeable closeable)
    {
        try
        {
            if (closeable != null)
                closeable.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
