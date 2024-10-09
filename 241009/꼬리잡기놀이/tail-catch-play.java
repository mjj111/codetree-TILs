// 40분동안 수도코드 작성
// 5시 17분 시작 

//n * n 격자
//3명 이상이 한 팀
//각 팀은 게임에서 주어진 이동 선을 따라서만 이동합니다. 
//각 팀의 이동 선은 끝이 이어져있습니다. 각 팀의 이동 선은 서로 겹치지 않습니다.
//게임은 라운드 별로 진행

//1. 머리 사람을 따라서 한 칸 이동

//2. 각 라운드마다 공이 정해진 선을 따라 던져집니다.
// 1부터 n 행 오른쪽 방향
// n +1 부터 2n 열 윗방향   
// 2n + 1 부터 3n 행 왼쪽 방향 
// 3n + 1 부터 4n 열 아래방향 

// 3. 공을 던졌을 때 최초 맞은 사람은 점수획득 
// 3.1 점수는 머릿사람을 시작으로 팀내의 K 번째 사람이라면 K 제곱만큼 점수 획득
// 3.2 획득시에 팀의 머릿사람과 꼬릿사람이 바뀐다. (방향이 바뀜)
// 각 팀이 획득한 점수의 총합을 구하는 프로그램을 구하세요.


// 0은 빈칸, 1은 머리사람, 2는 머리사람과 꼬리사람이 아닌 나머지, 3은 꼬리사람, 4 는  이동선
// 1인 머리 사람들의 위치를 팀 단위로 따로 저장해놓는다. Map<팀 번호, 머리사람 위치xy 꼬리사람 위치,길이> 
// 각 팀마다 머리 사람들 위치로 시작해서 길이를 측정하고 갱신한다. dfs로 4를 만날떄까지 길이를 측정한다. 
// board는 만난 영역은 4로 바꿔주고 peopleBoard는 각 숫자로 갱신해준다. 

// 1. 머리 사람을 따라서 한 칸 이동
// 머리사람은 4인 경로를 찾아, 해당 위치부터 반대로 길이만큼 이동하며 peopleBoard에숫자를 매겨준다. 
// 마지막은 0으로 매겨줘야한다.(꼬리 삭제) 

// 2. 각 라운드마다 공이 정해진 선을 따라 던져집니다.
// 해당 라운드에 적용되는 라인을 찾는다. 
// x,y 좌표 dir(하우상좌), binDir(우상좌우)
// 전달할꺼 따로 저장해놓고
// dir로 이동한다.
// 만약 벽에 부딪히면 bimDir, dir 90도 회전

// 3. 공을 던졌을 때 최초 맞은 사람은 점수획득 
// 해당bimDir 방향으로 bim을 계속 움직인다. 
// while(isIn(x,y)) 인 동안 반복해서 bim을 움직인다.
// 만약 만날경우! (!!!)break; 
// !!! peopleBoard에서 만난 녀석의 값 Math.pow(값, 2) 줄 점수를 계산한다.
// 녀석의 꼬리 혹은 머리를 탐색한다.
// 탐색했다면 해당 팀의 머리와 꼬리를 바꾼다. 
// 해당 팀의 점수를 추가해준다.  

import java.util.*;
public class Main {

    private static int N, M, K;

    private static int[][] board;    
    private static int[][] peopleBoard;
    private static boolean[][] visited;

    private static Map<Integer,Team> teamMap;

    private static int[] dx = {1,0,-1,0};//(하우상좌)
    private static int[] dy = {0,1,0,-1};

    private static int[] line = new int[3];
    private static Team foundTeam;

    public static void main(String[] args) {
        line = new int[]{0,0,0};
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt(); //격자의 크기 n
        M = sc.nextInt(); // 팀의 개수 m
        K = sc.nextInt(); //라운드 수 k

        // 1인 머리 사람들의 위치를 팀 단위로 따로 저장해놓는다.
        List<int[]> heads = new ArrayList<>();

        board = new int[N][N];
        peopleBoard = new int[N][N];

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                int value = sc.nextInt();
                board[i][j] = value;

                if(value == 1) {
                    heads.add(new int[]{i,j});
                }
            }
        }

        // 팀 단위로 저장한다. Map<팀 번호, Team()머리사람 위치xy 꼬리사람 위치,길이> 
        teamMap = new HashMap<>();
        for(int i = 0; i < heads.size(); i++) {
            int[] head = heads.get(i);
            teamMap.put(i, new Team(head));

            findTail(i,head,1);
        }

        int round = 0;
        while(K-- > 0) {
            round ++;
            peopleMove();
            ballAttack();
            // printPeople(round);
        }

        int answer =0;
        for(Team t : teamMap.values()) {
            answer += t.point;
        }
        System.out.println(answer);
    }

    private static void printPeople(int round) {
        System.out.println("현재 "+ round + "라운드");
        for(int i =0; i < N; i++) {
            System.out.println();
            for(int j = 0; j < N; j++) {
                System.out.print(peopleBoard[i][j] + " ");
            }
        }
        System.out.println();
    }

    // 2. 각 라운드마다 공이 정해진 선을 따라 던져집니다.
    private static void ballAttack() {
        // 해당 라운드에 적용되는 라인을 찾는다. 
        int x = line[0];
        int y = line[1];
        int vDir = (line[2] + 1) % 4;


        // 3. 공을 던졌을 때 최초 맞은 사람은 점수획득 
        while(!isOut(x,y)) {
            int value = peopleBoard[x][y];
            if(value != 0) {
                //peopleBoard에서 만난 녀석의 값 Math.pow(값, 2) 줄 점수를 계산한다.
                int plusScore =(int)Math.pow(value, 2);
                // 녀석의 팀을 탐색한다.
                visited = new boolean[N][N];
                findTeam(x,y);
                // 탐색했다면 해당 팀의 머리와 꼬리를 바꾼다. 
                foundTeam.change();

                // 해당 팀의 점수를 추가해준다.  
                foundTeam.point += plusScore;
                break;
            }
            // 해당bimDir 방향으로 bim을 계속 움직인다. 
            x += dx[vDir];
            y += dy[vDir];
        }

        //전달할꺼 따로 저장해놓고 dir로 이동한다
        moveLine();
        
    }

    private static void findTeam(int x, int y) {
        int value = peopleBoard[x][y];

        if(value == 1|| value == 3) {

            // 머리 찾음
            if(value == 1) {
                for(Team t : teamMap.values()) {
                    if(t.headX == x && t.headY == y) {
                        foundTeam = t;
                    }
                }
            }

            // 꼬리찾음 
            if(value == 3) {
                for(Team t : teamMap.values()) {
                    if(t.tailX == x && t.tailY == y) {
                        foundTeam = t;
                    }
                }
            }
            return;
        }

        visited[x][y] = true;

        for(int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if(isOut(nx,ny)) continue; 
            if(visited[nx][ny]) continue;

            if(peopleBoard[nx][ny] != 0) {
                findTeam(nx, ny);
            }
        }
    }

    private static void moveLine() {
        // System.out.println("디버깅");
        int xLine = line[0];
        int yLine = line[1];
        int dir = line[2];
        int nx = xLine + dx[dir];
        int ny = yLine + dy[dir];
        // 만약 벽에 부딪히면 dir 90도 회전
        if(isOut(nx,ny)) {
            line[0] = (line[0] + 1) % 4;
            nx = xLine;
            ny = yLine;
        }

        // dir로 이동한다.
        line[0] = nx;
        line[1] = ny;
    }


    // 1. 머리 사람을 따라서 한 칸 이동
    private static void peopleMove() {
        for(Team t : teamMap.values()) {

            int x = t.headX;
            int y = t.headY;

            for(int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i]; 

                if(isOut(nx,ny))continue;
                // 머리사람은 4인 경로를 찾아,
                if(board[nx][ny] == 4 && peopleBoard[nx][ny] == 0) {
                    //해당 위치부터 반대로 길이만큼 이동하며 peopleBoard에숫자를 매겨준다. 
                    //마지막은 0으로 매겨줘야한다.(꼬리 삭제) 
                    visited = new boolean[N][N];
                    t.move(nx,ny,1);
                }
            }
        }
    }

    // 각 팀마다 머리 사람들 위치로 시작해서 길이를 측정하고 갱신한다. 
    private static void findTail(int teamNumber, int[] now, int len) {
        int x = now[0];
        int y = now[1];
        int nowValue = board[x][y];

        //board는 만난 영역은 4로 바꿔주고 peopleBoard는 각 숫자로 갱신해준다. 
        board[x][y] = 4;
        peopleBoard[x][y] = len;

        //dfs로 3를 만날떄까지 길이를 측정한다. 
        if(nowValue == 3) {
            // 팀 단위로 저장한다. Map<팀 번호, Team()머리사람 위치xy 꼬리사람 위치,길이> 
            Team nowTeam = teamMap.get(teamNumber);
            nowTeam.tailX = x;
            nowTeam.tailY = y;
            nowTeam.len = len;
            return;
        }

        for(int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if(isOut(nx,ny)) continue;
            if(peopleBoard[nx][ny] != 0) continue;
            if(board[nx][ny] == 0 || board[nx][ny] == 4) continue;
            
            findTail(teamNumber, new int[]{nx,ny}, len + 1);
            
        }
    }

    private static boolean isOut(int x, int y) {
        return x < 0 || x >= N || y < 0 || y >= N;
    }

    private static class Team {
        int headX, headY, tailX, tailY, len;
        int point = 0;

        public void change() {
            int tmpX = headX;
            int tmpY = headY;
            headX = tailX;
            headY = tailY;

            tailX = tmpX;
            tailY = tmpY;
        }

        public Team(int[] point) {
            headX = point[0];
            headY = point[1]; 
        }

        //해당 위치부터 반대로 길이만큼 이동하며 peopleBoard에숫자를 매겨준다. 
        public void move(int x, int y, int count) {
            if(count > len) return;
            if(count == 1) {
                headX = x;
                headY = y;
            }
            if(count == len) {
                //마지막은 0으로 매겨줘야한다.(꼬리 삭제) 
                peopleBoard[tailX][tailY] = 0;
                tailX = x;
                tailY = y;
            }

            peopleBoard[x][y] = count;
            visited[x][y] = true;
            for(int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i]; 

                if(isOut(nx,ny)) continue; 
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 4 && peopleBoard[nx][ny] >= 1) {
                    move(nx,ny, count + 1);
                }
            }
        }            
    }
}