LCG

Linear congruential generator. {Mode} in link is “Lcg”. Numbers are generated like s:
public int Next()
{
_last = (a * _last + c) % m; // m is 2^32
return (int) _last;
}

The first one who writes “a” and “c” values to group chat will get +1 scores.
Lesson to learn: just never use LCG for anything

Ми не знаємо приросту та множника. Принаймні ми можемо дізнатися три послідовні значення від LCG.
Для пошуку множника складемо два лінійних рівняння з двома невідомі:
s_1 = s0*m + c  (mod n)
s_2 = s1*m + c  (mod n)

s_2 - s_1 = s1*m - s0*m  (mod n)
s_2 - s_1 = m*(s1 - s0)  (mod n)

m = (s_2 - s_1)/(s_1 - s_0)  (mod n)
Цей алгоритм використовує модульне поділ, тому нам також знадобиться модульне зворотне. Ми можемо скористатися цим.
Для пошуку приросту складемо елементарне математичне рівняння:
s1 = s0*m + c   (mod n)
c  = s1 - s0*m  (mod n)

Результультат: https://docs.google.com/document/d/1jhf3P6Iob5fxN4EkM9illeYgAnzwmaCJ2SbSkzpftH4/edit


MT19937

Mersenne Twister 19937. {Mode} in link is “Mt”. Numbers are generated by the standard MT19937 algorithm. Seed is “DateTimeOffset.UtcNow.ToUnixTimeSeconds()”. From the 32 bit RNG output discover the seed. Use it to win.
Lesson to learn: a hard algorithm is not always a secure algorithm. Never seed your RNG with time or any other easily guessable numbers. It will make a strong algorithm weak.
 
Для розвязку цього завдання було реалізовано алгорит Mersenne Twister який використовує DateTimeOffset.UtcNow.ToUnixTimeSeconds() як значення seed.

Результати:
https://docs.google.com/document/d/1_W00GZXLNTk6BML6jEaAJDqwMVjaQUv5WL1DCW7ipy4/edit

MT19937 with a strong seed

		MT19937 with a strong seed. {Mode} in link is “BetterMt”. Seed is created with “System.Security.Cryptography.RandomNumberGenerator.Create()”.You need to extract the whole state of every register of MT to break this one. Create a new MT19937 generator, tap it for 624 outputs, untemper each of them to recreate the state of the generator, and splice that state into a new instance of the MT19937 generator. Use it to predict next values
Lesson to learn: Weak algorithm turns a strong seed into garbage. The security of a system is made up not by the sum of parts but by the min.

Після спостереження n чисел можна передбачити всі майбутні ітерації шляхом реконструкції внутрішнього стану RNG, оскільки функція temper, яка використовується для отримання результатів, є біективною та оборотною.
Інвертування temper включає застосування оберненої до кожної операції функції відпуску в зворотному порядку.
	y = y ^ ((y >> MT19937.u) & MT19937.d)
	y = y ^ ((y << MT19937.s) & MT19937.b)
	y = y ^ ((y << MT19937.t) & MT19937.c)
	y = y ^ (y >> MT19937.l)

Зауважте, що існують два типи операцій:
Зсув ліворуч + порозрядний і
Зсув вправо + порозрядний і

Результультат: 
https://docs.google.com/document/d/121efoh98uQQdgpz1fc_Zu27SiNLzib1o2Ah_sr5f1Y/edit?usp=sharing