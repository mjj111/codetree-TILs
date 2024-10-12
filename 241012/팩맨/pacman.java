import java.util.*;

public class Main {
    private static final int BOARD_SIZE = 4;
    private static int monsterCount, turns;
    private static Set<Monster> monsters;
    private static Deque<Monster> eggs;
    private static Pacman pacman;

    private static final int[] DX = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static final int[] DY = {0, -1, -1, -1, 0, 1, 1, 1};
    private static final int[] PDX = {-1, 0, 1, 0}; // 상좌하우
    private static final int[] PDY = {0, -1, 0, 1};

    private static int[][] deadTurns;
    private static List<Monster>[][] board;
    private static int[] bestPath;

    public static void main(String[] args) {
        initializeGame();
        runGame();
        System.out.println(monsters.size());
    }

    private static void initializeGame() {
        Scanner sc = new Scanner(System.in);
        monsterCount = sc.nextInt();
        turns = sc.nextInt();

        pacman = new Pacman(sc.nextInt() - 1, sc.nextInt() - 1);

        board = new ArrayList[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ArrayList<>();
            }
        }

        monsters = new HashSet<>();
        eggs = new LinkedList<>();
        for (int i = 0; i < monsterCount; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            int d = sc.nextInt() - 1;
            Monster monster = new Monster(x, y, d);
            monsters.add(monster);
            board[x][y].add(monster);
        }

        deadTurns = new int[BOARD_SIZE][BOARD_SIZE];
        sc.close();
    }

    private static void runGame() {
        for (int turn = 1; turn <= turns; turn++) {
            moveMonsters();
            movePacman(turn);
            removeDeadBodies(turn);
            hatchEggs();
        }
    }

    private static void moveMonsters() {
        Set<Monster> newMonsters = new HashSet<>();
        for (Monster monster : monsters) {
            monster.move(newMonsters);
        }
        monsters = newMonsters;
    }

    private static void movePacman(int turn) {
        pacman.findBestPath();
        pacman.move(turn);
    }

    private static void removeDeadBodies(int turn) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (deadTurns[i][j] <= turn - 2) deadTurns[i][j] = 0;
            }
        }
    }

    private static void hatchEggs() {
        while (!eggs.isEmpty()) {
            Monster newborn = eggs.removeFirst();
            monsters.add(newborn);
            board[newborn.x][newborn.y].add(newborn);
        }
    }

    private static boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE;
    }

    private static class Monster {
        int x, y, d;

        public Monster(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }

        public void move(Set<Monster> newMonsters) {
            eggs.add(new Monster(x, y, d));
            for (int i = 0; i < 8; i++) {
                int nx = x + DX[d];
                int ny = y + DY[d];

                if (isOutOfBounds(nx, ny) || (pacman.x == nx && pacman.y == ny) || (deadTurns[nx][ny] > 0)) {
                    d = (d + 1) % 8;
                    continue;
                }

                board[x][y].remove(this);
                x = nx;
                y = ny;
                board[x][y].add(this);
                newMonsters.add(this);
                break;
            }
        }
    }

    private static class Pacman {
        int x, y;

        public Pacman(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void findBestPath() {
            bestPath = new int[3];
            int[] currentPath = new int[3];
            int[] maxCount = {0};
            findBestPathRecursive(currentPath, 0, 0, x, y, maxCount);
        }

        private void findBestPathRecursive(int[] path, int depth, int count, int cx, int cy, int[] maxCount) {
            if (depth == 3) {
                if (count > maxCount[0]) {
                    maxCount[0] = count;
                    System.arraycopy(path, 0, bestPath, 0, 3);
                }
                return;
            }

            for (int i = 0; i < 4; i++) {
                int nx = cx + PDX[i];
                int ny = cy + PDY[i];
                if (isOutOfBounds(nx, ny)) continue;

                path[depth] = i;
                findBestPathRecursive(path, depth + 1, count + board[nx][ny].size(), nx, ny, maxCount);
            }
        }

        public void move(int turn) {
            Set<Monster> killedMonsters = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                int dir = bestPath[i];
                int nx = x + PDX[dir];
                int ny = y + PDY[dir];
                if (isOutOfBounds(nx, ny)) continue;
                x = nx;
                y = ny;
                deadTurns[x][y] = turn + 2;
                killedMonsters.addAll(board[x][y]);
                board[x][y].clear();
            }
            monsters.removeAll(killedMonsters);
        }
    }
}