import java.util.*;
import java.io.*;
public class Main {

    private static int EMPTY = 0;
    private static int HOLL = 31;
    private static int WALL = 32;

    private static int[] dx = {-1, 0, 1, 0};
    private static int[] dy = {0, 1, 0, -1};

    private static int[][] board;
    private static int[][] nightBoard;
    private static Night[] nights;
    private static int L;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        int N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        board = new int[L][L];
        nightBoard = new int[L][L];
        
        for(int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < L; j++) {
                int value = Integer.parseInt(st.nextToken());
                if(value == 1) value = HOLL;
                if(value == 2) value = WALL;
                board[i][j] = value;
            }
        }

        nights = new Night[N+1];
        for(int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            nights[i] = new Night(i, r-1, c-1, h, w, k);
        }

        for(int ii = 0; ii < Q; ii++) {
            st = new StringTokenizer(br.readLine());

            int index = Integer.parseInt(st.nextToken());
            Night night = nights[index];

            int direction = Integer.parseInt(st.nextToken());

            moveCommend(night, direction);
        }

        int answer = 0;
        for(int i = 1; i <= N; i++) {
            if(!nights[i].isLive()) continue;
            answer += nights[i].damege;
        }

        System.out.println(answer);
    }

    private static void moveCommend(Night now, int direction) {
        if(!now.isLive()) return;
        move(now, direction);
    }

    private static void move(Night start, int direction) {
        // 먼저 모든 인원들의 뭉치를 체크한다. 
        Set<Integer> nightNumbers = new HashSet<>();
        nightNumbers.add(start.number);

        boolean[][] visited = new boolean[L+1][L+1];
        Deque<int[]> dq = new LinkedList<>();
        start.draw(dq, visited);
        
        while(!dq.isEmpty()) {
            int tmpSize = dq.size();
            for(int ii = 0; ii < tmpSize; ii++) {
                int[] now = dq.removeFirst();
                int x = now[0];
                int y = now[1];

                int nx = x + dx[direction];
                int ny = y + dy[direction];

                if(isOut(nx,ny)) return;
                if(visited[nx][ny]) continue;
                if(nightBoard[nx][ny] == 0) continue;

                nightNumbers.add(nightBoard[nx][ny]);
                Night nN = nights[nightBoard[nx][ny]];
                nN.draw(dq, visited);
            }
        }
    
        
        // 이들을 해당 방향으로 움직일 수 있는지 체크
        boolean flag = true;
        for(int i = 0; i < L; i++) {
            for(int j = 0; j < L; j++) {
                if(!visited[i][j]) continue;
                int nextX = i + dx[direction];
                int nextY = j + dy[direction];
                if(isOut(nextX, nextY) || board[nextX][nextY] == WALL) {
                    flag = false;
                }
            }
        }

        // 못 움직이면 그냥 지나가
        if(!flag) return;
        
        // 이들을 해당 방향으로 움직일 수 있다면? 
        // 해당 인원들을 그려주도록 한다. 
        for(int nigthNumber : nightNumbers) {
            Night nowN = nights[nigthNumber];
            nowN.drawNightBoard(0, true);
        }

        for(int nigthNumber : nightNumbers) {
            Night nowN = nights[nigthNumber];

            nowN.x = nowN.x + dx[direction];
            nowN.y = nowN.y + dy[direction];

            if(nowN.number == start.number) {
                nowN.drawNightBoard(nowN.number,true);
            } else {
                nowN.drawNightBoard(nowN.number, false);
            }
        }
    }

    private static boolean isOut(int x, int y) {
        return x < 0|| x >= L || y < 0 || y >= L || board[x][y] == WALL;
    }

    private static class Night {
        int number, x,y, h,w, k;
        int damege = 0;

        public Night(int number, int x, int y, int h, int w, int k) {
            this.number = number;
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
            this.k = k;

            drawNightBoard(number, true);
        }

        public void drawNightBoard(int value, boolean noDamege) {
            for(int i = x; i < x + h; i++) {
                for(int j = y; j < y + w; j++) {
                    nightBoard[i][j] = value;
                    if(board[i][j] == HOLL && !noDamege) {
                        this.damege += 1;
                    }
                }
            }

            if(this.damege >= k && !noDamege) {
                for(int i = x; i < x + h; i++) {
                    for(int j = y; j < y + w; j++) {
                        nightBoard[i][j] = 0;
                    }
                }
            }
        }   

        public boolean isLive() {
            return damege < k;
        }  

        public void draw(Deque<int[]> dq, boolean[][] visited) {
            for(int i = x; i < x + h; i++) {
                for(int j = y; j < y + w; j++) {
                    visited[i][j] = true;
                    dq.add(new int[]{i, j});
                }
            }
        } 
    }
}