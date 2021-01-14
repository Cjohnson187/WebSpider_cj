package com.chris.helper;


/****************************************************************************
 * <b>Title</b>: ConnectionManager.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> The connection manager will be used by the spider to
 * make and handle requests/cookies from the current page given by the spider.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Jan 5, 2021
 * @updates:
 ****************************************************************************/
public class ConnectionManager {

    private static String url;
    private static String cookie;
    // input buffer
    // output buffer


    private ConnectionManager(String url){
        this.url = url;
    }

    private static void makeRequest(){
        // send request to page
    }
    public static void readResponse(String siteResponse){
        // read response maybe
    }

    public static void saveCookie(String newCookie) {
        cookie = newCookie;
    }
    public static String getCookie() {
        return cookie;
    }


}
