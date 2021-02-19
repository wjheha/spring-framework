package com.example.importregistry;

import org.springframework.context.annotation.*;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 21:00
 */
// 对配置类中的组件进行统一设置
@Conditional({WindowsCondition.class}) // 满足当前条件，这个类中配置的所有bean注册才能生效
@Configuration
@Import({Color.class, Red.class, Bule.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
// @Import快速地导入组件，id默认是组件的全类名
public class MainConfig2 {

	@Lazy
	@Bean("person")
	public Person person() {
		System.out.println("给容器中添加咱们这个Person对象...");
		return new Person("美美侠", 25);
	}

	@Bean("bill")
	public Person person01() {
		return new Person("Bill Gates", 62);
	}

	@Conditional({LinuxCondition.class})
	@Bean("linus")
	public Person person02() {
		return new Person("linus", 48);
	}

}
