package id.ac.andri_databaseapiproject.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsyncInvokeURLTask extends AsyncTask<Void, Void, String> {
    public String mNoteItWebUrl = "www.smartneasy.com";
    private ArrayList<Pair<String, String>> mParams;
    private OnPostExecuteListener mPostExecuteListener = null;
    private ProgressDialog dialog;
    public boolean showdialog = false;
    public String message = "Proses Data";
    // Harus diganti dengan IP Server yang digunakan
    public String url_server = "http://www.bibitbagus.id/xphone/";
    public Context applicationContext;

    public static interface OnPostExecuteListener {
        void onPostExecute(String result);
    }

    public AsyncInvokeURLTask(ArrayList<Pair<String, String>> nameValuePairs, OnPostExecuteListener postExecuteListener) throws Exception {
        mParams = nameValuePairs;
        mPostExecuteListener = postExecuteListener;
        if (mPostExecuteListener == null)
            throw new Exception("Param cannot be null.");
    }

    @Override
    public void onPreExecute() {
        if (showdialog)
            this.dialog = ProgressDialog.show(applicationContext, message, "Silakan Menunggu...", true);
    }

    @Override
    public String doInBackground(Void... params) {
        String result = "timeout";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = createRequestBody();
            Request request = new Request.Builder()
                    .url(url_server + mNoteItWebUrl)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if (mPostExecuteListener != null) {
            try {
                if (showdialog) this.dialog.dismiss();
                mPostExecuteListener.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private RequestBody createRequestBody() {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Pair<String, String> pair : mParams) {
            formBodyBuilder.add(pair.first, pair.second);
        }
        return formBodyBuilder.build();
    }
}
