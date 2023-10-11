package com.zte.elasticsearch.support;

import com.zte.elasticsearch.exceptions.TemplateException;

public class JsonException extends TemplateException {

    private static final long serialVersionUID = -4473738425654914999L;

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }

}
