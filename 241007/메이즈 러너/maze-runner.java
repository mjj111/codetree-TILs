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
                if (board[i][j] >= 1) board[i][j] -= 1;
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

                if (board[startX + i][startY + j] == -2) {
                    exit[0] = startX + i;
                    exit[1] = startY + j;
                }
            }
        }

        for (Person person : people) {
            if (person.x >= startX && person.x <= endX && person.y >= startY && person.y <= endY && person.isIn) {
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
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isOut(nx, ny)) continue;
                if (board[nx][ny] >= 1) continue;
                if (getDistance(nx, ny) >= nowDistance) continue;

                this.x = nx;
                this.y = ny;
                moveCount += 1;
                if (exit[0] == x && exit[1] == y) isIn = false;
                return;
            }
        }

        @Override
        public int compareTo(Person op) {
            if (!this.isIn && op.isIn) {
                return 1;
            }
            if (this.isIn && !op.isIn) {
                return -1;
            }
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