package io.basquiat.common.utils;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * BigDecimal을 편하게 쓰기 위한 Comparator
 * created by basquiat
 */
@RequiredArgsConstructor
public final class BigDecimalComparator {

  	private final BigDecimal bigDecimal;

	/**
	 * target값과 같은지 비교한다.
	 * @param target
	 * @return boolean
	 */
	public boolean eq(BigDecimal target) {
		return bigDecimal.compareTo(target) == 0;
	}

	/**
	 * target값보다 큰지 비교한다.
	 * @param target
	 * @return boolean
	 */
	public boolean gt(BigDecimal target) {
		return bigDecimal.compareTo(target) > 0;
	}

	/**
	 * target값보다 크거나 같은지 비교한다.
	 * @param target
	 * @return boolean
	 */
	public boolean gte(BigDecimal target) {
		return bigDecimal.compareTo(target) >= 0;
	}

	/**
	 * target값보다 작은지 비교한다.
	 * @param target
	 * @return boolean
	 */
	public boolean lt(BigDecimal target) {
		return bigDecimal.compareTo(target) < 0;
	}

	/**
	 * target값보다 작거나 같은지 비교한다.
	 * @param target
	 * @return boolean
	 */
	public boolean lte(BigDecimal target) {
		return bigDecimal.compareTo(target) <= 0;
	}

}
