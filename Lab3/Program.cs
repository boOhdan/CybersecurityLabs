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
            var serverConnection = new ServerConnection();
            var account = serverConnection.CreateAccountAsync("user4").Result;

            CrackLCG(serverConnection, account);
            CrackMT19937(serverConnection, account);
            CrackMT19937UnknownSeed(serverConnection, account);
        }

        public static void CrackLCG(ServerConnection serverConnection, Account account)
        {
            var Lcg = new Lcg() { Modulus = (long)Math.Pow(2, 32) };

            for (int i = 0; i < 100; i++)
            {
                var states = new long[3];

                for (int j = 0; j < 3; j++)
                {
                    states[j] = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "Lcg").Result.RealNumber;
                }

                if (CrackingLcg.TryCrackUnknownMultiplier(states, Lcg.Modulus, out long multiplier))
                {

                    Lcg.Modulus = (int)Math.Pow(2, 32);
                    Lcg.Seed = states[^1];
                    Lcg.Multiplier = multiplier;
                    Lcg.Increment = CrackingLcg.CrackUnknownIncrement(states, Lcg.Modulus, multiplier);

                    Console.WriteLine($"Multiplier: {Lcg.Multiplier}, Increment {Lcg.Increment}");
                    Console.WriteLine($"Win Message: {serverConnection.MakeBetAsync(account.AccountId, 1, Lcg.Next(), "Lcg").Result.Message}");

                    break;
                }
            }
        }

        static void CrackMT19937(ServerConnection serverConnection, Account account)
        {
            var mt19937 = new MT19937();
            var time = DateTimeOffset.UtcNow.ToUnixTimeSeconds();
            var message = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "Mt").Result;

            int i = 0;

            while (true)
            {
                mt19937.seed_mt((uint)(time + i));

                if (message.RealNumber == mt19937.extract_number())
                    break;

                i++;
            }

            var seed = time + i;
            var winMessage = serverConnection.MakeBetAsync(account.AccountId, 1, mt19937.extract_number(), "Mt").Result;

            Console.WriteLine("Seed: " + seed);
            Console.WriteLine("Win Message: " + winMessage.Message);
        }

        static void CrackMT19937UnknownSeed(ServerConnection serverConnection, Account account)
        {
            var mt19937 = new MT19937();
            var states = new uint[624];

            for (int i = 0; i < states.Length; i++)
            {
                var message = serverConnection.MakeBetAsync(account.AccountId, 1, 7777, "BetterMt").Result;
                mt19937.MT[i] = mt19937.GetState((uint)message.RealNumber);
            }

            var winNumber = mt19937.extract_number();
            var winMessage = serverConnection.MakeBetAsync(account.AccountId, 1, winNumber, "BetterMt").Result;

            Console.WriteLine("RealNumber: " + winMessage.RealNumber + " Expected: " + winNumber);
            Console.WriteLine("Win Message: " + winMessage.Message);
        }
    }
}
