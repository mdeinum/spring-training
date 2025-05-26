package biz.deinum.moneytransfer.scanning;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marten Deinum
 */
@Configuration
@ComponentScan(basePackages = {
		"biz.deinum.moneytransfer.scanning",
		"biz.deinum.moneytransfer.repository" })
public class ApplicationContextConfiguration {
}
