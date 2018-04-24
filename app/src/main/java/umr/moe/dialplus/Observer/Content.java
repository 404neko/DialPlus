package umr.moe.dialplus.Observer;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import umr.moe.dialplus.GlobalApplication;


public class Content extends ContentObserver {

    private static final String TAG = "Content";
    private  Context context = null;

    public Content(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        GlobalApplication globalApplication = (GlobalApplication)context.getApplicationContext();
        globalApplication.need_update = true;
        globalApplication.script_cache = "";


    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        GlobalApplication globalApplication = (GlobalApplication)context.getApplicationContext();
        globalApplication.need_update = true;
        globalApplication.script_cache = "";


    }
}