package com.example.hwk02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public final String APP_TAG = "HWK02";
    private String[] mCommandHistory;
    private final int HISTORY_LENGTH = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommandHistory = new String[HISTORY_LENGTH];
        for (int i = 0; i < HISTORY_LENGTH; i++) {
            mCommandHistory[i] = "" + i;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview_item, mCommandHistory);
        ListView listView = (ListView) findViewById(R.id.commandHistory);
        listView.setAdapter(adapter);
    }

    private class SendMessageToServerTask extends AsyncTask<String, Integer, String> {
        private final String SERVER_ADDRESS = "10.0.2.2";
        private static final int PORT = 6789;

        protected String doInBackground(String... strings){
            try {
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToServer.writeBytes(strings[0] + '\n');
                String responseFromServer = serverBufferedReader.readLine();
                socket.close();
                return responseFromServer;
            } catch(IOException e) {
                return "error";
            }
        }

        protected void onPostExecute(String result) {
            Log.d(APP_TAG, result);
        }
    }
}
