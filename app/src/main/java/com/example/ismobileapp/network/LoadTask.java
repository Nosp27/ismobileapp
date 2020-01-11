package com.example.ismobileapp.network;

import android.os.AsyncTask;
import com.example.ismobileapp.model.Criteries;

import java.util.function.Consumer;
import java.util.function.Function;

public class LoadTask<T> extends AsyncTask<Criteries, Integer, T> {
    private Function<Criteries, T> task;
    private Consumer<T> callback;

    public LoadTask(Function<Criteries, T> task, Consumer<T> callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    protected T doInBackground(Criteries... criteries) {
        return task.apply(criteries[0]);
    }

    @Override
    protected void onPostExecute(T result) {
        callback.accept(result);
    }
}
