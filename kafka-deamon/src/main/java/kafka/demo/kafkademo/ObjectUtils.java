package kafka.demo.kafkademo;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ObjectUtils {

	private ObjectUtils() {
	}

	public static final <R, T> R getOrDefault(final T object, final Function<T, R> function) {
		return getOrDefault(object, function, () -> null);
	}

	public static final <T> T getOrElse(final T object, final T defaultObject) {
		if (Objects.nonNull(object)) {
			return object;
		}
		return defaultObject;
	}

	public static final <R, T> R getOrDefault(final T object, final Function<T, R> function,
			final Supplier<R> defaultValueSupplier) {
		// 1. Tries to apply object on provided Functions
		// 2. If returns null, then passes default supplier
		if (object != null && function != null && defaultValueSupplier != null) {
			final R result = function.apply(object);
			return result != null ? result : defaultValueSupplier.get();
		}
		return defaultValueSupplier != null ? defaultValueSupplier.get() : null;
	}

	public static final <R, T> R getOrDefault(final T object, final Function<T, R> function,
			final Supplier<R> defaultValueSupplier, final Predicate<? super T> predicate) {
		// 1. Tries to apply object on provided Functions
		// 2. If returns null, then passes default supplier
		if (object != null && function != null && defaultValueSupplier != null && predicate != null
				&& predicate.test(object)) {
			final R result = function.apply(object);
			return result != null ? result : defaultValueSupplier.get();
		}
		return defaultValueSupplier != null ? defaultValueSupplier.get() : null;
	}

	public static final <T> void update(final T object, final Consumer<T> consumer) {
		update(object, consumer, Objects::nonNull);
	}

	public static final <T> void update(final T object, final Consumer<T> consumer,
			final Predicate<? super T> predicate) {
		// 1. Validates the object against given predicate
		// 2. Passes the object to the consumer provided.
		if (consumer != null && predicate != null && predicate.test(object)) {
			consumer.accept(object);
		}
	}

	public static Predicate<String> isModified(final String other) {
		return cur -> cur != null && (Objects.hashCode(cur) != Objects.hashCode(other));
	}

	public static Predicate<Number> isModified(final Number other) {
		return cur -> cur != null && ((Objects.isNull(cur) ^ Objects.isNull(other))
				|| (Objects.hashCode(cur) != Objects.hashCode(other)));
	}

	public static <T> T getOptionalValue(final ResultSet rs, final String columnName, final Class<T> clazz)
			throws SQLException {
		final T value = rs.getObject(columnName, clazz);
		return rs.wasNull() ? null : value;
	}

	public static final <D, S> D copyObject(final D destination, final S source)
			throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(destination, source);
		return destination;
	}

	public static final <T> T parseExpression(final String expression, final Class<T> desiredResultType) {
		ExpressionParser parser = new SpelExpressionParser();
		return parser.parseExpression(expression).getValue(desiredResultType);
	}

	public static final <T> T parseExpression(final String expression, final Object evaluationContext,
			final Class<T> desiredResultType) {
		StandardEvaluationContext context = new StandardEvaluationContext(evaluationContext);
		ExpressionParser parser = new SpelExpressionParser();
		return parser.parseExpression(expression).getValue(context, desiredResultType);
	}

	public static final String parseTemplate(final String expression, final Object evaluationContext) {
		StandardEvaluationContext context = new StandardEvaluationContext(evaluationContext);
		ExpressionParser parser = new SpelExpressionParser();
		return parser.parseExpression(expression, ParserContext.TEMPLATE_EXPRESSION).getValue(context).toString();
	}

	public static <E extends Enum<E>> E lookup(Class<E> e, String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		try {
			E result = Enum.valueOf(e, id);
			return result;
		}
		catch (IllegalArgumentException ex) {
			log.error("Invalid value for enum {} : {}", ex.getMessage(), id);
			return null;
		}
	}

}
