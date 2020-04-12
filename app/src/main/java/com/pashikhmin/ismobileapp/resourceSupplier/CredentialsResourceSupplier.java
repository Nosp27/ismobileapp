package com.pashikhmin.ismobileapp.resourceSupplier;

import com.pashikhmin.ismobileapp.model.helpdesk.Actor;

import java.io.IOException;

public interface CredentialsResourceSupplier {
    String getCookie(String username, String password) throws IOException;
    String signIn(Actor actor, String password) throws IOException;
}
