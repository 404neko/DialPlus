package umr.moe.dialplus.Engine;

import umr.moe.dialplus.GlobalApplication;

public final class Core {

    GlobalApplication globalApplication = null;

    public Core(GlobalApplication application){
        globalApplication = application;
    }

    public final  String execute_raw(String script){

        try{
            Object result = globalApplication.cx.evaluateString(globalApplication.scope, script, "<sakura>", 0, null);
            return org.mozilla.javascript.Context.toString(result);
        }
        catch (Exception e){
            e.printStackTrace();
            return "EXECUTE_ERROR";
        }

    }

}
