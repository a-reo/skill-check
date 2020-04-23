package q005;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Q005 データクラスと様々な集計
 *
 * 以下のファイルを読み込んで、WorkDataクラスのインスタンスを作成してください。
 * resources/q005/data.txt
 * (先頭行はタイトルなので読み取りをスキップする)
 *
 * 読み込んだデータを以下で集計して出力してください。
 * (1) 役職別の合計作業時間
 * (2) Pコード別の合計作業時間
 * (3) 社員番号別の合計作業時間
 * 上記項目内での出力順は問いません。
 *
 * 作業時間は "xx時間xx分" の形式にしてください。
 * また、WorkDataクラスは自由に修正してください。
 *
[出力イメージ]
部長: xx時間xx分
課長: xx時間xx分
一般: xx時間xx分
Z-7-31100: xx時間xx分
I-7-31100: xx時間xx分
T-7-30002: xx時間xx分
（省略）
194033: xx時間xx分
195052: xx時間xx分
195066: xx時間xx分
（省略）
 */
public class Q005 {

	/** 区切り文字 */
	private static final String SEPARATOR = "\\,";
	/** ヘッダー判定文字 */
	private static final String HEADER = "社員番号";

    /**
     * メイン処理
     * @param args
     */
	public static void main(String[] args) {
		// CSVファイル読み込み、WorkDataに格納
		List<WorkData> dataList = new ArrayList<WorkData>();

		try {
			InputStreamReader reader = new InputStreamReader(openDataFile());
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				// ヘッダーを読み飛ばす
				if (line.startsWith(HEADER)) {
					continue;
				}
				String[] items = line.split(SEPARATOR);
				dataList.add(new WorkData(items[0], items[1], items[2], items[3], Integer.parseInt(items[4])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// (1) 役職別の合計作業時間
		Map<String, Integer> totaltimeByPositionMap = aggregateTotalTimeByPosition(dataList);
		for (String key : totaltimeByPositionMap.keySet()) {
			System.out.println(key + ": " + convertTime(totaltimeByPositionMap.get(key)));
		}

		// (2) Pコード別の合計作業時間
		Map<String, Integer> totaltimeByPCodeMap = aggregateTotalTimeByPCode(dataList);
		for (String key : totaltimeByPCodeMap.keySet()) {
			System.out.println(key + ": " + convertTime(totaltimeByPCodeMap.get(key)));
		}

		// (3) 社員番号別の合計作業時間
		Map<String, Integer> totaltimeByNumberMap = aggregateTotalTimeByNumber(dataList);
		for (String key : totaltimeByNumberMap.keySet()) {
			System.out.println(key + ": " + convertTime(totaltimeByNumberMap.get(key)));
		}
	}

    /**
     * データファイルを開く
     * resources/q005/data.txt
     */
    private static InputStream openDataFile() {
    	return Q005.class.getResourceAsStream("data.txt");
    }

	/**
	 * 役職別の合計作業時間算出
	 *
	 * @param dataList
	 * @return Map（key:役職、value:合計時間(分)）
	 */
	private static Map<String, Integer> aggregateTotalTimeByPosition(List<WorkData> dataList) {
		Map<String, Integer> totaltimeByPositionMap = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < dataList.size(); i++) {
			if (totaltimeByPositionMap.containsKey(dataList.get(i).getPosition())) {
				int workTime = totaltimeByPositionMap.get(dataList.get(i).getPosition()) + dataList.get(i).getWorkTime();
				totaltimeByPositionMap.put(dataList.get(i).getPosition(), workTime);
			} else {
				totaltimeByPositionMap.put(dataList.get(i).getPosition(), dataList.get(i).getWorkTime());
			}
		}
		return totaltimeByPositionMap;
	}

	/**
	 * Pコード別の合計作業時間算出
	 *
	 * @param dataList
	 * @return Map（key:Pコード、value:合計時間(分)）
	 */
	private static Map<String, Integer> aggregateTotalTimeByPCode(List<WorkData> dataList) {
		Map<String, Integer> totaltimeByPCodeMap = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < dataList.size(); i++) {
			if (totaltimeByPCodeMap.containsKey(dataList.get(i).getpCode())) {
				int workTime = totaltimeByPCodeMap.get(dataList.get(i).getpCode()) + dataList.get(i).getWorkTime();
				totaltimeByPCodeMap.put(dataList.get(i).getpCode(), workTime);
			} else {
				totaltimeByPCodeMap.put(dataList.get(i).getpCode(), dataList.get(i).getWorkTime());
			}
		}
		return totaltimeByPCodeMap;
	}

	/**
	 * 社員番号別の合計作業時間算出
	 *
	 * @param dataList
	 * @return Map（key:社員番号、value:合計時間(分)）
	 */
	private static Map<String, Integer> aggregateTotalTimeByNumber(List<WorkData> dataList) {
		Map<String, Integer> totaltimeByNumberMap = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < dataList.size(); i++) {
			if (totaltimeByNumberMap.containsKey(dataList.get(i).getNumber())) {
				int workTime = totaltimeByNumberMap.get(dataList.get(i).getNumber()) + dataList.get(i).getWorkTime();
				totaltimeByNumberMap.put(dataList.get(i).getNumber(), workTime);
			} else {
				totaltimeByNumberMap.put(dataList.get(i).getNumber(), dataList.get(i).getWorkTime());
			}
		}
		return totaltimeByNumberMap;
	}

	/**
	 * 分を"N時間N分"に変換する
	 *
	 * @param time
	 * @return
	 */
	private static String convertTime(int time) {
		int hour = time / 60;
		int minute = time % 60;
		return String.format("%4d", hour) + "時間" + String.format("%02d", minute) + "分";
	}

}
// 完成までの時間: 1時間 10分