package com.android.lagger.forms.login;

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
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.forms.main.MainActivity;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class LoginFragment extends Fragment {
    private Context mContext;
    private FloatingActionButton loginBtn;
    private TextView loginTextView;
    private TextView passwordTextView;
    private View parent;
    private HttpClient client;

    public LoginFragment(){
        client = new HttpClient();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent =  inflater.inflate(R.layout.fragment_login, container, false);
        setFields();
        addListenerOnLoginButton();
        return parent;
    }

    private void setFields(){
        loginBtn = (FloatingActionButton) parent.findViewById(R.id.buttonLogin);
        loginTextView = (TextView) parent.findViewById(R.id.editTextEmail);
        passwordTextView = (TextView) parent.findViewById(R.id.editTextPassword);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
    }

    public void addListenerOnLoginButton() {
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                login();
            }
        });
    }

    private void login(){
        String login = loginTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        LoginRequest loginReq = new LoginRequest(login, password);

        LoginUserTask loginUserTask = new LoginUserTask(mContext);
        loginUserTask.execute(loginReq);
    }

    private class LoginUserTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
        private Context context;

        public LoginUserTask(Context context) {
            this.context = context;
        }

        protected LoginResponse doInBackground(LoginRequest... loginReq) {
            return client.login(loginReq[0]);
        }

        protected void onPostExecute(LoginResponse loginResp) {
            checkUserAndShowResult(loginResp);
        }

        private void checkUserAndShowResult(final LoginResponse loginResp){
            final Integer status = loginResp.getStatus();
            if(status == 1){
                enableAccess(loginResp);
            }
            else {
                incorrectData(status);
            }
        }

        private void enableAccess(final LoginResponse loginResp){
            final Integer userId = loginResp.getIdUser();
            State.setLoggedUser(new User(userId));

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }

        private void incorrectData(final Integer status){
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

}