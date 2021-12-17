using Lab3.RNGs;
using System;

namespace Lab3.CrackingRNGs
{
    public static class CrackingLcg
    {
        public static long CrackUnknownIncrement(long[] states, long modulus, long multiplier)
        {
            return (states[1] - states[0] * multiplier) % modulus;
        }

        public static bool TryCrackUnknownMultiplier(long[] states, long modulus, out long multiplier)
        {
            if (TryGetModularInverse(states[1] - states[0], modulus, out long modinv)) 
            {
                multiplier = (states[2] - states[1]) * modinv % modulus;
                return true;
            }

            multiplier = 0;
            return false;
        }

        public static bool TryGetModularInverse(long b, long n, out long modinv)
        {
            var (g, x, _) = egcd(b, n);

            if (g == 1) 
            {
                modinv = x % n;
                return true;
            }
            
            modinv = 0;
            return false;
        }

        public static (long, long, long) egcd(long a, long b)
        {
            if (a == 0)
                return (b, 0, 1);
            else
            {
                var (g, x, y) = egcd(b % a, a);

                return (g, y - (b / a * x), x);
            }
        }
    }
}
