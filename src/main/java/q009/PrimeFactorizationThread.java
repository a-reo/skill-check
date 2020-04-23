package q009;

import java.math.BigInteger;

/**
 * 素因数分解処理クラス
 */
public class PrimeFactorizationThread extends Thread {
	private BigInteger target;
	private StringBuffer result = new StringBuffer();
	public PrimeFactorizationThread(BigInteger target) {
		this.target = target;
	}

	public void run() {
		// 実行中であることを格納
		ResultData.getInstance().putStatus(target, ResultData.STATUS_EXECUTE);
		BigInteger x = target;
		// 素因数分解 2から順番に割り算
		for (BigInteger i = new BigInteger("2"); compare(target, i);) {
			if (x.remainder(i).equals(BigInteger.ZERO)) {
            	result.append(i);
                if (!x.equals(i)) {
                	result.append(",");
                }
				x = x.divide(i);
			} else {
				// 割り切れなかったので次の数字で再度割り算
				i = i.add(BigInteger.ONE);
			}
		}

		// 結果を格納
		ResultData.getInstance().putStatus(target, result.toString());
	}

	/**
	 * BigIntegerの比較用
	 * @param x
	 * @param i
	 * @return
	 */
	private boolean compare(BigInteger x, BigInteger i) {
		if (i.compareTo(x) <= 0) {
			return true;
		} else {
			return false;
		}
	}

}
