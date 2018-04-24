package umr.moe.dialplus.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import umr.moe.dialplus.DefaultValue;
import umr.moe.dialplus.Engine.Core;
import umr.moe.dialplus.GlobalApplication;
import umr.moe.dialplus.Util.InitialJavaScript;

public class OutgoingCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        String trace = "";
        GlobalApplication globalApplication = (GlobalApplication)context.getApplicationContext();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(globalApplication);

        String history = preferences.getString("history", "");



        if((number != null)&&preferences.getBoolean("switch", false)&&(!history.contains(number)))
        {
            String append_script = "\n" + "onCall(\"" + number + "\");\n";
            final InitialJavaScript script_util = new InitialJavaScript();
            String final_script = script_util.makeJavascript(context, globalApplication.lastRunableScript) + append_script;

            String result;
            try
            {
                result = new Core(globalApplication).execute_raw(final_script);
            }
            catch (Exception e)
            {
                result = "EXECUTE_ERROR";
                trace = e.toString();
            }

            if(result.equals("BLOCK"))
            {
                Toast.makeText(context, "Number: " + number + "has been block.", Toast.LENGTH_LONG).show();
                setResultData(null);
            }
            if(result.equals("EXECUTE_ERROR"))
            {
                Toast.makeText(context, "Script execute error, please check the script.", Toast.LENGTH_LONG).show();
                Toast.makeText(context, trace, Toast.LENGTH_LONG).show();
                setResultData(number);
            }
            else
            {
                history+="|";
                history+=result;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("history", history);
                editor.commit();
                setResultData(result);

            }

        }
        else
        {
            if(number==null){
                Toast.makeText(context, "intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);:null", Toast.LENGTH_LONG).show();
            }
            else{
                setResultData(number);
            }

        }
    }
}
