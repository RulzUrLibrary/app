package com.rulzurlibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.rulzurlibrary.fragments.CollectionFragment;
import com.rulzurlibrary.common.User;

public class Signin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);

        if (User.getInstance().isAuthenticated()) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_frame, new CollectionFragment())
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_frame, new Login())
                    .commit();
        }

        return view;
    }

    public static class Login extends Fragment implements
            GoogleApiClient.OnConnectionFailedListener,
            View.OnClickListener {

        private static final String TAG = "SignInActivity";
        private static final int RC_SIGN_IN = 9001;

        private GoogleApiClient mGoogleApiClient;
        private TextView mStatusTextView;
        private ProgressDialog mProgressDialog;
        private View view;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.signin, container, false);

            // Views
            mStatusTextView =  view.findViewById(R.id.status);

            // Button listeners
            view.findViewById(R.id.sign_in_button).setOnClickListener(this);
            view.findViewById(R.id.sign_out_button).setOnClickListener(this);
            view.findViewById(R.id.disconnect_button).setOnClickListener(this);

            // [START configure_signin]
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .build();
            // [END configure_signin]

            // [START build_client]
            // Build a GoogleApiClient with access to the Google Sign-In API and the
            // options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                    .enableAutoManage(this.getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            // [END build_client]

            // [START customize_button]
            // Set the dimensions of the sign-in button.
            SignInButton signInButton = view.findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            // [END customize_button]
            return view;
        }

        @Override
        public void onStart() {
            super.onStart();

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            hideProgressDialog();
        }

        // [START onActivityResult]
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
        // [END onActivityResult]

        // [START handleSignInResult]
        private void handleSignInResult(GoogleSignInResult result) {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                assert acct != null;
                mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
                User.getInstance().Authenticate(acct.getIdToken(), acct.getEmail());
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_frame, new CollectionFragment())
                        .commit();

                updateUI(true);
            } else {
                // Signed out, show unauthenticated UI.
                updateUI(false);
            }
        }
        // [END handleSignInResult]

        // [START signIn]
        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        // [END signIn]

        // [START signOut]
        private void signOut() {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            // [START_EXCLUDE]
                            updateUI(false);
                            // [END_EXCLUDE]
                        }
                    });
        }
        // [END signOut]

        // [START revokeAccess]
        private void revokeAccess() {
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            // [START_EXCLUDE]
                            updateUI(false);
                            // [END_EXCLUDE]
                        }
                    });
        }
        // [END revokeAccess]

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            // An unresolvable error has occurred and Google APIs (including Sign-In) will not
            // be available.
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }

        private void showProgressDialog() {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(view.getContext());
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }

        private void hideProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }

        private void updateUI(boolean signedIn) {
            if (signedIn) {
                view.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                view.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            } else {
                mStatusTextView.setText(R.string.signed_out);

                view.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                view.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

        @Override
        public void onPause() {
            super.onPause();
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
                case R.id.sign_out_button:
                    signOut();
                    break;
                case R.id.disconnect_button:
                    revokeAccess();
                    break;
            }
        }
    }

}

