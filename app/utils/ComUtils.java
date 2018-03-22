package utils;

import play.templates.JavaExtensions;

import java.util.Date;

/**
 * @autor kevin.dai
 * @Date 2018/3/21
 */
public class ComUtils {

    public static String formatDate(Long date,String format){
        return JavaExtensions.format(new Date(date),format);
    }


}
