package com.example.config;

import com.example.beans.Entitlement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 15:09
 */
@Configuration
public class AppConfig {

	@Bean(name="entitlement")
	public Entitlement entitlement() {
		Entitlement ent= new Entitlement();
		ent.setName("Entitlement");
		ent.setTime(1);
		return ent;
	}

	@Bean(name="entitlement2")
	public Entitlement entitlement2() {
		Entitlement ent= new Entitlement();
		ent.setName("Entitlement2");
		ent.setTime(2);
		return ent;
	}
}
