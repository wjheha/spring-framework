package com.example.importregistry;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 21:04
 */
public class LinuxCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return true;
	}
}
