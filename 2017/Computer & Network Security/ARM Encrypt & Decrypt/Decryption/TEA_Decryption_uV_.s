;Chris Brodski
;Comp Security 

;After the program finishes r7 will have LZero!
;After the program finishes r8 will have RZero!

;You could also find r7 and r8 in memory at 0x1F4 and 0x1F8

		AREA 	Decryption,	CODE, READWRITE	
		ENTRY
		
			
							;r0 = TemporaryVar
		LDR		r1, Delta1  ;r1 = constant
		LDR		r2, Delta2  ;r2 = constant2
		LDR		r3, KZero  	;r3 = key0
		LDR		r4, KOne	;r4 = key1
		LDR		r5, KTwo    ;r5 = key2
		LDR		r6, KThree  ;r6 = key3
							;r7 = LZero 
							;r8 = RZero 
		LDR		r9,  LTwo	;r9 =  LTwo 
		LDR		r10, RTwo	;r10 = RTwo 
							;r11 = TemporaryVar2
							;r12 = TemporaryVar3
	
		BL		Decrypt_Right
		BL		Decrypt_Left
		B 		EndIt
		
		
Delta1	DCD		0x11111111
Delta2  DCD		0x22222222
KZero	DCD		0xFF000000
KOne	DCD		0x00FF0000
KTwo	DCD 	0x0000FF00
KThree  DCD		0x000000FF
LZero	DCD		0x00000000
RZero	DCD 	0x00000000
LTwo	DCD		0xACFB41EA
RTwo	DCD		0xB19A5F4D	
	
		ALIGN

Decrypt_Right
		MOV		r7, r9			; Temp1 = LTwo 
		MOV		r8, r9			; Temp2 = LTwo 
		MOV		r11, r9			; Temp3 = LTwo
		
		LSL		r11, #4			
		ADD		r11, r11, r5	; Temp3 = LTwo + K2
		
		ADD		r7, r7, r2		; Temp1 = Temp1 + delta2
		
		LSR		r8, #5
		ADD		r8, r8, r6		; Temp2 = Temp2 + K3
		
		EOR		r11, r11, r7		
		EOR		r11, r11, r8	; RTwo EOR Temp1 EOR Temp2
		SUB		r8, r10, r11	; RZero = RTwo - F(R,K,K,d)
		
		MOV		r0, #500
		STR		r8, [r0]
		BX		LR
		
		
Decrypt_Left
		MOV		r12, r8			; Temp1 = RZero 
		MOV		r7, r8			; Temp2 = RZero
		MOV		r11, r8			; Temp3 = RZero
		
		LSL		r11, #4			
		ADD		r11, r11, r3	; Temp3 = LTwo + K0
		
		ADD		r12, r12, r1	; Temp1 = Temp1 + delta1
		
		LSR		r7, #5
		ADD		r7, r7, r4		; Temp2 = Temp2 + K1
				
		EOR		r11, r11, r7		
		EOR		r11, r11, r12	; Temp3 EOR Temp1 EOR Temp2		
		SUB		r7, r9, r11		; LZero = RTwo - F(R,K,K,d)
		
		MOV		r0, #504
		STR		r7, [r0]
		BX		LR	
	
EndIt
		END