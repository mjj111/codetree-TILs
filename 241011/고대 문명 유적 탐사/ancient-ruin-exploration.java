// 6시 30분 시작 


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