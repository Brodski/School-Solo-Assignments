;Chris Brodski
;HW5 Comp Org 2

;Program that does a bubble sort on 11 randomly generated numbers
;between 0-0xFFFFFFFF. Few modification were done to RANDOM.S

; In uVision5, when debugging, you must first
; start the debugger, then go to Debug -> Memory Map, then enter
; a large enough range, eg, 0x000000000, 0x4000FFFF. And have
; read, write, and execute all enabled.

		AREA bubbleSort, CODE, READWRITE
			
		ENTRY
		LDMIA	ip, {a1, a2}		; Setup RANDOM.S
		LDR		ip, |seedpointer|
		MOV		r3, #500			;We will store the random numbers at [r3]. 500 is an arbitrary address.
		MOV 	r9, #0				;r5 = Temporary register to hold a loop counter
		B 		loopGetRandoms

loopGetRandoms

		CMP 	r9, #11				; If r9 != 11, then we do not have 11 random numbers. So we generate a new random number and store it.
		STRNE	r0, [r3]			; Store the random number inside r0 to [r3]. [r3] = r0
		ADDNE	r3, r3, #4			;Update Address
		ADDNE 	r9, r9, #1			;Increment counter
		BNE		randomnumber	
		B		sortNumbers
		
sortNumbers
							; r3 = Address of random numbers
							; r5, r6 = numbers from the random array
							; r9 = Outer Loop Decrementer
							; r10 = Innter Loop Incrementer
		MOV 	r11, #0 	; r11 = Swap : 1 = Swap occured, 0 = No swaps
							; r12 = Saved Address
		SUB		r3, r3, #44	; r3 points to the final random number. We must go back (11) * 4
		MOV 	r9, #10 	; Want to stop looping at the 10th element because bubble sort checks current element and next element
		MOV		r12, PC		
		B		bubblesortloop		
		
bubblesortloop			
		CMP		r9, #0
		BEQ		count_1s_main 		; if r9 =0 0 then We have looped through everything.
		SUB		r3, r3, r10, LSL #2	; r3 = r3 - Number_of_InnerLoop_Iterations * 4 => we are back to the original address of the array : myRandArray[0]
		MOV 	r11, #0				; r11 = SwapCounter. 0 if no swaps occured. Assume no swaps.
		MOV		r10, #1				; Initialize our inner loop
		B 		innerLoop
				
innerLoop 
		
		LDR 	r5, [r3]		; r5=[r3] => r5 = random number from array
		LDR 	r6, [r3, #4]	; r6=[r3,#4] => r6 = Next random numb from array
		CMP 	r5,r6		
		BLHI	Swap			; Swap if r5 > r6 
		BL		Load_to_memory 	; We know r5 =< r6 now.
		CMP		r10,r9			; IF Innerloop counter r10 != outerloop counter r9, then we havn't reached the end of the inner loop. 
		ADDNE	r10, r10, #1 	;Increment innerloop counter
		BNE		innerLoop		
		
		SUB		r9, r9, #1		; ELSE, then decrement outer loop and go through bubblesortloop (outerloop) again.
		BL		checkForSwap	;But check if no swaps have occured. Nos swaps => the array is sorted. 
		B		bubblesortloop
				
Swap
		MOV		r11, #1			; swap has occured, so r11 = 1
		MOV		r7, r5			; r7 = r5 => TemporyVar = r5
		MOV		r5, r6			; r5 = r6
		MOV		r6, r7			; r6 = r7
		BX 		LR
Load_to_memory
		STR		r5, [r3]		;[r3] = r5
		STR		r6, [r3, #4]	;[r3 + 4bytes] = r6
		ADD		r3, r3, #4	
		BX 		LR
		
checkForSwap
		CMP		r11, #0			; if r11 == 0 => We are done.
		BEQ		count_1s_main
		BX 		LR				; else go back to innerLooop
		
count_1s_main		
		MOV		r2,#0			;r2 = Counter for number of 1s
								;r3 = Address of random numbers
								;r5 = number from Random Array
		MOV		r9,#11			;r9 = Loop decrementer. Start at 11.
		SUB		r3, r3, r10, LSL #2	;bring our address back to starting point.
		B		count_1s_loop
		
count_1s_loop
		CMP 	r9, #0
		BEQ		EndIt			
		LDR		r5, [r3]		; r1 = random number : r1 = myRandomNumArray[i]
		ADD		r3, r3, #4		; increment our address
		SUB		r9, r9, #1		; decrement our loop
		B		count_fast
	;Algorithm to count 1s fast
count_fast
		CMP		r5, #0
		BEQ		count_1s_loop	; if equal we go back to count_1s, count_1s will then auto decrement to the end of its loop
		SUB		r7, r5, #1		; r7 = temparary register for algorith
		AND		r5, r5, r7
		ADD 	r2, r2, #1		; increment the counter
		B		count_fast

		
; 	Random number generator
; 	Modified version of RANDOM.S
randomnumber
; on exit:
;	a1 = low 32-bits of pseudo-random number
;	a2 = high bit (if you want to know it)
	TST	a2, a2, LSR#1		; to bit into carry
	MOVS	a3, a1, RRX		; 33-bit rotate right
	ADC	a2, a2, a2		; carry into LSB of a2
	EOR	a3, a3, a1, LSL#12	; (involved!)
	EOR	a1, a3, a3, LSR#20	; (similarly involved!)
	STMIA	ip, {a1, a2}
	
	;MOV	pc, lr
	B loopGetRandoms
	
|seedpointer|
	DCD	seed

;	AREA	|Random$$data|, DATA

;	EXPORT	seed
seed
	DCD	&55555555
	DCD	&55555555
		
EndIt
		END