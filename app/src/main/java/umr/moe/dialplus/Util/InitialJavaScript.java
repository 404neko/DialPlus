package umr.moe.dialplus.Util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import umr.moe.dialplus.GlobalApplication;

public class InitialJavaScript {

    public String makeJavascript(Context context){
        String result = "";
        result+=getContacts(context);
        result+="function handle_number(number)\n" +
                "{\n" +
                    "return nubmer;\n" +
                "}\n";

        return  result;

    }

    public String makeJavascript(Context context, String script){

        GlobalApplication globalApplication = (GlobalApplication) context.getApplicationContext();
        if(globalApplication.need_update){
            String result = "";
            result+=getContacts(context);
            result+=script;
            globalApplication.need_update = false;
            globalApplication.script_cache = result;
            return  result;
        }
        else{
            return globalApplication.script_cache;
        }



    }

    public String getContacts(Context context) {

        String result = "var contacts = {};\n";

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Long id = cursor.getLong(0);

                String name = cursor.getString(1);

                String[] phoneProjection = new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

                Cursor phonesCusor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);


                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String num = phonesCusor.getString(0);
                        String item = "contacts[\"" + name + "\"] = \"" + num + "\";\n";
                        result+=item;

                    }while (phonesCusor.moveToNext());
                }
            } while (cursor.moveToNext());
        }

        return result;
    }

}
