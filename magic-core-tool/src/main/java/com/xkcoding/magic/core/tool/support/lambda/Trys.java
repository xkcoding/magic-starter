/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xkcoding.magic.core.tool.support.lambda;

import com.xkcoding.magic.core.tool.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * Lambda 受检异常处理，https://segmentfault.com/a/1190000007832130
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/26 15:47
 */
@Slf4j
public class Trys {

	public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Exception e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper, R defaultR) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				return mapper.apply(t);
			} catch (Exception e) {
				log.error(ExceptionUtil.getStackTraceAsString(e));
				return defaultR;
			}
		};
	}

	public static <T> Consumer<T> of(UncheckedConsumer<T> mapper) {
		return of(mapper, false);
	}

	public static <T> Consumer<T> of(UncheckedConsumer<T> mapper, boolean ignoreError) {
		Objects.requireNonNull(mapper);
		return t -> {
			try {
				mapper.accept(t);
			} catch (Exception e) {
				if (!ignoreError) {
					throw ExceptionUtil.unchecked(e);
				} else {
					log.error(ExceptionUtil.getStackTraceAsString(e));
				}
			}
		};
	}

	public static <T> Supplier<T> of(UncheckedSupplier<T> mapper) {
		Objects.requireNonNull(mapper);
		return () -> {
			try {
				return mapper.get();
			} catch (Exception e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	public static <T> Supplier<T> of(UncheckedSupplier<T> mapper, T defaultR) {
		Objects.requireNonNull(mapper);
		return () -> {
			try {
				return mapper.get();
			} catch (Exception e) {
				log.error(ExceptionUtil.getStackTraceAsString(e));
				return defaultR;
			}
		};
	}

	@FunctionalInterface
	public interface UncheckedFunction<T, R> {
		/**
		 * apply
		 *
		 * @param t 入参
		 * @return 返回值
		 * @throws Exception 异常
		 */
		@Nullable
		R apply(@Nullable T t) throws Exception;
	}

	@FunctionalInterface
	public interface UncheckedConsumer<T> {
		/**
		 * accept
		 *
		 * @param t 入参
		 * @throws Exception 异常
		 */
		void accept(@Nullable T t) throws Exception;
	}

	@FunctionalInterface
	public interface UncheckedSupplier<T> {
		/**
		 * get
		 *
		 * @return 返回值
		 * @throws Exception 异常
		 */
		@Nullable
		T get() throws Exception;
	}
}
