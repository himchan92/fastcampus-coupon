package com.example.couponconsumer;

import com.example.couponcore.CouponCoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CouponCoreConfiguration.class) //Core 모듈 import 활용
@SpringBootApplication
public class CouponConsumerApplication {

  public static void main(String[] args) {
    //core, consumer yml 활용

    System.setProperty("spring.config.name", "application-core,application-consumer");
    SpringApplication.run(CouponConsumerApplication.class, args);
  }

}