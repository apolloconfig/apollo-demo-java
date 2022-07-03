package com.apolloconfig.apollo.demo.spring.bean;

import com.ctrip.framework.apollo.spring.events.ApolloConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public class TestApplicationListener implements ApplicationListener<ApolloConfigChangeEvent> {
  private static final Logger logger = LoggerFactory.getLogger(TestApplicationListener.class);

  @Override
  public void onApplicationEvent(ApolloConfigChangeEvent apolloConfigChangeEvent) {
    logger.info("[onApplicationEvent]ApolloConfigChangeEvent received, namespace: {}",
        apolloConfigChangeEvent.getConfigChangeEvent().getNamespace());
  }
}
