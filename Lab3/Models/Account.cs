using System.Text.Json.Serialization;

namespace Lab3.Models
{
    public class Account
    {
        [JsonPropertyName("id")]
        public string AccountId { get; set; }

        [JsonPropertyName("money")]
        public long Money { get; set; }

        [JsonPropertyName("deletionTime")]
        public string DeletionTime { get; set; }
    }
}
