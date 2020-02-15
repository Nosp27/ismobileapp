package com.pashikhmin.ismobileapp.resourceSupplier;

import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;

import java.io.IOException;
import java.util.List;

public interface HelpDeskResourceSupplier {
    List<Issue> getOpenedIssues() throws IOException;
    List<Message> getIssueHistory(Issue issue) throws IOException;
}
