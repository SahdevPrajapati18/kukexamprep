// Generated by view binder compiler. Do not edit!
package com.kuk.kukexamprep.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.gms.common.SignInButton;
import com.kuk.kukexamprep.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText email;

  @NonNull
  public final SignInButton googleSignInBtn;

  @NonNull
  public final Button loginBtn;

  @NonNull
  public final ProgressBar loginProgressBar;

  @NonNull
  public final TextView loginTitle;

  @NonNull
  public final EditText password;

  @NonNull
  public final Button signupBtn;

  private ActivityLoginBinding(@NonNull LinearLayout rootView, @NonNull EditText email,
      @NonNull SignInButton googleSignInBtn, @NonNull Button loginBtn,
      @NonNull ProgressBar loginProgressBar, @NonNull TextView loginTitle,
      @NonNull EditText password, @NonNull Button signupBtn) {
    this.rootView = rootView;
    this.email = email;
    this.googleSignInBtn = googleSignInBtn;
    this.loginBtn = loginBtn;
    this.loginProgressBar = loginProgressBar;
    this.loginTitle = loginTitle;
    this.password = password;
    this.signupBtn = signupBtn;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email;
      EditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.googleSignInBtn;
      SignInButton googleSignInBtn = ViewBindings.findChildViewById(rootView, id);
      if (googleSignInBtn == null) {
        break missingId;
      }

      id = R.id.loginBtn;
      Button loginBtn = ViewBindings.findChildViewById(rootView, id);
      if (loginBtn == null) {
        break missingId;
      }

      id = R.id.loginProgressBar;
      ProgressBar loginProgressBar = ViewBindings.findChildViewById(rootView, id);
      if (loginProgressBar == null) {
        break missingId;
      }

      id = R.id.loginTitle;
      TextView loginTitle = ViewBindings.findChildViewById(rootView, id);
      if (loginTitle == null) {
        break missingId;
      }

      id = R.id.password;
      EditText password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.signupBtn;
      Button signupBtn = ViewBindings.findChildViewById(rootView, id);
      if (signupBtn == null) {
        break missingId;
      }

      return new ActivityLoginBinding((LinearLayout) rootView, email, googleSignInBtn, loginBtn,
          loginProgressBar, loginTitle, password, signupBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
