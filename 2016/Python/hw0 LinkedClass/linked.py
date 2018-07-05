#Chris Brodski
#
#Homework 1 + extra credit, Linked List

import unittest

class linked_list:
    front = rear = None

    class node:
        def __init__(self, value, next):
            self.value = value
            self.next = next
    def empty(self):
        if self.front is None:
            return True
        else:
            return False
    def push_front(self, value):
        x = self.node(value, self.front)
        self.front = x
        if self.rear is None:
            self.rear = x
    def pop_front(self):
        if self.empty():
            raise RuntimeError("the list is empty, can't pop front") #If you use just a return statement (commented out below) the program will not stop when someone mistakenly tries to pop an empty list. But you know, I guess we got those tests.
            #print("list is empty")
            #return
        else:
            poppedValue = self.front.value
            self.front = self.front.next
            if self.front is None:
                self.rear = None
            return poppedValue
    def push_back(self, value):
        if self.empty():
            self.front = self.rear = self.node(value, None)
        else:
            x = self.node(value, None)
            self.rear.next = x
            self.rear = x
    def pop_back(self):
        if self.empty():
            raise RuntimeError("the list is empty, can't pop_back")
            #return
        elif self.front == self.rear:
            poppedValue = self.front.value
            self.front = self.rear = None
            return poppedValue
        else:
            poppedValue = self.rear.value
            x=self.front
            while x.next is not self.rear:
                x = x.next
            x.next = None
            self.rear = x
            return poppedValue
#-----------------------------
#   Extra Credit below
#-----------------------------
    def __str__(self):
        s = "["
        x = self.front
        while x:
            s = s + str(x.value)
            x = x.next
            if x:
                s = s +', '
        s = s + ']'
        return s
    def __repr__(self):
        s = '"linked_list(['
        x = self.front
        while x:
            s = s + repr(x.value)
            x = x.next
            if x:
                s = s +', '
        s = s + ']"'
        return s

    def __init__(self, userInput= None):
        if userInput:
            for i in userInput:
                self.push_back(i)


class test_linked_list(unittest.TestCase):
    def test_none(self):
        self.assertTrue(linked_list().empty())
    def test_pop_front_empty(self):
        self.assertRaises(RuntimeError, lambda: linked_list().pop_front())
    def test_pop_back_empty(self):
        self.assertRaises(RuntimeError, lambda: linked_list().pop_back())
    def test_pust_back_pop_front(self):
        ll = linked_list()
        ll.push_back(1)
        ll.push_back(2)
        ll.push_back(3)
        self.assertFalse(ll.empty())
        self.assertEquals(ll.pop_front(),1)
        self.assertEquals(ll.pop_front(),2)
        self.assertEquals(ll.pop_front(),3)
        self.assertTrue(ll.empty())
    def test_push_front_pop_front(self):
        ll = linked_list()
        ll.push_front(1)
        ll.push_front(2)
        ll.push_front(3)
        self.assertEquals(ll.pop_front(),3)
        self.assertEquals(ll.pop_front(),2)
        self.assertEquals(ll.pop_front(),1)
        self.assertTrue(ll.empty())
    def test_push_front_pop_back(self):
        ll = linked_list()
        ll.push_front(1)
        ll.push_front(2)
        ll.push_front(3)
        self.assertFalse(ll.empty())
        self.assertEquals(ll.pop_back(), 1)
        self.assertEquals(ll.pop_back(), 2)
        self.assertEquals(ll.pop_back(), 3)
        self.assertTrue(ll.empty())
    def test_push_back_pop_back(self):
        ll = linked_list()
        ll.push_back(1)
        ll.push_back("foo")
        ll.push_back([3, 2, 1])
        self.assertFalse(ll.empty())
        self.assertTrue(ll.pop_back(), [3,2,1])
        self.assertTrue(ll.pop_back(), "foo")
        self.assertTrue(ll.pop_back(), 1)
        self.assertTrue(ll.empty())

class factorial:
    def fact(self, a):
        if a < 0: raise ValueError("Less than zero")
        if a == 0 or a == 1: return 1
        stack = linked_list()
        while a>1:
            stack.push_front(a)
            a = a - 1
        result = 1
        while not stack.empty():
            result = result * stack.pop_front()
        return result



class test_factorial(unittest.TestCase):
    def test_less_than_zero(self):
        self.assertRaises(ValueError, lambda: factorial().fact(-1))
    def test_zero(self):
            self.assertEquals(factorial().fact(0),1)
    def test_one(self):
            self.assertEquals(factorial().fact(1),1)
    def test_two(self):
            self.assertEquals(factorial().fact(2),2)
    def test_10(self):
            self.assertEquals(factorial().fact(10), 10*9*8*7*6*5*4*1*2*3)


class test_extraCredit:
    def test_str():
        word1 = 'one'
        word2 = 'two'
        x3 = 32
        x4 = x3
        ll = linked_list()
        ll.push_front(word1)
        ll.push_front(word2)
        ll.push_front(x3)
        ll.push_front(x4)
        ll.push_front(x4)

        print(ll.__str__())
        print(ll.__repr__())
        #ll2 =  linked_list([32,32,32,'two','one'])
        #print(ll2.__str__())
        #print(ll2.__repr__())
        #eval( "linked_list([32,32,32,'two','one'])" )
        print(eval(ll.__repr__()) == ll) #Doesn't work :(
        print(eval(ll.__repr__()) == eval(ll.__repr__()))
        
        
if '__main__' == __name__:
    print(factorial().fact(1))
    print(factorial().fact(2))
    print(factorial().fact(100))
#    test_extraCredit.test_str() #my quickly made 'tests' for extra credit. 
