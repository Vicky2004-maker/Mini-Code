package com.example.minicode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static com.example.minicode.Helper.*;

import com.example.minicode.CodeViewBackend.CodeView;
import com.example.minicode.CodeViewBackend.Language;
import com.example.minicode.CodeViewBackend.Theme;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CodeViewActivity extends AppCompatActivity {

    CodeView codeView;
    ConstraintLayout menuBar;
    FloatingActionButton codeView_fab;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case PICK_FILE_CODE: {
                if (data != null) {
                    CodeFile codeFile = new CodeFile(CodeViewActivity.this, data.getData());
                    codeFileList.add(codeFile);

                 //   initializeCodeView();
                }
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.openFileMenu) {
            pickFile(CodeViewActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.codeview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_view);

        codeView = findViewById(R.id.codeView_main);
        menuBar = findViewById(R.id.menu_bar);
        codeView_fab = findViewById(R.id.codeView_fab);

        codeView_fab.setOnClickListener(a -> {
            View dialogView = getLayoutInflater().inflate(R.layout.menu_bar_sheet, null);
            BottomSheetDialog dialog = new BottomSheetDialog(CodeViewActivity.this);
            dialog.setContentView(dialogView);
            dialog.show();
        });
    }

    private void initializeCodeView(@NonNull final String code) {
        codeView.setTheme(Theme.ANDROIDSTUDIO)
                .setOnHighlightListener(new CodeViewListener())
                .setLanguage(Language.AUTO)
                .setCode(code)
                .setZoomEnabled(true)
                .apply();
    }

    private class CodeViewListener implements CodeView.OnHighlightListener {

        @Override
        public void onStartCodeHighlight() {
            //TODO: Progress Bar
            //CustomProgressBar.showDialog(CodeViewActivity.this, "Loading");
        }

        @Override
        public void onFinishCodeHighlight() {
            //CustomProgressBar.dismissDialog();
        }

        @Override
        public void onLanguageDetected(Language language, int relevance) {

        }

        @Override
        public void onFontSizeChanged(int sizeInPx) {

        }

        @Override
        public void onLineClicked(int lineNumber, String content) {

        }
    }
}