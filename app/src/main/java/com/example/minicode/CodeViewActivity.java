package com.example.minicode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.minicode.Helper.*;

import com.example.minicode.CodeViewBackend.CodeView;
import com.example.minicode.CodeViewBackend.Language;
import com.example.minicode.CodeViewBackend.Theme;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class CodeViewActivity extends AppCompatActivity {

    private CodeFile _CodeFile;
    CodeView codeView;
    ConstraintLayout menuBar;
    FloatingActionButton codeView_fab;

    Chip openFile_chip;

    TextView fileSize_textView, lineInfo_textView;

    Dialog dialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_FILE_CODE: {
                if (data != null) {
                    CodeFile codeFile = new CodeFile(CodeViewActivity.this, data.getData());
                    codeFileList.add(codeFile);
                    _CodeFile = codeFile;
                    menuBar.setVisibility(View.VISIBLE);
                    initializeCodeView(codeFile.get_Code());

                    lineInfo_textView.setText(String.valueOf(_CodeFile.get_Lines()));
                    fileSize_textView.setText(_CodeFile.getSizeStr());

                    Objects.requireNonNull(getSupportActionBar()).setSubtitle(getFileName(CodeViewActivity.this, codeFile.get_PathURI()));
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.openFileMenu) pickFile(CodeViewActivity.this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.codeview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_view);

        dialog = new Dialog(CodeViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_layout);

        codeView = findViewById(R.id.codeView_main);
        menuBar = findViewById(R.id.menu_bar);
        codeView_fab = findViewById(R.id.codeView_fab);
        openFile_chip = findViewById(R.id.openFile_chip);
        fileSize_textView = findViewById(R.id.fileSize_textView);
        lineInfo_textView = findViewById(R.id.lineInfo_textView);

        codeView_fab.setOnClickListener(a -> bottomMenuSheet());
    }

    private void bottomMenuSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(CodeViewActivity.this);
        dialog.setContentView(R.layout.menu_bar_sheet);

        TextView fileSize = dialog.findViewById(R.id.fileSize_menu);
        TextView totalLines = dialog.findViewById(R.id.totalLines_menu);
        ImageView closeMenu = dialog.findViewById(R.id.close_menu);
        ImageButton copyAll = dialog.findViewById(R.id.copyAll_menu);
        ImageButton fullScreen = dialog.findViewById(R.id.fullScreen_menu);
        ImageButton editCode = dialog.findViewById(R.id.editCode_menu);
        ImageButton searchCode = dialog.findViewById(R.id.searchCode_menu);

        assert fileSize != null;
        assert totalLines != null;
        assert closeMenu != null;
        assert copyAll != null;
        assert fullScreen != null;
        assert editCode != null;
        assert searchCode != null;

        if (isFullScreen) {
            fullScreen.setImageDrawable(AppCompatResources.getDrawable(CodeViewActivity.this, R.drawable.baseline_fullscreen_exit));
        } else {
            fullScreen.setImageDrawable(AppCompatResources.getDrawable(CodeViewActivity.this, R.drawable.baseline_fullscreen));
        }

        fileSize.setText(_CodeFile.getSizeStr());
        totalLines.setText(String.valueOf(_CodeFile.get_Lines()));
        closeMenu.setOnClickListener(x -> dialog.dismiss());
        copyAll.setOnClickListener(x -> copyAll());
        fullScreen.setOnClickListener(x -> {
            if (isFullScreen) {
                Objects.requireNonNull(getSupportActionBar()).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                isFullScreen = false;
                fullScreen.setImageDrawable(AppCompatResources.getDrawable(CodeViewActivity.this, R.drawable.baseline_fullscreen));
            } else {
                Objects.requireNonNull(getSupportActionBar()).hide();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                isFullScreen = true;
                fullScreen.setImageDrawable(AppCompatResources.getDrawable(CodeViewActivity.this, R.drawable.baseline_fullscreen_exit));
            }
        });
        editCode.setOnClickListener(x -> editCode());
        searchCode.setOnClickListener(x -> searchCode());

        dialog.show();
    }

    private void searchCode() {

    }

    private void editCode() {

    }

    private void copyAll() {
        ClipboardManager clipboard = ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE));
        clipboard.setPrimaryClip(ClipData.newPlainText(_CodeFile.get_Name(), _CodeFile.get_Code()));
    }

    private void initializeCodeView(@NonNull final String code) {
        codeView.setTheme(Theme.ANDROIDSTUDIO)
                .setOnHighlightListener(new CodeView.OnHighlightListener() {
                    @Override
                    public void onStartCodeHighlight() {
                        dialog.show();
                    }

                    @Override
                    public void onFinishCodeHighlight() {
                        dialog.hide();
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
                })
                .setLanguage(Language.AUTO)
                .setCode(code)
                .setZoomEnabled(true)
                .apply();
    }
}