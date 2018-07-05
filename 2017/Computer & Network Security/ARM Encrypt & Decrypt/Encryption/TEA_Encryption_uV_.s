;Chris Brodski
;Comp Security 

;After the program finishes, r9 will have LTwo's encrypted text!
;After the program finishes, r10 will have RTwo's encrypted text!

;You could also find r9 and r10 in memory at 0x1F4 and 0x1F8

		AREA 	Encryption,	CODE, READWRITE
		ENTRY			
							;r0 = TemporaryVar		LDR		r1, Delta1  ;r1 = constant
		LDR		r2, Delta2  ;r2 = constant2
		LDR		r3, KZero  	;r3 = key0
		LDR		r4, KOne	;r4 = key1
		LDR		r5, KTwo    ;r5 = key2
		LDR		r6, KThree  ;r6 = key3
		LDR		r7, LZero	;r7 = LZero
		LDR		r8, RZero	;r8 = RZero
							;r9 =  LTwo 
							;r10 = RTwo 
							;r11 = TemporaryVar2
							;r12 = TemporaryVar3
		B		Encrypt
		
		
Delta1	DCD		0x11111111
Delta2  DCD		0x22222222
KZero	DCD		0xFF000000
KOne	DCD		0x00FF0000
KTwo	DCD 	0x0000FF00
KThree  DCD		0x000000FF
LZero	DCD		0xABCDEF01
RZero	DCD 	0xABCABCAB
LTwo	DCD		0x00000000	
RTwo	DCD		0x00000000	
		ALIGN
			
Encrypt

		LDR		r1, Delta1		
		LDR		r11, RZero		; Temp1 = RZero 
		LDR		r12, RZero		; Temp1 = RZero 
		LDR		r0,  RZero		; Temp1 = RZero 
		LDR		r3, KZero  		; r3 = key0
		LDR		r4, KOne		; r4 = Key1
		BL		Encrypt_Right
		ADD		r9, r0, r7		; LTwo = temp + Lzero
		MOV		r0, #500
		STR		r9, [r0]
		
		LDR		r1, Delta2
		MOV		r11, r9			; Temp1 = LTwo 
		MOV		r12, r9			; Temp2 = LTwo 
		MOV		r0,  r9			; Temp3 = LTwo
		LDR		r3, KTwo  		; r3 = key2
		LDR		r4, KThree		; r4 = key3
		BL		Encrypt_Right
		ADD		r10, r0, r8		; RTwo = temp + rzero
		MOV		r0, #504
		STR		r10, [r0]
		
		B		EndIt
		

Encrypt_Right
		LSL		r0, #4
		ADD		r0, r0, r3		; RZero = RZero + K0
		
		LSR		r11, #5
		ADD		r11, r11, r4	; Temp1 = Temp1 + K1
		
		ADD		r12, r12, r1	; Temp2 = Temp2 + Delta 1
		
		EOR		r0, r0, r11		
		EOR		r0, r0, r12
		BX		LR
			
EndIt
		END