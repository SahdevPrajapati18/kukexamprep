// Generated by view binder compiler. Do not edit!
package com.kuk.kukexamprep.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.kuk.kukexamprep.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDashboardBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final CardView cardDateSheets;

  @NonNull
  public final CardView cardNotes;

  @NonNull
  public final CardView cardNotifications1;

  @NonNull
  public final CardView cardPyq;

  @NonNull
  public final CardView cardSyllabus;

  @NonNull
  public final Button logoutButton;

  @NonNull
  public final TextView userGreeting;

  private ActivityDashboardBinding(@NonNull ScrollView rootView, @NonNull CardView cardDateSheets,
      @NonNull CardView cardNotes, @NonNull CardView cardNotifications1, @NonNull CardView cardPyq,
      @NonNull CardView cardSyllabus, @NonNull Button logoutButton,
      @NonNull TextView userGreeting) {
    this.rootView = rootView;
    this.cardDateSheets = cardDateSheets;
    this.cardNotes = cardNotes;
    this.cardNotifications1 = cardNotifications1;
    this.cardPyq = cardPyq;
    this.cardSyllabus = cardSyllabus;
    this.logoutButton = logoutButton;
    this.userGreeting = userGreeting;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.card_date_sheets;
      CardView cardDateSheets = ViewBindings.findChildViewById(rootView, id);
      if (cardDateSheets == null) {
        break missingId;
      }

      id = R.id.card_notes;
      CardView cardNotes = ViewBindings.findChildViewById(rootView, id);
      if (cardNotes == null) {
        break missingId;
      }

      id = R.id.card_notifications1;
      CardView cardNotifications1 = ViewBindings.findChildViewById(rootView, id);
      if (cardNotifications1 == null) {
        break missingId;
      }

      id = R.id.card_pyq;
      CardView cardPyq = ViewBindings.findChildViewById(rootView, id);
      if (cardPyq == null) {
        break missingId;
      }

      id = R.id.card_syllabus;
      CardView cardSyllabus = ViewBindings.findChildViewById(rootView, id);
      if (cardSyllabus == null) {
        break missingId;
      }

      id = R.id.logoutButton;
      Button logoutButton = ViewBindings.findChildViewById(rootView, id);
      if (logoutButton == null) {
        break missingId;
      }

      id = R.id.userGreeting;
      TextView userGreeting = ViewBindings.findChildViewById(rootView, id);
      if (userGreeting == null) {
        break missingId;
      }

      return new ActivityDashboardBinding((ScrollView) rootView, cardDateSheets, cardNotes,
          cardNotifications1, cardPyq, cardSyllabus, logoutButton, userGreeting);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
