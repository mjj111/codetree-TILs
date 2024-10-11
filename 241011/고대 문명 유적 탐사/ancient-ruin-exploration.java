// 6시 30분 시작 

// 수도 코드
// K와 M을 입력받는다.
// 5개의 줄을 걸쳐 board격자를 채운다. 해당 격자는 전역변수다. 
// M개의 조각 번호가 주어진다.  전역변수다. 
// wallIndex를 전역변수로 0을 설정한다. 

// K만큼 반복한다.
// total을 전역변수로 0으로 선언해준다. 
// maxCount = 0으로 전역변수 선언한다. 
// rotatedBoard를 null로 설정한다. 
// 반복문을 통해 열부터 행순서(열증가 행증가) 방식으로 중심 구간에 값을 갖고 int y = 1; y <=3; y++ int x = 1; x<=3; x++
// 3번(90, 180,270) rotate한다. 

// rotate할 때마다,
// (현재 주어진 그림에서 유적 수를 탐색)현재 유적수를 확인한다. 
// tmpBoard로 board를 복사해준다. 
// vistied boolean[][]을 선언한다. 
// int count를 선언한다. 
// 반복문을 통해 visited가 되지 않은녀석을 bfs 동작시키고 count를 반환하게 한다.
// 만약 count가 maxCount를 초과한다면 갱신해주고, rotatedBoard도 갱신해준다. 

// rotate한 번 더한다. (복구)

// total에 maxCount를 추가해준다. 
// rotatedBoard가 null이라면 (유적을 찾을 수 없었다는 것) break 한다. 

// rotatedBoard가 널이 아닌동안 반복한다. 
//  board를 rotatedBoard로 갱신해준다. 
// 열부터 행순서(열은 증가, 행은 감소)순으로 접근한다. for(int y = 0; y < 5; y++) for(int x = 4; x >=0; x--)
//  만약 board[x][y] 가 0이라면 해당 값에 채워주고 wallIndex값을 상승시킨다.
// maxCount를 0으로 설정한다. 
// (현재 주어진 그림에서 유적 수를 탐색)현재 유적수를 확인한다.
// total에 maxCount를 추가해준다.  

// total이 0이 아니라면 출력한다. 만약 total이 0이라면 break한다. 

//---------------

// bfs 구현
// int tmpCount를 선언한다.
// 주어진 숫자를 확인한다.
// 인덱스와 값을 기반으로 탐색한다. tmpBoard를 0으로 그려준다.(List<int[]> history에 담아준다. )
// 만약 3번 이상 가지 못했다면 history를 따라 board 값으로 바꿔준다. 0을 반환한다.
// 만약 3번 이상 가게 됬다면 tmpCount를 반환한다.

// rotate(mx,my)
// int sx = mx - 1;
// int sy = my -1;
// int[][] tmp = new int[3][3];
// for(int i = 0; i < 3; i++) {
//     for(int j = 0; j < 3; j++) {
//         tmp[i][j] = board[sy-j+2][sx - i + 2];
//     }
// }
// for(int i = 0; i < 3; i++) {
//     for(int j = 0; j < 3; j++) {
//         board[sx+i][sy+j] = tmp[i][j];
//     }
// }
import java.util.*;
public class Main {
    private static int K, M;
    private static int[][] board;

    private static int[] wall;
    private static int wallIndex = 1; 

    private static int total, maxCount, direction;
    private static int[][] rotatedBoard;

    private static int[] dx = {0,0,1,-1};
    private static int[] dy = {1,-1,0,0};
    
    private static void printBoard() {
        for(int i = 0; i < 5; i++) {
            System.out.println();
            for(int j = 0; j < 5; j++) {
                System.out.print(board[i][j] + " ");
            }
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        K = sc.nextInt();
        M = sc.nextInt();

        board = new int[5][5];
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                board[i][j] = sc.nextInt();
            }   
        }
        
        wall = new int[M];
        for(int i = 0; i < M; i++) {
            wall[i] = sc.nextInt();
        }
        wallIndex = 0;

        while(K-->0) {
            total = 0;
            maxCount = 0;
            direction = 4;
            rotatedBoard = null;
            for(int x = 1; x <= 3; x++) {
                for(int y = 1; y <= 3; y++) {

                    for(int i = 1; i <=3; i++) {
                        rotate(x,y);
                        check(i);
                    }

                    rotate(x,y);
                }
            }
            total += maxCount;

            if(rotatedBoard == null) break;
            while(rotatedBoard != null) {
                copyB(board, rotatedBoard);
                // printBoard();
                for(int y = 0; y < 5; y++) {
                    for(int x = 4; x >= 0; x--) {
                        if(board[x][y] == 0) {
                            board[x][y] = wall[wallIndex];
                            wallIndex++;
                        }
                    }
                }

                maxCount = 0;
                rotatedBoard = null;
                check(0);
                total += maxCount;
            }

            if(total != 0) System.out.print(total + " ");
            if(total == 0) break;
        }
    }

    private static void copyB(int[][] copyed , int[][] origin) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                copyed[i][j] = origin[i][j];
            }
        }
    }

    private static boolean isOut(int x, int y) {
        return x <0 || x >= 5 || y <0 || y >= 5;
    }

    private static int bfs(boolean[][] visited, int sx, int sy, int[][] tmpBoard) {
        int tmpCount = 1;
        int value = tmpBoard[sx][sy];

        Deque<int[]> dq = new LinkedList<>();
        dq.addLast(new int[]{sx,sy});

        List<int[]> history = new ArrayList<>();
        history.add(new int[]{sx,sy});

        tmpBoard[sx][sy] = 0;
        visited[sx][sy] = true;

        while(!dq.isEmpty()) {
            int[] now = dq.removeFirst();
            int x = now[0];
            int y = now[1];

            for(int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if(isOut(nx,ny)) continue;
                if(tmpBoard[nx][ny] != value) continue;

                if(tmpBoard[nx][ny] == value) {
                    dq.addLast(new int[]{nx,ny});
                    visited[nx][ny] = true;

                    history.add(new int[]{nx,ny});
                    tmpBoard[nx][ny] = 0;

                    tmpCount += 1;
                }
            }
        }
        
        if(tmpCount < 3) {
            for(int[] h : history) {
                int tx = h[0];
                int ty = h[1];
                tmpBoard[tx][ty] = board[tx][ty];
            }
            return 0;
        }
        return tmpCount;
    }

    private static void check(int dir) {
        int[][] tmpBoard = new int[5][5];
        copyB(tmpBoard, board);
        
        boolean[][] visited = new boolean[5][5];

        int count = 0;
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                if(!visited[i][j] && tmpBoard[i][j] != 0) {
                    count += bfs(visited, i, j, tmpBoard);
                }
            }
        }

        // 전보다 크면 안묻고 바꿔준다. 
        if(count > maxCount) {
            maxCount = count;
            rotatedBoard = tmpBoard;
            direction = dir;
            return;
        } 

        // 만약 같은데, 각도가 더 작다면 바꿔준다. 
        if(count == maxCount && dir < direction) {
            maxCount = count;
            rotatedBoard = tmpBoard;
            direction = dir;
            return;
        }
        // 만약 값도, 각도도 같다면 필요없다 지나간다. 현재가 가장 작은 행과 열이다.
    }
    private static void rotate(int mx, int my) {
        int sx = mx - 1;
        int sy = my - 1;

        int[][] tmp = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tmp[i][j] = board[sy + i][sx + j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[sy + i][sx + j] = tmp[2 - j][i];
            }
        }
    }
}