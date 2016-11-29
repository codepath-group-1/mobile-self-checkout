package com.codepath.shopmyself.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.shopmyself.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity {


    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button signInButton;
    private Button createAccountButton;

    // [START declare_auth]
    private FirebaseAuth mAuth;

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        //text fields
        mEmailField = (EditText) findViewById(R.id.email_sign_in_text);
        mPasswordField = (EditText) findViewById(R.id.password_sign_in_text);

        // Buttons
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        createAccountButton = (Button) findViewById(R.id.email_create_account_button);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

        //sign in click listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });


        //create an account (sign up) click listener
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailPasswordActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void signIn(String email, String password) {

        //checking if inputs are valid
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //sign in failed dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(EmailPasswordActivity.this);
                            builder.setMessage(task.getException().getMessage())
                                    .setTitle(R.string.auth_failed)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Intent loginSuccessful
                                = new Intent(EmailPasswordActivity.this,
                                             MainActivity.class);
                            loginSuccessful
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            loginSuccessful
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(loginSuccessful);
                        }
                        
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
