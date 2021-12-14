using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;

namespace Lab4
{
    public class PasswordGenerator
    {
        public string Top25PasswordsFilePath { get; set; } = "../../../TopPasswords/Top25Passwords.json";
        public string Top100kPasswordsFilePath { get; set; } = "../../../TopPasswords/Top100kPasswords.json";
        public IList<string> GeneratePasswords(int number) 
        {
            var passwords = new List<string>();
            var random = new Random();

            for (int i = 0; i < number; i++)
            {
                switch (random.Next(0, 100))
                {
                    case < 5:
                        passwords.Add(GenerateRandomPassword(true, true, true, true, false, random.Next(6, 12)));
                        break;
                    case >= 5 and < 15:
                        passwords.Add(GenerateTop25Password()); 
                        break;
                    case >= 15 and < 30:
                        passwords.Add(GenerateRandomPassword(true, true, true, true, true, random.Next(10, 20)));
                        break;
                    case >= 30 and < 100:
                        passwords.Add(GenerateMostCommom100kPassword()); 
                        break;
                };
            }

            return passwords;
        }

        public string GenerateTop25Password() 
        {
            var random = new Random();

            var passwords = ReadPasswordsFromFile(Top25PasswordsFilePath);
            var index = random.Next(passwords.Count);

            return passwords[index];
        }
        public string GenerateMostCommom100kPassword() 
        {
            var random = new Random();

            var passwords = ReadPasswordsFromFile(Top100kPasswordsFilePath);
            var index = random.Next(passwords.Count);

            return passwords[index];
        }

        public List<string> ReadPasswordsFromFile(string filePath) 
        {
            var jsonString = File.ReadAllText(filePath);

            return JsonSerializer.Deserialize<List<string>>(jsonString);
        }

        public string GenerateRandomPassword(bool IncludeWords, bool IncludeNumbers, bool IncludeLowercaseCharacters, bool IncludeUppercaseCharacters, bool IncludeSymbols, int passwordLength) 
        {
            var random = new Random();
            var password = string.Empty;
            var commonWords = new List<string> { "password", "qwerty", "monkey", "letmein", "dragon", "baseball", "master", "sunshine", "ashley", "bailey", "shadow", "superman", "michael", "Football", "welcome", "jesus", "michael", "ninja", "mustang", "princess", "access", "batman" };
            var numbers = "0123456789";
            var lowercaseCharacters = "abcdefghijklmnopqrstuvwxyz";
            var uppercaseCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            var symbols = "~`!@#$%^&*()_-\'+={[}]|\\:;\"<,>.?/";

            var word = string.Empty;

            if (IncludeWords && commonWords.Where(w => w.Length <= passwordLength).Any()) 
            {
                word = commonWords[random.Next(commonWords.Count)];

                while (word.Length > passwordLength) 
                {
                    word = commonWords[random.Next(commonWords.Count)];
                }
            }

            while(password.Length + word.Length < passwordLength)
            {
                switch (random.Next(0, 4))
                {
                    case 0 when IncludeNumbers == true:
                        password += numbers[random.Next(numbers.Length)];
                        break;
                    case 1 when IncludeLowercaseCharacters == true:
                        password += lowercaseCharacters[random.Next(lowercaseCharacters.Length)];
                        break;
                    case 2 when IncludeUppercaseCharacters == true:
                        password += uppercaseCharacters[random.Next(uppercaseCharacters.Length)];
                        break;
                    case 3 when IncludeSymbols == true:
                        password += symbols[random.Next(symbols.Length)];
                        break;
                };
            }

            return password.Insert(random.Next(password.Length), word);
        }
    }
}
