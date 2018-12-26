package cn.iocoder.oceans.assembly;

import cn.iocoder.oceans.item.service.ItemServiceApplication;
import cn.iocoder.oceans.user.service.UserServiceApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;

// TODO 芋艿，暂时失败。想要模仿 apollo 的做法
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication()
public class AssemblyApplication {

    public static void main(String[] args) {
        // Common
        ConfigurableApplicationContext commonContext =
                new SpringApplicationBuilder(AssemblyApplication.class).web(false).run(args);
        commonContext.addApplicationListener(new ApplicationPidFileWriter());

        // User
        if (true) {
            ConfigurableApplicationContext configContext =
                    new SpringApplicationBuilder(UserServiceApplication.class).parent(commonContext).run(args);
        }

        // Item
        if (false) {
            ConfigurableApplicationContext configContext =
                    new SpringApplicationBuilder(ItemServiceApplication.class).parent(commonContext).run(args);
        }
    }

}