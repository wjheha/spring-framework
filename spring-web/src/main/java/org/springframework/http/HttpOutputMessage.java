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

package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents an HTTP output message, consisting of {@linkplain #getHeaders() headers}
 * and a writable {@linkplain #getBody() body}.
 *
 * <p>Typically implemented by an HTTP request handle on the client side,
 * or an HTTP response handle on the server side.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
// 这个类是 Spring MVC 内部对一次 Http 响应报文的抽象，在 HttpMessageConverter 的 #write(...) 方法中，有一个 HttpOutputMessage 的形参，
// 它正是 Spring MVC 的消息转换器所作用的受体“响应消息”的内部抽象，消息转换器将“响应消息”按照一定的规则写到响应报文中。
public interface HttpOutputMessage extends HttpMessage {

	/**
	 * Return the body of the message as an output stream.
	 * @return the output stream body (never {@code null})
	 * @throws IOException in case of I/O errors
	 */
	OutputStream getBody() throws IOException;

}
