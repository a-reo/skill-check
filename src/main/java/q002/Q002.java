package q002;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Q002 並べ替える
 *
 * dataListに "ID,名字" の形式で20個のデータがあります。
 * これをID順に並べて表示するプログラムを記述してください。
 *
 * dataListの定義を変更してはいけません。
 *
 *
[出力結果イメージ]
1,伊藤
2,井上
（省略）
9,清水
10,鈴木
11,高橋
（省略）
20,渡辺
 */
public class Q002 {
    /**
     * データ一覧
     */
    private static final String[] dataList = {
            "8,佐藤",
            "10,鈴木",
            "11,高橋",
            "12,田中",
            "20,渡辺",
            "1,伊藤",
            "18,山本",
            "13,中村",
            "5,小林",
            "3,加藤",
            "19,吉田",
            "17,山田",
            "7,佐々木",
            "16,山口",
            "6,斉藤",
            "15,松本",
            "2,井上",
            "4,木村",
            "14,林",
            "9,清水"
    };

    /**
     * メイン処理
     * @param args
     */
	public static void main(String[] args) {

		// ArrayListに変換
		List<String> list = Arrays.asList(dataList);

		// ソート
	    Collections.sort(list, new java.util.Comparator<String>() {
	        @Override
	        public int compare(String data1, String data2) {
	        	// ID部分取得
	        	int id1 = Integer.parseInt(data1.split(",")[0]);
	        	int id2 = Integer.parseInt(data2.split(",")[0]);
	        	// 数値比較
	        	return id1 - id2;
	        }
	    });

	    // System.out.println(list);
	    for (int i = 0; i < list.size(); i++) {
	    	System.out.println(list.get(i));
	    }
    }


}
// 完成までの時間: 0時間 6分