#Chris Brodski
#CS 2

from __future__ import print_function
import unittest

class my_hash_set:
    def __init__(self, init=None):
        self.__limit = 10
        self.__items = [None] * self.__limit
        self.__count = 0

        if init:
            for i in init:
                self.__setitem__(i[0], i [1])

    def __len__(self): return(self.__count)

    def __flattened(self):
        flattened = filter(lambda x : x != None, self.__items)
        flattened = [item for inner in flattened for item in inner]
        return(flattened)

    def __iter__(self): return(iter(self.__flattened()))

    def __str__(self): return (str(self.__flattened()))

    def __setitem__(self, key, value):
        h = hash(key) % self.__limit

#I added this if block in here.  B/c I think we were missing a statement to prevent duplicates, ie keep our dictionary as a set.
        #It just searches if the key already exists in the dictionary, if so then we send an empty return.
        if self.__items[h]:
            for i in range(len(self.__items[h])):
                if self.__items[h][i][0]==key:
                    return

        if not self.__items[h]:
            self.__items[h] = [[key, value]]
        else:
            self.__items[h].append([key, value])
        self.__count += 1

        if (0.0 + self.__count) / self.__limit > 0.75: self.__rehash()

    def __rehash(self):
        self.__limit = self.__limit * 2
        flat = self.__flattened()
        self.__items = [None] * self.__limit
        for i in range(len(flat)):
             self.__setitem__(flat[i][0], flat[i][1])
             self.__count = self.__count - 1

    def __contains__(self,key):
        h = hash(key) % self.__limit
        if self.__items[h]:
            for i in range(len(self.__items[h])):
                if self.__items[h][i][0] == key:
                    return True
     should    else:
            return False


    def __getitem__(self, key):
        h = hash(key) % self.__limit
        if self.__items[h]:
            for i in range(len(self.__items[h])):
                if self.__items[h][i][0] == key:
                    return self.__items[h][i][1]
        raise KeyError('that key does not exist')

    def __delitem__(self, key):
        h = hash(key) % self.__limit
        if self.__items[h]:
            for i in range(len(self.__items[h])):
                if self.__items[h][i][0] == key:
                    del self.__items[h]     #alternative, self.__items[h] = None
                    self.__count = self.__count - 1
                    return
        else:
            raise KeyError('that key does not exist')

class test_my_hash_set(unittest.TestCase):
    def test_empty(self):
        self.assertEqual(len(my_hash_set()), 0)
    def test_add_one(self):
        s = my_hash_set()
        s[1] = "one"
        self.assertEquals(len(s), 1)
        self.assertEquals(s[1], "one")
    def test_add_two(self):
        s = my_hash_set()
        s[1] = "one"
        s[2] = "two"
        self.assertEquals(len(s), 2)
    def test_add_twice(self):
        s = my_hash_set()
        s[1] = "one"
        s[1] = "one"
        self.assertEquals(len(s), 1)
    def test_remove(self):
        s = my_hash_set()
        s["one"] = 1
        del s["one"]

        try:
            del s["two"]
            self.assertTrue(False)
        except KeyError:
            pass

        self.assertEquals(len(s), 0)
    def test_one_in(self):
        s = my_hash_set()
        s["one"] = 1
        self.assertTrue("one" in s)
        self.assertFalse("two" in s)
        self.assertRaises(KeyError, lambda: s[0])
    def test_collide(self):
        s = my_hash_set()
        s[0] = "zero"
        s[10] = "ten"
        self.assertEquals(len(s), 2)
        self.assertTrue(0 in s)
        self.assertTrue(10 in s)
        self.assertFalse(20 in s)
    def test_rehash(self):
        s = my_hash_set()
        s[0] = "zero"
        s[10] = "ten"
        s[1] = "one"
        s[2] = "two"
        s[3] = "three"
        s[4] = "four"
        s[5] = "five"
        s[6] = "six"
        s[7] = "seven"
        s[8] = "eight"
        s[9] = "nine"
        s[11] = "eleven"
        self.assertEquals(len(s), 12)

        expected = \
'''[[0, 'zero'], [1, 'one'], [2, 'two'], [3, 'three'], \
[4, 'four'], [5, 'five'], [6, 'six'], [7, 'seven'], \
[8, 'eight'], [9, 'nine'], [10, 'ten'], [11, 'eleven']]'''
        self.assertEquals(str(s), expected)
        t = my_hash_set(s)
        self.assertEquals(str(t), expected)
    def test_store_false(self):
        s = my_hash_set()
        s[1] = False
        self.assertTrue(1 in s)
        self.assertFalse(s[1])
    def test_store_none(self):
        s = my_hash_set()
        s[1] = None
        self.assertTrue(1 in s)
        self.assertEquals(s[1], None)
    def test_none_key(self):
        s = my_hash_set()
        s[None] = 1
        self.assertTrue(None in s)
        self.assertEquals(s[None], 1)
    def test_False_key(self):
        s = my_hash_set()
        s[False] = 1
        self.assertTrue(False in s)
        self.assertEquals(s[False], 1)


if '__main__' == __name__:

    unittest.main()
'''
    s = my_hash_set()
    s[1] = "one"
    s[2] = "two"
    s = []
    s.append([1, 2])
    s.append(["two",'to'])
    s.append('poopy')
    s.append(None)
    print(s)
   # flattened = [item for inner in s for item in inner]
   # print(flattened)
   # flattened = [item for item in s]
   # print(flattened)

     for i in range(self.__limit-1): #self.__items[0] = [ [k,v] [k2,v2] ]
            if self.__items[i] != None:
                for k in range(len(self.__items[i])-1):
                    self.__items[i][k][0] == key
            if self.__items[0][i][0] == key: #self.__items[0][0][i] = [k,v]
                return True '''

