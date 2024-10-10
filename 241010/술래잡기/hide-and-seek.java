//1시 시작 

import java.util.*;
public class Main {

    private static int N, M, H, K;

    private static Map<String, Set<Integer>> board;
    private static Map<Integer, Person> people;
    private static boolean[][] tree;

    private static int sulX, sulY, sulDir, len;
    private static boolean[][] visited;
    private static int sulRound, answer;
    private static boolean right;

    private static int[][] personDx = new int[][]{{0,0},{1,-1}}; // [0] 우좌 [1] 하상 
    private static int[][] personDy = new int[][]{{1,-1},{0,0}};

    private static int[] sulDx = new int[]{-1,0,1,0};
    private static int[] sulDy = new int[]{0,1,0,-1};

    private static int realRound;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();
        M = sc.nextInt();
        H = sc.nextInt();
        K = sc.nextInt();

        tree = new boolean[N][N];
        people = new HashMap<>();
        board = new HashMap<>();

        for(int i = 0; i < M; i++) {

            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            int d = sc.nextInt();

            people.put(i, new Person(i, x, y, d)); 
            board.putIfAbsent(x + " " + y, new HashSet<>());
            board.get(x + " " + y).add(i);
        }

        for(int i = 0; i < H; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            tree[x][y] = true;
        }

        sulInit();

        answer = 0;
        realRound = 0;
        while(hasLive() && K-- > 0) {
            realRound++;
            peopleMove();
            sulMove();
            sulChangeDirection();
            // printPeople();
            calculateScore();
        }  
        System.out.println(answer);  
    }
    private static void printPeople() {
        System.out.println();
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(tree[i][j]) {
                    System.out.print("T ");
                    continue;
                }
                if(i == sulX && j == sulY) {
                    System.out.print(3 + " ");
                    continue;
                }
                if(board.containsKey(i + " " + j) && board.get(i + " " + j).size() >= 1) {
                    System.out.print(1 + " ");
                }
                else {
                    System.out.print(0 + " ");
                }
            }
            System.out.println();
        }
    }

    // 4. 술래 점수 계산 ()
    private static void calculateScore() {
        int killCount = 0;
        int nx = sulX;
        int ny = sulY;

        for(int i = 0; i < 3; i++) {
            if(isOut(nx,ny)) continue;
            if(!tree[nx][ny] && board.containsKey(nx + " " + ny)) {
                
                Set<Integer> killed = board.get(nx + " " + ny);
                killCount += killed.size();

                for(int dead : killed) {
                    people.remove(dead);
                }
                board.remove(nx + " " + ny);
            }
            nx += sulDx[sulDir];
            ny += sulDy[sulDir];
        }
        answer+= realRound * killCount;
    }
    // 3. 술래 방향 전환()
    private static void sulChangeDirection() {
        if(sulX == N/2 && sulY == N/2) {
            sulInit();
            return;
        }

        if((sulX == 0 && sulY == 0) || (sulX == 0 && sulY == N-1) || (sulX == N-1 && sulY == 0) || (sulX == N-1 && sulY == N-1)) {
            if(allVisited()) {
                visited = new boolean[N][N];
                right = false;
                sulDir = (sulDir + 2) % 4;
                sulRound += 1;
                return;
            }
        }

        turnSul();
    }

    private static void turnSul() {
        int value = right ? 1 : -1;
        if(sulRound % 2 == 0) len += value;
        sulDir = ((sulDir + value) + 4) % 4;
        sulRound += value;
    }

    private static boolean allVisited() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(!visited[i][j]) return false;
            }
        }
        return true;
    }

    private static void sulInit() {
        sulX = N/2;
        sulY = N/2;
        sulDir = 0;
        sulRound = 1;
        len = 1;
        visited = new boolean[N][N];
        right = true;
    }

    // 2. 술래 움직인다()
    private static void sulMove() {
        visited[sulX][sulY] = true;

        for(int i = 0; i < len; i++) {
            int nx = sulX + sulDx[sulDir];
            int ny = sulY + sulDy[sulDir];

            if(isOut(nx,ny)) break;

            visited[nx][ny] = true;
            sulX = nx;
            sulY = ny;
        }
    }

    // 1. 사람들 움직인다()
    // people에 있는 사람들(살아있다)을 탐색한다.
    // 만약 술래와의 거리가 3이하라면 움직인다. move()
    private static void peopleMove() {
        for(Person p : people.values()) {
            if(p.isClose()) p.move();
        }
    }

    private static boolean hasLive() {
        if(people.values().size() !=0) return true;
        return false;
    }

    private static class Person {
        int i,x,y,d;
        int dir = 0;
        // i,x,y,d, dir 

        public Person(int i, int x, int y, int d) {
            this.i = i;
            this.x = x;
            this.y = y;
            this.d = d - 1;
        }

        public boolean isClose() {
            return 3 >= Math.abs(sulX-x) + Math.abs(sulY-y);
        }

        public void move() {
            board.get(x + " " + y).remove(this.i);
            int nx = x + personDx[d][dir];
            int ny = y + personDy[d][dir];

            if(isOut(nx, ny)) {
                this.dir = (this.dir + 1) % 2;
                nx = x + personDx[d][dir];
                ny = y + personDy[d][dir];
            }

            if(nx == sulX && ny == sulY) {
                board.get(x + " " + y).add(this.i);
                return;
            }else { 
                board.putIfAbsent(nx + " " + ny, new HashSet<>());
                board.get(nx + " " + ny).add(this.i);
                x = nx;
                y = ny;
            }
        }
    }

    private static boolean isOut(int x, int y) {
        return x < 0 || x >= N || y < 0 || y >= N;
    }
}