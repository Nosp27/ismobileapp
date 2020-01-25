package com.pashikhmin.ismobileapp.network.exceptions;

import java.io.IOException;

public class LoginRequiredException extends IOException {
    private String redirectPage;

    public LoginRequiredException() {
    }

    public LoginRequiredException(Throwable cause) {
        super(cause);
    }

    public LoginRequiredException(String redirect) {
        super();
        redirectPage = redirect;
    }

    public String getRedirectPage() {
        return redirectPage;
    }
}
