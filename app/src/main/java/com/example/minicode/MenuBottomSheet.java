package com.example.minicode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.example.minicode.Helper.*;

public class MenuBottomSheet extends BottomSheetDialogFragment {

    TextView fileSize_menu, totalLines_menu;
    private final Context context;

    public MenuBottomSheet(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.menu_bar_sheet, container, false);

        totalLines_menu = v.findViewById(R.id.totalLines_menu);
        fileSize_menu = v.findViewById(R.id.fileSize_menu);

        return v;
    }
}
