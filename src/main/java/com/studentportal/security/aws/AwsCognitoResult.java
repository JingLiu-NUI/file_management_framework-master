package com.studentportal.security.aws;

public class AwsCognitoResult {
    private boolean isSuccessful;
    private ResultReasons reason;

    public AwsCognitoResult(boolean isSuccessful, ResultReasons reason) {
        this.isSuccessful = isSuccessful;
        this.reason = reason;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public ResultReasons getReason() {
        return reason;
    }

    public void setReason(ResultReasons reason) {
        this.reason = reason;
    }
}
