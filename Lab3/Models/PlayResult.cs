using System.Text.Json.Serialization;

namespace Lab3.Models
{
    public class PlayResult
    {
        [JsonPropertyName("message")]
        public string Message { get; set;}

        [JsonPropertyName("realNumber")]
        public int RealNumber { get; set; }

        [JsonPropertyName("account")]
        public Account account { get; set; }
    }
}
