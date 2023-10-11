package com.zte.elasticsearch.exceptions;

public class TemplateException extends RuntimeException {
    private static final long serialVersionUID = -7512834501370232060L;

    public TemplateException() {
        super();
    }

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }

}
