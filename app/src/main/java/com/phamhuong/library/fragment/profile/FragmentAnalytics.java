package com.phamhuong.library.fragment.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.fragment.app.Fragment;

import com.phamhuong.library.R;

import java.util.Random;

public class FragmentAnalytics extends Fragment {

    private GridLayout gridLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        gridLayout = view.findViewById(R.id.grid_contributions);

        int totalWeeks = 53;
        int totalDays = 7;

        for (int week = 0; week < totalWeeks; week++) {
            for (int day = 0; day < totalDays; day++) {
                View square = new View(getContext());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 24;
                params.height = 24;
                params.setMargins(4, 4, 4, 4);

                square.setLayoutParams(params);

                // Set random color
                int level = new Random().nextInt(6); // 0-5
                square.setBackgroundColor(getContributionColor(level));

                gridLayout.addView(square);
            }
        }

        return view;
    }

    private int getContributionColor(int level) {
        switch (level) {
            case 1:
                return Color.parseColor("#0e4429");
            case 2:
                return Color.parseColor("#006d32");
            case 3:
                return Color.parseColor("#26a641");
            case 4:
                return Color.parseColor("#39d353");
            case 5:
                return Color.parseColor("#39d353");
            default:
                return Color.parseColor("#161b22");
        }
    }
}
