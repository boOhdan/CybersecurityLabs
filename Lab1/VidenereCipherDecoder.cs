using System;

namespace Lab1
{
    public static class VidenereCipherDecoder
    {
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

        public static string DecodeVigenereCipher(this string ciphertext, int keyLength)
        {
            var key = string.Empty;

            for (int i = 0; i < keyLength; i++)
            {
                var textChipheredWithSameKeyCharacter = ciphertext.SplitEveryNthCharacter(i, keyLength);
                var maxCoincidenceNumber = (сoincidenceNumber: 0, character: '\0');

                for (int k = 0; k < 128; k++)
                {
                    var coincidenceNumber = textChipheredWithSameKeyCharacter.CountCoincidenceNumber(k);

                    if (coincidenceNumber > maxCoincidenceNumber.сoincidenceNumber)
                    {
                        maxCoincidenceNumber = (coincidenceNumber, (char)k);
                    }

                }

                key += maxCoincidenceNumber.character;
            }

            return ciphertext.DecodeXorCipher(key);
        }

        public static string SplitEveryNthCharacter(this string text, int startIndex, int period)
        {
            var result = string.Empty;

            for (int j = startIndex; j < text.Length; j += period)
            {
                result += text[j];
            }

            return result;
        }

        public static int CountCoincidenceNumber(this string text, int keyChar) 
        {
            var coincidenceNumber = 0;

            for (int s = 0; s < text.Length; s++)
            {
                var decodedChar = (char)(text[s] ^ keyChar);

                if (char.IsLetter(decodedChar) || char.IsPunctuation(decodedChar) || char.IsWhiteSpace(decodedChar))
                {
                    coincidenceNumber++;
                }
            }

            return coincidenceNumber;
        }
    }
}
