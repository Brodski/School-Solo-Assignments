
rowBelow( [A,B],[X]) :- X is B - A.
rowBelow( [A,B|L], [X|L2]) :- X is B - A, rowBelow([B|L],L2).

getLast( [X,Y], A) :- A is Y.
getLast( [F|L], A) :- getLast(L,A).

nextItem([L1,N], X) :- rowBelow(L1, [A|L2]), getLast(L2,Z), Z =A, getLast(L1,Z2), X is A+Z2.
nextItem(L,N) :- rowBelow(L,A), getLast(L,I), nextItem(A,N2), N is N2+I.







oldLast( R, A) :- myLength(R,L), L2 is L-1, nth(R, L2, A).

myLength( [], 0 ).
myLength( [H|T], N ) :- myLength(T,N1), N is N1+1.

nth( [H|T], 0, H ).
nth( [H|T], N, A ) :- N1 is N-1, nth( T, N1, A ).

