package ru.netology.graphics.image.impl;

import ru.netology.graphics.image.TextColorSchema;

/**
 * @author Dm.Petrov
 * DATE: 14.05.2022
 */
public class TextColorSchemaImpl implements TextColorSchema {
    public static final char[] COLORS = {'#', '$', '@', '%', '*', '+', '-', ' '};
    @Override
    public char convert(int color) {
            return COLORS[color/32];
    }
}
