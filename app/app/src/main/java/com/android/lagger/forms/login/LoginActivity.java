package com.android.lagger.forms.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.forms.main.MainActivity;
import com.android.lagger.model.User;
import com.android.lagger.serverConnection.ServerConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginActivity extends ActionBarActivity {

    private Button loginBtn;
    private TextView loginTextView;
    private TextView passwordTextView;
  //  private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setFields();
        addListenerOnLoginButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void addListenerOnLoginButton() {

        final Context context = this;
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... urls) {
                        String login = loginTextView.getText().toString();
                        String password = passwordTextView.getText().toString();
                        User user = new User(login, password);
                        JsonObject userJson = user.createLoginJson();
                        //TODO refactoring
                        return ServerConnection.POST(ServerConnection.LOGIN_URL, userJson);
                    }
                    // onPostExecute displays the results of the AsyncTask.
                    @Override
                    protected void onPostExecute(String result) {
//                        loginTextView.setText(responseJsonObject.toString());
                        JsonParser parser = new JsonParser();
                        JsonObject responseJson = (JsonObject)parser.parse(result);

                        int status = responseJson.get("Status").getAsInt();
                        if(status == 1){
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            String message = "";
                            switch (status) {
                                case 0:
                                    message = getString(R.string.unregistered_user);
                                    break;
//                                case 1:
//                                    message = getString(R.string.success_login);
//                                    break;
                                case 2:
                                    message = getString(R.string.incorrect_password);
                                    break;
                            }

                            Toast.makeText(context, message,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();

            }

        });
    }

    private void setFields(){
        loginBtn = (Button) findViewById(R.id.buttonLogin);
        loginTextView = (TextView) findViewById(R.id.editTextEmail);
        passwordTextView = (TextView) findViewById(R.id.editTextPassword);
    }
}
