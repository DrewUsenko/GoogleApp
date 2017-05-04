/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package slGal.LiveEdu;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;
/**
 *
 * @author slgal
 */
public class ZipManager {
     public static void write(InputStream in, OutputStream out)
        throws IOException {
	    byte[] buffer = new byte[1024];
	    int len;
	    while ((len = in.read(buffer)) >= 0)
		out.write(buffer, 0, len);
	    in.close();
	    out.close();
	}

    public static void main(String file) {
Enumeration entries = null;
	ZipFile zip = null;

	try {
	    zip = new ZipFile(file);
	    entries = zip.entries();

	    while (entries.hasMoreElements()) {
	        ZipEntry entry = (ZipEntry) entries.nextElement();
	        System.out.println("Extracting:" + entry.getName());

	        write(zip.getInputStream(entry),
	          new BufferedOutputStream (new
		    FileOutputStream(entry.getName())));
	    }

		zip.close();
	}
	catch (IOException e) {
	    System.out.println("Exception:");
	    e.printStackTrace();
	    return;
	}
}
}
