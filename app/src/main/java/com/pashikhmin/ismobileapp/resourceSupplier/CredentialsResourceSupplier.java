package com.pashikhmin.ismobileapp.resourceSupplier;

import java.io.IOException;

public interface CredentialsResourceSupplier {
    String getCookie(String username, String password) throws IOException;
}
