using System.IO;
using System.Linq;

namespace Lab4
{
    class Program
    {
        static void Main(string[] args)
        {
            var passwordNumber = 100;
            var passwordGenerator = new PasswordGenerator();
            var hashGenerator = new HashGenerator();

            var md5Passwords = passwordGenerator.GeneratePasswords(passwordNumber);
            var bCryptpasswords = passwordGenerator.GeneratePasswords(passwordNumber);

            for (int i = 0; i < passwordNumber; i++) 
            {
                hashGenerator.GetMd5Hash(md5Passwords[i]);
                hashGenerator.GetBCryptHash(bCryptpasswords[i]);
            }
        }
    }
}
