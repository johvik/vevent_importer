package john.veventimporter;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class MyAsyncTaskLoader<T> extends AsyncTaskLoader<T> {
    private T mData;

    public MyAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            return;
        }
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        mData = null;
    }
}
