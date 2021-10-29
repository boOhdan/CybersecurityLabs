using System;
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
        
    }
}
