using Lab3.Models;
using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text.Json;
using System.Threading.Tasks;

namespace Lab3.BLL
{
    public class ServerConnection
    {
        private readonly HttpClient client = new HttpClient();

        public async Task<Account> CreateAccountAsync(string id) 
        {
            Account account = null;

            HttpResponseMessage response = await client.GetAsync($"http://95.217.177.249/casino/createacc?id={id}");

            if (response.IsSuccessStatusCode)
            {
                var httpContent = await response.Content.ReadAsStringAsync();
                account = JsonSerializer.Deserialize<Account>(httpContent); 
            }

            return account;
        }

        public async Task<PlayResult> MakeBetAsync(string playerId, int amountOfMoney, long theNumberYouBetOn, string mode)
        {
            PlayResult playResult = null;

            HttpResponseMessage response = await client.GetAsync($"http://95.217.177.249/casino/play{mode}?id={playerId}&bet={amountOfMoney}&number={theNumberYouBetOn}");

            if (response.IsSuccessStatusCode)
            {
                var httpContent = await response.Content.ReadAsStringAsync();
                playResult = JsonSerializer.Deserialize<PlayResult>(httpContent);
            }

            return playResult;
        }
    }
}
