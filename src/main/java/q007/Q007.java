package q007;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * q007 最短経路探索
 *
 * 壁を 'X' 通路を ' ' 開始を 'S' ゴールを 'E' で表現された迷路で、最短経路を通った場合に
 * 何歩でゴールまでたどり着くかを出力するプログラムを実装してください。
 * もし、ゴールまで辿り着くルートが無かった場合は -1 を出力してください。
 * なお、1歩は上下左右のいずれかにしか動くことはできません（斜めはNG）。
 *
 * 迷路データは MazeInputStream から取得してください。
 * 迷路の横幅と高さは毎回異なりますが、必ず長方形（あるいは正方形）となっており、外壁は全て'X'で埋まっています。
 * 空行が迷路データの終了です。
 *

[迷路の例]
XXXXXXXXX
XSX    EX
X XXX X X
X   X X X
X X XXX X
X X     X
XXXXXXXXX

[答え]
14
 */
public class Q007 {
    /** 迷路のサイズ */
    private static int width = 0;
    private static int height = 0;
    /** スタート地点 */
    private static int start_x = 0;
    private static int start_y = 0;
    /** ゴール地点 */
    private static int end_x = 0;
    private static int end_y = 0;

    /**
     * メイン処理
     * @param args
     */
    public static void main(String[] args) {
        MazeInputStream maze = new MazeInputStream();

        // A-star
        // https://ja.wikipedia.org/wiki/A*
        try {
            // 迷路読み込み & 迷路表示
            List<String> lineList = readMaze(maze);
            width = lineList.get(0).length();
            height = lineList.size();

            // 二次元配列化 & 各ブロックの評価
            Block[][] mazeBlocks = createMazeBlocks(lineList);

            //探索開始
            mazeSearch(mazeBlocks);

            // 答え合わせ用に辿った経路を出力
            System.out.println();
            System.out.println("[辿った経路]");
            // ブロックに格納した親（進んだ経路）をEndからStartまで辿る
            Block parent = mazeBlocks[end_x][end_y].parent;
            while (true) {
                if (parent.start == false) {
                    parent.correct = true;
                    parent = parent.parent;
                } else {
                    break;
                }
            }
            int count = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Block block = mazeBlocks[x][y];
                    if (block.wall) {
                        System.out.print("X");
                    } else if (block.start) {
                        System.out.print("S");
                    } else if (block.goal) {
                        System.out.print("E");
                    } else if (block.correct) {
                        System.out.print("*");
                        count++;
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("[答え]");
            System.out.println(count);
        } catch (UnreachableException ue) {
            // ゴールへ辿り着けなかった場合
            System.out.println();
            System.out.println("[答え]");
            System.out.println(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * MazeInputStreamを標準出力し、メモリに読み込む
     *
     * @param maze
     * @return
     * @throws IOException
     */
    private static List<String> readMaze(MazeInputStream maze) throws IOException {
        System.out.println("[迷路]");
        BufferedReader br = new BufferedReader(new InputStreamReader(maze));
        List<String> lineList = new ArrayList<String>();
        String line;
        while ((line = br.readLine()) != null) {
            // 空行は読み込まない
            if (!(line.isEmpty() || line.startsWith(" "))) {
                lineList.add(line);
                System.out.println(line);
            }
        }
        return lineList;
    }

    /**
     * 迷路を1ブロック単位の二次元配列として読み込む<br>
     * Start,Endの位置を特定、各ブロックのゴールまでの最短移動距離を算出する（Xを無視しての距離）
     *
     * @param lineList
     * @return
     */
    private static Block[][] createMazeBlocks(List<String> lineList) {
        Block[][] mazeBlocks = new Block[width][height];
        for (int y = 0; y < lineList.size(); y++) {
            for (int x = 0; x < lineList.get(y).length(); x++) {
                mazeBlocks[x][y] = new Block();
                String block = lineList.get(y).substring(x, x + 1);
                if (" ".equals(block)) {

                } else if ("X".equals(block)) {
                    mazeBlocks[x][y].wall = true;
                } else if ("S".equals(block)) {
                    mazeBlocks[x][y].start = true;
                    start_x = x;
                    start_y = y;
                } else if ("E".equals(block)) {
                    mazeBlocks[x][y].goal = true;
                    end_x = x;
                    end_y = y;
                }
                mazeBlocks[x][y].x = x;
                mazeBlocks[x][y].y = y;
            }
        }

        // 各ブロックのゴールまでの最短移動距離を算出・格納
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mazeBlocks[x][y].expected = Math.abs(end_x - x) + Math.abs(end_y - y);
            }
        }
        return mazeBlocks;
    }

    /**
     * 迷路を探索する
     *
     * @param mazeBlocks
     * @throws Exception
     */
    private static void mazeSearch(Block[][] mazeBlocks) throws Exception {
        // 到達可能なブロックのリスト
        List<Block> openList = new ArrayList<Block>();
        // スタート地点をオープン
        mazeBlocks[start_x][start_y].cost = 0;
        openList.add(mazeBlocks[start_x][start_y]);

        while (true) {
            // スタートからゴールまでの期待値が最小のものから順次探索する
            Block now = getShortestOpenBlock(openList);
            now.stamped = true;

            // 現在地点がゴールに隣接していたら探索終了
            if (now.x + 1 == end_x && now.y == end_y) {
                mazeBlocks[end_x][end_y].parent = now;
                break;
            } else if (now.x == end_x && now.y + 1 == end_y) {
                mazeBlocks[end_x][end_y].parent = now;
                break;
            } else if (now.x - 1 == end_x && now.y == end_y) {
                mazeBlocks[end_x][end_y].parent = now;
                break;
            } else if (now.x == end_x && now.y - 1 == end_y) {
                mazeBlocks[end_x][end_y].parent = now;
                break;
            }

            // 現在地点から進めるブロックをオープンリストに追加
            if (now.x + 1 <= width &&
                    !mazeBlocks[now.x + 1][now.y].wall &&
                    !mazeBlocks[now.x + 1][now.y].stamped) {
                mazeBlocks[now.x + 1][now.y].parent = now;
                openList.add(mazeBlocks[now.x + 1][now.y]);
            }
            if (now.x - 1 >= 0 &&
                    !mazeBlocks[now.x - 1][now.y].wall &&
                    !mazeBlocks[now.x - 1][now.y].stamped) {
                mazeBlocks[now.x - 1][now.y].parent = now;
                mazeBlocks[now.x - 1][now.y].cost = now.cost + 1;
                openList.add(mazeBlocks[now.x - 1][now.y]);
            }
            if (now.y + 1 <= height &&
                    !mazeBlocks[now.x][now.y + 1].wall &&
                    !mazeBlocks[now.x][now.y + 1].stamped) {
                mazeBlocks[now.x][now.y + 1].parent = now;
                mazeBlocks[now.x][now.y + 1].cost = now.cost + 1;
                openList.add(mazeBlocks[now.x][now.y + 1]);
            }
            if (now.y - 1 >= 0 &&
                    !mazeBlocks[now.x][now.y - 1].wall &&
                    !mazeBlocks[now.x][now.y - 1].stamped) {
                mazeBlocks[now.x][now.y - 1].parent = now;
                mazeBlocks[now.x][now.y - 1].cost = now.cost + 1;
                openList.add(mazeBlocks[now.x][now.y - 1]);
            }
        }
    }

    /**
     * オープンされているブロックのうち、最小コストのブロックを返却する
     *
     * @return 最小コストのブロック
     */
    private static Block getShortestOpenBlock(List<Block> openList) throws Exception {
        if (openList.size() == 0) {
            throw new UnreachableException("ゴールへ辿り付く経路がありません.");
        }
        Block shortest = null;
        int index = 0;
        for (int i = 0; i < openList.size(); i++) {
            if (shortest == null ||
                    shortest.getPoint() >= openList.get(i).getPoint()) {
                shortest = openList.get(i);
                index = i;
            }
        }
        return openList.remove(index);
    }

    /**
     * 迷路の1ブロックを表す格納クラス
     */
    private static class Block {
    	/**  座標 */
        int x;
        int y;
        /**  スタート地点判定フラグ */
        boolean start = false;
        /**  ゴール地点か判定フラグ */
        boolean goal = false;
        /** 壁判定フラグ */
        boolean wall = false;
        /**  Endまで辿り付く経路か否か */
        boolean correct = false;
        /** 探索済みフラグ */
        boolean stamped = false;
        /** スタート地点からの移動距離 1ブロック=1コスト */
        int cost;
        /** ゴールまでの最短移動距離 */
        int expected;
        /** スタートからゴールまでの最小期待値 */
        int getPoint() {
            return cost + expected;
        }
        //親ブロック
        Block parent = null;
    }

    /**
     * 到達不可能例外
     */
    private static class UnreachableException extends Exception {

        private static final long serialVersionUID = 1L;

        UnreachableException(String message) {
            super(message);
        }
    }
}
// 完成までの時間: 3時間 20分