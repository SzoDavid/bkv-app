package com.bkv.tickets.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bkv.tickets.Activities.HomeActivity;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.FirestoreServices.FirestoreUserService;
import com.bkv.tickets.Services.Interfaces.IUserService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private static final String LOG_TAG = ProfileFragment.class.getName();

    private IUserService userService;
    private HomeActivity homeActivity;
    private User mUser;

    private TextView emailTV;
    private EditText fullNameET;
    private Button saveButton;
    private Button logoutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) requireActivity();

        if (homeActivity.getFirebaseUser() == null) {
            Log.d(LOG_TAG, "User not logged in");
            homeActivity.finish();
        }

        mUser = homeActivity.getCurrentUser();

        userService = new FirestoreUserService(homeActivity.getDb());

        emailTV = view.findViewById(R.id.emailTextView);
        fullNameET = view.findViewById(R.id.fullNameEditText);
        saveButton = view.findViewById(R.id.saveButton);
        logoutButton = view.findViewById(R.id.logOutButton);

        userService.getByAuthId(homeActivity.getFirebaseUser().getUid(), this::getByIdCallback);

        saveButton.setOnClickListener(this::saveOnClick);
        logoutButton.setOnClickListener(this::logoutOnClick);
    }

    private void loadData() {
        emailTV.setText(mUser.getEmail());
        fullNameET.setText(mUser.getName());
    }

    private void getByIdCallback(Task<User> task) {
        if (!task.isSuccessful() || task.getResult() == null) {
            Exception exception = task.getException();
            if (exception == null) {
                exception = new Exception("Failed to query user");
            }
            Log.e(LOG_TAG, exception.getMessage());
            Toast.makeText(getActivity(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
        }
        mUser = task.getResult();
        loadData();
    }

    private void saveOnClick(View view) {
        if (mUser == null) {
            Toast.makeText(getActivity(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
        }

        String fullName = fullNameET.getText().toString();

        if (fullName.isEmpty()) {
            fullNameET.setText(mUser.getName());
            fullNameET.requestFocus();
            Toast.makeText(getActivity(), getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        userService.update(mUser.setName(fullName), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), getString(R.string.success_update_user), Toast.LENGTH_SHORT).show();
                fullNameET.clearFocus();
                return;
            }
            Log.e(LOG_TAG, task.getException().getMessage());
            Toast.makeText(getActivity(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
        });
    }

    private void logoutOnClick(View view) {
        FirebaseAuth.getInstance().signOut();
        homeActivity.finish();
    }
}