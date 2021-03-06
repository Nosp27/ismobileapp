package com.pashikhmin.ismobileapp.cache;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class CacheBuilder {
    Matcher<String> getMatcher, postMatcher;
    CacheWarmer warmer;

    public CacheBuilder() {
        getMatcher = Matchers.any(String.class);
        postMatcher = Matchers.any(String.class);
    }

    public CacheBuilder postMatcher(Matcher<String> matcher) {
        postMatcher = matcher;
        return this;
    }

    public CacheBuilder getMatcher(Matcher<String> matcher) {
        getMatcher = matcher;
        return this;
    }

    public CacheBuilder cacheWarmer(CacheWarmer warmer) {
        this.warmer = warmer;
        return this;
    }
}
