package com.example.minicode;

import static com.example.minicode.Helper.getChars;
import static com.example.minicode.Helper.getFileName;
import static com.example.minicode.Helper.getFileSize;
import static com.example.minicode.Helper.getLines;
import static com.example.minicode.Helper.readFile;

import android.content.Context;
import android.net.Uri;

import com.example.minicode.CodeViewBackend.Language;

public class CodeFile {
    private final Context _Context;
    private String _Name;
    private double _Size;
    private final Uri _Path;
    private Language _Language;
    private int _Lines;
    private int _CharsCount;

    private String _Code;

    public CodeFile(Context context, Uri _Path) {
        this._Context = context;
        this._Path = _Path;
        init();
    }

    public String get_Code() {
        return _Code;
    }

    public int get_CharsCount() {
        return _CharsCount;
    }

    public String getLineInfo() {
        return get_Lines() + " (" + get_CharsCount() + ")";
    }

    private void init() {
        this._Name = getFileName(_Context, _Path);
        this._Code = readFile(_Context, _Path);
        this._Lines = getLines(_Context, _Code);
        this._Size = getFileSize(_Context, _Path);
        this._CharsCount = getChars(_Code);
    }

    public String get_Name() {
        return _Name;
    }

    public double get_Size() {
        return _Size;
    }

    public String getSizeStr() {
        return get_Size() + " KB";
    }

    public String get_Path() {
        return String.valueOf(_Path);
    }

    public Uri get_PathURI() {
        return this._Path;
    }

    public Language get_Language() {
        return _Language;
    }

    public int get_Lines() {
        return _Lines;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public void set_Size(int _Size) {
        this._Size = _Size;
    }

    public void set_Language(Language _Language) {
        this._Language = _Language;
    }

    public void set_Lines(int _Lines) {
        this._Lines = _Lines;
    }
}
