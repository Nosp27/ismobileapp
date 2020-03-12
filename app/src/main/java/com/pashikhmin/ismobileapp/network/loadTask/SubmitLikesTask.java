package com.pashikhmin.ismobileapp.network.loadTask;

import android.os.AsyncTask;
import android.util.Log;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;

import java.io.IOException;
import java.util.function.Consumer;

public class SubmitLikesTask extends AsyncTask<Facility, Object, Boolean> {
    private static final String TAG = "SubmitLikesTask";
    private Consumer<LoadTaskResult<Boolean>> callback;


    public SubmitLikesTask(Consumer<LoadTaskResult<Boolean>> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Facility... facilities) {
        boolean ret = true;
        for (Facility f : facilities) {
            try {
                boolean likedFacility = Connectors.api().changeLike(f);
                ret &= likedFacility;
                f.setLiked(likedFacility);
            } catch (IOException e) {
                Log.e(TAG, String.format("Liking facility #%d failed.", f.getId()), e);
            }
        }
        return ret;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        callback.accept(new LoadTaskResult<>(result));
    }
}
