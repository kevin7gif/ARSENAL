package com.xiehn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableDiscoveryClient
@EnableCaching
@EnableAsync
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "com.xiehn")
public class Application {
    public static void main(String[] args) {

        log.warn("-----------------------ARSENAL Start-----------------------");
        SpringApplication.run(Application.class, args);
        printBuddha();
        log.info("-----------------------ARSENAL End  -----------------------");
    }

    public static void printBuddha() {
        String buddhaArt =
                "                   _ooOoo_\n" +
                        "                  o8888888o\n" +
                        "                  88\" . \"88\n" +
                        "                  (| -_- |)\n" +
                        "                  O\\  =  /O\n" +
                        "               ____/`---'\\____\n" +
                        "             .'  \\\\|     |//  `.\n" +
                        "            /  \\\\|||  :  |||//  \\\n" +
                        "           /  _||||| -:- |||||-  \\\n" +
                        "           |   | \\\\\\  -  /// |   |\n" +
                        "           | \\_|  ''\\---/''  |   |\n" +
                        "           \\  .-\\__  `-`  ___/-. /\n" +
                        "         ___`. .'  /--.--\\  `. . __\n" +
                        "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n" +
                        "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                        "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
                        "======`-.____`-.___\\_____/___.-`____.-'======\n" +
                        "                   `=---='\n" +
                        "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                        "           佛祖保佑       永无BUG\n";
        log.info(buddhaArt);
    }

}