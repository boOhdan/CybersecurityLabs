using System;
using System.Collections.Generic;
using System.Text;

namespace Lab1
{
    public static class XorCipherDecoder
    {
        public static string DecodeXorCipher(this string encryptedText, string key) 
        {
            var result = new StringBuilder();
            var generatedKey = GenerateKey(key, encryptedText.Length);

            for (int i = 0; i < encryptedText.Length; i++)
            {
                result.Append((char)(encryptedText[i] ^ generatedKey[i]));
            }

            return result.ToString();
        }

        public static string GenerateKey(this string key, int length) 
        {
            var repitNumber = length / key.Length + 1;
            var result = new StringBuilder(repitNumber * key.Length);

            for (int i = 0; i < repitNumber; i++)
            {
                result.Append(key);
            }

            return result.ToString().Substring(0, length);
        }
        public static (char key, string text) BruteForceDecode(string encodedText)
        {
            var results = new List<(char, string)>();

            for (byte key = 0; key < byte.MaxValue; key++)
            {
                results.Add(((char)key, DecodeXorCipher(encodedText, ((char)key).ToString())));
            }

            var index = 1;
            var maxNumber = 0f;

            for (int i = 1; i < results.Count; i++)
            {
                var p = GetTextPercent(results[i].Item2);

                if (p > maxNumber)
                {
                    maxNumber = p;
                    index = i;
                }
            }

            return results[index];
        }

        private static float GetTextPercent(string encodedText)
        {
            var counter = 0f;

            for (int i = 0; i < encodedText.Length; i++)
            {
                char c = encodedText[i];

                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '.' 
                    || c == ',' || c == ' ' || c == '/' || c == ':' || c == '-')
                {
                    counter++;
                }
            }

            return counter / encodedText.Length * 100.0f;
        }
    }
}
