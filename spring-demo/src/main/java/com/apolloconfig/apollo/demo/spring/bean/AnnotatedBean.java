/*
 * Copyright 2022 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.apolloconfig.apollo.demo.spring.bean;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("annotatedBean")
public class AnnotatedBean {

  private static final Logger logger = LoggerFactory.getLogger(AnnotatedBean.class);

  private int timeout;
  private int batch;
  private List<JsonBean> jsonBeans;
  @ApolloConfig(appId = "100004459")
  private Config anotherAppConfig;

  /**
   * ApolloJsonValue annotated on fields example, the default value is specified as empty list - []
   * <br /> jsonBeanProperty=[{"someString":"hello","someInt":120,"someDate":"2024-01-04T10:01:02"},{"someString":"world!","someInt":101,"someDate":"2024-01-05T11:02:03"}]
   */
  @ApolloJsonValue(value = "${jsonBeanProperty:[]}", datePattern = "yyyy-MM-dd'T'HH:mm:ss")
  private List<JsonBean> anotherJsonBeans;

  @Value("${batch:100}")
  public void setBatch(int batch) {
    logger.info("updating batch, old value: {}, new value: {}", this.batch, batch);
    this.batch = batch;
  }

  @Value("${timeout:200}")
  public void setTimeout(int timeout) {
    logger.info("updating timeout, old value: {}, new value: {}", this.timeout, timeout);
    this.timeout = timeout;
  }

  /**
   * ApolloJsonValue annotated on methods example, the default value is specified as empty list - []
   * <br /> jsonBeanProperty=[{"someString":"hello","someInt":120,"someDate":"2024-01-04T10:01:02"},{"someString":"world!","someInt":101,"someDate":"2024-01-05T11:02:03"}]
   */
  @ApolloJsonValue(value = "${jsonBeanProperty:[]}", datePattern = "yyyy-MM-dd'T'HH:mm:ss")
  public void setJsonBeans(List<JsonBean> jsonBeans) {
    logger.info("updating json beans, old value: {}, new value: {}", this.jsonBeans, jsonBeans);
    this.jsonBeans = jsonBeans;
  }

  @ApolloConfigChangeListener(appId = "100004459")
  public void testChange(ConfigChangeEvent configChangeEvent) {
    logger.info("Changes for appId {} namespace {}", configChangeEvent.getAppId(), configChangeEvent.getNamespace());
    for (String key : configChangeEvent.changedKeys()) {
      logger.info("Change - key: {}, oldValue: {}, newValue: {}, changeType: {}", key,
          configChangeEvent.getChange(key).getOldValue(), anotherAppConfig.getProperty(key, null),
          configChangeEvent.getChange(key).getChangeType());
    }
  }

  @Override
  public String toString() {
    return String.format("[AnnotatedBean] timeout: %d, batch: %d, jsonBeans: %s", timeout, batch,
        jsonBeans);
  }

  private static class JsonBean {

    private String someString;
    private int someInt;
    private Date someDate;

    @Override
    public String toString() {
      return "JsonBean{" + "someString='" + someString + '\'' + ", someInt=" + someInt
             + ", someDate=" + someDate + '}';
    }
  }
}
