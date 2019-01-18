package com.example.library.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;

import java.util.concurrent.TimeUnit;

/**
 * // Note: This authentication is not works on emulator.
 * Steps for Phone Auth in Firebase
 * 1. Open Firebase and add Project
 * 2. Select Authentication and Goto Sign In Method and Enable Phone Authentication Disable to Enable.
 * 3. Add google-services in App Folder Inside Project.
 * 4. Add Firebase Implementation Dependecy and Classpath.
 * 5. Add this class to ur project.
 * 6. pass data to constructor (Context, phone number with country code, callback)
 * call back has 2 method:
 * 1: onCodeRecieve() : this method execute when we get otp code
 * 2: onVerificationSuccess(): this method execute when otp verified successfully.
 */

public class FirebasePhoneAuthentication {

    private static String phoneNumber;
    private static Context context;
    private static String verificationId;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseOTPVerificationListener firebaseOTPVerificationListener;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                firebaseOTPVerificationListener.onOTPReceived(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException && ((FirebaseAuthInvalidCredentialsException) e).getErrorCode().equalsIgnoreCase("ERROR_INVALID_PHONE_NUMBER")) {
                Toast.makeText(context, "Invalid Phone Number Entered..", Toast.LENGTH_SHORT).show();

            } else if (e instanceof FirebaseAuthException && ((FirebaseAuthException) e).getErrorCode().equalsIgnoreCase("ERROR_APP_NOT_AUTHORIZED")) {
                Toast.makeText(context, "Firebase Verification Use only in Physical Device..", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(context, "Code Sent Successfully", Toast.LENGTH_SHORT).show();
            verificationId = s;
            resendToken = forceResendingToken;
        }
    };

    public FirebasePhoneAuthentication() {
    }

    public FirebasePhoneAuthentication(Context context, String phoneNumberWithCountryCode, FirebaseOTPVerificationListener firebaseOTPVerificationListener) {
        FirebasePhoneAuthentication.context = context;
        phoneNumber = phoneNumberWithCountryCode;
        FirebasePhoneAuthentication.firebaseOTPVerificationListener = firebaseOTPVerificationListener;

        firebaseAuth = FirebaseAuth.getInstance();
        sendVerificationCode();
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBacks);
    }

    public void verifyVerificationCode(String code) {
        Toast.makeText(context, "Wait For Verification", Toast.LENGTH_SHORT).show();
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);

        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseOTPVerificationListener.onVerificationSuccess();

                } else {
                    String message = "Something is Wrong. we will fix it soon.";

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid Code Entered";
                    }

                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface FirebaseOTPVerificationListener {
        void onOTPReceived(String code);

        void onVerificationSuccess();
    }
}
