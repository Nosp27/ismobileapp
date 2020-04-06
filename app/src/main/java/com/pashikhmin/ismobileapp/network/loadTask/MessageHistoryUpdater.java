package com.pashikhmin.ismobileapp.network.loadTask;

import android.util.Log;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;

public class MessageHistoryUpdater {
    private final List<Message> messageHistory;
    private final Set<Integer> allMessageIds;
    private long lastUpdateTime;
    private Runnable onUpdate;

    private final long timeout;
    private final int issue_id;
    private boolean running = true;

    private static final String TAG = "MessageHistoryUpdater";

    public MessageHistoryUpdater(List<Message> initialHistory, int issue_id) {
        this(initialHistory, issue_id, 700);
    }

    public MessageHistoryUpdater(List<Message> initialHistory, int issue_id, long timeout) {
        messageHistory = new ArrayList<>(initialHistory);

        allMessageIds = new HashSet<>();

        for (Message m : messageHistory)
            allMessageIds.add(m.getId());

        if (messageHistory.size() > 0)
            lastUpdateTime = messageHistory.get(messageHistory.size() - 1).getSendTime();
        else lastUpdateTime = 0;

        this.issue_id = issue_id;
        this.timeout = timeout;
        Executors.defaultThreadFactory().newThread(this::updateTask).start();
    }

    private void updateTask() {
        try {
            while (true) {
                synchronized (this) {
                    if (!running)
                        return;
                }
                try {
                    List<Message> newMessages = Connectors
                            .api()
                            .getNewMessages(issue_id, returnAndUpdateTime());
                    newMessages.sort(Comparator.comparing(Message::getSendTime));
                    synchronized (this) {
                        for (Message m : newMessages)
                            if (!allMessageIds.contains(m.getId())) {
                                messageHistory.add(m);
                                allMessageIds.add(m.getId());
                            }
                        if (onUpdate != null)
                            onUpdate.run();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "updateTask: Cannot load new messages", e);
                }
                synchronized (this) {
                    wait(timeout);
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "updateTask: Interrupted Exception while updating message history", e);
        }
    }

    public synchronized List<Message> getUpToDateHistory() {
        return messageHistory;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public synchronized void stop() {
        running = false;
    }

    private synchronized long returnAndUpdateTime() {
        long currentLastUpdateTime = lastUpdateTime;
        lastUpdateTime = System.currentTimeMillis() - timeout * 2 - 1000 * 3; // two timeouts ago
        return currentLastUpdateTime;
    }
}
