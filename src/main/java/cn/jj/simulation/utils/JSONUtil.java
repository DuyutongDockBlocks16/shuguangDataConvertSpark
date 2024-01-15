package cn.jj.simulation.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {
    public static JSONObject JSONString2JSONObject(String str) {
        return JSONObject.fromObject(str);
    }

    public static String object2JSONString(Object obj) {
        JSONObject jsonObj = JSONObject.fromObject(obj);
        return jsonObj.toString();
    }

    public static JSONArray JSONString2JSONArray(String str) {
        return JSONArray.fromObject(str);
    }

    public static String JSONObject2JSONString(JSONObject jsonObj) {
        return jsonObj.toString();
    }
}