package com.example.hwk02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final String APP_TAG = "HWK02";
    private String[] mCommandHistory;
    private final int HISTORY_LENGTH = 10;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommandHistory = new String[HISTORY_LENGTH];
        for (int i = 0; i < HISTORY_LENGTH; i++) {
            mCommandHistory[i] = "";
        }

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item, mCommandHistory);
        ListView listView = (ListView) findViewById(R.id.commandHistory);
        listView.setAdapter(adapter);
    }

    private class SendMessageToServerTask extends AsyncTask<String, Integer, String> {

        private String SERVER_ADDRESS = "10.0.2.2";
        private static final int PORT = 6789;
        Socket socket;

        protected String doInBackground(String... strings) {
            try {
                socket = new Socket(SERVER_ADDRESS, PORT);
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                outToServer.writeBytes(strings[0] + '\n');
                String serverResponse = serverBufferedReader.readLine();
                socket.close();

                return "S: " + serverResponse;

            } catch (IOException e) {
                return "C: error";
            }
        }

        protected void onPostExecute(String result) {
            Log.d(APP_TAG, result);
            for (int i = HISTORY_LENGTH - 1; i > 0; i--)
            {
                mCommandHistory[i] = mCommandHistory[i - 1];
            }
            mCommandHistory[0] = result;
            adapter.notifyDataSetChanged();
        }
    }

    public void onClick(View view)
    {
        EditText editText = findViewById(R.id.editText);
        String clientInput = editText.getText().toString();

        switch (view.getId()) {
            case R.id.button:
                new SendMessageToServerTask().onPostExecute("C: " + clientInput);
                new SendMessageToServerTask().execute(clientInput);
        }
    }


}