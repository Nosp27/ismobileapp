package com.pashikhmin.ismobileapp.network.loadTask;

import android.os.AsyncTask;
import com.pashikhmin.ismobileapp.model.Criteries;

import java.util.function.Consumer;
import java.util.function.Function;

public class LoadTask<T> extends AsyncTask<Criteries, Integer, LoadTaskResult<T>> {
    private Function<Criteries, T> task;
    private Consumer<LoadTaskResult<T>> callback;

    public LoadTask(Function<Criteries, T> task, Consumer<LoadTaskResult<T>> callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    protected LoadTaskResult<T> doInBackground(Criteries... criteries) {
        T result;
        try {
            if (criteries != null && criteries.length > 0)
                result = task.apply(criteries[0]);
            else
                result = task.apply(null);
        } catch (Throwable t) {
            return new LoadTaskResult<>(t);
        }
        return new LoadTaskResult<>(result);
    }

    @Override
    protected void onPostExecute(LoadTaskResult<T> result) {
        callback.accept(result);
    }
}
