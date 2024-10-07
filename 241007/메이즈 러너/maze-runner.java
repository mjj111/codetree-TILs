//5시 23분 시작 // 6시 23분 휴식
// 최상단 1, 1 
//빈칸, 벽1-9, 출구 
// 움직이는 조건
// 최단거리 = Math.abs(x2-x1) + Math.abs(y2 - y1)
// 모든 참가자는 동시 상하좌우로 빈칸에 이동가능
// 움직인 칸은 출구까지의 최단거리에 무조건 가까워야한다. 
// 움직일 수 있는 칸이 2개 이상이라면상하로 움직이는 것을 우선시 
//못 움직이면 안움직임
// 한 칸에 2명 이상의 참가자 존재 가능

//움직이고 나면,
// 아래 조건을 맞추면 미로가 회전 
// 한 명 이상이 출구를 포함한 가장 작은 정사각형을 잡는다.
// 가장 작은 크기를 갖는 정사각형이 2개 이상이라면 x 갖 작은 것이 우선되고 아니면 y가 작은 것이 우선된다. 
// 정사각형은 90도 회전하며 회전된 벽은 내구도가 1 깎인다. 



// 움직이는 방법
// 사용자들은 현재 exit에 있어서 가장 가까운 방향으로 움직인다.
// 만약 벽이라면, 밖이라면 스킵 
// 만약 출구라면, 해당 인원은 탈출한다. 
// 만약 거리가 현재보다 이상이라면 움직이지 않는다. 

// 출구 회전 방법
// 가장 가까운 사람을 찾는다.x가 작고 y 가 작은 순으로해서 ! 
// 해당 사람과의 거리만큼 해서 정사각형을 만든다. 
// 해당 정사각형을 90도 회전시킨다. 
// 해당 사각형 안에 있는 1이상의 값들은 -1로 만든다. 
import java.io.*;
import java.util.*;

public class Main {
    
    private static int[][] board;
    private static int[] exit;
    private static int[] dx = {-1, 1, 0, 0};
    private static int[] dy = {0, 0, 1, -1};

    private static int N, M;
    private static Person[] people;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        people = new Person[M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            people[i] = new Person(x - 1, y - 1);
        }

        exit = new int[2];
        st = new StringTokenizer(br.readLine());
        exit[0] = Integer.parseInt(st.nextToken()) - 1;
        exit[1] = Integer.parseInt(st.nextToken()) - 1;
        board[exit[0]][exit[1]] = -2;

        int time = 1;
        while (K-- > 0 && keep()) {
            peopleMove();
            boardMove();
            time++;
        }

        int count = 0;
        for (Person p : people) {
            count += p.moveCount;
        }

        System.out.println(count);
        System.out.println((exit[0] + 1) + " " + (exit[1] + 1));
    }

    private static void peopleMove() {
        for (Person now : people) {
            if (!now.isIn) continue;
            now.move();
        }
    }

    private static void boardMove() {
        Arrays.sort(people);
        Person closest = people[0];

        int minX = Math.min(exit[0], closest.x);
        int maxX = Math.max(exit[0], closest.x);
        int minY = Math.min(exit[1], closest.y);
        int maxY = Math.max(exit[1], closest.y);

        int squareSize = Math.max(maxX - minX, maxY - minY);

        int startX = 0, endX = 0, startY = 0, endY = 0;
        boolean found = false;
        
        for (int i = 0; i <= N - squareSize; i++) {
            if (found) break;
            for (int j = 0; j <= N - squareSize; j++) {
                if (!isInRange(i, j, squareSize, closest, exit)) continue;
                startX = i;
                endX = i + squareSize;
                startY = j;
                endY = j + squareSize;
                found = true;
                break;
            }
        }

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (board[i][j] >= 1 && board[i][j] <= 9) board[i][j] -= 1;
            }
        }
        
        rotateBoard(startX, endX, startY, endY, squareSize);
    }

    private static void rotateBoard(int startX, int endX, int startY, int endY, int squareSize) {
        int[][] copyBoard = new int[squareSize + 1][squareSize + 1];
        
        for (int i = 0; i <= squareSize; i++) {
            for (int j = 0; j <= squareSize; j++) {
                copyBoard[j][squareSize - i] = board[startX + i][startY + j];
            }
        }

        for (int i = 0; i <= squareSize; i++) {
            for (int j = 0; j <= squareSize; j++) {
                board[startX + i][startY + j] = copyBoard[i][j];
                if(board[startX + i][startY + j] == -2) {
                    exit[0] = startX + i;
                    exit[1] = startY + j;
                }
            }
        }

        // 사람들의 좌표를 회전된 보드에 맞게 수정
        for (Person person : people) {
            if (person.x >= startX && person.x <= endX && person.y >= startY && person.y <= endY) {
                int relativeX = person.x - startX;
                int relativeY = person.y - startY;
                int newX = startX + relativeY;
                int newY = startY + (squareSize - relativeX);
                person.x = newX;
                person.y = newY;
            }
        }
    }

    private static boolean keep() {
        for (Person p : people) {
            if (p.isIn) return true;
        }
        return false;
    }

    private static int getDistance(int x, int y) {
        return Math.abs(exit[0] - x) + Math.abs(exit[1] - y);
    }

    private static void printBoard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isOut(int x, int y) {
        return x < 0 || x >= N || y < 0 || y >= N;
    }

    private static boolean isInRange(int startX, int startY, int size, Person closest, int[] exit) {
        return closest.x >= startX && closest.x <= startX + size && closest.y >= startY && closest.y <= startY + size &&
               exit[0] >= startX && exit[0] <= startX + size && exit[1] >= startY && exit[1] <= startY + size;
    }

    private static class Person implements Comparable<Person> {
        int x, y;
        int moveCount = 0;
        boolean isIn = true;

        public Person(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            int nowDistance = getDistance(x, y);
            board[x][y] = 0;
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isOut(nx, ny)) continue;
                if (board[nx][ny] >= 1) continue;
                if (getDistance(nx, ny) >= nowDistance) continue;

                this.x = nx;
                this.y = ny;
                moveCount += 1;
                if (exit[0] == x && exit[1] == y) {
                    isIn = false;
                    return;
                }

                board[x][y] = -1;
                return;
            }
            board[x][y] = -1;
        }

        @Override
        public int compareTo(Person op) {
             // 현재 사람이 탈출한 경우 최후위로 보냄
            if (!this.isIn && op.isIn) {
                return 1;
            }
            // 비교 대상 사람이 탈출한 경우 그 사람이 뒤로 감
            if (this.isIn && !op.isIn) {
                return -1;
            }
            // 두 사람 모두 탈출한 경우 동등하게 처리
            if (!this.isIn && !op.isIn) {
                return 0;
            }
            int thisDistance = getDistance(this.x, this.y);
            int opDistance = getDistance(op.x, op.y);

            if (thisDistance == opDistance) {
                if (this.x == op.x) {
                    return this.y - op.y;
                }
                return this.x - op.x;
            }
            return thisDistance - opDistance;
        }
    }
}