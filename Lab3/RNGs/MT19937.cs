
using System;

namespace Lab3.RNGs
{
    public class MT19937
    {
        public int w = 32, n = 624;
        public uint f = 1812433253;
        public int m = 397, r = 31;
        public uint a = 0x9908B0DF;
        public uint d = 0xFFFFFFFF, b = 0x9D2C5680, c = 0xEFC60000;
        public int u = 11, s = 7, t = 15, l = 18;

        public uint[] MT;
        public int index;

        public MT19937(uint[] MT = null) 
        {
            index = n;
            this.MT = MT ?? new uint[n];
        }

        public void seed_mt(uint seed)
        {
            index = n;
            Array.Clear(MT, 0, MT.Length);//MT = new uint[n];
            MT[0] = seed;

            for (int i = 1; i < MT.Length; i++)
            {
                MT[i] = (uint)(f * (MT[i - 1] ^ (MT[i - 1] >> (w - 2))) + i);
            }
        }

        public uint extract_number()
        {
            if (index == n)
                twist();

            uint y = MT[index];

            y ^= (y >> u) & d;
            y ^= (y << s) & b;
            y ^= (y << t) & c;
            y ^= y >> l;

            index++;
            return y;
        }
        public uint GetState(uint y)
        {
            var x = y;

            x = UntemperR(x, l);
            x = UntemperL(x, t, c);
            x = UntemperL(x, s, b);
            x = UntemperR(x, u, d);
            return x;
        }
        public uint UntemperR(uint n, int shift)
        {
            uint cur = n;
            for (int accuracy = shift; accuracy < w; accuracy += shift)
                cur = n ^ (cur >> shift);
            return cur;
        }

        public uint UntemperL(uint n, int shift, uint mask)
        {
            uint cur = n;
            for (int accuracy = shift; accuracy < w; accuracy += shift)
                cur = n ^ ((cur << shift) & mask);
            return cur;
        }

        public uint UntemperR(uint n, int shift, uint mask)
        {
            uint cur = n;
            for (int accuracy = shift; accuracy < w; accuracy += shift)
                cur = n ^ ((cur >> shift) & mask);
            return cur;
        }

        public void twist()
        {
            for (int i = 0; i < MT.Length; i++)
            {
                int lower_mask = (1 << r) - 1;
                int upper_mask = ~lower_mask;
                uint tmp = (uint)((MT[i] & upper_mask) + (MT[(i + 1) % n] & lower_mask));
                uint tmpA = tmp >> 1;

                if (tmp % 2 != 0)
                {
                    tmpA ^= a;
                }

                MT[i] = MT[(i + m) % n] ^ tmpA;
            }

            index = 0;
        }
    }
}
