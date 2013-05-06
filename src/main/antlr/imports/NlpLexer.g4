/*
 Copyright (c) 2013 HAN University of Applied Sciences
 Arjan Oortgiese
 Boyd Hofman
 Joëll Portier
 Michiel Westerbeek
 Tim Waalewijn
 
 Permission is hereby granted, free of charge, to any person
 obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without
 restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following
 conditions:
 
 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 OTHER DEALINGS IN THE SOFTWARE.
*/
lexer grammar NlpGlobal;

MAAND : ('januari'|'februari'|'maart'|'april'|'mei'|'juni'|'july'|'augustus'|'september'|'oktober'|'november'|'december');
LIDWOORD : ('de'|'het'|'een'|'De'|'Het'|'Een');
KOMMA: ',';
NUMMER : ('0'..'9')+;
EINDEZIN : '.';
WOORD : (('a'..'z')|('A'..'Z')|'ë'|'ï'|'\'')+;

/*
*	Grammar that concerns the primitive types of the UML.
*/
STRING : ('"') (('a'..'z')|('A'..'Z')|('0'..'9')|'Ã«'|'Ã¯'|'\''|','|SEPARATOR|WS)+ ('"');
DECIMAAL : '0'..'9'+ (',') '0'..'9'+;
TIJD : ('0'..'9'+ ':' '0'..'9'+) | ('0'..'9'+ ':' '0'..'9'+ ' uur') | ('0'..'9'+ ' uur') | ('0'..'9'+ ' uur ' '0'..'9'+);
SEPARATOR : ('-'|'/');
DATUM : ('0'..'9' '0'..'9'? SEPARATOR '0'..'9' '0'..'9'? SEPARATOR ('0'..'9' '0'..'9' ('0'..'9' '0'..'9')?)) | ('0'..'9' '0'..'9'? ' 'MAAND) | ('0'..'9' '0'..'9'? ' 'MAAND' ' ('0'..'9' '0'..'9')('0'..'9' '0'..'9')?);

WS  :  (' ' |'\n' |'\r' )+ -> skip ;