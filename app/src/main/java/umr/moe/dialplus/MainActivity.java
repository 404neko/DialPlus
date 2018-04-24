package umr.moe.dialplus;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;


import umr.moe.dialplus.Engine.Core;
import umr.moe.dialplus.Util.InitialJavaScript;


public class MainActivity extends AppCompatActivity {

    Boolean initialized = false;
    String last_result = "";
    SharedPreferences preferences = null;
    Context context = null;
    Handler mHandler = new Handler();
    AppCompatActivity self = this;
    umr.moe.dialplus.Observer.Content observer_content = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        observer_content = new umr.moe.dialplus.Observer.Content(mHandler, context.getApplicationContext());

        final EditText number_to_test = (EditText)findViewById(R.id.number_to_test);
        final EditText script = (EditText)findViewById(R.id.script);
        script.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        script.setGravity(Gravity.TOP);
        script.setSingleLine(false);
        script.setHorizontallyScrolling(false);

        final TextView log_out = (TextView)findViewById(R.id.log_out);
        log_out.setGravity(Gravity.BOTTOM);

        Button button_test = (Button)findViewById(R.id.button_test);
        final Button button_save = (Button)findViewById(R.id.button_save);
        final Button button_switch = (Button)findViewById(R.id.button_switch);
        Button button_initialize = (Button)findViewById(R.id.button_initialize);


        requestPermission(Manifest.permission.PROCESS_OUTGOING_CALLS);
        requestPermission(Manifest.permission.READ_CONTACTS);

        final GlobalApplication globalApplication = (GlobalApplication)getApplication();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String now_script = preferences.getString("now_script", DefaultValue.DEFAULT_SCRIPT);
        initialized = preferences.getBoolean("initialized", false);

        globalApplication.lastRunableScript = now_script;
        script.setText(now_script);

        final Core engine_core = new Core((GlobalApplication) getApplication());

        final InitialJavaScript script_util = new InitialJavaScript();

        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now_script = script.getText().toString();
                globalApplication.need_update = true;
                globalApplication.script_cache = "";
                String result = engine_core.execute_raw( script_util.makeJavascript(globalApplication, now_script)+ "\n" + "onCall(\"" + number_to_test.getText().toString() + "\")");
                log_out.append("TestResult:");
                log_out.append("\n");
                log_out.append(result);
                log_out.append("\n");
                last_result = result;
                if(result.equals("EXECUTE_ERROR")){
                    ;
                }
                else{
                    globalApplication.lastRunableScript = now_script;
                }

            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now_script = script.getText().toString();
                if(now_script.equals(globalApplication.lastRunableScript)){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("now_script", now_script);
                    editor.commit();
                }
                else{
                    Toast.makeText(context, "You have not tested your code before you save it.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Boolean now = preferences.getBoolean("switch", false);
        button_switch.setText("Dial+: " + now);

        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean now = preferences.getBoolean("switch", false);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch", !now);
                editor.commit();
                button_switch.setText("Dial+: " + !now);
            }
        });

        button_initialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false, observer_content);
                    Toast.makeText(context, "registerContentObserver: OK", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(context, "registerContentObserver: " + e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    private void requestPermission(String permission) {
        PermissionsUtil.requestPermission(getApplication(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permissions) {
                Toast.makeText(MainActivity.this, "Permission Grant", Toast.LENGTH_LONG).show();
            }

            @Override
            public void permissionDenied(@NonNull String[] permissions) {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }, permission);
    }


}
