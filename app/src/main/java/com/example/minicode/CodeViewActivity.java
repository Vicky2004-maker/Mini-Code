package com.example.minicode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.minicode.Helper.*;

import com.example.minicode.CodeViewBackend.CodeView;
import com.example.minicode.CodeViewBackend.Language;
import com.example.minicode.CodeViewBackend.Theme;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

    private ChipGroup chipGroupLayout;
    private LinearLayout chipContainer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_FILE_CODE: {
                if (data != null) {
                    if(codeFileList.stream().noneMatch(x -> data.getData().equals(x.get_PathURI()))) {
                        CodeFile codeFile = new CodeFile(CodeViewActivity.this, data.getData());
                        codeFileList.add(codeFile);
                        _CodeFile = codeFile;
                        menuBar.setVisibility(View.VISIBLE);
                        initializeCodeView(codeFile.get_Code());

                        lineInfo_textView.setText(String.valueOf(_CodeFile.get_Lines()));
                        fileSize_textView.setText(_CodeFile.getSizeStr());

                        Objects.requireNonNull(getSupportActionBar()).setSubtitle(getFileName(CodeViewActivity.this, codeFile.get_PathURI()));

                        if (codeFileList.size() == 2) {
                            chipGroupLayout.setVisibility(View.VISIBLE);
                            createNewChip(codeFileList.get(0).get_Name(), 0);
                            createNewChip(codeFileList.get(1).get_Name(), 1);
                        } else if (codeFileList.size() > 2) {
                            createNewChip(_CodeFile.get_Name(), codeFileList.size() - 1);
                        }
                    } else {
                        Toast.makeText(CodeViewActivity.this, "File Already Opened", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CodeViewActivity.this, "No File Selected", Toast.LENGTH_SHORT).show();
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

        chipContainer = findViewById(R.id.chip_container);
        chipGroupLayout = findViewById(R.id.chipGroup_layout);
        codeView = findViewById(R.id.codeView_main);
        menuBar = findViewById(R.id.menu_bar);
        codeView_fab = findViewById(R.id.codeView_fab);
        openFile_chip = findViewById(R.id.openFile_chip);
        fileSize_textView = findViewById(R.id.fileSize_textView);
        lineInfo_textView = findViewById(R.id.lineInfo_textView);

        openFile_chip.setOnClickListener(a -> pickFile(CodeViewActivity.this));
        codeView_fab.setOnClickListener(a -> bottomMenuSheet());
    }

    private void createNewChip(@NonNull final String fileName, final int chipId) {
        Chip chip = new Chip(CodeViewActivity.this);
        chip.setId(chipId);
        chip.setText(fileName);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(5, 0, 5, 0);
        chip.setLayoutParams(layoutParams);
        chip.setOnClickListener(a -> {
            ;
        });
        chipContainer.addView(chip);
    }

    private void setActiveFile() {

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
        //TODO: Improve Dialog
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