package com.example.minicode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.minicode.Helper.*;

import com.example.minicode.CodeViewBackend.CodeView;
import com.example.minicode.CodeViewBackend.Language;
import com.example.minicode.CodeViewBackend.Theme;

public class CodeViewActivity extends AppCompatActivity {

    CodeView codeView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch(requestCode) {
            case PICK_FILE_CODE: {
                if(data != null) {
                    initializeCodeView(readFile(CodeViewActivity.this, data.getData()));
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
        ProgressDialog progressDialog;

        @Override
        public void onStartCodeHighlight() {
            progressDialog = ProgressDialog.show(CodeViewActivity.this, null, "Loading", true);
        }

        @Override
        public void onFinishCodeHighlight() {
            if(progressDialog != null) progressDialog.dismiss();
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