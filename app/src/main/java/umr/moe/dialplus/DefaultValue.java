package umr.moe.dialplus;

public class DefaultValue {
    public static String DEFAULT_SCRIPT = "function onCall(number)\n" +
            "{\n" +
            "    if(number===\"10010\")\n" +
            "    {\n" +
            "        return \"010\" + number;\n" +
            "    }\n" +
            "    else\n" +
            "    {\n" +
            "        return number;\n" +
            "    }\n" +
            "}";
}
