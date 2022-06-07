# Week6_2
b) Как устроены корутины? В чем плюсы корутин? Какие есть минусы?

Корутины - легковесные потоки. Они запускаются с помощью билдера сопрограмм launch в контексте некоторого CoroutineScope. 

Плюсы:
- более эффективное использование ресурсов 
- все задачи связаны с определенным scope (позволяет избежать утечки памяти)
- возможность запускать асинхронный код без всяких блокировок
- корутины контролируются более гибко
- простота в использовании, вся сложность внутри библиотеки

Минусы:
- теряем возможность использования некоторых библиотек на Java, просто потому что они ожидают Thread
- узкая специализация (Корутины — высокоуровневое решение. Ускорить сложные вычисления с помощью сопрограмм не получится.)