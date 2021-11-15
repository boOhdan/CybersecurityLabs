using Lab3.BLL;
using Lab3.CrackingRNGs;
using Lab3.Models;
using Lab3.RNGs;
using System;
using System.Collections;

namespace Lab3
{
    class Program
    {
        static void Main(string[] args)
        {
            CrackLCG();
        }

        //static void CrackMT19937UnknownSeed()
        //{
        //    var states = new uint[624];

        //    var serverConnection = new ServerConnection();
        //    var account = serverConnection.CreateAccountAsync("first70").Result;

        //    var mt19937 = new MT19937();

        //    for (int i = 0; i < states.Length; i++) 
        //    {
        //        var message = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "BetterMt").Result;
        //        mt19937.MT[i] = mt19937.GetState(message.RealNumber);
        //    }

        //    var winNumber = mt19937.extract_number();
        //    var winMessage = serverConnection.MakeBetAsync(account.AccountId, 1, winNumber, "BetterMt").Result;

        //    Console.WriteLine("Win Message: " + winMessage.Message);
        //    Console.WriteLine("RealNumber: " + winMessage.RealNumber + " Expected: " + winNumber);
        //}
        //static void CrackMT19937()
        //{
        //    var serverConnection = new ServerConnection();
        //    var account = serverConnection.CreateAccountAsync("first63").Result;

        //    uint time = (uint)DateTimeOffset.UtcNow.ToUnixTimeSeconds();
        //    var message = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "Mt").Result;
        //    var mt19937 = new MT19937();

        //    int i = 0;

        //    while (true)
        //    {
        //        mt19937.seed_mt((uint)(time + i));

        //        if (message.RealNumber == mt19937.extract_number())
        //            break;

        //        i++;
        //    }

        //    var seed = time + i;
        //    Console.WriteLine("Seed: " + seed);

        //    var winMessage = serverConnection.MakeBetAsync(account.AccountId, 1, mt19937.extract_number(), "Mt").Result;
        //    Console.WriteLine("Win Message: " + winMessage.Message);
        //}

        public static void CrackLCG() 
        {
            var serverConnection = new ServerConnection();
            var account = serverConnection.CreateAccountAsync("err3").Result;
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
                        int last = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "Lcg").Result.RealNumber;

                        if (TestLcg.Next() != last) 
                        {
                            check = false;
                            break;
                        }
                    }

                    if (check) 
                    {
                        Lcg = TestLcg;

                        Console.WriteLine($"Multiplier: {TestLcg.Multiplier}, Increment {TestLcg.Increment}");
                        Console.WriteLine($"Message: {serverConnection.MakeBetAsync(account.AccountId, 1, Lcg.Next(), "Lcg").Result.Message}");

                        break;
                    }
                }
            }
        }
    }
}
