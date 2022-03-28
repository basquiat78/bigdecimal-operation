# bigdecimal-operation
java에서 BigDecimal을 다뤄보자!

## 개요

자바에서 좀 더 정확한 연산을 위해서 BigInteger 또는 BigDecimal을 사용하게 된다.      

예를들면 소수점 버그같은 것인데 이것은 비단 자바뿐만 아니라 자바스크립트에서도 자주 볼 수 있는 버그이디ㅏ.      

```
//double a = 0.6;
//double b = 0.3;
float a = 0.6f;
float b = 0.3f;
log.info("{}", a + b);
```

위와 같은 연산을 하게 되면 아마도 여러분은 여러분이 상상하는 그 이상의 것을 보게 된다.      

그렇기 위해서는 일반적으로 BigDecimal을 많이 사용하게 되고 이더리움같은 블록체인의 경우 관련 개발을 하다 보면 BigInteger를 사용하는 것을 많이 보게 된다.      

하지만 BigDecimal을 사용하게 되면 우리가 사칙연산을 할때 기존과는 다르게 해당 API를 사용하게 된다.      

간략하게 소개해 본다면

```
BigDecimal prevValue = BigDicimal.valueOf(10000);
BigDecimal afterValue = BigDicimal.valueOf(100);

log.info("더하기: {} ", prevValue.add(afterValue));
log.info("빼기: {} ", prevValue.subtract(afterValue));
log.info("곱하기: {} ", prevValue.multiply(afterValue));
log.info("나누기: {} ", prevValue.divide(afterValue));
log.info("나머지: {}", prevValue.remainder(afterValue));
log.info("나누기 몫: {}", prevValue.divideAndRemainder(afterValue)[0]);
log.info("나누기 나머지: {}", prevValue.divideAndRemainder(afterValue)[1]);
```

이런 방식으로 계산하게 되어 있다.     

그 외에도 BigDecimal은 precision을 제공해서 소수점을 다루기 위한 소수점 표시 스케일과 RoundingMode를 제공한다.

하지만 비교의 경우에는 좀 짜증난다. 예를 들면

```
int a = 1;
int b = 2;

log.info("a가 b보다 크니? true or false? : {}", a > b);
 
```
같은 방식이 아니라 역시 이것도 compareTo api를 이용해야 한다.      

```
BigDecimal prev = new BigDecimal("10");
BigDecimal next = new BigDecimal("11");
log.info("{}", prev.compareTo(next) > 0);
```
즉 compareTo를 통해서 나온 값이 0을 기준으로 비교한다.     

위 코드에서 prev가 next보다 큰 수라면 0보다 클것이고 값다면 0 만일 작다면 0보다 작은 값이 나온다.       

결국 다음과 같은 방식을 통해서 비교를 해야 한다.

```
BigDecimal prev = new BigDecimal("10");
BigDecimal next = new BigDecimal("11");
log.info("{}", prev.compareTo(next) > 0);
log.info("{}", prev.compareTo(next) = 0);
log.info("{}", prev.compareTo(next) >= 0);
log.info("{}", prev.compareTo(next) <= 0);
```

하지만 만일 비지니스 로직에서 저런 식으로 사용한다면?     

아이쿠! 코드가 지저분 해질것이라는 것은 자명한 일이다. 그리고 가독성이 떨어진다.      

### 가독성은 중요하다.     

당연히 우리는 개발자이다. 그렇기 위해서는 이것을 처리하기 위한 유틸을 한번 만들어 볼 생각이다.     

전략은 다음과 같다.

1. jpa의 queryDSL을 보면 이러한 비교를 다음과 같은 방식으로 처리한다.
   - gte : greater than or equal : 크거나 같거나 : >=
   - gt  : greater than : 크거나 : >
   - lte : less than or equ : 작거나 같거나 : <=
   - lt  : less than : 작거나 : < 
   - eq  : equal : 같음 : = 
2. 비교할 대상의 prev는 저 위의 만들어진 방식의 api를 사용할 수 있게 Comparator를 통해 감싸서 반환한다.
3. Comparator를 통해 감싸진 정보에 비교대상이 될 값을 받아서 비교를 한다.

일단 BigDecimalComparator를 만들어 보자. 일명 BigDecimal 비교기이다.

BigDecimalComparator.java
```
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
```
참고로 Lombok를 사용하고 있다.     
Lombok를 사용하지 않는다면 생성자를 만들면 된다.    

자 이제는 이것을 사용하기 위한 BigDecimalUtil를 만들어 사용해 볼까 한다.

BigDecimalUtil.java
```
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
```

사용법은 다음과 같이 사용하면 된다.

```
import static io.basquiat.common.utils.BigDecimalUtil.*;

BigDecimal prev = convertBigDecimal("100");
BigDecimal next = convertBigDecimal("100");
Assert.isTrue(compare(prev).gte(next), "prev가 next보다 크거나 같지 않습니다.");
```

위 코드라면 prev와 next는 값은 값이기 때문에 테스트가 통과할 것이다.     

저 위 값들을 바꿔가면서 api를 사용해 보자.       

일반적으로 convertBigDecimal이라는 메소드명이 길다면 toBigDecimal처럼 바꿀 수 있고 compare의 경우에도 is같은 단어로 대체할 수 있다.      

나의 경우에는 가독성을 위해서 좀 길게 쓰는 편이긴 한데 불편하다면 언제든지 입맛에 맞춰 바꾸는 것은 개발자의 몫일 것이다.
