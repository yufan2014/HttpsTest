package com.httpstest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private Button httpsButton;
    private TextView conTextView;

    private CreateHttpsConnTask httpsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpsButton = (Button) findViewById(R.id.create_https_button);
        httpsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                runHttpsConnection();
            }
        });

        conTextView = (TextView) findViewById(R.id.content_textview);
        conTextView.setText("初始为空");
    }

    private void runHttpsConnection() {
        if (httpsTask == null || httpsTask.getStatus() == AsyncTask.Status.FINISHED) {
            httpsTask = new CreateHttpsConnTask();
            httpsTask.execute();
        }
    }

    private class CreateHttpsConnTask extends AsyncTask<Void, Void, Void> {
        private static final String HTTPS_EXAMPLE_URL = "自定义";
        private StringBuffer sBuffer = new StringBuffer();

        @Override
        protected Void doInBackground(Void... params) {
            HttpUriRequest request = new HttpPost(HTTPS_EXAMPLE_URL);
            HttpClient httpClient = HttpUtils.getHttpsClient();
            try {
                HttpResponse httpResponse = httpClient.execute(request);
                if (httpResponse != null) {
                    StatusLine statusLine = httpResponse.getStatusLine();
                    if (statusLine != null
                            && statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new InputStreamReader(
                                    httpResponse.getEntity().getContent(),
                                    "UTF-8"));
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sBuffer.append(line);
                            }

                        } catch (Exception e) {
                            Log.e("https", e.getMessage());
                        } finally {
                            if (reader != null) {
                                reader.close();
                                reader = null;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("https", e.getMessage());
            } finally {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!TextUtils.isEmpty(sBuffer.toString())) {
                conTextView.setText(sBuffer.toString());
            }
        }

    }
}