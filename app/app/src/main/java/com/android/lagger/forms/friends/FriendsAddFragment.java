package com.android.lagger.forms.friends;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.model.AdapterUser;
import com.android.lagger.requestObjects.AddFriendRequest;
import com.android.lagger.requestObjects.FindFriendRequest;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AddFriendTask;
import com.android.lagger.tasks.FindFriendsTask;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Kubaa on 2015-04-28.
 */
public class FriendsAddFragment extends Fragment {
    private View parent;
    private Context mContext;
    private FloatingActionButton btnAdd;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<AdapterUser> adapter;
    private final Integer NUMBER_CHAR_TO_SEARCH = 3;
    private AdapterUser chosenUser = null;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FriendsAddFragment(Context context) {
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_friends_add, container, false);

        initializeAutoCompleteEmailAndArrayAdapter();

        fragmentManager = getFragmentManager();
        btnAdd = (FloatingActionButton) parent.findViewById(R.id.btnDone);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                addNewFriend();
                adapter = null;
            }
        });

        return parent;
    }

    private void initializeAutoCompleteEmailAndArrayAdapter() {
        autoCompleteTextView = (AutoCompleteTextView) parent.findViewById(R.id.editTextMeeting);
        adapter = new ArrayAdapter<AdapterUser>(mContext,
                android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= NUMBER_CHAR_TO_SEARCH) {
                    String searchText = autoCompleteTextView.getText().toString();
                    updateArrayAdapter(searchText);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(NUMBER_CHAR_TO_SEARCH - 1);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                chosenUser = adapter.getItem(position);
                hideKeyboard();
//                adapter = null;
            }
        });

    }

    private void addNewFriend() {
        if (chosenUser != null && (chosenUser.toString()).equals(autoCompleteTextView.getText().toString())) {
            final Integer friendId = chosenUser.getId();
            AddFriendRequest addFriendRequest = new AddFriendRequest(State.getLoggedUserId(), friendId);
            AddFriendTask addFriendTask = new AddFriendTask(mContext, true);
            addFriendTask.execute(addFriendRequest);

            FragmentManager fm = getFragmentManager();
            int count = fm.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }

            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, new FriendsListFragment(mContext)).commit();

        } else {
            showError();
        }
    }

    private void showError() {
        String text = mContext.getString(R.string.choose_email);
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    private void updateArrayAdapter(String searchText) {
        FindFriendRequest findFriendRequest = new FindFriendRequest(State.getLoggedUserId(), searchText);
        FindFriendsTask findFriendsTask = new FindFriendsTask(mContext, autoCompleteTextView);
        findFriendsTask.execute(findFriendRequest);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(parent.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}