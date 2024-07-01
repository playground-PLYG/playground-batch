package com.playground.batch.utils;

import java.util.Arrays;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtil {
  private final Environment environment;

  public ProfileUtil(Environment environment) {
    this.environment = environment;
  }

  public String getActiveProfile() {
    return environment.getProperty("spring.profiles.active");
  }

  public boolean isLocal() {
    return isActiveProfile("local");
  }

  public boolean isDev() {
    return isActiveProfile("dev");
  }

  public boolean isProd() {
    return isActiveProfile("prod");
  }

  private boolean isActiveProfile(String profileName) {
    String activeProfile = getActiveProfile();

    if (activeProfile == null || activeProfile.isEmpty()) {
      return false;
    }

    return Arrays.stream(activeProfile.split(",")).anyMatch(profile -> profile.trim().equalsIgnoreCase(profileName));
  }
}
