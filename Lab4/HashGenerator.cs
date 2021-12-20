using BCryptH = BCrypt.Net.BCrypt;
using System;
using System.Security.Cryptography;
using System.Text;
using System.IO;
using System.Collections.Generic;

namespace Lab4
{
    public class HashGenerator
    {
        private readonly MD5 md5 = MD5.Create();
        public string WeakPasswordPath { get; set; } = "../../../Passwords/weak.csv";
        public string StrongPasswordPath { get; set; } = "../../../Passwords/strong.csv";

        public void GetMd5Hash(List<string> passwords)
        {
            using StreamWriter file = new(WeakPasswordPath, append: true);

            foreach (var p in passwords)
            {
                var date = md5.ComputeHash(Encoding.UTF8.GetBytes(p));
                var hexDate = Convert.ToHexString(date);

                file.WriteLine(hexDate);
            }
        }

        public void GetBCryptHash(List<string> passwords) 
        {
            using StreamWriter file = new(StrongPasswordPath, append: true);
            
            foreach (var p in passwords) 
            {
                string salt = BCryptH.GenerateSalt();
                string hash = BCryptH.HashPassword(p, salt);

                file.WriteLine(hash);
            }
        }
    }
}
