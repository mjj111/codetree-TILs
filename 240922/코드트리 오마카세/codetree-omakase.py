# 10시 13분

#입력으로 주어지는 t 값은 모두 다르며,
# 오름차순으로 정렬되어 주어집니다.
#시간이 무한히 흘렀을 때 만들어진 모든 초밥은 손님에 의해 다 사라지며,
# 손님 역시 정확히 n개의 초밥을 먹고 코드트리 오마카세를 떠나게 됨을 가정해도 좋습니다.

# L개 의자
# 10시 13분
#x = 0..L-1
#처음에는 암도 없음
# 시계방향 회전

#(1) 주방장의 초밥 만들기
# 100 t x name
#시각 t에 초밥 회전이 일어난 직후 위치 x 앞에 있는 벨트 위에
# name 이름을 부착한 회전 초밥을 하나 올려놓습니다
#같은 위치에 여러 회전 초밥이 올라갈 수 있으며
# 자신의 이름이 적혀 있는
# 초밥이 같은 위치에 여러 개 놓여 있는 것 역시 가능

#(2) 손님 입장 (사람이 없음을 가정)
# 200 t x name n
#name인 사람이 시각 t에 초밥 회전이 일어난 직후 위치 x에 있는 의자로 가서 앉습니다.
#x 앞으로 오는 초밥들 중
# 자신의 이름이 적혀있는 초밥을 정확히 n개를 먹고 자리를 떠납니다.
#자리에 착석하는 즉시 먹음
#동시에 여러 개 가능 시간이 소요 x

# T- 현재 시간만큼 레일을 돌린다그리고 먹는다.
# 10시 13분

import collections

class Rail :
    # def dishes(self) :
    #     for i in range(self.size):
    #         print(f"접시 위치 {i}")
    #         for name,count in self.sits[i].items():
    #             print(f"{name} {count}")

    def __init__(self, size):
        self.size = size
        self.now_index = 0
        self.sits = []
        for i in range(size):
            self.sits.append(collections.defaultdict(int))

    def get_foods(self, x) :
        location = abs(x - self.now_index)% self.size 
        return self.sits[location]

    def put(self, x, name):
        location = abs(x - self.now_index)% self.size 
        self.sits[location][name] += 1

    def getCount(self) :
        count = 0
        for sit in self.sits:
            for value in sit.values():
                if value > 0: count += value 
        return count

class Clinet :
    def __init__(self, x, name, n):
        self.x = x
        self.name = name
        self.n = n

def rotate(t):
    global now
    rotate_amount = t - now

    for _ in range(rotate_amount):
        rail.now_index += 1
        # if(now >= 9) :
        #     print(f"{now} -------")
        #     rail.dishes()
        done_clinet = []
        for client in clients:
            foods = rail.get_foods(client.x)

            while foods[client.name] != 0 :
                client.n -= 1
                foods[client.name] -= 1

            if foods[client.name] == 0:
                del foods[client.name]

            if client.n == 0 :
                done_clinet.append(client)

        for client in done_clinet:
            clients.remove(client)
    now = t

def offer_food(t,x,name) :
    rotate(t)
    rail.put(x,name)
    done_clinet = []
    for client in clients:
        foods = rail.get_foods(client.x)

        while foods[client.name] != 0 :
            client.n -= 1
            foods[client.name] -= 1

        if foods[client.name] == 0:
            del foods[client.name]

        if client.n == 0 :
            done_clinet.append(client)

    for client in done_clinet:
        clients.remove(client)

   
def sit(t, x, name, n):
    rotate(t)
    new_clinet = Clinet(x, name, n)
    clients.add(new_clinet)

    foods = rail.get_foods(x)
    while foods[new_clinet.name] != 0 :
        new_clinet.n -= 1
        foods[new_clinet.name] -= 1
        
    if foods[new_clinet.name] == 0:
        del foods[new_clinet.name]

    if new_clinet.n == 0:
        clients.remove(new_clinet)


def take_picture(t) :
    rotate(t)
    print(f"{len(clients)} {rail.getCount()}")

L, Q = map(int,input().split())
clients = set()
rail = Rail(L)
now = 0

for _ in range(Q) :
    line = list(input().split())
    commend = int(line[0])
    
    if commend == 100 :
        t = int(line[1])
        x = int(line[2])
        name = line[3] 
        
        offer_food(t,x,name)
        
    if commend == 200 :
        t = int(line[1])
        x = int(line[2])
        name = line[3]
        n = int(line[4])
        
        sit(t,x,name,n)

    if commend == 300 :
        t = int(line[1])
        
        take_picture(t)