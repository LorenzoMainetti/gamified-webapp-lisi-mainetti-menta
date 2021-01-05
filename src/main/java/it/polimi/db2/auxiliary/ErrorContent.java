package it.polimi.db2.auxiliary;

import java.io.Serializable;

public class ErrorContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String errorType;
    private String errorInfo;

    public ErrorContent(String errorType, String errorInfo) {
        this.errorType = errorType;
        this.errorInfo = errorInfo;
    }
}
