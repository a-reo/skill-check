package q008;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Q008 埋め込み文字列の抽出
 *
 * 一般に、定数定義の場合を除いて、プログラム中に埋め込み文字列を記述するのは良くないとされています。
 * そのような埋め込み文字列を見つけるために、埋め込み文字列や埋め込み文字（"test"や'a'など）が
 * 記述された行を出力するプログラムを作成してください。
 *
 * - 実行ディレクトリ配下（再帰的にチェック）に存在するすべてのjavaファイルをチェックする
 * - ファイル名はディレクトリ付きでも無しでも良い
 * - 行の内容を出力する際は、先頭のインデントは削除しても残しても良い

[出力イメージ]
Q001.java(12): return "test";  // テストデータ
Q002.java(10): private static final DATA = "test"
Q002.java(11): + "aaa";
Q002.java(20): if (test == 'x') {
Q004.java(13): Pattern pattern = Pattern.compile("(\".*\")|(\'.*\')");

 */
public class Q008 {

	/** チェックパターン */
	private static final String CHECK_PATTERN = "\".*\"";

	/** 改行コード */
	private static String LINE_SEP = System.getProperty("line.separator");

    /**
     * メイン処理
     *
     * @param args
     */
    public static void main(String[] args) {

        Pattern pattern = Pattern.compile(CHECK_PATTERN);
        try {
            Stream<File> javaFiles = listJavaFiles();
            for (Iterator<File> it = javaFiles.iterator(); it.hasNext(); ) {
                File file = (File) it.next();
                // コメント行を削除
                String text = readText(file);
                text = deleteLineComment(text);
                text = deleteBlockComment(text);

                // 埋め込み文字列表示
                BufferedReader br = new BufferedReader(new StringReader(text));
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    if (check(pattern, line)) {
                        System.out.println(file.getName() + "(" + lineNumber + "): " + line);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * JavaファイルのStreamを作成する
     *
     * @return
     * @throws IOException
     */
    private static Stream<File> listJavaFiles() throws IOException {
        return Files.walk(Paths.get(".")).map(Path::toFile).filter(f -> f.getName().endsWith(".java"));
    }

    /**
     * ファイルを読み込む
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    private static FileInputStream openJavaFile(File file) throws FileNotFoundException {
        return new FileInputStream(file.getAbsolutePath());
    }

    /**
     * パターンチェック
     *
     * @param pattern
     * @param target
     * @return
     */
    private static boolean check(Pattern pattern, String target) {
        Matcher matcher = pattern.matcher(target);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 行コメントを削除する
     *
     * @param text
     * @return
     */
    public static String deleteLineComment(String text) {
        StringBuffer buf = new StringBuffer();

        boolean isLineComment = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (isLineComment) {
                if (ch == LINE_SEP.charAt(0)) {
                    isLineComment = false;
                }
            } else if (ch == '/' && text.charAt(i + 1) == '/') {
                isLineComment = true;
            } else {
                buf.append(ch);
            }
        }

        return buf.toString();
    }

    /**
     * ブロックコメントを削除する
     *
     * @param text
     * @return
     */
    public static String deleteBlockComment(String text) {
        StringBuffer buf = new StringBuffer();

        boolean isBlockComment = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (isBlockComment) {
                if (ch == '/' && text.charAt(i - 1) == '*') {
                    isBlockComment = false;
                }
            } else if (ch == '/' && text.charAt(i + 1) == '*') {
                isBlockComment = true;
            } else {
                buf.append(ch);
            }
        }

        return buf.toString();
    }

    /**
     * ファイルを文字列として読み込む
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static String readText(File file) throws IOException {
        InputStreamReader inReader = new InputStreamReader(openJavaFile(file));
        StringBuffer sBuf = new StringBuffer();

        int ch;
        while ((ch = inReader.read()) != -1) {
            sBuf.append((char) ch);
        }

        return sBuf.toString();
    }

}
// 完成までの時間: 1時間 20分
