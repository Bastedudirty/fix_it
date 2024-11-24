package com.example.boost.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.boost.R;
import com.example.boost.main.calendar.CalendarFragment;
import com.example.boost.databinding.ActivityMainInterfaceBinding;
import com.example.boost.main.notepad.NoteFragment;
import com.example.boost.main.timer.TimerFragment;
import com.example.boost.main.todolist.TodoFragment;
import com.example.boost.main.track.TrackFragment;

public class MainInterface extends AppCompatActivity {

    private ActivityMainInterfaceBinding binding;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainInterfaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new TrackFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.Track) {
                replaceFragment(new TrackFragment());
            } else if (itemId == R.id.Note) {
                replaceFragment(new NoteFragment());
            } else if (itemId == R.id.Timer) {
                replaceFragment(new TimerFragment());
            } else if (itemId == R.id.todo) {
                replaceFragment(new TodoFragment());
            } else if (itemId == R.id.Calendar) {
                replaceFragment(new CalendarFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void animateFabSelection(View fab) {
        Animation scaleAnimation = new ScaleAnimation(
                1f, 1.5f,
                1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f
        );
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        fab.startAnimation(scaleAnimation);
    }
}
