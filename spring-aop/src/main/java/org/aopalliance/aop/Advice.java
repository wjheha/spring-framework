/*
 * Copyright 2002-2016 the original author or authors.
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

package org.aopalliance.aop;

/**
 * Tag interface for Advice. Implementations can be any type
 * of advice, such as Interceptors.
 *
 * @author Rod Johnson
 * @version $Id: Advice.java,v 1.1 2004/03/19 17:02:16 johnsonr Exp $
 */
// 通知 Advice 即我们定义的横切逻辑，比如我们可以定义一个用于监控方法性能的通知，也可以定义一个安全检查的通知等。
// 如果说切点解决了通知在哪里调用的问题，那么现在还需要考虑了一个问题，即通知在何时被调用？
// 是在目标方法前被调用，还是在目标方法返回后被调用，还在两者兼备呢？
// Spring 帮我们解答了这个问题，Spring 中定义了以下几种通知类型：

// 前置通知（Before advice）- 在目标方便调用前执行通知
// 后置通知（After advice）- 在目标方法完成后执行通知
// 返回通知（After returning advice）- 在目标方法执行成功后，调用通知
// 异常通知（After throwing advice）- 在目标方法抛出异常后，执行通知
// 环绕通知（Around advice）- 在目标方法调用前后均可执行自定义逻辑
public interface Advice {

}
