/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	private PostProcessorRegistrationDelegate() {
	}


	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// 定义一个 set 保存所有的 BeanFactoryPostProcessors
		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		Set<String> processedBeans = new HashSet<>();

		// 1.判断beanFactory是否为BeanDefinitionRegistry，beanFactory为DefaultListableBeanFactory,
		// 而DefaultListableBeanFactory实现了BeanDefinitionRegistry接口，因此这边为true
		// 如果当前 BeanFactory 为 BeanDefinitionRegistry
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			// 用于存放普通的BeanFactoryPostProcessor
			// BeanFactoryPostProcessor 集合
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			// 用于存放BeanDefinitionRegistryPostProcessor
			// BeanDefinitionRegistryPostProcessor 集合
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			// 2.首先处理入参中的beanFactoryPostProcessors
			// 遍历所有的beanFactoryPostProcessors, 将BeanDefinitionRegistryPostProcessor和普通BeanFactoryPostProcessor区分开
			// 迭代注册的 beanFactoryPostProcessors
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				// 如果是 BeanDefinitionRegistryPostProcessor，则调用 postProcessBeanDefinitionRegistry 进行注册，
				// 同时加入到 registryProcessors 集合中
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					// 2.1 如果是BeanDefinitionRegistryPostProcessor
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					// 2.1.1 直接执行BeanDefinitionRegistryPostProcessor接口的postProcessBeanDefinitionRegistry方法
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					// 2.1.2 添加到registryProcessors(用于最后执行postProcessBeanFactory方法)
					registryProcessors.add(registryProcessor);
				}
				else {
					// 2.2 否则，只是普通的BeanFactoryPostProcessor
					// 2.2.1 添加到regularPostProcessors(用于最后执行postProcessBeanFactory方法)
					// 否则当做普通的 BeanFactoryPostProcessor 处理
					// 添加到 regularPostProcessors 集合中即可，便于后面做后续处理
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
			// 用于保存当前处理的 BeanDefinitionRegistryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// 首先处理实现了 PriorityOrdered (有限排序接口)的 BeanDefinitionRegistryPostProcessor
			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			// 3.调用所有实现PriorityOrdered接口的BeanDefinitionRegistryPostProcessor实现类
			// 3.1 找出所有实现BeanDefinitionRegistryPostProcessor接口的Bean的beanName
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				// 3.3 校验是否实现了PriorityOrdered接口
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					// 3.4 获取ppName对应的bean实例, 添加到currentRegistryProcessors中,
					// beanFactory.getBean: 这边getBean方法会触发创建ppName对应的bean对象, 目前暂不深入解析
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					// 3.5 将要被执行的加入processedBeans，避免后续重复执行
					processedBeans.add(ppName);
				}
			}
			// 3.6 进行排序(根据是否实现PriorityOrdered、Ordered接口和order值来排序)
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			// 加入registryProcessors集合
			// 3.7 添加到registryProcessors(用于最后执行postProcessBeanFactory方法)
			registryProcessors.addAll(currentRegistryProcessors);
			// 调用所有实现了 PriorityOrdered 的 BeanDefinitionRegistryPostProcessors 的 postProcessBeanDefinitionRegistry()
			// 3.8 遍历currentRegistryProcessors, 执行postProcessBeanDefinitionRegistry方法
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			// 清空，以备下次使用
			// 3.9 执行完毕后, 清空currentRegistryProcessors
			currentRegistryProcessors.clear();

			// 其次，调用实现了 Ordered（普通排序接口）的 BeanDefinitionRegistryPostProcessors，逻辑和上面一样
			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			// 4.调用所有实现了Ordered接口的BeanDefinitionRegistryPostProcessor实现类（过程跟上面的步骤3基本一样）
			// 4.1 找出所有实现BeanDefinitionRegistryPostProcessor接口的类, 这边重复查找是因为执行完上面的BeanDefinitionRegistryPostProcessor,
			// 可能会新增了其他的BeanDefinitionRegistryPostProcessor, 因此需要重新查找
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				// 校验是否实现了Ordered接口，并且还未执行过
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
			// 4.2 遍历currentRegistryProcessors, 执行postProcessBeanDefinitionRegistry方法
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

			// 5.最后, 调用所有剩下的BeanDefinitionRegistryPostProcessors
			// 最后调用其他的 BeanDefinitionRegistryPostProcessors
			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				// 获取 BeanDefinitionRegistryPostProcessor
				// 5.1 找出所有实现BeanDefinitionRegistryPostProcessor接口的类
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					// 没有包含在 processedBeans 中的（因为包含了的都已经处理了）
					// 5.2 跳过已经执行过的
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						// 5.3 如果有BeanDefinitionRegistryPostProcessor被执行, 则有可能会产生新的BeanDefinitionRegistryPostProcessor,
						// 因此这边将reiterate赋值为true, 代表需要再循环查找一次
						reiterate = true;
					}
				}
				// 与上面处理逻辑一致
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				// 5.4 遍历currentRegistryProcessors, 执行postProcessBeanDefinitionRegistry方法
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			// 调用所有 BeanDefinitionRegistryPostProcessor (包括手动注册和通过配置文件注册)
			// 和 BeanFactoryPostProcessor(只有手动注册)的回调函数(postProcessBeanFactory())
			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			// 6.调用所有BeanDefinitionRegistryPostProcessor的postProcessBeanFactory方法(BeanDefinitionRegistryPostProcessor继承自BeanFactoryPostProcessor)
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			// 7.最后, 调用入参beanFactoryPostProcessors中的普通BeanFactoryPostProcessor的postProcessBeanFactory方法
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// 如果不是 BeanDefinitionRegistry 只需要调用其回调函数（postProcessBeanFactory()）即可
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// 到这里 , 入参beanFactoryPostProcessors和容器中的所有BeanDefinitionRegistryPostProcessor已经全部处理完毕,
		// 下面开始处理容器中的所有BeanFactoryPostProcessor

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		// 8.找出所有实现BeanFactoryPostProcessor接口的类
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// 这里同样需要区分 PriorityOrdered 、Ordered 和 no Ordered
		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		// 用于存放实现了PriorityOrdered接口的BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		// 用于存放实现了Ordered接口的BeanFactoryPostProcessor的beanName
		List<String> orderedPostProcessorNames = new ArrayList<>();
		// 用于存放普通BeanFactoryPostProcessor的beanName
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		// 8.1 遍历postProcessorNames, 将BeanFactoryPostProcessor按实现PriorityOrdered、实现Ordered接口、普通三种区分开
		for (String ppName : postProcessorNames) {
			// 已经处理过了的，跳过
			// 8.2 跳过已经执行过的
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				// 8.3 添加实现了PriorityOrdered接口的BeanFactoryPostProcessor
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				// 8.4 添加实现了Ordered接口的BeanFactoryPostProcessor的beanName
				orderedPostProcessorNames.add(ppName);
			}
			else {
				// 8.5 添加剩下的普通BeanFactoryPostProcessor的beanName
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, PriorityOrdered 接口
		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		// 9.调用所有实现PriorityOrdered接口的BeanFactoryPostProcessor
		// 9.1 对priorityOrderedPostProcessors排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		// 9.2 遍历priorityOrderedPostProcessors, 执行postProcessBeanFactory方法
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, Ordered 接口
		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		// 10.调用所有实现Ordered接口的BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String postProcessorName : orderedPostProcessorNames) {
			// 10.1 获取postProcessorName对应的bean实例, 添加到orderedPostProcessors, 准备执行
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 10.2 对orderedPostProcessors排序
		sortPostProcessors(orderedPostProcessors, beanFactory);
		// 10.3 遍历orderedPostProcessors, 执行postProcessBeanFactory方法
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, no ordered
		// Finally, invoke all other BeanFactoryPostProcessors.
		// 11.调用所有剩下的BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			// 11.1 获取postProcessorName对应的bean实例, 添加到nonOrderedPostProcessors, 准备执行
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 11.2 遍历nonOrderedPostProcessors, 执行postProcessBeanFactory方法
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		// 12.清除元数据缓存（mergedBeanDefinitions、allBeanNamesByType、singletonBeanNamesByType），
		// 因为后处理器可能已经修改了原始元数据，例如， 替换值中的占位符...
		beanFactory.clearMetadataCache();
	}

	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

		// 获取所有的 BeanPostProcessor 的 beanName
		// 这些 beanName 都已经全部加载到容器中去，但是没有实例化
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// 记录所有的beanProcessor数量
		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		// 注册 BeanPostProcessorChecker，它主要是用于在 BeanPostProcessor 实例化期间记录日志
		// 当 Spring 中高配置的后置处理器还没有注册就已经开始了 bean 的实例化过程，这个时候便会打印 BeanPostProcessorChecker 中的内容
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		// PriorityOrdered 保证顺序
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		// MergedBeanDefinitionPostProcessor
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
		// 使用 Ordered 保证顺序
		List<String> orderedPostProcessorNames = new ArrayList<>();
		// 没有顺序
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			// PriorityOrdered
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				// 调用 getBean 获取 bean 实例对象
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			// 有序 Ordered
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			// 无序
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// 第一步，注册所有实现了 PriorityOrdered 的 BeanPostProcessor
		// First, register the BeanPostProcessors that implement PriorityOrdered.
		// 先排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		// 后注册
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// 第二步，注册所有实现了 Ordered 的 BeanPostProcessor
		// Next, register the BeanPostProcessors that implement Ordered.
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		// 先排序
		sortPostProcessors(orderedPostProcessors, beanFactory);
		// 后注册
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// 第三步注册所有无序的 BeanPostProcessor
		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		// 注册，无需排序
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// 最后，注册所有的 MergedBeanDefinitionPostProcessor 类型的 BeanPostProcessor
		// Finally, re-register all internal BeanPostProcessors.
		sortPostProcessors(internalPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// 加入ApplicationListenerDetector（探测器）
		// 重新注册 BeanPostProcessor 以检测内部 bean，因为 ApplicationListeners 将其移动到处理器链的末尾
		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		// 获得 Comparator 对象
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {  // 依赖的 Comparator 对象
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {  // 默认 Comparator 对象
			comparatorToUse = OrderComparator.INSTANCE;
		}
		// 排序
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		// 遍历 BeanPostProcessor 数组，注册
		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
