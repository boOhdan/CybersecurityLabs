using Lab3.BLL;
using Lab3.CrackingRNGs;
using Lab3.Models;
using Lab3.RNGs;
using System;

namespace Lab3
{
    class Program
    {
        static void Main(string[] args)
        {
            CrackLCG();
        }

        public static void CrackLCG() 
        {
            var serverConnection = new ServerConnection();
            var account = serverConnection.CreateAccountAsync("first21").Result;
            var Lcg = new Lcg() { Modulus = (int) Math.Pow(2, 32) };

            for(int i = 0; i < 50; i++) 
            {
                var states = new int[3];

                for (int j = 0; j < 3; j++)
                {
                    states[j] = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "Lcg").Result.RealNumber;
                }

                if (CrackingLcg.TryCrackUnknownMultiplier(states, Lcg.Modulus, out int multiplier))
                {
                    var TestLcg = new Lcg() { Modulus = (int)Math.Pow(2, 32), Seed = states[^1]};

                    TestLcg.Multiplier = multiplier;
                    TestLcg.Increment = CrackingLcg.CrackUnknownIncrement(states, Lcg.Modulus, multiplier);

                    var check = true;

                    for(int k = 0; k < 10; k++) 
                    {
                        if (TestLcg.Next() != serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "Lcg").Result.RealNumber) 
                        {
                            check = false;
                            break;
                        }
                    }

                    if (check) 
                    {
                        Lcg = TestLcg;

                        Console.WriteLine($"Multiplier: {TestLcg.Multiplier}, Increment {TestLcg.Increment}");
                    }
                }
            }
        }
    }
}
