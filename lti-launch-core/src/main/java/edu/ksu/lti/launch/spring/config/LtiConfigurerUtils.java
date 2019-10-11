package edu.ksu.lti.launch.spring.config;

import edu.ksu.lti.launch.service.LtiLoginService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;

class LtiConfigurerUtils {

    static <B extends HttpSecurityBuilder<B>> LtiLoginService getLtiLoginService(B builder) {
        LtiLoginService ltiLoginService = builder.getSharedObject(LtiLoginService.class);
        if (ltiLoginService == null) {
            ltiLoginService = getLtiLoginServiceBean(builder);
            builder.setSharedObject(LtiLoginService.class, ltiLoginService);
        }
        return ltiLoginService;
    }

    private static <B extends HttpSecurityBuilder<B>> LtiLoginService getLtiLoginServiceBean(B builder) {
        return builder.getSharedObject(ApplicationContext.class).getBean(LtiLoginService.class);
    }


    static <B extends HttpSecurityBuilder<B>> ToolConsumerService getToolConsumerService(B builder) {
        ToolConsumerService toolConsumerService = builder.getSharedObject(ToolConsumerService.class);
        if (toolConsumerService == null) {
            toolConsumerService = getToolConsumerServiceBean(builder);
            builder.setSharedObject(ToolConsumerService.class, toolConsumerService);
        }
        return toolConsumerService;
    }

    private static <B extends HttpSecurityBuilder<B>> ToolConsumerService getToolConsumerServiceBean(B builder) {
        return builder.getSharedObject(ApplicationContext.class).getBean(ToolConsumerService.class);
    }

}
