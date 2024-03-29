Первый метод - https://github.com/Exilon1/hard-work/blob/main/src/cyclic_complexity/ExampleOne.java  
Второй метод - https://github.com/Exilon1/hard-work/blob/main/src/cyclic_complexity/ExampleTwo.java  
Третий метод - https://github.com/Exilon1/hard-work/blob/main/src/cyclic_complexity/ExampleThree.java  

Комментарии к методам содержат оценку цикломатической сложности и приёмы, применённые для её 
понижения. Выводы:
1) Наиболее часто используемыми приёмами оказались устранение вложенных if, вынесение цикла из if,
устранение else. Примеров кода с этими дефектами оказалось больше, чем остальных.
2) Если использовать только приёмы из пункта 1, то это очень созвучно с курсом code style.
3) Переделывание классических циклов на функциональные forEach не всегда применимы при наличии 
управления через break, continue, return. Нужны дополнительные средства, наподобие filter, findFirst...
4) В Java применение ad-hoc полиморфизма даёт не совсем то, чего от него ждёшь. Невозможно избежать 
сопоставления с образцом instanceof и явного приведения типов.
5) Возникла мысль, так уж ли плох switch case, если в java 21 в нем можно производить сопоставление 
с образцом.
6) К сожалению не нашёл вариантов кода, сложность которого можно снизить с помощью дженериков, хотя
понимаю, механизм мощный.
7) Похоже, что самое большое снижение цикломатической сложности приходится на принципы SOLID, когда
мы дробим большой метод на меньшие зоны ответственности, при условии, что метод уже отрефакторин,
и содержит только одиночные if с return в теле.