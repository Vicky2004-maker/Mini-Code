package com.example.minicode;

import com.example.minicode.CodeViewBackend.Language;

public class CodeFile {
    private String _Name;
    private int _Size;
    private String _Path;
    private Language _Language;
    private int _Lines;

    public CodeFile(String _Path) {
        this._Path = _Path;
        init();
    }

    private void init() {

    }
}
