package com.smartaqi.util;

public final class JsonUtil {
    private JsonUtil() {}

    public static String escape(String value) {
        if (value == null) return "";
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }

    public static String readJsonField(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return "";
        int colon = json.indexOf(':', idx);
        if (colon < 0) return "";
        int pos = colon + 1;
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) pos++;
        if (pos >= json.length()) return "";
        char c = json.charAt(pos);
        if (c == '"') {
            int end = json.indexOf('"', pos + 1);
            if (end < 0) return "";
            return json.substring(pos + 1, end).trim();
        }
        int end = pos;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') end++;
        return json.substring(pos, end).trim();
    }
}
