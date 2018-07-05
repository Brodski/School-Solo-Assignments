;Chris Brodski
;HW4 Comp Org 2

		;For the assignment we use 31.75 = 1 1111.11. Work shown at bottom of script.
		AREA TNS_IEEE, CODE, READONLY
		ENTRY
		LDR r0, IEEEnum			; r0 = #0x41FE0000 = 31.75 in IEEE
		LDR r1, TNSnum			; r1 = #0x7E000104 = 31.75 in TNS
								; r2 = IEEE converted into TNS afterwards
								; r3 = TNS converted into IEEE aferwards
		LDR	r4, signMask		; r4 = #0x80000000 = Sign mask
		LDR	r5, IEEE_expoMask	; r5 = #0x7F800000 = IEEE exponent mask
		LDR	r6, IEEE_mantMask	; r6 = #0x007FFFFF = IEEE mantissa mask
		LDR	r7, TNS_expoMask	; r7 = #0x000001FF = TNS exponent mask
		LDR	r8, TNS_mantMask	; r8 = #0x7FFFFE00 = TNS mantissa mask

		BL		GetExponentAndMantissa
		BL		Test_If_Compatible
		BL		Put_Numbers_Together
		BL		Check_for_Equality
		B		EndIt
		
IEEEnum			DCD 0x41FE0000	
TNSnum  		DCD 0x7E000104
signMask 		DCD 0x80000000
IEEE_expoMask 	DCD 0x7F800000
IEEE_mantMask 	DCD 0x007FFFFF
TNS_expoMask  	DCD 0x000001FF
TNS_mantMask 	DCD 0x7FFFFE00



GetExponentAndMantissa 
		AND r4, r0, r4		; r4 = r0 AND #0x80000000 = IEEEnum AND signMask => sign of IEEEnum and TNS
		AND	r5, r0, r5		; r5 = r0 AND #0x7F800000 = IEEEnum AND IEEE_expoMask => exponent of IEEEnum
		AND r6, r0, r6		; r6 = r0 AND #0x007FFFFF = IEEEnum AND IEEE_mantMask => mantissa of IEEEnum
		AND r7, r1, r7		; r7 = r1 AND #0x000001FF = TNSnum AND TNS_expoMask => exponent of TNSnum
		AND r8, r1, r8		; r8 = r1 AND #0x7FFFFE00 = TNSnum AND TNS_mantmask => mantissa of TNSnum
		BX	LR

		
Test_If_Compatible
	; Check if TNS exponent fits in IEEE exponent
		; Largest IEEE true exponent can be 0111 1111 = 127. Thus to check if  
		; TNS's 9 bit exponent can fit in IEEE's exponent, the  
		; exponent  must be equal to or less than 127.
		CMP r7, #127					;Branch if 127 < TNS_expo
		BMI		IncompatibleException	
		BX LR
		
	;Check if IEEE Mantissa fits in TNS mantissa
		; Note that TNS mantissa has 22bits. IEEE mantissa has 23.
		; To make sure that the TNS mantissa fits inside IEEE's Mantisa we check if the last digit is 0,
		; since the number in the manitssa is read as 1.xxxx...x, from left to right,  all
		; trailing zeroes at the right of 1.xxx...x are irrelevent.
		
		AND r10, r6, #0x00000001	;r10= IEEE_mantMask AND 0x00000001 => r10 = the very last digit in the mantissa
		CMP r10, #0					;r10 == 0? : Check if the last digit in the mantissa == 0.
									;if r10 = 1 then the mantissa of TNS and IEEE is incompatible
		BNE		IncompatibleException
		BX LR
		
Put_Numbers_Together
		;IEEE --> TNS
		
 		LSR r5, r5, #23			; r5 = IEEE exponent shifted right 23 bits 
								; r5 = 0|000 0000 00000 0000 0000 000|0 xxxx xxxx
		ADD r5, r5, #129		; Unpack IEE expo and then pack it into TNS. 
								; IEEE_Expo = True_expo + 127. TNS_expo = True_expo + 256 => TNS_Expo = (IEEE_Expo - 127 + 256) = IEEE_expo + 129
		LSL r6, r6, #8			; r6 = IEEE mantissa shifted left 8 bits 
								; r6 = 0|xxxx xxxx xxxx xxxx xxxx xxx|0 0000 0000
		ORR r2, r5, r6			; r2 = r5 OR r6 = exponent OR mantissa
		ORR r2, r2, r4			; r2 = r2 OR r4 = (exponent OR mantissa) OR sign
		;TNS --> IEEE
		SUB r7, r7, #129		; IEEE_expo = TNS_expo - 256 + 127 = TNS_expo - 129
		LSL r7, r7, #23			; r7 = TNS exponent shifted left 23 bits
								; r7 = 0 | xxxx xxxx | 0000 0000 0000 00000 0000 000
		LSR r8, r8, #8			; r8 = TNS mantissa shifted right 8 bits.
								; r8 = 0 | 0000 0000 | xxxx xxxx xxxx xxxx xxxx xxx
		ORR r3, r7, r8			; r3 = r7 OR r8 = exponent OR mantissa
		ORR r3, r3, r4			; r3 = r3 OR r4 = (exponent OR mantissa) OR sign
		BX LR


Check_for_Equality
		CMP r0, r3			; r0 == r3? ; Check if (Original IEEE number) == (TNS number convered to IEEE number)
		BNE Error_in_conversion_exception
		CMP r1, r2			; r1 == r1? ; (Check if Orignal TNS number) == (IEEE number convered to TNS)
		BNE Error_in_conversion_exception
		BX LR

IncompatibleException
		;Do something if the numbers are incompatible
		BX LR
Error_in_conversion_exception
		;Do something if the numbers are not equal.
		BX LR
EndIt	
		END
		
		
		; 31 = 0001 1111
		; 0.75 = 0.11
		; 31.75 = 1 1111.11 -->IEEE --> 1.1111 11 *2^4 = 1.111111 *2^100
		; IEEE & TNS Mantissa = 11 1111
		; IEEE exponent = 0111 1111 + 0000 0100 =  1000 0011
		; TNS exponent  =10000 0000 + 0000 0100 = 10000 0100
		
		; Thus:
		;31.75 in IEEE = 0 | 1000 0011 | 1111 1100 0000 0000 0000 000
		;			   = 0100 0001 1111 1110 0000 0000 0000 0000
		;			   = 41FE0000
		;31.75 in TNS  = 0 | 1111 1100 0000 0000 0000 00 | 1 0000 0100
		;			   = 0111 1110 0000 0000 0000 0001 0000 0100 
		;			   = 7E000104
		