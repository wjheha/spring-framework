package com.example;

import com.example.beans.Entitlement;
import com.example.config.AppConfig;
import com.example.importregistry.MainConfig2;
import com.example.importregistry.Person;
import com.example.importregistry.RainBow;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 15:08
 */
public class JavaConfigTest {
	public static void main(String[] arg) {
//		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//		ctx.register(AppConfig.class);
//		ctx.refresh();
//
//		Entitlement ent = (Entitlement) ctx.getBean("entitlement");
//		System.out.println(ent.getName());
//		System.out.println(ent.getTime());
//
//		Entitlement ent2 = (Entitlement) ctx.getBean("entitlement2");
//		System.out.println(ent2.getName());
//		System.out.println(ent2.getTime());

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(MainConfig2.class);
		ctx.refresh();

		Person person = (Person) ctx.getBean("bill");
		System.out.println(person.getName());
		RainBow rainBow = (RainBow) ctx.getBean("rainBow");
		System.out.println(rainBow.getName());

		ctx.close();
	}
}
