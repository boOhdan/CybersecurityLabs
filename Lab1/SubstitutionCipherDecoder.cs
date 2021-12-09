using System;
using System.Collections.Generic;

namespace Lab1
{
    public class SubstitutionCipherDecoder
    {
        public int MaximumNumberOfGenerations { get; set; } = 1000;
        public int PopulationSize { get; set; } = 500;
        public int TournamentSize { get; set; } = 20;
        public decimal TournamentWinnerProbability { get; set; } = 0.75m;
        public decimal ProbabilityOfCrossover { get; set; } = 0.65m;
        public int NumberOfCrossoverPoints { get; set; } = 5;
        public decimal ProbabilityOfMutation { get; set; } = 0.20m;
        public int PercentageOfElitism {get; set;} = 15;
        public bool PopulationSeeding { get; set; } = true;
    }
}

public class Individual
{
    public double Fitness = 0;
    public List<char> Genes = new List<char>();
    public int GeneLength = 26;
    public string EncryptedText;
    public string PlainText;

    public Individual(string encryptedText, string plainText)
    {
        EncryptedText = encryptedText;
        PlainText = plainText;
    }

    public void InitializeGenes() 
    {
        Random rn = new Random();

        for (int i = 0; i < GeneLength; i++)
        {
            var posibleLetter = '\0';

            do
            {
                posibleLetter = (char)rn.Next(65, 91);
            }
            while (Genes.Contains(posibleLetter));

            Genes.Add(posibleLetter);
        }
    }

    public string DecipherText() 
    {
        var result = EncryptedText;
        var startLetter = 'A';

        for (int i = 0; i < GeneLength; i++) 
        {
            char c = (char)(startLetter + i);
            char r = Genes[i];

            result.Replace(c, r);
        }

        return result;
    }

    public void CalcFitness(int n)
    {
        var plainTextGrams = DivideIntoGrams(n, PlainText);
        var decryptedTextGrams = DivideIntoGrams(n, DecipherText());
        var plainTextFrequency = CanculateGramFrequency(plainTextGrams);
        var decryptedTextFrequency = CanculateGramFrequency(decryptedTextGrams);

        //Console.WriteLine("Plain text:");
        //foreach (var p in plainTextFrequency)
        //{
        //    Console.WriteLine(p.Item1 + " " + p.Item2);
        //}

        //Console.WriteLine("Decrypted text:");
        //foreach (var p in decryptedTextFrequency)
        //{
        //    Console.WriteLine(p.Item1 + " " + p.Item2);
        //}

        foreach (var gramFrequency in decryptedTextFrequency) 
        {
            double frequency = plainTextFrequency.Find(p => p.Item1 == gramFrequency.Item1).Item2;

            if(frequency is not 0) 
            {
                Fitness += gramFrequency.Item2 * Math.Log2(frequency);
            }
        }
    }

    public List<string> DivideIntoGrams(int n, string text) 
    {
        var result = new List<string>();

        for (int i = 0; i + n < text.Length; i++) 
        {
            result.Add(text.Substring(i, n));

            i += n;
        }

        return result;
    }

    public List<(string, double)> CanculateGramFrequency(List<string> grams) 
    {
        var gramFrequency = new List<(string, double)>();
        var gramLength = grams.Count;

        foreach (var gram in grams) 
        {
            if(!gramFrequency.Exists(g => g.Item1 == gram)) 
            {
                gramFrequency.Add((gram, (double) grams.FindAll(g => g == gram).Count / gramLength));
            }
        }

        return gramFrequency;
    }
}