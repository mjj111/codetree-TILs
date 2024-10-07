// N*M개의 포탑
// 공격력이 줄거나 늘어남
// 정렬  10
// 20시 시작
// 포탄 정렬해주고 맨 앞단과 뒷단이 공격할 애와 공격당할 애다. 
// 레이저 빔 순차적으로 이동해서 접근 가능할 경우! 해당 경로로 빔쏜다. 
// 만약 안되면 포탄던져 
// 공격한 애랑 공격당한 애들은 경로 저장해놨다가 나머지애들 1씩 상승
import java.util.*;
import java.io.*;

public class Main {

    private static int N, M;

    private static int[][] board;
    private static Map<String, Port> portMap;

    private static int[] dx = {0,1,0,-1}; // 우/하/좌/상으로 우선
    private static int[] dy = {1,0,-1,0};

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        portMap = new HashMap<>();
        board = new int[N][M];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < M; j++) {
                int value = Integer.parseInt(st.nextToken());
                board[i][j] = value;
                if(value != 0) {
                    portMap.put(i+" "+j, new Port(i,j,value));
                }
            }
        }

        int count = 1;
        while(K-- > 0) {
            attack(count);
            hill(count);;
        }

        List<Port> portList = new ArrayList<>(portMap.values());
        Collections.sort(portList);
        System.out.println(portList.get(portList.size()-1).power);
    }
    private static void printBoard() {
        for(int  i = 0; i < N; i++) {
            for(int j = 0; j < M; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void attack(int count) {
        List<Port> portList = new ArrayList<>(portMap.values());
        Collections.sort(portList);

        Port attacker = portList.get(0);
        Port server = portList.get(portList.size()-1);

        attacker.power += N + M;
        board[attacker.x][attacker.y] = attacker.power;
        attacker.used = count;
        attacker.count = count;

        if(!attacker.laizer(attacker.x, attacker.y, server, count)) {
            attacker.portAttack(server, count);
        } 
    }

    private static void hill(int count) {
        for(Port p : portMap.values()) {
            if(p.count == count) continue;
            p.power += 1;
            board[p.x][p.y] = p.power;
        }
    }

    private static class Port implements Comparable<Port> {
        int x, y, power;
        int used = 0;
        int count = -1;

        public Port(int x, int y, int power) {
            this.x = x;
            this.y = y;
            this.power = power;
        }

        // 레이저는 쏘면 반대편으로도 간다. 
        // 우/하/좌/상으로 이동
        // 발견했다면 flag true로 더이상 움직이지 않는다. 
        // visited 된 곳 -1 제외 ㅎ 
        //  해당 좌표에 있는 포탄 조회해서 다 때려준다. 
        // count 변경  
        // return ;
        public boolean laizer(int startX, int startY, Port target, int count) {
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[N][M];
            int[][] parent = new int[N][M];
            
            queue.offer(new int[]{startX, startY});
            visited[startX][startY] = true;
            
            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int x = current[0], y = current[1];
                
                if (x == target.x && y == target.y) {
                    // 경로 찾음, 공격 수행
                    List<int[]> path = getPath(parent, startX, startY, target.x, target.y);
                    performLaserAttack(path, target, count);
                    return true;
                }
                
                for (int i = 0; i < 4; i++) {
                    int nx = (x + dx[i] + N) % N;
                    int ny = (y + dy[i] + M) % M;
                    
                    if (!visited[nx][ny] && board[nx][ny] != 0) {
                        queue.offer(new int[]{nx, ny});
                        visited[nx][ny] = true;
                        parent[nx][ny] = i;
                    }
                }
            }
            
            return false; // 경로를 찾지 못함
        }
        
        private List<int[]> getPath(int[][] parent, int startX, int startY, int targetX, int targetY) {
            List<int[]> path = new ArrayList<>();
            int x = targetX, y = targetY;
            
            while (!(x == startX && y == startY)) {
                path.add(0, new int[]{x, y});
                int direction = parent[x][y];
                x = (x - dx[direction] + N) % N;
                y = (y - dy[direction] + M) % M;
            }
            
            return path;
        }
        
        private void performLaserAttack(List<int[]> path, Port target, int count) {
            for (int[] pos : path) {
                int attackedX = pos[0], attackedY = pos[1];
                String key = attackedX + " " + attackedY;
                Port attacked = portMap.get(key);
                if (attackedX == x && attackedY == y) continue; // 본인은 공격 안받음

                int attackPower = (attackedX == target.x && attackedY == target.y) ? power : power / 2;
                attacked.power -= attackPower;
                attacked.count = count;
                board[attackedX][attackedY] = attacked.power;

                // 포탑이 파괴되었는지 확인
                if (attacked.power <= 0) {
                    board[attackedX][attackedY] = 0;
                    portMap.remove(key);
                }
            }
        }


        public void portAttack(Port target, int count) {
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++){
                    int attackedX = (target.x + i + N) % N;
                    int attackedY = (target.y + j + M) % M;

                    if(attackedX == x && attackedY == y) continue; // 공격자라면 지나감
                    if(board[attackedX][attackedY]== 0) continue; // 죽은 포탑 지나감 
                    
                    Port attacked = portMap.get(attackedX + " " + attackedY);
                     
                    int attackPower = (i==0 && j==0) ? power : power/ 2; 
                    attacked.power -= attackPower;
                    attacked.count = count;

                    if(attacked.power <= 0) { // 포탑이 죽으면 board 0으로 표시하고 Map에서 지움 
                        board[attackedX][attackedY] = 0;
                        portMap.remove(attackedX + "" + attackedY);
                    }
                }
            }
        }

        @Override
        public int compareTo(Port op) {
            if(this.power == op.power) {
                if(this.used == op.used) {
                    if(this.x + this.y == op.x + op.y) {
                        return this.y - op.y;
                    }
                    return this.x + this.y - op.x + op.y;
                }
                return this.used - op.used;
            }
            return this.power - op.power;
        }
    }
}