;Chris Brodski
;HW3 Comp Org 2

		AREA vowels, CODE, READWRITE
		ENTRY
		LDR 	r0, =myString
		MOV 	r5, #0	;r5 will count the number of vowels
		B 		CheckVowels
		
myString DCB "This is a string",0
		ALIGN
		
CheckVowels
		LDRB	r3, [r0]	;r3 has first byte of r0, if r0 = 44 33 22 11, then r2 = 11, We wil update the address of r0 later.
		CMP		r3, #0		;use CMP to check if we reached the end of our null-terminated string
		BEQ		EndIt		;if we are at the end of the string, go to END
		TEQ 	r3, #0x61	; r3==a?
		TEQNE 	r3, #0x65	; or r3==e?
		TEQNE 	r3, #0x69	; or r3==i?
		TEQNE 	r3, #0x6F	; or r3==o?
		TEQNE 	r3, #0x75	; or r3==u?
		BLEQ	toUppercase
		
		TEQ		r3, #0x41		;r3==A?
		TEQNE	r3, #0x45		;or r3==E?
		TEQNE	r3, #0x49		;or r3==I?
		TEQNE	r3, #0x4F		;or r3==O?
		TEQNE	r3, #0x55		;or r3==U?
		BLEQ	countVowels
		STRB	r3, [r0], #1	;[r0] = r3. If r3 contained a vowel at first it is now uppercase. And we increment the address of r0 by 1
		B		CheckVowels
	
countVowels
		ADD		r5, r5, #1	;r5 = r5 + 1
		BX		LR			;go back to BL
	
toUppercase
		SUB		r3, r3, #32	;(example) r3 = 'A' - 32 => r3 = 'a'
		BX		LR
EndIt
		END