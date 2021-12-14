using BCryptH = BCrypt.Net.BCrypt;
using System;
using System.Security.Cryptography;
using System.Text;
using System.IO;

namespace Lab4
{
    public class HashGenerator
    {
        private readonly MD5 md5 = MD5.Create();
        public string WeakPasswordPath { get; set; } = "../../../Passwords/weak.csv";
        public string StrongPasswordPath { get; set; } = "../../../Passwords/strong.csv";

        public string GetMd5Hash(string input)
        {
            var date = md5.ComputeHash(Encoding.UTF8.GetBytes(input));
            var hexDate = Convert.ToHexString(date);

            WriteToFile(WeakPasswordPath, hexDate);

            return hexDate;
        }
        public bool VerifyMd5Hash(string input, string hash)
        {
            var hashOfInput = GetMd5Hash(input);
            StringComparer comparer = StringComparer.OrdinalIgnoreCase;

            return comparer.Compare(hashOfInput, hash) == 0;
        }

        public (string hash, string salt) GetBCryptHash(string input) 
        {
            string salt = BCryptH.GenerateSalt();
            string hash = BCryptH.HashPassword(input, salt);

            WriteToFile(StrongPasswordPath, "salt: " + salt + " hash: " + hash);

            return (hash, salt);
        }

        public void WriteToFile(string path, string input) 
        {

            using StreamWriter file = new(path, append: true);

            file.WriteLineAsync(input);
        }
    }
}
