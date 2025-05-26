package biz.deinum.moneytransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class ApplicationContextLogger {

  private static final Logger LOG = LoggerFactory.getLogger(ApplicationContextLogger.class);

  public static void log(ApplicationContext context) {
    LOG.info("-".repeat(60));
    LOG.info("Context: {},{}", context.getClass(), context.getDisplayName());
    LOG.info("Beans: {}", context.getBeanDefinitionCount());
    LOG.info("Parent: {}", context.getParent() != null ? context.getParent().getDisplayName() : "no-parent");
    LOG.info("Active profiles: {}", StringUtils.arrayToCommaDelimitedString(context.getEnvironment().getActiveProfiles()));
    for (var name : context.getBeanDefinitionNames()) {
      LOG.info("Bean: {}", name);
    }
  }
}
