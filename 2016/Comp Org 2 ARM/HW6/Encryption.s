;Chris Brodski
;Comp Org, HW6

		AREA 	Encryption,	CODE, READWRITE	; name this block of code
		ENTRY
		
		;In uVision, a memory range of 0xFFFFFF00 to 0xFFFFFFFF is sufficent for the pointer
		;And a memory range of 0x000003E8, 0x00000A00 is sufficent for the arrays.
		
		ADR		r0, JumpTable
		LDR		r1, =myStr	;r1 = message that we encode and decode
		MOV		r2, #1000 	;r2 = address of new string. #1000 has no signifigance, it's just a address.
							;r3 = maskA
							;r4 = maskB
							;r5 = temporary
							;r6 = temporary 2
							;r7 = byte that we encrypt
		LDR		r9, encrypt	;r9 = encryption key	
		MOV		r10, #0		;r10 = counter for the number of words in myStr
		
		B		Encrypt
		
		
mask1	DCD		0x00FF0000
mask2	DCD		0x0000FF00
mask3	DCD		0xFF000000
mask4	DCD		0x000000FF
encrypt	DCD 	0xABCDEF01
myStr	DCB		"Yesterday, all my troubles seemed so far away. [...]",0 
		ALIGN
			
JumpTable
		DCD		Swap
		DCD		EXOR_funct

Encrypt						
		LDR 	r7, [r1]		; Load word from the orignal string. [r1] points to myStr.
		CMP		r7, #0			; If r7 == #0, it means we reached the end of the string.
		BEQ    	Decrypt
		
		ADD		r10, r10, #1	;counter = counter + 1
		
		STMDB 	sp!, {PC}		;Save this address, we will return here. ;equivlanet to PUSH {PC}
		LDR		pc, [r0, #0] 	;go to Swap, via JumpTable
		STMDB 	sp!, {PC}		
		LDR		pc, [r0, #4] 	;go to EXOR_funct
		
		ADD		r1, r1, #4		;update address by 4 byte
		ADD		r2, r2, #4		;update address by 4 byte
		B		Encrypt			;go to Encrypt until we reached the end of the string, which is 0x00000000.
		
		;We decrypt backwards from [r1] and [r2], which is pointing to the end of the orignal string and the encoded string, respectively
Decrypt
		SUB		r1, r1, #4		;We decrement at first b/c we incremented prior in Encrypt
		SUB		r2, r2, #4		;update address by 4 byte
								
		LDR		r7, [r2]		;Load the encoded byte
		CMP		r10, #0			;r10 is the counter. If counter == 0 then we have gone through the entire array.
		BEQ		Compare
		
		SUB		r10,r10,#1		;decrement counter. 
		
		STMDB 	sp!, {PC}		;
		LDR		pc, [r0, #4] 	;go to EXOR_funct
		STMDB 	sp!, {PC}
		LDR		pc, [r0, #0] 	;go to Swap
		
		B		Decrypt			;go to Decrypt until counter r10 = 0

Swap
		MOV		r6, #0			; r6 = temparay variable
		
		LDR		r3, mask1
		LDR		r4, mask2
		;Swap 2nd and 3rd byte
		AND		r5, r7, r3 		;r5 = byte * mask = 00xx0000
		LSR		r5, r5, #8		;r5 shift-->0000xx00
		ORR		r6, r6, r5		;r6 = r6 OR 0000xx00
		AND		r5, r7, r4		;r5 = byte * mask = 0000xx00
		LSL		r5, r5, #8		;r5 shift-->00xx0000
		ORR		r6, r6, r5		;r6 = r6 OR 00xx0000
		
		LDR		r3, mask3
		LDR		r4, mask4
		;Swap 1st and 4th byte
		AND		r5, r7, r3		;r5 = byte * mask = xx000000
		LSR		r5, r5, #24		;r5 shift--->000000xx
		ORR		r6, r6, r5		;r6 = r6 OR 000000xx
		AND		r5, r7, r4		;r5 = byte * mask = 000000xx
		LSL		r5, r5, #24		;r5 shift --->xx000000
		ORR		r6, r6, r5		;r6 = r6 OR xx000000
		
		MOV		r7, r6			; replace r7 with the modified word.
		STR		r6, [r2]		;Store modified word
		LDMIA	sp!, {LR}		;go back
		SUB		LR, LR, #4
		MOV		pc, LR
		

EXOR_funct
								
		EOR 	r7, r7, r9		;exclusive-or with key
		STR		r7, [r2]
		
		LDMIA	sp!, {LR}
		SUB		LR, LR, #4
		MOV		pc, LR
	
	;We will compare the first word from the orignal word's array with first word with
	;the encoded/decoded word's array. Then compare the 2nd with the 2nd, 3rd with 3rd, ect..
Compare
		ADD		r1, r1, #4			;increment our address first b/c we decremented prior in Decrypt
		ADD		r2, r2, #4
		LDR		r5, [r1]
		LDR		r6,	[r2]
		CMP		r5,r6
		BNE		notEqualException	;If not equal then go to notEqualException
		CMP		r5, #0				
		BEQ		EndIt				;If r5 (the orignal string) reaches the end ,#0, then we are done.
		B 		Compare				;Else continue looping
notEqualException
		;does something if r0 and r1 are not equal
		MOV		r10, #10
EndIt
		END