package com.zte.elasticsearch.support;

import com.zte.elasticsearch.exceptions.TemplateException;

public class EsRequestException extends TemplateException {

    private static final long serialVersionUID = -5581314623116918488L;

    public EsRequestException() {
        super();
    }

    public EsRequestException(String message) {
        super(message);
    }

    public EsRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsRequestException(Throwable cause) {
        super(cause);
    }

}
