package it.polimi.db2.auxiliary.json;

import java.io.Serializable;

public class ErrorContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String errorType;
    private final String errorInfo;

    public ErrorContent(String errorType, String errorInfo) {
        this.errorType = errorType;
        this.errorInfo = errorInfo;
    }
}
