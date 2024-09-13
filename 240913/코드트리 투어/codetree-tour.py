# 21시 35분 
# N 개의 도시, M개의 간선
# 도시 번호 0 --> n-1 /
# 간선은 방향성을 같지 않음 , 여러개 존재할 수 있고 자신을 향할 수 도있음
# 여행 상품의 출발지를 하나로 통일 0 번!

# 5가지 명령

# 1. 다리 건설 -> 다 연결하면 0부터 다익스트라로 값 계산 
#  1도시와 2도시는 3가중치 간선으로 이뤄졌다는 것

# 2. 여행 상품 생성(3만번) -> 현재 주어진 다익스트라 결과로 상품을 만든다. 
# 1고유식별자로 2매출 3도착지

# 3. 여행 상품 취소(3만번) dict에서 찾아서 판매하지 않음으로 클래스 변수 변경
# 1고유식별자에 해당하는 여행상품을 목록에서 삭제

# 4. 최적의 여행 상품 판매(3만번) 관리 목록은 heap과 dict로 관리한다.  -> 만약 뽑은 상품이 판매하지 않음이라면 버려버린다.  
# 이득 - 비용이 최대치인 상품우선 고려(+id 오름차순)
# 비용은 출발지로부터 id 상품의 도착지 까지 최단거리 
# 만약 목적지에 도달하는게 불가능하거나 비용이 이득보다 크다면 판매불가
# 판매 가능한 상품중 가장 우선순위가 높은 1개를 판매하여 해당 id상품 삭제 
# 만약 없다면 -1 출력하고 제거 x 

# 5. 상품 출발지 변경(15번) 출발지부터 다시 다익스트라로 값 계산 후 상품 다시 갱신/ 만약 클래스가 판매하지 않음이라면 스킵 
# 모든 출발지를 s로 변경 (변경함에 따라 각 상품의 비용이 변경된다)

# Q번에 걸쳐 명령(10만번)
# 1≤n≤2,000
# 1≤m≤10,000

import heapq
from collections import defaultdict
import itertools

INF = float('inf')


class TravelManager:
    def __init__(self, n):
        self.n = n  # 도시의 수
        self.graph = defaultdict(list)  # 도시 간 간선 정보
        self.distances = [INF] * n  # 0번 도시부터의 최단 거리
        self.products = {}  # 여행 상품 정보 (고유식별자 -> (매출, 도착지))
        self.unavailable = set()  # 판매되지 않는 상품의 고유식별자
        self.heap = []  # 최적의 상품 판매를 위한 힙

    def add_road(self, u, v, cost):
        self.graph[u].append((v, cost))
        self.graph[v].append((u, cost))  # 양방향 간선 추가

    def dijkstra(self, start):
        self.distances = [INF] * self.n
        self.distances[start] = 0
        pq = [(0, start)]

        while pq:
            current_dist, current_node = heapq.heappop(pq)
            if current_dist > self.distances[current_node]:
                continue

            for neighbor, weight in self.graph[current_node]:
                distance = current_dist + weight
                
                if distance < self.distances[neighbor]:
                    self.distances[neighbor] = distance
                    heapq.heappush(pq, (distance, neighbor))

    def add_product(self, product_id, revenue, destination):
        self.products[product_id] = (revenue, destination)
        cost = self.distances[destination]
        heapq.heappush(self.heap, (-(revenue - cost), product_id))  # 최대 힙 구현

    def cancel_product(self, product_id):
        if product_id in self.products:
            self.unavailable.add(product_id)

    def sell_best_product(self):
        while self.heap:
            profit, product_id = heapq.heappop(self.heap)
            
            if product_id in self.unavailable:
                continue  # 판매하지 않는 상품은 무시

            revenue, destination = self.products[product_id]
            cost = self.distances[destination]
            if cost == INF or cost > revenue:
                print(-1)
                return

            print(product_id)
            del self.products[product_id]
            return
        print(-1)

    def change_departure(self, new_start):
        self.dijkstra(new_start)
        self.heap = []
        for product_id, (revenue, destination) in self.products.items():
            if product_id not in self.unavailable:
                cost = self.distances[destination]
                heapq.heappush(self.heap, (-(revenue - cost), product_id))


def main():
    q = int(input())
    input_line = list(map(int, input().split()))

    n, m = input_line[1], input_line[2]
    manager = TravelManager(n)
    creations = input_line[3:]

    for i in range(0,len(creations),3):
        creation = creations[i:i+3]
        a = creation[0]
        b = creation[1]
        cost = creation[2]
        manager.add_road(a, b, cost)  
    manager.dijkstra(0)

    for _ in range(q-1):
        input_line = list(map(int, input().split()))
        commend = input_line[0]
        
        if commend == 200:
            product_id, revenue, destination = input_line[1], input_line[2], input_line[3]
            manager.add_product(product_id, revenue, destination)

        elif commend == 300:
            product_id = input_line[1]
            manager.cancel_product(product_id)

        elif commend == 400:
            manager.sell_best_product()

        elif commend== 500:
            new_start = input_line[1]
            manager.change_departure(new_start)

if __name__ == "__main__":
    main()