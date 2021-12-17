using System;
using System.Numerics;

namespace Lab3.RNGs
{
    public class Lcg
    {
        public long Modulus { get; set; }
        public long Multiplier { get; set; }
        public long Increment { get; set; }
        public long Seed { get; set; }

        public long Next()
        {
            Seed = (Multiplier * Seed + Increment) % Modulus;
            return Seed;
        }

        public long Next(int last)
        {
            Seed = (Multiplier * last + Increment) % Modulus;
            return Seed;
        }
    }
}
