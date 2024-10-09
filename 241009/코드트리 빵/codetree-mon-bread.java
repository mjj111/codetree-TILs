//12 시 시작 
// 빵구하는 사람 M명
// 1번 사람 1 분 M번 사람 M분에 베이스캠프 출발
// 출발 전에는 격자 밖, 목표 편의점이 다름
// n*n 크기 격자

// 3가지 행동은 총 1분 동안 진행되며, 1 2 3 순으로 진행되야한다. 
// 1. 편의점 방향으로 1 칸 이동(최단거리) 상좌우하 우선순위로 움직임 

// 2. 도착하면 편의점에서 멈추고, 다른 사람들은 해당 편의점 지나갈 수 없음
// 2.1 모두 이동한 뒤에 해당 칸에 지나갈 수 없게된다.

// 3. 현재 t분 t <= m을 만족한다면 t 번 사람은 자신이 가고 싶은 편의점과 가장 가까운 베이스캠프에 들어감
// 3.1 베이스캠프 갈때 2.때문에 못지나가려나? 
// 3.2 편의점과 가장 가까운 베이스캠프가 여러개라면 행이 작고, 같다면 열이작은 캠프로 이동
// 3.3 t번 사람이 베이스 캠프 이동시에는 시간이 소요되지 않음 
// 3.4 이때부터 다른 사람들은 베이스캠프 지나갈 수 없음 
// 3.5 t번 사람이 편의점 향해 시작했더라도 해당 베이스캠프 앞은 이동할 수 없다.
// 3.6 해당 턴 격자 사람 모두 이동한 뒤에 해당 칸을 지나갈 수 없어진다. 

// 격자내에 사람이 없으면 1,2번 행동 안하고 3번 부터시작 
// 동일한 칸에 둘 이상의 사람이 위치가 가능하다. 

// N과 M을 입력받는다. 사람은 100명이다.
// N만큼 board를 입력 받는다.
// 여기서 1인 경우는 캠프이기 때문에 x,y 좌표를 따라 저장해준다. 최대 100개다. (Set에 넣어준다.나중에 리스트에 넣고 정렬해야한다.)
// 캠프는 x 혹은 y 좌표로 정렬되어야한다. 
// N 번 사람들이 가고싶은 좌표와 id를 사람에게 넣어준다. MAP

// 사람을 담은 곳에 Map의 keySet 사이즈가 1이상인 동안 동작한다. 

// 1. 사람을 이동시킨다. 
// 1.1 그래프 탐색을 통해 가장 가까운 편의점으로 가는 경로를 찾는다. 
// 1.2 찾았다면 역탐색하여 해당 경로의 첫번째 값으로 움직인다. 

// 2. 도착하면 편의점을 더이상 지나갈 수 없게 해주고 사람 목록에서 지운다. 
// 사람들 중에 현재 위치와 본인 편의점 위치가 같은 사람들을 찾는다.
// 찾은 경우 해당 위치는 더 이상 가지 못하는 곳으로 표시한다. (이미 해당 위치에 이동했더라도 괜찮다.)

// 3. 베이스캠프에 넣어준다. 
// t분에 사람이 있다면 해당 사람의 편의점 위치와 가장 가까운 캠프를 찾는다. 출발한 적 있는 캠프는 사용하지 못한다. 
// 해당 캠프에 사람을 넣어준다.
// 더 이상 접근하지 못하는 캠프로 지워준다.


// 정답 = -1;
// 사람마다 동작 가장 가까운 캠프 선정, 캠프까지의 거리 계산, 현재 id + 거리 와 정답 갱신 
import java.util.*;
import java.io.*;

public class Main {
    private static int targetX, targetY;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        
        List<Camp> camps = new ArrayList<>();
        int[][] board = new int[N][N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
                if(board[i][j] == 1) {
                    camps.add(new Camp(i,j));
                }
            }
        }

        List<int[]> target = new ArrayList<>(); 
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine()); 
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            target.add(new int[]{a-1,b-1});
        }

        int answer = -1;
        for(int i = 0; i < M; i++) {
            int[] nowTarget = target.get(i);
            targetX = nowTarget[0];
            targetY = nowTarget[1];

            for(Camp c : camps) c.distance = getDistacne(c.x, c.y);
            Collections.sort(camps);
            Camp clost = camps.get(0);

            int distance = getDistacne(clost.x ,clost.y);
            answer = Math.max((distance + i + 1), answer);
            camps.remove(0);
        }

        System.out.println(answer);
    }

    private static int getDistacne(int x, int y) {
        return Math.abs(targetX - x) +  Math.abs(targetY - y);
    }

    private static class Camp implements Comparable<Camp> {
        int x,y;
        int distance =0;

        public Camp(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // 최단 거리, 작은 X, 작은 Y
        @Override 
        public int compareTo(Camp op) {
            if(this.distance == op.distance) {
                if(this.x == op.x) {
                    return this.y - op.y;
                }
                return this.x - op.x;
            }
            return this.distance - op.distance;
        }
    }
}