package dev.lemon.recode.module;

public @interface ModuleInfo {
    String name();
    int key();
    Category category();
    boolean toggled();
}
