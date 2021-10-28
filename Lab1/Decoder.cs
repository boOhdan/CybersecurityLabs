using System;
using System.Text;

namespace Lab1
{
    public static class Decoder
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

        public static string DecodeFromBase64(this string ciphertext)
        {
            var data = Convert.FromBase64String(ciphertext);
            return Encoding.UTF8.GetString(data);
        }

        public static void FindKeyLength(this string ciphertext)
        {
            for (int i = 0; i < ciphertext.Length; i++)
            {
                var coincidenceNumber = 0;
                var offsetСiphertext = ciphertext.Substring(ciphertext.Length - i, i) + ciphertext.Substring(0, ciphertext.Length - i);

                for (int j = 0; j < ciphertext.Length; j++)
                {
                    if (offsetСiphertext[j] == ciphertext[j])
                        coincidenceNumber++;
                }

                Console.WriteLine(coincidenceNumber);
            }
        }

        public static string DecodeVigenereCipherWithoutKey(this string ciphertext)
        {
            var keyLength = 3; //encryptedText.FindKeyLength();
            var key = string.Empty;

            for (int i = 0; i < keyLength; i++)
            {
                var textChipheredWithSameKeyCharacter = string.Empty;

                for (int j = 0; j < ciphertext.Length; j += keyLength)
                {
                    textChipheredWithSameKeyCharacter += ciphertext[i + j];
                }

                var  maxCoincidenceNumber = (сoincidenceNumber: 0, character: '\0');

                for (int k = 0; k < 128; k++)
                {
                    var coincidenceNumber = 0;

                    for (int s = 0; s < textChipheredWithSameKeyCharacter.Length; s++)
                    {
                        var decodedChar = (char)(textChipheredWithSameKeyCharacter[s] ^ k);

                        if(char.IsLetter(decodedChar) || char.IsPunctuation(decodedChar) || char.IsWhiteSpace(decodedChar))
                        {
                            coincidenceNumber++;
                        }
                    }

                    if (coincidenceNumber > maxCoincidenceNumber.сoincidenceNumber) 
                    {
                        maxCoincidenceNumber = (coincidenceNumber, (char)k);
                    }

                }

                key += maxCoincidenceNumber.character;
            }

            return ciphertext.DecodeXorCipher(key);
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
