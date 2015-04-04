package com.vsgh.pronounceit.apihelpers.forvo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Slawa on 2/8/2015.
 */
public class ForvoParams {
    public static final String LANGUAGE = "language";
    public static final String ACTION = "action";
    public static final String FORMAT = "format";
    public static final String WORD = "word";
    public static final String SEX = "sex";
    public static final String LIMIT = "limit";
    private static final String API_DOMAIN_ROOT = "http://apifree.forvo.com/";
    private static final String API_KEY_METHOD = "key";
    private static final String API_KEY = "847d9bbb005db4aff177b30755e722ad";
    private HashMap<String, String> args = new HashMap<String, String>();

    public ForvoParams() {
    }

    public static String getUrl(String word) {
        ForvoParams forvoParams = new ForvoParams();
        String request = forvoParams.buildTypicalRequest(word);
        return API_DOMAIN_ROOT + request;
    }

    private void put(String name, String value) {
        if (name == null || value.length() == 0)
            return;
        args.put(name, value);
    }

    private String getParamsString() {
        StringBuilder paramsBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            if (paramsBuilder.length() != 0) {
                paramsBuilder.append("/");
            }
            paramsBuilder
                    .append(entry.getKey())
                    .append("/")
                    .append(entry.getValue());
        }
        return paramsBuilder.toString();
    }

    private String buildTypicalRequest(String word) {
        this.put(API_KEY_METHOD, API_KEY);
        this.put(FORMAT, "json");
        this.put(ACTION, "word-pronunciations");
        this.put(WORD, word);
        this.put(LANGUAGE, "en");
        //NEXT PARAM MUST BE IGNORED BECAUSE SOME WORDS HAVE NOT BEEN PRONOUNCED BY WOMEN
        //this.put(SEX, "f");
        this.put(LIMIT, "1");
        return getParamsString();
    }
}

