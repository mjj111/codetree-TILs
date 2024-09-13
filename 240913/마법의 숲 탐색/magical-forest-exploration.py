from collections import deque

dx = [-1, 0, 1, 0]  # 상, 좌, 하, 우
dy = [0, -1, 0, 1]

n, m, K = map(int, input().split())
a = [[0] * m for _ in range(n)]
ans = 0

def getExit(x, y, d):
    if d == 0:  # 상
        return [x - 1, y]
    elif d == 1:  # 우
        return [x, y + 1]
    elif d == 2:  # 하
        return [x + 1, y]
    else:  # 좌
        return [x, y - 1]

def inBoard(nx, ny):
    return 0 <= nx < n and 0 <= ny < m

def check(x, y):
    if not inBoard(x, y):  
        if x < n and 0 <= y < m:  
            return True
    else: 
        if a[x][y] == 0:  
            return True
    return False

def move(c, d, no):
    global a

    x, y = -2, c  
    while True:
        # 골렘 수직 이동
        if check(x + 2, y) and check(x + 1, y - 1) and check(x + 1, y + 1):
            x += 1
        # 골렘 왼쪽 이동
        elif check(x + 1, y - 1) and check(x - 1, y - 1) and check(x, y - 2) and check(x + 1, y - 2) and check(x + 2, y - 1):
            x += 1
            y -= 1
            d = (d - 1) % 4
        # 골렘 오른쪽 이동
        elif check(x + 1, y + 1) and check(x - 1, y + 1) and check(x, y + 2) and check(x + 1, y + 2) and check(x + 2, y + 1):
            x += 1
            y += 1
            d = (d + 1) % 4
        else:
            break

    # 골렘의 최종 위치를 지도에 표시
    if not inBoard(x, y) or not inBoard(x + 1, y) or not inBoard(x - 1, y) or not inBoard(x, y + 1) or not inBoard(x, y - 1):
        return [False, -1, -1]
    else:
        a[x][y] = a[x + 1][y] = a[x - 1][y] = a[x][y + 1] = a[x][y - 1] = no
        ex, ey = getExit(x, y, d)  # 출구 위치 계산
        a[ex][ey] = -no
        return [True, x, y]

# 정령의 이동을 BFS로 처리하는 함수
def bfs(sx, sy, no):
    global ans

    cand = []
    q = deque()
    q.append((sx, sy))
    visit = [[False] * m for _ in range(n)]
    visit[sx][sy] = True

    while q:
        x, y = q.popleft()
        for k in range(4):
            nx, ny = x + dx[k], y + dy[k]
            if not inBoard(nx, ny) or visit[nx][ny] or a[nx][ny] == 0:
                continue
            # 절댓값이 같은 칸으로 움직이거나, 출구 칸에서 다른 칸으로 이동 가능
            if abs(a[x][y]) == abs(a[nx][ny]) or (a[x][y] < 0 and abs(a[nx][ny]) != abs(a[x][y])):
                q.append((nx, ny))
                visit[nx][ny] = True
                cand.append(nx)

    cand.sort(reverse=True)
    point = cand[0] + 1
    return point

# 메인 로직
for no in range(1, K + 1):
    c, d = map(int, input().split())
    c -= 1

    res = move(c, d, no)
    inBound, x, y = res

    if inBound:
        ans += bfs(x, y, no)
    else:
        a = [[0] * m for _ in range(n)]

print(ans)