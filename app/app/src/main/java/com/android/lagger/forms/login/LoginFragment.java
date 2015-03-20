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
import com.android.lagger.model.User;
import com.android.lagger.serverConnection.ServerConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class LoginFragment extends Fragment {
    private Context mContext;
    private Button loginBtn;
    private TextView loginTextView;
    private TextView passwordTextView;
    View parent;

    public LoginFragment(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent =  inflater.inflate(R.layout.fragment_login, container, false);
        setFields();
        addListenerOnLoginButton();
        return parent;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void addListenerOnLoginButton() {
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
                            Intent intent = new Intent(mContext, MainActivity.class);
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

                            Toast.makeText(mContext, message,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();

            }

        });
    }

    private void setFields(){
        loginBtn = (Button) parent.findViewById(R.id.buttonLogin);
        loginTextView = (TextView) parent.findViewById(R.id.editTextEmail);
        passwordTextView = (TextView) parent.findViewById(R.id.editTextPassword);
    }
}