/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.servlet.support;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * {@link org.springframework.web.WebApplicationInitializer WebApplicationInitializer}
 * to register a {@code DispatcherServlet} and use Java-based Spring configuration.
 *
 * <p>Implementations are required to implement:
 * <ul>
 * <li>{@link #getRootConfigClasses()} -- for "root" application context (non-web
 * infrastructure) configuration.
 * <li>{@link #getServletConfigClasses()} -- for {@code DispatcherServlet}
 * application context (Spring MVC infrastructure) configuration.
 * </ul>
 *
 * <p>If an application context hierarchy is not required, applications may
 * return all configuration via {@link #getRootConfigClasses()} and return
 * {@code null} from {@link #getServletConfigClasses()}.
 *
 * @author Arjen Poutsma
 * @author Chris Beams
 * @since 3.2
 */
public abstract class AbstractAnnotationConfigDispatcherServletInitializer
		extends AbstractDispatcherServletInitializer {

	/**
	 * {@inheritDoc}
	 * <p>This implementation creates an {@link AnnotationConfigWebApplicationContext},
	 * providing it the annotated classes returned by {@link #getRootConfigClasses()}.
	 * Returns {@code null} if {@link #getRootConfigClasses()} returns {@code null}.
	 */
	@Override
	@Nullable
	protected WebApplicationContext createRootApplicationContext() {
		Class<?>[] configClasses = getRootConfigClasses();
		if (!ObjectUtils.isEmpty(configClasses)) {
			AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
			context.register(configClasses);
			return context;
		}
		else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation creates an {@link AnnotationConfigWebApplicationContext},
	 * providing it the annotated classes returned by {@link #getServletConfigClasses()}.
	 */
	@Override
	protected WebApplicationContext createServletApplicationContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		Class<?>[] configClasses = getServletConfigClasses();
		if (!ObjectUtils.isEmpty(configClasses)) {
			context.register(configClasses);
		}
		return context;
	}

	/**
	 * 为{@linkPlan#createRootApplicationContext()根应用程序上下文}
	 * 指定{@code@Configuration}和/或{@code@Component}类
	 * @ 返回根应用上下文的配置，如果不需要创建和注册根上下文，则返回{@code null}
	 */
	/**
	 * Specify {@code @Configuration} and/or {@code @Component} classes for the
	 * {@linkplain #createRootApplicationContext() root application context}.
	 * @return the configuration for the root application context, or {@code null}
	 * if creation and registration of a root context is not desired
	 */
	@Nullable
	protected abstract Class<?>[] getRootConfigClasses();

	/**
	 *  为{@linkPlan#createServletApplicationContext()Servlet应用程序上下文}指定
	 * {@code@Configuration}和/或{@code@Component}类
	 * @返回Servlet应用程序上下文的配置，或者如果所有配置都通过根配置类指定，则为{@code null}
	 */
	/**
	 * Specify {@code @Configuration} and/or {@code @Component} classes for the
	 * {@linkplain #createServletApplicationContext() Servlet application context}.
	 * @return the configuration for the Servlet application context, or
	 * {@code null} if all configuration is specified through root config classes.
	 */
	@Nullable
	protected abstract Class<?>[] getServletConfigClasses();

}
