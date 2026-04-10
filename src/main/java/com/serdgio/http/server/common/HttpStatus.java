package com.serdgio.http.server.common;

public enum HttpStatus {
    OK(200, Series.SUCCESFUL, "OK"),
    CREATED(201, Series.SUCCESFUL, "Created"),
    NOT_FOUND(404, Series.CLIENT_ERROR, "Not Found");

    private final int code;
    private final Series series;
    private final String reasonPhrase;

    HttpStatus(int code, Series series, String reasonPhrase) {
        this.code = code;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return code;
    }

    public Series getSeries() {
        return series;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public boolean is1xxInformational() {
        return series == Series.INFORMATIONAL;
    }

    public boolean is2xxSuccessful() {
        return series == series.SUCCESFUL;
    }

    public boolean is3xxRedirection() {
        return series == series.REDIRECTION;
    }

    public boolean is4xxClientError() {
        return series == series.CLIENT_ERROR;
    }

    public boolean is5xxServerError() {
        return series == series.SERVER_ERROR;
    }

    public boolean isError() {
        return is4xxClientError() || is5xxServerError();
    }

    public enum Series {
        INFORMATIONAL(1),
        SUCCESFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;
        Series(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
