package com.sleep_zoner.sleepzoner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity
        implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
{

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signIn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button signUp;
    private TextView summaryText;
    private Button showSummaryBtn;
    private Button clearSummaryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(this);
        signUp = (Button) findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(this);
        summaryText = (TextView) findViewById(R.id.summary_text);
        showSummaryBtn = (Button) findViewById(R.id.showSummaryBtn);
        showSummaryBtn.setOnClickListener(this);
        clearSummaryBtn = (Button) findViewById(R.id.clearSummaryBtn);
        clearSummaryBtn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
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
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//                customSummery("yes ");
//                customSummery(account.getEmail());
//            } else {
//                // Google Sign In failed, update UI appropriately
//                // [START_EXCLUDE]
//                customSummery("no");
//                // [END_EXCLUDE]
//            }
//        }
//    }
    private void customSummery(String txt) {
        summaryText.append(":: " + txt);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            showSummary();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            showSummary();
                        }
                    }
                });
    }

    private void signUp(View view) {
        Toast.makeText(this, "Hello signUp", Toast.LENGTH_SHORT).show();
        String email = "test@gmail.com";
        String password = "change_this";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            String msg = "failed to sign up user.";
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            customSummery("\nfailed to create user with firebase");
                            customSummery("\n\n details: " + task.getException().toString());
                        }
                    }
                });
    }

    private void signIn(View view) {
        Toast.makeText(this, "Hello signIn", Toast.LENGTH_SHORT).show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        String email = "test@gmail.com";
        String password = "change_this";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            String msg = "failed to sign in user.";
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void showSummary() {
        String summary = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            summary += "User is signed in. ";
            String name = user.getEmail();
            summary += "uname: " + name;
        } else {
            summary += "User is not signed in. ";
        }

        customSummery("initialized summary. " + summary);
    }
    private void clearSummary() {
        summaryText.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                signUp(v);
                break;
            case R.id.sign_in_button:
                signIn(v);
                break;
            case R.id.showSummaryBtn:
                showSummary();
                break;
            case R.id.clearSummaryBtn:
                clearSummary();
                break;
        }
    }
}
