package q003;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Q003 集計と並べ替え
 *
 * 以下のデータファイルを読み込んで、出現する単語ごとに数をカウントし、アルファベット辞書順に並び変えて出力してください。
 * resources/q003/data.txt
 * 単語の条件は以下となります
 * - "I"以外は全て小文字で扱う（"My"と"my"は同じく"my"として扱う）
 * - 単数形と複数形のように少しでも文字列が異れば別単語として扱う（"dream"と"dreams"は別単語）
 * - アポストロフィーやハイフン付の単語は1単語として扱う（"isn't"や"dead-end"）
 *
 * 出力形式:単語=数
 *
[出力イメージ]
（省略）
highest=1
I=3
if=2
ignorance=1
（省略）

 * 参考
 * http://eikaiwa.dmm.com/blog/4690/
 */
public class Q003 {

	/** 区切り文字 */
	private static final String SEPARATOR = "\\,|\\.|[\\s]|\\–";

    /**
     * メイン処理
     * @param args
     */
	public static void main(String[] args) {
		// 辞書データ
		Map<String, Integer> data = new HashMap<>();

		// 単語数ごとにカウント
		try {
			InputStreamReader reader = new InputStreamReader(openDataFile(), "utf-8");
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(SEPARATOR);
				for (String word : words) {
					// "I"以外の単語をすべて小文字にする
					if (!word.equals("I")) {
						word = word.toLowerCase();
					}
					// 単語カウント
					if (!word.isEmpty()) {
						if (data.containsKey(word)) {
							int count = data.get(word) + 1;
							data.put(word, count);
						} else {
							data.put(word, 1);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// アルファベット順にソート（大文字小文字を区別しない）
		List<String> mapkey = new ArrayList<>(data.keySet());
		Collections.sort(mapkey, new java.util.Comparator<String>() {
			@Override
			public int compare(String data1, String data2) {
				String key1 = data1.toLowerCase();
				String key2 = data2.toLowerCase();
				return key1.compareTo(key2);
			}
		});

		// 表示
		for (String key : mapkey) {
			System.out.println(key + "=" + data.get(key));
		}
	}

	/**
     * データファイルを開く
     * resources/q003/data.txt
     */
    private static InputStream openDataFile() {
    	return Q003.class.getResourceAsStream("data.txt");
    }

}
// 完成までの時間: 0時間 35分