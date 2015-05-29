package com.android.lagger.forms.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.forms.main.MainActivity;
import com.android.lagger.logic.forms.SaveSharedPreference;
import com.android.lagger.model.entities.User;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.melnykov.fab.FloatingActionButton;


public class LoginActivity extends ActionBarActivity {
    private Context mContext;
    private FloatingActionButton loginBtn;
    private TextView loginTextView;
    private TextView passwordTextView;
    private HttpClient client;
    static String mActivityTitle;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_login);
        mActivityTitle = getTitle().toString();
        mActivity = this;

        client = new HttpClient(mContext);

        setFields();
        addListenerOnLoginButton();
    }


    private void setFields() {
        loginBtn = (FloatingActionButton) findViewById(R.id.buttonLogin);
        loginTextView = (TextView) findViewById(R.id.editTextEmail);
        passwordTextView = (TextView) findViewById(R.id.editTextPassword);
    }

    public void addListenerOnLoginButton() {
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                loginBtn.setEnabled(false);
                login();
            }
        });
    }

    private void login() {
        String login = loginTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        com.android.lagger.requestObjects.LoginRequest loginReq = new com.android.lagger.requestObjects.LoginRequest(login, password);

        LoginUserTask loginUserTask = new LoginUserTask(mContext);
        loginUserTask.execute(loginReq);
    }

    private class LoginUserTask extends AsyncTask<com.android.lagger.requestObjects.LoginRequest, Void, com.android.lagger.responseObjects.LoginResponse> {
        private Context context;

        public LoginUserTask(Context context) {
            this.context = context;
        }

        protected com.android.lagger.responseObjects.LoginResponse doInBackground(com.android.lagger.requestObjects.LoginRequest... loginReq) {
            return client.login(loginReq[0]);
        }

        protected void onPostExecute(com.android.lagger.responseObjects.LoginResponse loginResp) {
            if (!loginResp.isError()) {
                checkUserAndShowResult(loginResp);
            } else {
                Toast.makeText(context, loginResp.getResponse(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        private void checkUserAndShowResult(final LoginResponse loginResp) {
            final Integer status = loginResp.getStatus();
            if (status == 1) {
                enableAccess(loginResp);
            } else {
                incorrectData(status);
                loginBtn.setEnabled(true);
            }
        }

        private void enableAccess(final com.android.lagger.responseObjects.LoginResponse loginResp) {
            final Integer userId = loginResp.getIdUser();
            State.setLoggedUser(new User(userId));
            SaveSharedPreference.setUserName(context, userId.toString());

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        private void incorrectData(final Integer status) {
            String message = "";
            switch (status) {
                case 0:
                    message = getString(R.string.unregistered_user);
                    break;
                case 2:
                    message = getString(R.string.incorrect_password);
                    break;
            }
            Toast.makeText(context, message,
                    Toast.LENGTH_SHORT).show();
        }
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
}
