using System;

namespace Lab1
{
    public static class VidenereCipherDecoder
    {
        public static void FindKeyLength(string encodedText)
        {
            for (int i = 0; i < encodedText.Length; i++)
            {
                var n = 0;
                var offsetText = encodedText.Substring(encodedText.Length - i, i) + encodedText.Substring(0, encodedText.Length - i);

                for (int j = 0; j < encodedText.Length; j++)
                {
                    if (offsetText[j] == encodedText[j])
                        n++;
                }

                Console.WriteLine(n);
            }
        }

        public static string DecodeVigenereCipher(string ciphertext, int keyLength)
        {
            var key = string.Empty;

            for (int i = 0; i < keyLength; i++)
            {
                var textChipheredWithSameKeyCharacter = ciphertext.SplitEveryNthCharacter(i, keyLength);
                var maxCoincidenceNumber = (сoincidenceNumber: 0, character: '\0');

                for (int k = 0; k < 128; k++)
                {
                    var coincidenceNumber = textChipheredWithSameKeyCharacter.GetCoincidenceNumber(k);

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

        public static int GetCoincidenceNumber(this string text, int keyChar) 
        {
            var n = 0;

            for (int i = 0; i < text.Length; i++)
            {
                var ch = (char)(text[i] ^ keyChar);

                if (char.IsLetter(ch) || char.IsPunctuation(ch) || char.IsWhiteSpace(ch))
                {
                    n++;
                }
            }

            return n;
        }
    }
}
