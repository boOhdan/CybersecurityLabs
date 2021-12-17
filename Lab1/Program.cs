using System;
using System.Collections.Generic;
using System.Text;

namespace Lab1
{
    class Program
    {
        static void Main(string[] args)
        {
        }
        static string Task0(string binaryText)
        {
            return Base64Decoder.ConvertFromBinaryAndBase64(binaryText);
        }

        static string Task1(string hexText) 
        {
            var asciiText = string.Empty;

            for (int i = 0; i < hexText.Length; i += 2)
            {
                asciiText += (char)Convert.ToInt32(hexText.Substring(i, 2), 16);
            }

            return XorCipherDecoder.BruteForceDecode(asciiText).text;
        }

        static string Task2(string base64Text)
        {
            var data = Convert.FromBase64String(base64Text);
            var asciiText = Encoding.ASCII.GetString(data);

            VidenereCipherDecoder.FindKeyLength(asciiText);
            var keyLength = 3;

            return VidenereCipherDecoder.DecodeVigenereCipher(asciiText, keyLength);
        }
    }
}
