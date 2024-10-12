import java.util.Scanner;
import java.util.ArrayList;

class Pair {
    int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isSame(Pair p) {
        return this.x == p.x && this.y == p.y;
    }
}

class Tuple {
    int x, y, dir;

    public Tuple(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
}

class Route {
    int dir1, dir2, dir3;

    public Route(int dir1, int dir2, int dir3) {
        this.dir1 = dir1;
        this.dir2 = dir2;
        this.dir3 = dir3;
    }
}

public class Main {
    public static final int MAX_DECAY = 2;
    public static final int P_DIR_NUM = 4;
    public static final int DIR_NUM = 8;
    public static final int MAX_N = 4;
    public static final int MAX_T = 25;
    
    public static int n = 4;
    public static int m, t;
    
    public static int[][][][] monster = new int[MAX_T + 1][MAX_N][MAX_N][DIR_NUM];
    
    public static int[][][] dead = new int[MAX_N][MAX_N][MAX_DECAY + 1];

    public static int[] dx = new int[]{-1, -1,  0,  1, 1, 1, 0, -1};
    public static int[] dy = new int[]{ 0, -1, -1, -1, 0, 1, 1,  1};
    

    public static int[] pDx = new int[]{-1,  0, 1, 0};
    public static int[] pDy = new int[]{ 0, -1, 0, 1};
    
    public static int px, py;
    
    public static int tNum = 1;
    
    public static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
    
    public static boolean canGo(int x, int y) {
        return inRange(x, y) && dead[x][y][0] == 0 && dead[x][y][1] == 0 
               && !new Pair(x, y).isSame(new Pair(px, py));
    }
    
    public static Tuple getNextPos(int x, int y, int moveDir) {
        for(int cDir = 0; cDir < DIR_NUM; cDir++) {
            int nDir = (moveDir + cDir + DIR_NUM) % DIR_NUM;
            int nx = x + dx[nDir];
            int ny = y + dy[nDir];
            if(canGo(nx, ny)) {
                return new Tuple(nx, ny, nDir);
            }
        }
    
        return new Tuple(x, y, moveDir);
    }
    
    public static void moveM() {

        // 일일이 몬스터 마다 위치를 구해 이동시키면 시간초과가 발생
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                for(int k = 0; k < DIR_NUM; k++) {
                    Tuple nextPos = getNextPos(i, j, k);
                    int x = nextPos.x;
                    int y = nextPos.y;
                    int nextDir = nextPos.dir;
                    monster[tNum][x][y][nextDir] += monster[tNum - 1][i][j][k];
                }
    }

    public static boolean contains(ArrayList<Pair> vPos, Pair p) {
        for(int i = 0; i < vPos.size(); i++)
            if(vPos.get(i).isSame(p))
                return true;
        
        return false;
    } 
    
    public static int getKilledNum(int dir1, int dir2, int dir3) {
        int[] dirs = new int[]{dir1, dir2, dir3};
        int x = px, y = py;
        int killedNum = 0;
    

        ArrayList<Pair> vPos = new ArrayList<>();
    
        for(int i = 0; i < 3; i++) {
            int nx = x + pDx[dirs[i]], ny = y + pDy[dirs[i]];

            if(!inRange(nx, ny))
                return -1;

            if(!contains(vPos, new Pair(nx, ny))) {
                for(int j = 0; j < DIR_NUM; j++)
                    killedNum += monster[tNum][nx][ny][j];
                
                vPos.add(new Pair(nx, ny));
            }
    
            x = nx; y = ny;
        }
        return killedNum;
    }
    
    public static void doKill(Route bestRoute) {
        int dir1 = bestRoute.dir1;
        int dir2 = bestRoute.dir2; 
        int dir3 = bestRoute.dir3;

        int[] dirs = new int[]{dir1, dir2, dir3};
        for(int i = 0; i < 3; i++) {
            int nx = px + pDx[dirs[i]], ny = py + pDy[dirs[i]];
    
            for(int j = 0; j < DIR_NUM; j++) {
                dead[nx][ny][MAX_DECAY] += monster[tNum][nx][ny][j];
                monster[tNum][nx][ny][j] = 0;
            }
    
            px = nx; py = ny;
        }
    }
    
    public static void moveP() {
        int maxCnt = -1;
        Route bestRoute = new Route(-1, -1, -1);
        
        for(int i = 0; i < P_DIR_NUM; i++)
            for(int j = 0; j < P_DIR_NUM; j++)
                for(int k = 0; k < P_DIR_NUM; k++) {
                    int mCnt = getKilledNum(i, j, k);
                    if(mCnt > maxCnt) {
                        maxCnt = mCnt;
                        bestRoute = new Route(i, j, k);
                    }
                }
        doKill(bestRoute);
    }
    
    public static void decayM() {
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++) {
                for(int k = 0; k < MAX_DECAY; k++)
                    dead[i][j][k] = dead[i][j][k + 1];
                dead[i][j][MAX_DECAY] = 0;
            }
    }
    
    public static void addM() {
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                for(int k = 0; k < DIR_NUM; k++)
                    monster[tNum][i][j][k] += monster[tNum - 1][i][j][k];
    }
    
    public static void simulate() {
        moveM();
        moveP();
        decayM();
        addM();
    }
    
    public static int countMonster() {
        int cnt = 0;
    
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                for(int k = 0; k < DIR_NUM; k++)
                    cnt += monster[t][i][j][k];
    
        return cnt;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        m = sc.nextInt();
        t = sc.nextInt();

        px = sc.nextInt() -1;
        py = sc.nextInt() -1;
        
        for(int i = 0; i < m; i++) {
            int mx = sc.nextInt();
            int my = sc.nextInt(); 
            int mdir = sc.nextInt();
            monster[0][mx - 1][my - 1][mdir - 1]++;
        }
        
        while(tNum <= t) {
            simulate();
            tNum++;
        }

        System.out.print(countMonster());
    }
}