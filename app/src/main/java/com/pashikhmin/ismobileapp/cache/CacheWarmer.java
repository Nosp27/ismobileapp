package com.pashikhmin.ismobileapp.cache;

import java.io.IOException;

public interface CacheWarmer {
    void warmUp() throws IOException;
    void reset();
}
