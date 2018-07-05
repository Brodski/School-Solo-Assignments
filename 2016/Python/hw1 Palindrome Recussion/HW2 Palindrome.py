#Chris Brodski
# Homework 2, Recursion

import unittest

''' This method removes all spaces from some text. The method accepts only strings.  '''
def remove_spaces(text):
    if text == None: #Judging by 'test_none' below, if text=None then we want false returned.
        return None
    if type(text) != str:
        raise TypeError("The data type isn't a string")
    if len(text) > 1:
        if text[0] != ' ':
            return text[0] + remove_spaces(text[1:])
        else:
            return remove_spaces(text[1:] )
    elif text == ' ' or text == '': #note if text = '' then text[0] is nonsense, python is upset, and it makes this homework harder than it should be
        return ''
    elif text != ' ':
        return text

'''This method returns True or False for whether some text is a palindrome.
    The method accepts only strings. It assumes that the arguements do not have spaces.'''
def palindrome(text):
    if text == None:
        return False
    if type(text) != str: #Ideally we woudldn't run this EVERYTIME in our recursion.
        raise TypeError("The data type isn't a string")
    if len(text) <= 1 or  text=='':
        return True
    elif text[0].upper() == text[ len(text)-1 ].upper(): #check if first and last characters are equal
        return palindrome(text[1: len(text)-1] )
        # return
    else:
        return False

class test_remove_spaces (unittest.TestCase):
    def test_remove_space_none(self):
        self.assertEquals (remove_spaces (None), None)
    def test_remove_space_empty(self):
        self.assertEquals (remove_spaces (""), "")
    def test_remove_space_one(self):
        self.assertEquals (remove_spaces (" "), "")
    def test_remove_space_two(self):
        self.assertEquals (remove_spaces ("  "), "")
    def test_remove_space_inside(self):
        self.assertEquals (remove_spaces ("a b c"), "abc")
    def test_remove_space_before(self):
        self.assertEquals (remove_spaces (" a b c"), "abc")
    def test_remove_space_after(self):
        self.assertEquals (remove_spaces ("a b c "), "abc")
    def test_remove_space_before_and_after(self):
        self.assertEquals (remove_spaces (" a b c "), "abc")
    # this is extra credit
    def test_raise_typerror(self):
        self.assertRaises (TypeError, lambda: remove_spaces (1))
    # My tests
    def test_arrayNumbers(self):
        self.assertRaises (TypeError, lambda: remove_spaces([1,2,3]) )
    def test_arrayStrings(self):
        self.assertRaises (TypeError, lambda: remove_spaces(['hello', 'hi', 'hey']) )
    def test_lotsOfSpaces(self):
        self.assertEquals (remove_spaces ("    1     2 "), "12")

class test_palindrome (unittest.TestCase):
    def test_none(self):
        self.assertFalse (palindrome (None))
    def test_empty(self):
        self.assertTrue (palindrome (""))
    def test_one_letter(self):
        self.assertTrue (palindrome ("v"))
    def test_two_letters(self):
        self.assertTrue (palindrome ("vv"))
    def test_toyota(self):
        self.assertTrue (palindrome ("atoyota"))
    def test_toyota_with_spaces(self):
        self.assertTrue (palindrome (remove_spaces ("a toyota")))
    def test_odd_even(self):
        self.assertTrue (palindrome (remove_spaces ("never odd or even")))
    def test_rat(self):
        self.assertTrue (palindrome (remove_spaces ("Was It a Rat I saW")))
    def test_not(self):
       self.assertFalse (palindrome (remove_spaces ("i'm not a palindrome")))
    # My tests
    def test_odd(self):
       self.assertTrue (palindrome ('acbca'))
    def test_even(self):
       self.assertTrue (palindrome('abba'))
    def test_arrayNumbers(self):
        self.assertRaises (TypeError, lambda: palindrome([1,2,3]) )
    def test_arrayStrings(self):
        self.assertRaises (TypeError, lambda: palindrome(['hello', 'hi', 'hey']) )
    def test_capitalsLowercase(self):
        self.assertTrue (palindrome('AAaa'))
    def test_capitalsLowercase2(self):
        self.assertTrue (palindrome('Abba'))
    def test_capitalsLowercase_numbers(self):
        self.assertTrue (palindrome('111'))


if '__main__' == __name__:
    unittest.main()