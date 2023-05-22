package com.example.minicode;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {
    public static final short PICK_FILE_CODE = 1001;
    public static final short PERMISSION_REQ_CODE = 1002;
    public static final String PRIVACY_POLICY_URL = "";
    public static List<CodeFile> codeFileList = new ArrayList<>();
    public static final String[] PERMISSIONS = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };
    public static final String PREFERENCE_NAME = "application";

    public static void pickFile(@NonNull AppCompatActivity activity) {
        Intent filePicker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        filePicker.addCategory(Intent.CATEGORY_OPENABLE);
        filePicker.setType("*/*");

        activity.startActivityForResult(filePicker, PICK_FILE_CODE);
    }

    public static String readFile(Context context, Uri uri) {
        StringBuilder sb = new StringBuilder();

        try {
            ContentResolver cr = context.getContentResolver();
            cr.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            InputStream is = cr.openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String reveiveString;

            while ((reveiveString = br.readLine()) != null) sb.append(reveiveString).append("\n");

            is.close();
        } catch (IOException e) {
            //TODO: Add String Resource
            Toast.makeText(context, "Error Reading File", Toast.LENGTH_SHORT).show();
        }

        return sb.toString();
    }

    public static String getFileName(Context context, Uri uri) {
        @SuppressLint("Recycle")
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String fileName = returnCursor.getString(nameIndex);
        returnCursor.close();

        return fileName;
    }

    public static String getFileSize(Context context, Uri uri) {
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String size = returnCursor.getString(sizeIndex);
        returnCursor.close();
        //TODO: Change the return value to decimal format
        return size;
    }

    public static int getLines(@NonNull Context context, @NonNull final String code) {
        int lines = 0;

        BufferedReader br = new BufferedReader(new StringReader(code));
        try {
            while (br.readLine() != null) lines++;
        } catch (IOException e) {
            //TODO: Create a string resource
            Toast.makeText(context, "Unable to count lines", Toast.LENGTH_SHORT).show();
        }

        return lines;
    }

    private static int arraySum(final int[] arr) {
        int sum = 0;
        for (int j : arr) sum += j;
        return sum;
    }

    public static boolean checkPermissions(Context context) {
        int[] temp = new int[PERMISSIONS.length];

        for (int i = 0; i < PERMISSIONS.length; i++)
            temp[i] = (int) context.checkCallingOrSelfPermission(PERMISSIONS[i]);

        return arraySum(temp) == 0;
    }

    public static void launchPermission(AppCompatActivity activity) {
        activity.requestPermissions(PERMISSIONS, PERMISSION_REQ_CODE);
    }
}
