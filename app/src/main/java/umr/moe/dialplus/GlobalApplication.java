package umr.moe.dialplus;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.mozilla.javascript.*;


public class GlobalApplication extends Application {

    public Context cx = null;
    public Scriptable scope = null;
    public String lastRunableScript = "";
    public String script_cache = "";
    public Boolean need_update = true;


    @Override
    public void onCreate() {
        super.onCreate();

        cx = Context.enter();
        cx.setOptimizationLevel(-1);
        scope = cx.initStandardObjects();


    }

}
