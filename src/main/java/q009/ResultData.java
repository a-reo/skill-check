package q009;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 結果格納クラス
 */
public class ResultData {

    /** 実行中を表す文字列 */
    public static final String STATUS_EXECUTE = "実行中";

    /** 処理・結果を格納 */
    private ConcurrentHashMap<BigInteger, String> resultMap;

    /** ロックオブジェクト */
    private Object lock = new Object();

    private ResultData() {
        resultMap = new ConcurrentHashMap<BigInteger, String>();
    }

    public static ResultData getInstance() {
        return ResultDataHolder.INSTANCE;
    }

    /**
     * 素因数分解の状態を格納
     *
     * @param target
     * @param result
     */
    public void putStatus(BigInteger target, String result) {
        synchronized (lock) {
            resultMap.put(target, result);
        }
    }

    /**
     * 結果を表示し、実行中以外の要素を削除する。
     */
    public void printResult() {
        synchronized (lock) {
            if (resultMap.isEmpty()) {
                return;
            } else {
                // 結果を表示
                ArrayList<BigInteger> removeList = new ArrayList<BigInteger>();
                for (BigInteger target : resultMap.keySet()) {
                    System.out.println(target + ": " + resultMap.get(target));
                    if (!resultMap.get(target).equals(STATUS_EXECUTE)) {
                        removeList.add(target);
                    }
                }
                // 実行中以外で表示した要素を削除
                for (BigInteger target : removeList) {
                    resultMap.remove(target);
                }
            }
        }
    }

    public static class ResultDataHolder {
        private static final ResultData INSTANCE = new ResultData();
    }
}
