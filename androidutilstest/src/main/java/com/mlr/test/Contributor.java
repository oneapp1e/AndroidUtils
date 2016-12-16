package com.mlr.test;

/**
 * Created by mulinrui on 12/12 0012.
 */
public class Contributor {

    String login;
    String html_url;

    int contributions;

    @Override
    public String toString() {
        return login + "  " + html_url + " (" + contributions + ")";
    }
}
