package me.iz.pdfdownload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private static final String downloadUrl =
      "http://www.pdf995.com/samples/pdf.pdf";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void openPdf(View v) {
    new DownloadAndOpenPdf(this).execute();
  }

  private static final class DownloadAndOpenPdf extends AsyncTask<Void, Void, String> {

    WeakReference<Context> weakContext;

    DownloadAndOpenPdf(Context context) {
      weakContext = new WeakReference<>(context);
    }

    @Override protected String doInBackground(Void... voids) {

      String fileUrl = Downloader.downloadFile(downloadUrl, "index");
      Log.i(TAG, "doInBackground: "+fileUrl);
      return fileUrl;
    }

    @Override protected void onPostExecute(String path) {
      super.onPostExecute(path);

      if(path == null || path.isEmpty()) {
        Log.w(TAG, "onPostExecute: File is empty");
        return;
      }
      File file = new File(path);
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

      Uri uri = FileProvider.getUriForFile(
          weakContext.get(),
          weakContext.get().getApplicationContext()
              .getPackageName() + ".provider", file);

      intent.setDataAndType(uri, "application/pdf");
      weakContext.get().startActivity(intent);
    }
  }
}
