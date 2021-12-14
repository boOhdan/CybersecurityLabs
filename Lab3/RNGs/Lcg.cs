using System;
using System.Numerics;

namespace Lab3.RNGs
{
    public class Lcg
    {
        public int Modulus { get; set; }
        public int Multiplier { get; set; }
        public int Increment { get; set; }
        public int Seed { get; set; }

        public int Next()
        {
            Seed = (Multiplier * Seed + Increment) % Modulus;
            return Seed;
        }

        public int Next(int last)
        {
            Seed = (Multiplier * last + Increment) % Modulus;
            return Seed;
        }
    }
}
