package io.basquiat.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigDecimal Util
 * created by basquiat
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigDecimalUtil {

	/**
	 * BigDecimal을 비교기로 감싼다.
	 * @param decimal
	 * @return BigDecimalComparator
	 */
	public static BigDecimalComparator compare(BigDecimal decimal) {
		return new BigDecimalComparator(decimal);
	}

	/**
	 * 숫자형 오브젝트를 받아서 BigDecimal로 변환한다.
	 * @param value
	 * @return BigDecimal
	 */
	public static BigDecimal convertBigDecimal(Object value) {
		if(value == null) {
			return BigDecimal.ZERO;
		}
		if(value instanceof String) {
			return new BigDecimal(value.toString());
		}
		if(value instanceof BigInteger) {
			return BigDecimal.valueOf(((BigInteger)value).longValue());
		}
		if(value instanceof Long) {
			return BigDecimal.valueOf((Long)value);
		}
		if(value instanceof Integer) {
			return BigDecimal.valueOf(((Integer)value).longValue());
		}
		return BigDecimal.valueOf(Double.parseDouble(value.toString()));
	}

}
