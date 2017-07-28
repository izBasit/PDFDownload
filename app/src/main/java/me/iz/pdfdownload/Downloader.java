package me.iz.pdfdownload;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

  private static final String TAG = "Downloader";

  private static final String DIRECTORY = "/DNA/PDF";

  public static String downloadFile(String fileURL, String fileName) {
    try {

      String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
      File folder = new File(extStorageDirectory, DIRECTORY);
      if(!folder.exists())
      folder.mkdir();
      File file = new File(folder, fileName + ".pdf");
      Log.d(TAG, "downloadFile: Saving at "+file.getPath());
      FileOutputStream f = new FileOutputStream(file);
      URL u = new URL(fileURL);
      Log.d(TAG, "downloadFile: url "+u.getPath());
      HttpURLConnection c = (HttpURLConnection) u.openConnection();
      //c.setRequestMethod("GET");
      //c.setDoOutput(true);
      c.connect();

      InputStream in = c.getInputStream();

      byte[] buffer = new byte[1024];
      int len1 = 0;
      while ((len1 = in.read(buffer)) > 0) {
        f.write(buffer, 0, len1);
      }
      f.close();
      return file.getPath();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }
}