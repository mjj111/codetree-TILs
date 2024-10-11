// 3시 33분 시작
// RC 행열 격자
// 가장 위를 1행으로 둔다
// 정령들은 북쪽을 통해서 숲에 들어온다. 
//K명의 정령은 각자 골렘을 타고 숲을 탐색
//+ 모양의 구조 골렘을 타고 탐색한다.
// 중앙을 제외한 4칸 중 한 칸은 골렘의 출구.
// 어떤 방향이든 정령은 탑승 가능, 하지만 내릴 때는 정해진 출구로만 내릴 수 있다. 
//i번째로 숲을 탐색하는 골렘은 숲의 북쪽부터 시작해 골렘의 중앙이 ci열이 되도록 위치에 내려ㅗㄴ다. 
// 초기 골렘의 출구는 di의 방향에 위치해있다. 

// 골렘 이동방법
// 남쪽에서 한 칸 내려간다. 
// 만약 내려갈 수 없으면 서쪽 방향으로 회전하며 내려간다. 
// 출구는 서쪽 방향으로 회전, 위치는 한칸 왼쪽으로 이동
// 만약 서쪽 회전도 불가하면 동쪽 회전후 내려간다. 
// 더 이상 남쪽으로 도달할 수 없다면 정령은 골렘의 칸에서 이동이 가능하다.
//만약 현재 골렘의 출구위치와 인접 골렘의 출구 위치가 인접하다면 다른 골렘으로 이동이 가능하다. 
//그렇게 정령은 갈 수 있는 모든 칸 중에 가장 남쪽 칸으로 이동하고 완전히 종료한다.
// 해당 위치가 정령의 최종 위치가 된다. !! 정령 최종 위치 행의 번호 합을 구해야해서 누적한다. (행 + 1)
// 
//만약 이동했는데 골렘의 몸 일부가 숲을 벗어난 상태라면, 골렘을 포함해 숲에 위치한 모든 골렘을 지운다.
// 이 경우에 정령이 도달하는 최종 위치 답에 포함하지 않는다.
// 

// 수도코드 ----------------------------------------------
// RCK값을 전역 변수로해서 받는다.
//R+3 C로 이루어진 숫자 격자를 만든다. 
// K 줄만큼 반복해서 각 정령들을 저장한다. 
// 정령들은 순서가 보장되어야하므로 배열로 저장한다.
// 정령들의 내부를 클래스로 저장한다.
// 정령 클래스는 (번호1부터, 중심부 열, 출구 정보)를 갖는다.!! 열은 -1로 받는다. 행은 2부터 시작한다.
// 출구 정보는 0, 1, 2, 3 으로 북동남서다.
// 방향 정보를 전역변수로 저장한다. 
// dx = {-1,0,1,0};
// dy = {0,1,0,-1}; 
// 정답을 전역 변수로 저장한다. 
// 숫자 board를 만든다.
// boolean exit을 만든다. 
// K 만큼 반복해서 골렘들을 이동시킨다. 
// 이동 시킨 골렘에서 정령을 이동시킨다.
// 정답을 출력한다. 


// 골렘 이동 방법
// while 문으로 꾸준히 반복한다. (움직임이 가능하다면 움직이고 continue 한다)
// 현재 정령의 위치가 온전한지 확인한다. 아니라면 break
// 정령의 위치로 아래 위치가 비었는지 확인한다. 아래좌(x+1, y - 1) 아래중(x+2, y) 아래우(x+1, y+1)
//                                              아래로 움직인다.

// 만약 아래로 움직이지 못한다면 서쪽 방향을 확인한다. 위(x-1, y-1) 좌(x, y-2) 하(x+1,y-1) 
//                               서쪽 아래를 확인한다. 하(x+2 y-1) 좌(x+1, y-2)
//                              움직일 수 있다면 움직이고 방향을 -1 수정하고 continue

// 만약 서쪽으로 움직이지 못한다면 동쪽 방향을 확인한다. 위(x-1,y+1) 우(x, y+2) 하(x+1, y+1)
//                                동쪽 아래를 확인한다. 하(x+2, y+1) 우(x+1, y+2)
//                              움직일 수 있다면 움직이고 방향을 +1 수정하고 continue
//
// 못움직였다면 break

// 정령 이동 방법  !! 정령이 이동하려는데 만약 행이 3행 이하라면 범위를 벗어난 것으로 exit, board터뜨린다. 
// 현재 정령의 위치로부터 상하좌우로 board에 자기번호를 기록한다. (출구 방향인 경우에는 exit[x][y] true)
// board크기의 visited를 만든다
// 현재 위치를 true 해준다. 
// Deque에 자기 번호와 현재 위치를 담는다. 

// 최고로 작은 행값을 설정한다. 

// Deque가 빌 떄까지 반복한다.
//      현재를 꺼낸다.
//      4방향을 만든다.
//          만약 격자밖이라면 접근하지 않는다.
//          만약 이전에 visited한 곳이라면 접근하지 않는다.
//          만약 다음이 0이라면 접근하지 않는다.

//          만약 내가 있는 위치가 출구고 && 다음 녀석 값이 나와 다르다면
//              Deque에 다음 녀석의 번호와 다음 위치로 넣는다.
//              vistied처리한다.
//              현재 행을 갱신한다.          
//          만약 내가 있는 위치가 출구가 아니고&& 다음 녀석 값이 나와 같다면 
//              Deque에 내 현재 번호와 다음 위치로 넣는다.
//              vistied처리한다.
//              현재 행을 갱신한다. 
//  최고로 큰 행 값 -2가 1이상이라면 answer에 더해준다. 
import java.util.*;
public class Main {
    private static int R, C, K;
    private static int[][] board;
    private static boolean[][] exit;
    private static Map<Integer, Pairy> pairys;

    private static int[] dx = {-1,0,1,0}; //북동남서
    private static int[] dy = {0,1,0,-1}; 
    
    private static int answer = 0;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        R = sc.nextInt();
        C = sc.nextInt();
        K = sc.nextInt();
        
        pairys = new HashMap<>();
        for(int i = 1; i <= K; i++) {
            int y = sc.nextInt();
            int d = sc.nextInt();
            pairys.put(i, new Pairy(i,1,y-1,d));
        }
        answer = 0;

        board = new int[R+3][C];
        exit = new boolean[R+3][C];

        for(int i = 1; i <= K; i++) {
            Pairy now = pairys.get(i);
            now.golemMove();
            now.selfMove();
            // printGolem(i);
        }
        System.out.println(answer);
    }

    private static boolean isOut(int x, int y) {
        return x < 0 || x >= R+3 || y < 0 || y >= C;
    }

    private static boolean isOut(int[] xy) {
        int x = xy[0];
        int y = xy[1];
        return x < 0 || x >= R+3 || y < 0 || y >= C;
    }

    private static void printGolem(int ii) {
        System.out.println(ii + "번째 라운드");
        for(int i = 0 ; i < R+3; i++) {
            for(int j = 0; j < C; j++) {
                if(exit[i][j]) System.out.print(-1 + " ");
                else System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static class Pairy {
        int id, x,y,d;
        public Pairy(int id, int x, int y, int d) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.d = d;
        }

        private boolean canDown() {
            int[] lD= {x+1, y-1};
            int[] mD = {x+2, y};
            int[] rD = {x+1, y+1};

            if(isOut(lD) || isOut(mD) || isOut(rD)) return false; //범위 밖
            if(board[lD[0]][lD[1]] != 0) return false; // 이미 골렘
            if(board[mD[0]][mD[1]] != 0) return false;
            if(board[rD[0]][rD[1]] != 0) return false;

            return true;
        }

        private boolean canSwift(int value) {
            int[] up = {x-1, y+value};
            int[] side = {x, y+value*2};
            int[] down = {x+1, y+value};

            if(isOut(up) || isOut(side) || isOut(down)) return false; //범위 밖 
            if(board[up[0]][up[1]] != 0) return false; // 이미 골렘 
            if(board[side[0]][side[1]] != 0) return false; 
            if(board[down[0]][down[1]] != 0) return false;  

            int[] sD = {x+1, y + value*2};
            int[] dD = {x+2, y + value};

            if(isOut(sD) || isOut(dD)) return false; //범위 밖 
            if(board[sD[0]][sD[1]] != 0) return false; //이미 골렘
            if(board[dD[0]][dD[1]] != 0) return false;  

            return true;
        }
        
        public void golemMove() {
            while(true) {
                if(canDown()) {
                    x = x + 1;
                    continue;
                } 
                if (canSwift(-1)) {
                    x = x + 1;
                    y = y - 1;
                    d = ((d - 1) + 4) % 4;
                    continue;
                }
                if (canSwift(1)) {
                    x = x  + 1;
                    y = y + 1;
                    d = ((d + 1) + 4) % 4;
                    continue;
                }
                break;
            }
        }
        public void selfMove() {
            if(x <= 3) {
                board = new int[R+3][C];
                exit = new boolean[R+3][C];
                return;
            }

            board[x][y] = this.id;
            for(int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                board[nx][ny] = this.id;
                if(i == d) exit[nx][ny] = true;
            }

            boolean[][] visited = new boolean[R+3][C];
            visited[x][y] = true;

            Deque<int[]> dq = new LinkedList<>();
            dq.add(new int[]{x,y,id});
            int maxRow = y;

            while(!dq.isEmpty()) {
                int now[] = dq.removeFirst();
                int nowX = now[0];
                int nowY = now[1];
                int nowId = now[2];

                for(int i = 0; i < 4; i++) {
                    int nx = nowX + dx[i];
                    int ny = nowY + dy[i];
                    
                    if(isOut(nx,ny)) continue;
                    if(visited[nx][ny]) continue;
                    if(board[nx][ny] == 0) continue;

                    if(exit[nowX][nowY] && board[nx][ny] != nowId) {
                        dq.addLast(new int[]{nx,ny, board[nx][ny]});
                        visited[nx][ny] = true;
                        maxRow = Math.max(maxRow, nx);
                        continue;
                    }
                    if(board[nx][ny] == nowId) {
                        dq.addLast(new int[]{nx,ny, nowId});
                        visited[nx][ny] = true;
                        maxRow = Math.max(maxRow, nx);
                        continue;
                    }
                }
            }
            if(maxRow -2 >= 3) {
                answer += (maxRow-2);
            }
        }
    }
}