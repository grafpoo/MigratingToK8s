package liveproject.m2k8s.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataConfig.class)
@ComponentScan(basePackages={"liveproject.m2k8s"})
public class RootConfig {
}
