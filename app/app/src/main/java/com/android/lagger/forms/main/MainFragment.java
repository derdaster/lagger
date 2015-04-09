package com.android.lagger.forms.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.serverConnection.TestServerConnection;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MainFragment extends Fragment {

    private Context mContext;

    Button btnGet;
    Button btnPost;
    TextView getResponseTextView;
    TextView postResponseTextView;

    String responseGET = "testowy string";
    String responsePOST;


    public MainFragment(){}

    public MainFragment(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(
                R.layout.fragment_main, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addListenerOnServerButtons();
    }


    public void addListenerOnServerButtons(){

        btnGet = (Button) getView().findViewById(R.id.GETbutton);
        btnPost = (Button) getView().findViewById(R.id.POSTbutton);

        btnGet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... urls) {
                        return  TestServerConnection.GET(TestServerConnection.TEST_CONNECTION_URL);
                    }
                    // onPostExecute displays the results of the AsyncTask.
                    @Override
                    protected void onPostExecute(String result) {
                        getResponseTextView = (TextView) getView().findViewById(R.id.textView2);
                        getResponseTextView.setText("GET response: " + result);
                    }
                }.execute();

            }

        });

        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... urls) {
                        return  TestServerConnection.POST(TestServerConnection.TEST_POST);
                    }
                    // onPostExecute displays the results of the AsyncTask.
                    @Override
                    protected void onPostExecute(String result) {
                        postResponseTextView = (TextView) getView().findViewById(R.id.textView3);
                        postResponseTextView.setText("POST response: " + result);
                    }
                }.execute();

            }

        });

    }


}