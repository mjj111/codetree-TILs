//2시 30분 
// NN 격자 
// 초기에는 무기 x 빈 격자에 플레이어가 위치
// 사용자 = 번호, 능력치
// 사용자 번호 순으로 본인의 방향으로 한 칸 만큼 이동한다. 
// 격자를 벗어나면 방향을 반대로 바꾸고, 1만큼 다시 이동한다.
// 만약 이동한 칸에 플레이어가 없다면 총이 있는지 확인후 획득
// 만약 이미 총 가지고 있으면 놓여있는 총과 현재 총 중에 가장 쎈 총으로 획득
// 나머지 총들은 격자에둔다. 

// 만약 이동한 방향에 플레이어가 있는경우 두 플레이어 싸움
// 초기 능력치와 총 공격력 합 비교해서 더큰 플레이어가 이김. 
// 만약 수치가같으면 초기 능력치가 높은 플레이어 승리
// 이긴 플레이어는 각 플레이어의 초기 능력치와 가지고 있는 총의 공격력 합의 차이만큼 포인트 획득 

// 진 플레이어는 본인이 갖고있는 총을 격자에 내려놓고 원래 방향으로 한 칸 이동. 
// 만약 이동하려는 칸에 다른 플레이어가 있거나 격자 범위 밖이면 90도 회전하여 빈칸이 보이는 곳으로 순가니동 
// 만약 해당 칸에 총이 있다면 가장 공격력이 높은 총을 획득하고 나머지 총들은 해당 격자에 내려놓음
//이긴 플레이어는 승리한 칸에 떨어져있는 총들과 원래 들고 있던 총 중 가장 공격력이 높은 총 획득하고 나머지 버림 
import java.util.*;
public class Main {

    private static int N, M, K;
    
    private static int[] dx = {-1, 0,1,0};
    private static int[] dy = {0,1,0,-1}; // 상우하좌

    private static Person[] people;

    private static int[][] peopleBoard;
    private static int[][] board;
    private static Map<String, List<Integer>> gunMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        K = sc.nextInt();

        peopleBoard = new int[N][N];
        board = new int[N][N];

        for(int i = 0 ; i< N; i ++) {
            Arrays.fill(peopleBoard[i], -1);

            for(int j = 0; j < N; j++) {
                gunMap.put(i + " " + j, new ArrayList<>());
                
                int value = sc.nextInt();
                board[i][j] = 1;
                gunMap.get(i + " " + j).add(value);
            }
        }

        people = new Person[M];
        for(int i = 0; i < M; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int d = sc.nextInt();
            int s = sc.nextInt();
            people[i] = new Person(i,x-1,y-1,d,s);
            peopleBoard[x-1][y-1] = i;
        }

        while(K-- > 0) {
            movePeople();
        }

        for(Person p : people) {
            System.out.print(p.point + " ");
        }

    }
    private static void movePeople() {
        for(Person p : people) {
            move(p);
        }
    }

    private static void move(Person now) {
        int nx = now.x + dx[now.d];
        int ny = now.y + dy[now.d];

        // 격자를 벗어나면 방향을 반대로 바꾸고, 1만큼 다시 이동한다.
        if(isOut(nx,ny)) {
            now.d = (now.d + 2) % 4;
            nx =  now.x + dx[now.d];
            ny = now.y + dy[now.d];
        }

        // 이동처리 
        peopleBoard[now.x][now.y] = -1;
        now.x = nx;
        now.y = ny; 

        // 만약 이동한 칸에 플레이어가 없다면 총이 있는지 확인후 획득
        if(peopleBoard[nx][ny] == -1) {
            //이동 한 칸에 본인 처리
            peopleBoard[nx][ny] = now.i;

            // 만약 이미 총 가지고 있으면 놓여있는 총과 현재 총 중에 가장 쎈 총으로 획득
            // 나머지 총들은 격자에둔다. 
            if(board[nx][ny] != 0) now.checkGun(); 
        }

        // 만약 이동한 방향에 플레이어가 있는경우 두 플레이어 싸움
        else if(peopleBoard[nx][ny] != -1) {
            Person op = people[peopleBoard[nx][ny]];
            int nowPower = now.s + now.gun;
            int opPower = op.s + op.gun; 

            // 초기 능력치와 총 공격력 합 비교해서 더 큰 플레이어가 이김.
            Person winner = null;
            Person loser = null;
            // 만약 수치가같으면 초기 능력치가 높은 플레이어 승리
            if( nowPower == opPower) {
                if(now.s > op.s) {
                    winner = now;
                    loser = op;
                }
                else if(op.s > now.s) {
                    winner = op;
                    loser = now; 
                }
            }
            else if(nowPower > opPower) {
                winner = now;
                loser = op;
            }
            else if(opPower > nowPower) {
                winner = op;
                loser = now;
            }

            // 이긴 플레이어는 각 플레이어의 초기 능력치와 가지고 있는 총의 공격력 합의 차이만큼 포인트 획득 
            winner.point += Math.abs(nowPower - opPower);
            // 이긴 플레이어로  본인 처리
            peopleBoard[nx][ny] = winner.i;
       
            // 진 플레이어는 본인이 갖고있는 총을 격자에 내려놓고  
            loser.dropGun();
            // 진 플레이어는 원래 방향으로 한 칸 이동. 원래 방향으로 한 칸 이동. 
            loser.run();
            //이긴 플레이어는 승리한 칸에 떨어져있는 총들과 원래 들고 있던 총 중 가장 공격력이 높은 총 획득하고 나머지 버림 
            winner.checkGun();
        }
    }

    private static boolean isOut(int x, int y) {
        return x < 0 || x >= N || y < 0 || y >= N;
    }

    private static class Person {
        int i, x,y,d,s;
        int gun = 0;
        int point = 0;

        // 진 플레이어는 원래 방향으로 한 칸 이동. 원래 방향으로 한 칸 이동. 
        public void run() {
            int nx = x + dx[d];
            int ny = y + dy[d];

            // 만약 이동하려는 칸에 다른 플레이어가 있거나 격자 범위 밖이면 90도 회전하여 빈칸이 보이는 곳으로 순가니동 
            while(isOut(nx, ny) || peopleBoard[nx][ny] != -1) {
                d = (d + 1) % 4;
                nx = x + dx[d];
                ny = y + dy[d];
            }

            this.x = nx;
            this.y = ny;
            peopleBoard[x][y] = i;

            // 만약 해당 칸에 총이 있다면 가장 공격력이 높은 총을 획득하고 나머지 총들은 해당 격자에 내려놓음
            if(board[x][y] != 0) checkGun();
            
        }

        // 진 플레이어는 본인이 갖고있는 총을 격자에 내려놓음 
        public void dropGun() {
            List<Integer> gunsOfMap = gunMap.get(x + " " + y);
            gunsOfMap.add(this.gun);
            this.gun = 0;
            // 총을 버렸기 때문에 1추가 해준다. 
            board[x][y] += 1; 
        }
        // 만약 이미 총 가지고 있으면 놓여있는 총과 현재 총 중에 가장 쎈 총으로 획득
        // 나머지 총들은 격자에둔다. 
        public void checkGun() {
            List<Integer> gunsOfMap = gunMap.get(x + " " + y);
            Collections.sort(gunsOfMap, Collections.reverseOrder());
            int strongestGun = gunsOfMap.get(0);

            //총이 있는데 더 좋은 총을 발견하면 바꾼다. 
            if(this.gun != 0 && this.gun < strongestGun) {
                gunsOfMap.add(this.gun);
                this.gun = strongestGun;
                gunsOfMap.remove(0);
            }

            // 만약 총이 없다면 가장 좋은 총을 줍는다. 
            // board에 총 하나가 지워졌다고 적어준다. 
            if(this.gun == 0) {
                this.gun = strongestGun;
                gunsOfMap.remove(0);
                board[x][y] -= 1;
            }
        }

        public Person(int i, int x, int y, int d, int s) {
            this.i = i;
            this.x = x;
            this.y = y;
            this.d = d;
            this.s = s;
        }
    }
}