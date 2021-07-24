package com.example.backend.utils.interfaces.string;

import java.io.UnsupportedEncodingException;

public interface UrlBuilder {

    String FORMAT_TYPE = "json";

    String ENCODING_TYPE = "UTF-8";

    String FORWARD_SLASH = "/";

    String openApiUrlBuilder(String url, String key, String name, int startIdx, int endIdx) throws UnsupportedEncodingException;

}
