package com.bastosbf.mugloar;

import com.bastosbf.mugloar.daemon.DragonsOfMugloarDaemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DragonsOfMugloarApplication implements ApplicationRunner {

  @Autowired
  private DragonsOfMugloarDaemon dragonsOfMugloarDaemon;

  public static void main(String[] args) {
    SpringApplication.run(DragonsOfMugloarApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    dragonsOfMugloarDaemon.init();
    dragonsOfMugloarDaemon.start();
  }

}