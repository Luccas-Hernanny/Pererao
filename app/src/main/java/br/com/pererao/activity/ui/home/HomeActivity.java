package br.com.pererao.activity.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import br.com.pererao.R;
import br.com.pererao.activity.DashboardActivity;
import br.com.pererao.activity.LoadingDialog;
import br.com.pererao.activity.LoginActivity;
import br.com.pererao.activity.UpdateProfileActivity;
import br.com.pererao.activity.VerifyAccount;
import br.com.pererao.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    TextView nameUser, emailUser;
    CircleImageView user_image;
    DatabaseReference mDatabaseReference;
    private static final String USUARIO = "Usuario";
    LoadingDialog loadingDialog = new LoadingDialog(HomeActivity.this);
    String UserID;
    RatingBar ratingBar;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    Toolbar toolbar;
    FloatingActionButton fab_update_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        user_image = findViewById(R.id.user_image);
        nameUser = findViewById(R.id.tv_name_user);
        emailUser = findViewById(R.id.tv_email_user);
        ratingBar = findViewById(R.id.ratingBarUser);
        fab_update_profile = findViewById(R.id.fab_edit_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        loadingDialog.startLoadingDialog();

        FirebaseUser usernull = mFirebaseAuth.getCurrentUser();
        if (usernull != null) {
            UserID = usernull.getUid();
        }

        //ToolBar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.profile);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDashboardActivity();
            }
        });

        fab_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
                ActivityCompat.startActivity(getApplicationContext(), intent, activityOptionsCompat.toBundle());
                finish();
            }
        });

        if (mFirebaseUser != null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference(USUARIO).child(mFirebaseUser.getUid());
            mDatabaseReference.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;
                            loadingDialog.dismissDialog();
                            nameUser.setText(user.getNomeUser());
                            emailUser.setText(user.getEmailUser());
                            if (user.getUserUrl().equals("default")) {
                                user_image.setImageResource(R.drawable.ic_user_icon);
                            } else {
                                Glide.with(getApplicationContext())
                                        .load(user.getUserUrl())
                                        .into(user_image);
                            }
                            ratingBar.setRating(user.getRating());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("HomeActivity", "Erro ao ler o valor: " + databaseError.toException());
                        }
                    }
            );
        } else {
            gotoLoginActivity();
        }
    }

    private void status(String status) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(USUARIO).child(mFirebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        mDatabaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("On-line");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Off-line");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseUser != null) {
            if (!mFirebaseUser.isEmailVerified()) {
                gotoVerifyAccount();
            }
        } else {
            gotoLoginActivity();
        }

    }

    public void onBackPressed() {
        gotoDashboardActivity();
    }

    private void gotoDashboardActivity() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
        ActivityCompat.startActivity(getApplicationContext(), intent, activityOptionsCompat.toBundle());
        finish();
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
        ActivityCompat.startActivity(getApplicationContext(), intent, activityOptionsCompat.toBundle());
        finish();
    }

    private void gotoVerifyAccount() {
        Intent intent = new Intent(getApplicationContext(), VerifyAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
        ActivityCompat.startActivity(getApplicationContext(), intent, activityOptionsCompat.toBundle());
        finish();
    }
}