package io.isometrik.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.isometrik.ui.IsometrikCallSdk;
import io.isometrik.ui.meetings.list.MeetingsActivity;
import io.isometrik.ui.users.list.UsersActivity;

/**
 * The activity to show splash screen while application loads.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        if (IsometrikCallSdk.getInstance().getUserSession().getUserToken() == null) {

            intent = new Intent(SplashActivity.this, UsersActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, MeetingsActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
