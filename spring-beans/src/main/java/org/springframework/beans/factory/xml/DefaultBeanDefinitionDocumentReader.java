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

package org.springframework.beans.factory.xml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * Default implementation of the {@link BeanDefinitionDocumentReader} interface that
 * reads bean definitions according to the "spring-beans" DTD and XSD format
 * (Spring's default XML bean definition format).
 *
 * <p>The structure, elements, and attribute names of the required XML document
 * are hard-coded in this class. (Of course a transform could be run if necessary
 * to produce this format). {@code <beans>} does not need to be the root
 * element of the XML document: this class will parse all bean definition elements
 * in the XML file, regardless of the actual root element.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Erik Wiersma
 * @since 18.12.2003
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;

	public static final String NESTED_BEANS_ELEMENT = "beans";

	public static final String ALIAS_ELEMENT = "alias";

	public static final String NAME_ATTRIBUTE = "name";

	public static final String ALIAS_ATTRIBUTE = "alias";

	public static final String IMPORT_ELEMENT = "import";

	public static final String RESOURCE_ATTRIBUTE = "resource";

	public static final String PROFILE_ATTRIBUTE = "profile";


	protected final Log logger = LogFactory.getLog(getClass());

	@Nullable
	private XmlReaderContext readerContext;

	@Nullable
	private BeanDefinitionParserDelegate delegate;


	/**
	 * This implementation parses bean definitions according to the "spring-beans" XSD
	 * (or DTD, historically).
	 * <p>Opens a DOM Document; then initializes the default settings
	 * specified at the {@code <beans/>} level; then parses the contained bean definitions.
	 */
	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
		this.readerContext = readerContext;
		// 从 xml 根节点开始解析文件
		doRegisterBeanDefinitions(doc.getDocumentElement());
	}

	/**
	 * Return the descriptor for the XML resource that this parser works on.
	 */
	protected final XmlReaderContext getReaderContext() {
		Assert.state(this.readerContext != null, "No XmlReaderContext available");
		return this.readerContext;
	}

	/**
	 * Invoke the {@link org.springframework.beans.factory.parsing.SourceExtractor}
	 * to pull the source metadata from the supplied {@link Element}.
	 */
	@Nullable
	protected Object extractSource(Element ele) {
		return getReaderContext().extractSource(ele);
	}


	/**
	 * Register each bean definition within the given root {@code <beans/>} element.
	 */
	@SuppressWarnings("deprecation")  // for Environment.acceptsProfiles(String...)
	protected void doRegisterBeanDefinitions(Element root) {
		// Any nested <beans> elements will cause recursion in this method. In
		// order to propagate and preserve <beans> default-* attributes correctly,
		// keep track of the current (parent) delegate, which may be null. Create
		// the new (child) delegate with a reference to the parent for fallback purposes,
		// then ultimately reset this.delegate back to its original (parent) reference.
		// this behavior emulates a stack of delegates without actually necessitating one.
		// 记录老的 BeanDefinitionParserDelegate 对象
		// 我们看名字就知道，BeanDefinitionParserDelegate 必定是一个重要的类，它负责解析 Bean 定义，
		// 这里为什么要定义一个 parent? 看到后面就知道了，是递归问题，
		// 因为 <beans /> 内部是可以定义 <beans /> 的，所以这个方法的 root 其实不一定就是 xml 的根节点，
		// 也可以是嵌套在里面的 <beans /> 节点，从源码分析的角度，我们当做根节点就好了

		BeanDefinitionParserDelegate parent = this.delegate;
		// <1> 创建 BeanDefinitionParserDelegate 对象，并进行设置到 delegate
		this.delegate = createDelegate(getReaderContext(), root, parent);
		// <2> 检查 <beans /> 根标签的命名空间是否为空，或者是 http://www.springframework.org/schema/beans
		if (this.delegate.isDefaultNamespace(root)) {
			// 这块说的是根节点 <beans ... profile="dev" /> 中的 profile 是否是当前环境需要的，
			// 如果当前环境配置的 profile 不包含此 profile，那就直接 return 了，不对此 <beans /> 解析
			// 不熟悉 profile 为何物，不熟悉怎么配置 profile 读者的请移步附录区
			// <2.1> 处理 profile 属性。可参见《Spring3自定义环境配置 <beans profile="">》http://nassir.iteye.com/blog/1535799
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (StringUtils.hasText(profileSpec)) {
				// <2.2> 使用分隔符切分，可能有多个 profile 。
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				// <2.3> 如果所有 profile 都无效，则不进行注册
				// We cannot use Profiles.of(...) since profile expressions are not supported
				// in XML config. See SPR-12458 for details.
				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
								"] not matching: " + getReaderContext().getResource());
					}
					return;
				}
			}
		}

		// <3> 钩子，解析前处理,目前 Spring 中是空实现
		preProcessXml(root);
		// <4> 解析
		parseBeanDefinitions(root, this.delegate);
		// <5> 钩子，解析后处理,目前 Spring 中是空实现
		postProcessXml(root);

		// 设置 delegate 回老的 BeanDefinitionParserDelegate 对象
		this.delegate = parent;
	}

	protected BeanDefinitionParserDelegate createDelegate(
			XmlReaderContext readerContext, Element root, @Nullable BeanDefinitionParserDelegate parentDelegate) {

		// 创建 BeanDefinitionParserDelegate 对象
		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
		// 初始化默认
		delegate.initDefaults(root, parentDelegate);
		return delegate;
	}

	// default namespace 涉及到的就四个标签 <import />、<alias />、<bean /> 和 <beans />，
	// 其他的属于 custom 的
	/**
	 * Parse the elements at the root level in the document:
	 * "import", "alias", "bean".
	 * @param root the DOM root element of the document
	 */
	protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
		// <1> 如果根节点使用默认命名空间，执行默认解析
		if (delegate.isDefaultNamespace(root)) {
			// 遍历子节点
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof Element) {
					Element ele = (Element) node;
					// <1.1> 如果该节点使用默认命名空间，执行默认解析
					if (delegate.isDefaultNamespace(ele)) {
						// 解析 default namespace 下面的几个元素
						parseDefaultElement(ele, delegate);
					}
					// <1.2> 如果该节点非默认命名空间，执行自定义解析
					else {
						//  解析其他 namespace 的元素
						delegate.parseCustomElement(ele);
					}
				}
			}
		}
		// <2> 如果根节点非默认命名空间，执行自定义解析
		else {
			delegate.parseCustomElement(root);
		}
	}

	/**
	 * parseDefaultElement(ele, delegate) 代表解析的节点是 <import />、<alias />、<bean />、<beans /> 这几个
	 * @param ele
	 * @param delegate
	 */
	private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {  //import
			// 处理 <import /> 标签
			importBeanDefinitionResource(ele);
		}
		else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {  //alias
			// 处理 <alias /> 标签定义
			// <alias name="fromName" alias="toName"/>
			processAliasRegistration(ele);
		}
		else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {  //bean
			// 处理 <bean /> 标签定义，这也算是我们的重点吧
			processBeanDefinition(ele, delegate);
		}
		else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {  //beans
			// recurse
			// 如果碰到的是嵌套的 <beans /> 标签，需要递归
			doRegisterBeanDefinitions(ele);
		}
	}

	/**
	 * Parse an "import" element and load the bean definitions
	 * from the given resource into the bean factory.
	 */
	protected void importBeanDefinitionResource(Element ele) {
		// <1> 获取 resource 的属性值
		String location = ele.getAttribute(RESOURCE_ATTRIBUTE);
		// 为空，直接退出
		if (!StringUtils.hasText(location)) {
			getReaderContext().error("Resource location must not be empty", ele);
			return;
		}

		// <2> 解析系统属性，格式如 ："${user.dir}"
		// Resolve system properties: e.g. "${user.dir}"
		location = getReaderContext().getEnvironment().resolveRequiredPlaceholders(location);

		// 实际 Resource 集合，即 import 的地址，有哪些 Resource 资源
		Set<Resource> actualResources = new LinkedHashSet<>(4);

		// <3> 判断 location 是相对路径还是绝对路径
		// Discover whether the location is an absolute or relative URI
		boolean absoluteLocation = false;
		try {
			absoluteLocation = ResourcePatternUtils.isUrl(location) || ResourceUtils.toURI(location).isAbsolute();
		}
		catch (URISyntaxException ex) {
			// cannot convert to an URI, considering the location relative
			// unless it is the well-known Spring prefix "classpath*:"
		}

		// <4> 绝对路径
		// Absolute or relative?
		if (absoluteLocation) {
			try {
				// 添加配置文件地址的 Resource 到 actualResources 中，并加载相应的 BeanDefinition 们
				int importCount = getReaderContext().getReader().loadBeanDefinitions(location, actualResources);
				if (logger.isTraceEnabled()) {
					logger.trace("Imported " + importCount + " bean definitions from URL location [" + location + "]");
				}
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error(
						"Failed to import bean definitions from URL location [" + location + "]", ele, ex);
			}
		}
		// <5> 相对路径
		else {
			// No URL -> considering resource location as relative to the current file.
			try {
				int importCount;
				// 创建相对地址的 Resource
				Resource relativeResource = getReaderContext().getResource().createRelative(location);
				// 存在
				if (relativeResource.exists()) {
					// 加载 relativeResource 中的 BeanDefinition 们
					importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
					// 添加到 actualResources 中
					actualResources.add(relativeResource);
				}
				// 不存在
				else {
					// 获得根路径地址
					String baseLocation = getReaderContext().getResource().getURL().toString();
					// 添加配置文件地址的 Resource 到 actualResources 中，并加载相应的 BeanDefinition 们
					importCount = getReaderContext().getReader().loadBeanDefinitions(
							StringUtils.applyRelativePath(baseLocation, location)/* 计算绝对路径 */, actualResources);
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Imported " + importCount + " bean definitions from relative location [" + location + "]");
				}
			}
			catch (IOException ex) {
				getReaderContext().error("Failed to resolve current resource location", ele, ex);
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error(
						"Failed to import bean definitions from relative location [" + location + "]", ele, ex);
			}
		}
		// <6> 解析成功后，进行监听器激活处理
		Resource[] actResArray = actualResources.toArray(new Resource[0]);
		getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
	}

	/**
	 * Process the given alias element, registering the alias with the registry.
	 */
	protected void processAliasRegistration(Element ele) {
		String name = ele.getAttribute(NAME_ATTRIBUTE);
		String alias = ele.getAttribute(ALIAS_ATTRIBUTE);
		boolean valid = true;
		if (!StringUtils.hasText(name)) {
			getReaderContext().error("Name must not be empty", ele);
			valid = false;
		}
		if (!StringUtils.hasText(alias)) {
			getReaderContext().error("Alias must not be empty", ele);
			valid = false;
		}
		if (valid) {
			try {
				getReaderContext().getRegistry().registerAlias(name, alias);
			}
			catch (Exception ex) {
				getReaderContext().error("Failed to register alias '" + alias +
						"' for bean with name '" + name + "'", ele, ex);
			}
			getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
		}
	}

	/**
	 * Process the given bean element, parsing the bean definition
	 * and registering it with the registry.
	 */
	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		// 将 <bean /> 节点中的信息提取出来，然后封装到一个 BeanDefinitionHolder 中。
		// <1> 进行 bean 元素解析，如果解析成功，则返回 BeanDefinitionHolder 对象。
		// 而 BeanDefinitionHolder 为 name 和 alias 的 BeanDefinition 对象；如果解析失败，则返回 null 。
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			// 如果有自定义属性的话，进行相应的解析
			// <2> 进行自定义标签处理
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// <3> 进行 BeanDefinition 的注册
				// Register the final decorated instance.
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// 注册完成后，发送事件
			// <4> 发出响应事件，通知相关的监听器，已完成该 Bean 标签的解析。
			// Send registration event.
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}


	/**
	 * Allow the XML to be extensible by processing any custom element types first,
	 * before we start to process the bean definitions. This method is a natural
	 * extension point for any other custom pre-processing of the XML.
	 * <p>The default implementation is empty. Subclasses can override this method to
	 * convert custom elements into standard Spring bean definitions, for example.
	 * Implementors have access to the parser's bean definition reader and the
	 * underlying XML resource, through the corresponding accessors.
	 * @see #getReaderContext()
	 */
	protected void preProcessXml(Element root) {
	}

	/**
	 * Allow the XML to be extensible by processing any custom element types last,
	 * after we finished processing the bean definitions. This method is a natural
	 * extension point for any other custom post-processing of the XML.
	 * <p>The default implementation is empty. Subclasses can override this method to
	 * convert custom elements into standard Spring bean definitions, for example.
	 * Implementors have access to the parser's bean definition reader and the
	 * underlying XML resource, through the corresponding accessors.
	 * @see #getReaderContext()
	 */
	protected void postProcessXml(Element root) {
	}

}
