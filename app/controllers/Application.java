package controllers;

import cn.bran.play.JapidController;



public class Application extends JapidController {

    public static void index() {
        System.err.println(12);
        renderHtml("start.....");
    }

}