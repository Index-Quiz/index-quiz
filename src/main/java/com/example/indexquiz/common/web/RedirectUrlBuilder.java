package com.example.indexquiz.common.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RedirectUrlBuilder {

    private static final String REDIRECT_PREFIX = "redirect:";

    private final String path;
    private final List<String> params;

    public RedirectUrlBuilder(String path) {
        this.path = path;
        this.params = new ArrayList<>();
    }

    public RedirectUrlBuilder addParam(String key, Optional<String> value) {
        value.ifPresent(v -> params.add(key + "=" + v));
        return this;
    }

    public String build() {
        if (params.isEmpty()) {
            return REDIRECT_PREFIX + path;
        }
        return REDIRECT_PREFIX + path + "?" + String.join("&", params);
    }
}
