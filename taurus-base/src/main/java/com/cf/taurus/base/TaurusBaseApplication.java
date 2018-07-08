package com.cf.taurus.base;

import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;


@Slf4j
@ServletComponentScan
@SpringBootApplication
public class TaurusBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaurusBaseApplication.class, args);
		log.info("Server start success");
	}

}
