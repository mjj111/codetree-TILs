//1시 시작 
// n*n 격자 
// m명의 도망자 
// 술래는 항상 중앙에 있다. 
//도망자는 좌우로 상하로 움직이는 유형 2가지가 있다. 
// 좌우는 우부터 상하는 하부터 시작
// h개의 나무가 있다. + 초기에 도망자와 겹쳐져 주어질 수 있다. 

// 이게 한 턴!!!
// m명의 도망자가 동시에 움직임
// 그다음에 술래 움직임

// 도망자가 움직일 때 현재 술래와의 거리Math.abs(x1-x2) + Math.abs(y1-y2)가 3 이하인 도망자만 움직인다. 
// 현재 바라보고 있는 방향으로 1칸 움직인다 했을 때,(격자 안벗어남)
// 움직이려는 칸에 술래라면 움직이지 않음
// 술래가 없다면 해당 칸으로 이동(나무가 있어도 괜찮다.)
// 현재 바라보고 있는 방향으로 1칸 움직인다 했을 떄,(격자에 벗어남)
// 반대로 방향을 틀고, 해당 방향으로 1 칸 움직였을 떄 해당 위치에 술래가 없다면 이동 

//술래가 움직이는 경우 
//만약 중앙을 위치하게 된다면, 상우하좌로 2개씩기존 방법 +1 칸씩 이동한다. 
//만약 0,0을 만나게된다면,     2개씩 기존방법 -1 칸씩 기존것 반대 방향으로 움직인다. //하우상좌
// 만약 방향을 틀어야하는 위치에 존재한다면 도착과 동시에 방향을 튼다. 

//술래가 움직이고 나서, 술래의 방향으로부터 본인포함 3칸에 있는 사용자를 잡게된다. 
// 하지만 만약 나무가 놓여있다면, 해당 칸에 도망자는 나무에 가려져 보이지 않게된다. 
// 잡힌 도망자는 사라지게 되고 술래는 현재 턴을 t턴이라고 했을 떄 t * 현재턴에 잡힌 도망자의 수만큼 점수를 얻게된다. 

//k번에 걸쳐 술래잡기를 진행하는 동안, 술래가 총 얻게된 점수를 출력. 

//n, m, h, k 
// m개의 줄에 걸쳐 도망자 위치 x, y와 이동방법 d 가 주어짐 (격자 위치가 1,1 부터 시작하므로 -1을 해줘야한다. )
// Map<Integer, Person> people = new HashMAp<>();  객체로 넣어준다.
// Map<String, Set<Integer>> board 에 해당 위치 사람들을 기록해준다. -> String으로 편하게 해당 보드에 존재하는 사람들을 확인한다. 추가 삭제한다.
// h개의 줄에 걸쳐 나무의 위치가 주어진다. tree[][] 에 h 만큼 true로 넣어준다. 
// 
// 술래의 위치 전역 변수로 설정. 
// 정답 전역 변수로 설정. 
// 현재 라운드 전역 변수로 설정  
// 살아남은 사람이 있으면, K번 동작할 때까지 반복하도록 한다. 
// 1. 사람들 움직인다.()
// 2. 술래 움직인다. ()
// 3. 술래 방향 전환. ()
// 4. 술래 점수 계산. ()

// 길이 측정 함수 Math.abs(술래X -x) + Math.abs(술래Y-y)

// 1. 사람들 움직인다()
// people에 있는 사람들(살아있다)을 탐색한다.
// 만약 술래와의 거리가 3이하라면 움직인다. move()

// 2. 술래 움직인다()
// 주어진 방향과 길이만큼 이동 시킨다. 
// 이동된 곳들 true로 설정 
// 벽을 넘기지는 말것

// 3. 술래 방향 전환()
//
// 끝을 찍었는가 혹은 중앙을 찍었는가를 확인 후 갱신한다
// +중앙이라면 right = true 설정 
// settingInit()
// + 현재턴 1로 설정,길이 1로 설정, 방향 0로 설정  
// + visited를 모두 false로 전환
// return;
//
// 만약 술래의 x나 y가 벽에 찍었다면
// visited가 모두 true인지 확인한다. 
// + 맞다면 right = false로 설정한다. 
// + dir을 반대방향으로 설정 (dir + 2) % 4 
// 현재 턴을 + 1 
// return  
//
// right가 true 라면
// rightTurn()
// 아니라면 
// leftTurn()

//int[] dx = {-1,0,1,0};
//int[] dy = {0,1,0,-1};

//rightTurn()
// 만약 현재 턴이 2로 나눠진다면 움직이는 길이를 1 상승시킨다. 
// 방향을 바꿔준다 -> ((dir + 1)+ 4) % 4; 해준다. 
// turn + 1

//leftTurn()
// 만약 현재 턴이 2로 나눠진다면 움직이는 길이를 1 하강시킨다.
// 방향을 바꿔준다 -> ((dir - 1) + 4) % 4 
// turn - 1



// 4. 술래 점수 계산 ()
// 현재 술래 위치부터 3만큼 이동하며 사용자가 있는지 확인한다. 
// 만약 현재 위치가 나무라면 지나간다. 
// board.contains(x+ " "+ y) 하고 있다면 
// 해당 목록에서 사용자를 죽여버린다. 
// 카운트 상승

// 3번 다 이동했다면 정답에 += 현재 라운드 * 죽인 사람들 


//personDx = {{0,0},{1,-1}}; [0] 우좌 [1] 하상 
//personDy = {{1,-1},{0,0}};
//사람 객체
// i,x,y,d, dir // 처음 주어질 떄, dir 무조건 0이다.  d들어오면 1빼고 설정한다(1,2여서)

// move ()
// 먼저 현재 위치를 board에서 지운다. board.get(x + " " + y).remove(this.i);
// int nx = x + personDx[d][dir];
// int ny = y + personDy[d][dir];

// 현재 바라보고 있는 방향으로 1칸 움직인다 했을 떄,(격자에 벗어남)
// 방향을 틀어준다. dir = (dir+ 1) % 2 해준다. nx,ny 고친다. 

// 술래가 위치하고 있다면 움직이지 않고, !!기존에 위치로 다시 간다. 
// 아니라면 이동하고 해당 위치에 기록한다. 

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

        // m개의 줄에 걸쳐 도망자 위치 x, y와 이동방법 d 가 주어짐 (격자 위치가 1,1 부터 시작하므로 -1을 해줘야한다. )
        for(int i = 0; i < M; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            int d = sc.nextInt();
            // Map<Integer, Person> people = new HashMAp<>();  객체로 넣어준다.
            people.put(i, new Person(i, x, y, d)); 
            // Map<String, Set<Integer>> board 에 해당 위치 사람들을 기록해준다. -> String으로 편하게 해당 보드에 존재하는 사람들을 확인한다. 추가 삭제한다.
            board.putIfAbsent(x + " " + y, new HashSet<>());
            board.get(x + " " + y).add(i);
        }

        // h개의 줄에 걸쳐 나무의 위치가 주어진다. tree[][] 에 h 만큼 true로 넣어준다. 
        for(int i = 0; i < H; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            tree[x][y] = true;
        }

        //술래 위치, 방향 길이 초기화 
        sulInit();

        answer = 0;
        realRound = 0;
        // 살아남은 사람이 있고, K번 동작할 때까지 반복하도록 한다. 
        while(hasLive() && K-- > 0) {
            realRound++;
            peopleMove();
            sulMove();
            sulChangeDirection();
            // if(realRound >= 1) {
            //     System.out.println("현재 라운드 : "+ realRound);
            //     // printSul();
            //     printPeople();
            // }
            calculateScore();
        }  
        System.out.println(answer);  
    }
    private static void printSul() {
        int[][] tmpBoard=  new int[N][N];
        tmpBoard[sulX][sulY] = 1;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                System.out.print(tmpBoard[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void printPeople() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
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

        // 현재 술래 위치부터 3만큼 이동하며 사용자가 있는지 확인한다. 
        for(int i = 0; i < 3; i++) {
            if(isOut(nx,ny)) continue;
            // System.out.println("공격" + nx + ny);

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
        // +중앙이라면 right = true 설정
        if(sulX == N/2 && sulY == N/2) {
            sulInit();
            return;
        }

        // 만약 술래의 x나 y가 벽에 찍었다면
        if((sulX == 0 && sulY == 0) || (sulX == 0 && sulY == N-1) || (sulX == N-1 && sulY == 0) || (sulX == N-1 && sulY == N-1)) {
            // visited가 모두 true인지 확인한다. 
            if(allVisited()) {
                visited = new boolean[N][N];
                // 반대 방향으로 움직이도록
                right = false;
                //dir을 반대방향으로 설정 (dir + 2) % 4 
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
        // 이동된 곳들 true로 설정 
        visited[sulX][sulY] = true;

        // 주어진 방향과 길이만큼 이동 시킨다. 
        for(int i = 0; i < len; i++) {
            int nx = sulX + sulDx[sulDir];
            int ny = sulY + sulDy[sulDir];

            // 벽을 넘기지는 말것
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
        //처음 주어질 떄, dir 무조건 0이다.  d들어오면 1빼고 설정한다(1,2여서)
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
            // 현재 위치를 board에서 지운다.
            board.get(x + " " + y).remove(this.i);
            int nx = x + personDx[d][dir];
            int ny = y + personDy[d][dir];

            //현재 바라보고 있는 방향으로 1칸 움직인다 했을 떄,(격자에 벗어남)
            // 방향을 틀어준다.
            if(isOut(nx, ny)) {
                this.dir = (this.dir + 1) % 2;
                nx = x + personDx[d][dir];
                ny = y + personDy[d][dir];
            }

            //술래가 위치하고 있다면 움직이지 않고, !!기존에 위치로 다시 간다. 
            if(nx == sulX && ny == sulY) {
                board.get(x + " " + y).add(this.i);
                return;
            }else { // 아니라면 이동하고 해당 위치에 기록한다. 
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