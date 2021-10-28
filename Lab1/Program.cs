using System;

namespace Lab1
{
    class Program
    {
        static void Main(string[] args)
        {
        }

        static string Task1(string hexText) 
        {
            var asciiText = string.Empty;

            for (int i = 0; i < hexText.Length; i += 2)
            {
                asciiText += (char)Convert.ToInt32(hexText.Substring(i, 2), 16);
            }

            return asciiText.DecodeXorCipher(((char)55).ToString());
        }

        static string Task2(string base64Text)
        {
            var asciiText = base64Text.DecodeFromBase64();

            return asciiText.DecodeVigenereCipherWithoutKey();
        }
    }
}
