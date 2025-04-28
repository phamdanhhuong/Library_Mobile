package com.phamhuong.library.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phamhuong.library.R;
import com.phamhuong.library.fragment.reservation.BasketFragment;
import com.phamhuong.library.fragment.reservation.BorrowHistoryFragment;
import com.phamhuong.library.fragment.reservation.ReservationHistoryFragment;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.utils.CustomDialogHelper;

public class FragmentProfile extends Fragment  {
    APIService apiService;
    String token ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        // Load các Fragment con
        loadFragment(new FragmentUserInfo(), R.id.fragmentUserInfo);

        view.findViewById(R.id.btnBorrowHistory).setOnClickListener(v -> {
            Fragment borrowHistoryFragment = new BorrowHistoryFragment();
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, borrowHistoryFragment)
                .addToBackStack(null)
                .commit();
        });

        view.findViewById(R.id.btnReservationHistory).setOnClickListener(v -> {
            Fragment reservationHistoryFragment = new ReservationHistoryFragment();
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, reservationHistoryFragment)
                .addToBackStack(null)
                .commit();
        });

        view.findViewById(R.id.btnBasket).setOnClickListener(v -> {
            Fragment basketFragment = new BasketFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, basketFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadFragment(Fragment fragment, int containerId) {
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }
}
