# 11시 7분

class Node :
    def __init__(self, number):
        self.number = number
        self.parent = None
        self.left = None
        self.right = None
        self.authority = 0
        self.notification = True

    def child_change(self, old_child, new_child):
        if self.left == old_child:
            self.left = new_child
        else:
            self.right = new_child

    def put_parent(self, parent):
        self.parent = parent
        
    def put_authority(self, authority):
        self.authority = authority

    def put_child(self, child):
        if self.left:
            self.right = child
        else :
            self.left = child 

    def notification_change(self):
        if self.notification :
            self.notification = False
        else :
            self.notification = True

def get_available_notification_count(node, count):
    if not node.left and not node.right:
        return count

    left_count = 0
    if node.left and node.left.authority >= count + 1 and node.left.notification:
        left_count = get_available_notification_count(node.left, count + 1)

    right_count = 0
    if node.right and node.right.authority >= count + 1 and node.right.notification:
        right_count = get_available_notification_count(node.right, count + 1)

    if right_count + left_count == 0 : return count
    return right_count + left_count   

root = Node(0)
root.put_parent(-1)

nodes_dict = {}
nodes_dict[0] = root 

n, q = map(int,input().split())
input_line = list(map(int,input().split()))

for i in range(1,n+1):
    nodes_dict[i] = Node(i)

parents = input_line[1:n+1]
for i in range(0,n):
    parent = nodes_dict[parents[i]]
    child = nodes_dict[i+1]

    parent.put_child(child)
    child.put_parent(parent)

authoritys = input_line[n+1:]
for i in range(0,n):
    nodes_dict[i+1].put_authority(authoritys[i])

while q > 1:
    q -= 1
    input_line = list(map(int, input().split()))
    commend = input_line[0]

    if commend == 200:
        target = input_line[1]
        target_node = nodes_dict[target]
        target_node.notification_change()

    if commend == 300:    
        target = input_line[1]
        authority = input_line[2]

        target_node = nodes_dict[target]
        target_node.put_authority(authority)

    if commend == 400:  
        target1 = input_line[1]
        target_node1 = nodes_dict[target1]

        target2 = input_line[2]
        target_node2 = nodes_dict[target2]

        target1_parent = target_node1.parent
        target2_parent = target_node2.parent

        target_node1.put_parent(target1_parent)
        target_node2.put_parent(target2_parent)  

        target1_parent.child_change(target_node1, target_node2)
        target2_parent.child_change(target_node2, target_node1)

    if commend == 500:
        target = input_line[1]
        target_node = nodes_dict[target]
        count = get_available_notification_count(target_node,0)
        print(count)