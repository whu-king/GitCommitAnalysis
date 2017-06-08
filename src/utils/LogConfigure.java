package utils;

import org.apache.log4j.PropertyConfigurator;

import org.apache.log4j.Logger;;

/**
 * Created by Administrator on 2017/5/16.
 */
public class LogConfigure {

    public static Logger log;

    static {
        String path2 = LogConfigure.class.getResource("/").toString();
        System.out.println("path2 = " + path2);
        PropertyConfigurator.configure(path2.replace("file:/","") + "log4j.properties");
        log = Logger.getLogger("test");
    }


}
