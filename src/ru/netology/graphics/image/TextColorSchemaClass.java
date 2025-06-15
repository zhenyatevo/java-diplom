package ru.netology.graphics.image;

public class TextColorSchemaClass implements TextColorSchema {
    private final char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        int index = (int) Math.round((symbols.length - 1) * color / 255.0);
        index = Math.max(0, Math.min(index, symbols.length - 1));
        return symbols[index];
    }
}
