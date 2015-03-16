package com.android.lagger.forms.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.forms.login.LoginActivity;
import com.android.lagger.forms.meetings.CreateEditMeetingActivity;
import com.android.lagger.gpslocation.GPSActivity;
import com.android.lagger.serverConnection.TestServerConnection;


public class MainActivity extends ActionBarActivity {

    Button button;
    Button btnGet;
    Button btnPost;
    Button buttonL;
    Button buttonM;

    TextView getResponseTextView;
    TextView postResponseTextView;

    String responseGET = "testowy string";
    String responsePOST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        addListenerOnServerButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, GPSActivity.class);
                startActivity(intent);

            }

        });
        buttonL = (Button) findViewById(R.id.button2);
        buttonL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);

            }

        });

        buttonM = (Button) findViewById(R.id.button3);
        buttonM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, CreateEditMeetingActivity.class);
                startActivity(intent);

            }

        });
    }

    public void addListenerOnServerButtons(){
        final Context context = this;

        btnGet = (Button) findViewById(R.id.GETbutton);
        btnPost = (Button) findViewById(R.id.POSTbutton);

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
                        getResponseTextView = (TextView) findViewById(R.id.textView2);
                        getResponseTextView.setText(getResponseTextView.getText() + result);
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
                        postResponseTextView = (TextView) findViewById(R.id.textView3);
                        postResponseTextView.setText(postResponseTextView.getText() + result);
                    }
                }.execute();

            }

        });

    }

}
