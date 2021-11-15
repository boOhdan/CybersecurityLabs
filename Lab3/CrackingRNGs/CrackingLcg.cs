using Lab3.RNGs;
using System;

namespace Lab3.CrackingRNGs
{
    public static class CrackingLcg
    {
        public static int CrackUnknownIncrement(int[] states, int modulus, int multiplier)
        {
            return (states[1] - states[0] * multiplier) % modulus;
        }

        public static bool TryCrackUnknownMultiplier(int[] states, int modulus, out int multiplier)
        {
            if (TryGetModularInverse(states[1] - states[0], modulus, out int modinv)) 
            {
                multiplier = ((states[2] - states[1]) * modinv % modulus);
                return true;
            }

            multiplier = 0;
            return false;
        }

        public static bool TryGetModularInverse(int b, int n, out int modinv)
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

        public static (int, int, int) egcd(int a, int b)
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
