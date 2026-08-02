package com.serdgio.http.server.common;

public class ApplicationParametrs {
    private static volatile ApplicationParametrs INSTANCE;
    private String directory;

    private ApplicationParametrs() {
    }

    public static ApplicationParametrs getInstance() {
        if (INSTANCE == null) {
            synchronized (ApplicationParametrs.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApplicationParametrs();
                }
            }
        }
        return INSTANCE;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d") || args[i].equals("--directory")) {
                directory = args[i + 1];
            }
        }
    }

    public boolean isDirectorySet() {
        return directory != null && !directory.isBlank();
    }
}
