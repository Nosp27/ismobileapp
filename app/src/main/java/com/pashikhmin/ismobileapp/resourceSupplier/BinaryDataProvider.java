package com.pashikhmin.ismobileapp.resourceSupplier;

import android.graphics.drawable.Drawable;

import java.io.IOException;

public interface BinaryDataProvider {
    Drawable loadImage(Integer key) throws IOException;
}
