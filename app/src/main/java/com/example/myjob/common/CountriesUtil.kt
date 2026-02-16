package com.example.myjob.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data Models
data class Country(
    val code: String,
    val nameEn: String,
    val nameFr: String,
    val flag: String,
    val phoneCode: String,
    val cities: List<String>
)

// Comprehensive list of countries with their major cities
fun getAllCountries(): List<Country> {
    return listOf(
        // North Africa (Maghreb)
        Country(
            code = "TN",
            nameEn = "Tunisia",
            nameFr = "Tunisie",
            flag = "🇹🇳",
            phoneCode = "+216",
            cities = listOf(
                "Tunis", "Sfax", "Sousse", "Kairouan", "Bizerte", "Gabès", "Ariana",
                "Gafsa", "Monastir", "Ben Arous", "Kasserine", "Médenine", "Nabeul",
                "Tataouine", "Béja", "Jendouba", "Mahdia", "Siliana", "Kébili",
                "Zaghouan", "Manouba", "Tozeur", "Sidi Bouzid", "La Marsa",
                "Hammam-Lif", "Kélibia", "Hammamet", "Djerba", "Grombalia"
            )
        ),
        Country(
            code = "DZ",
            nameEn = "Algeria",
            nameFr = "Algérie",
            flag = "🇩🇿",
            phoneCode = "+213",
            cities = listOf(
                "Alger", "Oran", "Constantine", "Annaba", "Blida", "Batna",
                "Sétif", "Sidi Bel Abbès", "Biskra", "Tébessa", "Tlemcen",
                "Béjaïa", "Tiaret", "Bordj Bou Arreridj", "Tizi Ouzou",
                "Mostaganem", "Médéa", "El Oued", "Skikda", "Chlef"
            )
        ),
        Country(
            code = "MA",
            nameEn = "Morocco",
            nameFr = "Maroc",
            flag = "🇲🇦",
            phoneCode = "+212",
            cities = listOf(
                "Casablanca", "Rabat", "Fès", "Marrakech", "Agadir", "Tanger",
                "Meknès", "Oujda", "Kénitra", "Tétouan", "Safi", "Temara",
                "Mohammedia", "Khouribga", "El Jadida", "Béni Mellal", "Nador",
                "Taza", "Settat", "Larache"
            )
        ),
        Country(
            code = "LY",
            nameEn = "Libya",
            nameFr = "Libye",
            flag = "🇱🇾",
            phoneCode = "+218",
            cities = listOf(
                "Tripoli", "Benghazi", "Misrata", "Tarhuna", "Al Bayda",
                "Zawiya", "Tobruk", "Ajdabiya", "Sirte", "Sabha"
            )
        ),
        Country(
            code = "EG",
            nameEn = "Egypt",
            nameFr = "Égypte",
            flag = "🇪🇬",
            phoneCode = "+20",
            cities = listOf(
                "Cairo", "Alexandria", "Giza", "Shubra El Kheima", "Port Said",
                "Suez", "Luxor", "Aswan", "Mansoura", "Tanta", "Asyut",
                "Ismailia", "Faiyum", "Zagazig", "Damanhur", "Minya",
                "Hurghada", "Sharm El Sheikh", "Marsa Alam"
            )
        ),

        // Middle East
        Country(
            code = "SA",
            nameEn = "Saudi Arabia",
            nameFr = "Arabie Saoudite",
            flag = "🇸🇦",
            phoneCode = "+966",
            cities = listOf(
                "Riyadh", "Jeddah", "Mecca", "Medina", "Dammam", "Khobar",
                "Tabuk", "Buraidah", "Khamis Mushait", "Hail", "Najran",
                "Jizan", "Yanbu", "Abha", "Dhahran"
            )
        ),
        Country(
            code = "AE",
            nameEn = "United Arab Emirates",
            nameFr = "Émirats Arabes Unis",
            flag = "🇦🇪",
            phoneCode = "+971",
            cities = listOf(
                "Dubai", "Abu Dhabi", "Sharjah", "Al Ain", "Ajman",
                "Ras Al Khaimah", "Fujairah", "Umm Al Quwain"
            )
        ),
        Country(
            code = "QA",
            nameEn = "Qatar",
            nameFr = "Qatar",
            flag = "🇶🇦",
            phoneCode = "+974",
            cities = listOf(
                "Doha", "Al Rayyan", "Al Wakrah", "Al Khor", "Dukhan",
                "Mesaieed", "Umm Salal", "Lusail"
            )
        ),
        Country(
            code = "KW",
            nameEn = "Kuwait",
            nameFr = "Koweït",
            flag = "🇰🇼",
            phoneCode = "+965",
            cities = listOf(
                "Kuwait City", "Hawalli", "Salmiya", "Farwaniya", "Fahaheel",
                "Jahra", "Ahmadi", "Mangaf"
            )
        ),
        Country(
            code = "OM",
            nameEn = "Oman",
            nameFr = "Oman",
            flag = "🇴🇲",
            phoneCode = "+968",
            cities = listOf(
                "Muscat", "Salalah", "Sohar", "Nizwa", "Sur", "Ibri",
                "Bahla", "Khasab", "Buraimi"
            )
        ),
        Country(
            code = "BH",
            nameEn = "Bahrain",
            nameFr = "Bahreïn",
            flag = "🇧🇭",
            phoneCode = "+973",
            cities = listOf(
                "Manama", "Muharraq", "Riffa", "Hamad Town", "Isa Town",
                "Sitra", "Budaiya", "Jidhafs"
            )
        ),
        Country(
            code = "JO",
            nameEn = "Jordan",
            nameFr = "Jordanie",
            flag = "🇯🇴",
            phoneCode = "+962",
            cities = listOf(
                "Amman", "Zarqa", "Irbid", "Aqaba", "Russeifa", "Madaba",
                "Jerash", "Karak", "Petra", "Wadi Musa"
            )
        ),
        Country(
            code = "LB",
            nameEn = "Lebanon",
            nameFr = "Liban",
            flag = "🇱🇧",
            phoneCode = "+961",
            cities = listOf(
                "Beirut", "Tripoli", "Sidon", "Tyre", "Nabatieh", "Jounieh",
                "Zahle", "Baalbek", "Byblos", "Batroun"
            )
        ),
        Country(
            code = "SY",
            nameEn = "Syria",
            nameFr = "Syrie",
            flag = "🇸🇾",
            phoneCode = "+963",
            cities = listOf(
                "Damascus", "Aleppo", "Homs", "Latakia", "Hama", "Deir ez-Zor",
                "Raqqa", "Tartus", "Idlib", "Daraa"
            )
        ),
        Country(
            code = "IQ",
            nameEn = "Iraq",
            nameFr = "Irak",
            flag = "🇮🇶",
            phoneCode = "+964",
            cities = listOf(
                "Baghdad", "Basra", "Mosul", "Erbil", "Sulaymaniyah", "Najaf",
                "Karbala", "Kirkuk", "Nasiriyah", "Ramadi"
            )
        ),
        Country(
            code = "PS",
            nameEn = "Palestine",
            nameFr = "Palestine",
            flag = "🇵🇸",
            phoneCode = "+970",
            cities = listOf(
                "Jerusalem", "Gaza", "Ramallah", "Hebron", "Nablus", "Bethlehem",
                "Khan Yunis", "Rafah", "Jenin", "Jericho"
            )
        ),

        // Europe
        Country(
            code = "FR",
            nameEn = "France",
            nameFr = "France",
            flag = "🇫🇷",
            phoneCode = "+33",
            cities = listOf(
                "Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes",
                "Strasbourg", "Montpellier", "Bordeaux", "Lille", "Rennes",
                "Reims", "Le Havre", "Saint-Étienne", "Toulon", "Grenoble",
                "Dijon", "Angers", "Nîmes", "Villeurbanne"
            )
        ),
        Country(
            code = "DE",
            nameEn = "Germany",
            nameFr = "Allemagne",
            flag = "🇩🇪",
            phoneCode = "+49",
            cities = listOf(
                "Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart",
                "Düsseldorf", "Dortmund", "Essen", "Leipzig", "Bremen",
                "Dresden", "Hanover", "Nuremberg", "Duisburg", "Bochum"
            )
        ),
        Country(
            code = "IT",
            nameEn = "Italy",
            nameFr = "Italie",
            flag = "🇮🇹",
            phoneCode = "+39",
            cities = listOf(
                "Rome", "Milan", "Naples", "Turin", "Palermo", "Genoa",
                "Bologna", "Florence", "Venice", "Verona", "Catania",
                "Bari", "Messina", "Padua", "Trieste", "Brescia"
            )
        ),
        Country(
            code = "ES",
            nameEn = "Spain",
            nameFr = "Espagne",
            flag = "🇪🇸",
            phoneCode = "+34",
            cities = listOf(
                "Madrid", "Barcelona", "Valencia", "Seville", "Zaragoza",
                "Málaga", "Murcia", "Palma", "Las Palmas", "Bilbao",
                "Alicante", "Córdoba", "Valladolid", "Granada", "Vigo"
            )
        ),
        Country(
            code = "GB",
            nameEn = "United Kingdom",
            nameFr = "Royaume-Uni",
            flag = "🇬🇧",
            phoneCode = "+44",
            cities = listOf(
                "London", "Birmingham", "Manchester", "Glasgow", "Liverpool",
                "Leeds", "Sheffield", "Edinburgh", "Bristol", "Cardiff",
                "Belfast", "Leicester", "Nottingham", "Newcastle", "Brighton"
            )
        ),
        Country(
            code = "BE",
            nameEn = "Belgium",
            nameFr = "Belgique",
            flag = "🇧🇪",
            phoneCode = "+32",
            cities = listOf(
                "Brussels", "Antwerp", "Ghent", "Charleroi", "Liège", "Bruges",
                "Namur", "Leuven", "Mons", "Mechelen", "Aalst", "Kortrijk"
            )
        ),
        Country(
            code = "NL",
            nameEn = "Netherlands",
            nameFr = "Pays-Bas",
            flag = "🇳🇱",
            phoneCode = "+31",
            cities = listOf(
                "Amsterdam", "Rotterdam", "The Hague", "Utrecht", "Eindhoven",
                "Tilburg", "Groningen", "Almere", "Breda", "Nijmegen"
            )
        ),
        Country(
            code = "CH",
            nameEn = "Switzerland",
            nameFr = "Suisse",
            flag = "🇨🇭",
            phoneCode = "+41",
            cities = listOf(
                "Zurich", "Geneva", "Basel", "Lausanne", "Bern", "Winterthur",
                "Lucerne", "St. Gallen", "Lugano", "Biel/Bienne"
            )
        ),
        Country(
            code = "SE",
            nameEn = "Sweden",
            nameFr = "Suède",
            flag = "🇸🇪",
            phoneCode = "+46",
            cities = listOf(
                "Stockholm", "Gothenburg", "Malmö", "Uppsala", "Västerås",
                "Örebro", "Linköping", "Helsingborg", "Jönköping", "Norrköping"
            )
        ),
        Country(
            code = "NO",
            nameEn = "Norway",
            nameFr = "Norvège",
            flag = "🇳🇴",
            phoneCode = "+47",
            cities = listOf(
                "Oslo", "Bergen", "Trondheim", "Stavanger", "Drammen",
                "Fredrikstad", "Kristiansand", "Tromsø", "Sandnes"
            )
        ),

        // North America
        Country(
            code = "US",
            nameEn = "United States",
            nameFr = "États-Unis",
            flag = "🇺🇸",
            phoneCode = "+1",
            cities = listOf(
                "New York", "Los Angeles", "Chicago", "Houston", "Phoenix",
                "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
                "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte",
                "San Francisco", "Indianapolis", "Seattle", "Denver", "Washington DC",
                "Boston", "Nashville", "Detroit", "Portland", "Las Vegas", "Miami"
            )
        ),
        Country(
            code = "CA",
            nameEn = "Canada",
            nameFr = "Canada",
            flag = "🇨🇦",
            phoneCode = "+1",
            cities = listOf(
                "Toronto", "Montreal", "Vancouver", "Calgary", "Edmonton",
                "Ottawa", "Winnipeg", "Quebec City", "Hamilton", "Kitchener",
                "London", "Victoria", "Halifax", "Oshawa", "Windsor"
            )
        ),
        Country(
            code = "MX",
            nameEn = "Mexico",
            nameFr = "Mexique",
            flag = "🇲🇽",
            phoneCode = "+52",
            cities = listOf(
                "Mexico City", "Guadalajara", "Monterrey", "Puebla", "Tijuana",
                "León", "Ciudad Juárez", "Zapopan", "Mérida", "Cancún"
            )
        ),

        // Asia
        Country(
            code = "CN",
            nameEn = "China",
            nameFr = "Chine",
            flag = "🇨🇳",
            phoneCode = "+86",
            cities = listOf(
                "Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Chengdu",
                "Tianjin", "Chongqing", "Wuhan", "Xi'an", "Hangzhou",
                "Nanjing", "Suzhou", "Dongguan", "Qingdao", "Dalian"
            )
        ),
        Country(
            code = "JP",
            nameEn = "Japan",
            nameFr = "Japon",
            flag = "🇯🇵",
            phoneCode = "+81",
            cities = listOf(
                "Tokyo", "Yokohama", "Osaka", "Nagoya", "Sapporo", "Fukuoka",
                "Kobe", "Kyoto", "Kawasaki", "Saitama", "Hiroshima"
            )
        ),
        Country(
            code = "KR",
            nameEn = "South Korea",
            nameFr = "Corée du Sud",
            flag = "🇰🇷",
            phoneCode = "+82",
            cities = listOf(
                "Seoul", "Busan", "Incheon", "Daegu", "Daejeon", "Gwangju",
                "Suwon", "Ulsan", "Changwon", "Goyang"
            )
        ),
        Country(
            code = "IN",
            nameEn = "India",
            nameFr = "Inde",
            flag = "🇮🇳",
            phoneCode = "+91",
            cities = listOf(
                "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Chennai",
                "Kolkata", "Pune", "Ahmedabad", "Jaipur", "Surat", "Lucknow"
            )
        ),
        Country(
            code = "SG",
            nameEn = "Singapore",
            nameFr = "Singapour",
            flag = "🇸🇬",
            phoneCode = "+65",
            cities = listOf(
                "Singapore", "Jurong", "Woodlands", "Tampines", "Bedok"
            )
        ),
        Country(
            code = "MY",
            nameEn = "Malaysia",
            nameFr = "Malaisie",
            flag = "🇲🇾",
            phoneCode = "+60",
            cities = listOf(
                "Kuala Lumpur", "George Town", "Johor Bahru", "Ipoh",
                "Shah Alam", "Petaling Jaya", "Malacca City", "Kota Kinabalu"
            )
        ),
        Country(
            code = "TH",
            nameEn = "Thailand",
            nameFr = "Thaïlande",
            flag = "🇹🇭",
            phoneCode = "+66",
            cities = listOf(
                "Bangkok", "Chiang Mai", "Phuket", "Pattaya", "Hat Yai",
                "Nakhon Ratchasima", "Udon Thani", "Khon Kaen"
            )
        ),
        Country(
            code = "TR",
            nameEn = "Turkey",
            nameFr = "Turquie",
            flag = "🇹🇷",
            phoneCode = "+90",
            cities = listOf(
                "Istanbul", "Ankara", "Izmir", "Bursa", "Adana", "Gaziantep",
                "Konya", "Antalya", "Diyarbakır", "Mersin", "Kayseri"
            )
        ),

        // Sub-Saharan Africa
        Country(
            code = "ZA",
            nameEn = "South Africa",
            nameFr = "Afrique du Sud",
            flag = "🇿🇦",
            phoneCode = "+27",
            cities = listOf(
                "Johannesburg", "Cape Town", "Durban", "Pretoria", "Port Elizabeth",
                "Bloemfontein", "East London", "Nelspruit", "Polokwane"
            )
        ),
        Country(
            code = "NG",
            nameEn = "Nigeria",
            nameFr = "Nigéria",
            flag = "🇳🇬",
            phoneCode = "+234",
            cities = listOf(
                "Lagos", "Kano", "Ibadan", "Abuja", "Port Harcourt",
                "Benin City", "Kaduna", "Enugu", "Zaria"
            )
        ),
        Country(
            code = "KE",
            nameEn = "Kenya",
            nameFr = "Kenya",
            flag = "🇰🇪",
            phoneCode = "+254",
            cities = listOf(
                "Nairobi", "Mombasa", "Kisumu", "Nakuru", "Eldoret",
                "Thika", "Malindi", "Kitale"
            )
        ),
        Country(
            code = "SN",
            nameEn = "Senegal",
            nameFr = "Sénégal",
            flag = "🇸🇳",
            phoneCode = "+221",
            cities = listOf(
                "Dakar", "Touba", "Thiès", "Kaolack", "Saint-Louis",
                "Ziguinchor", "Mbour", "Rufisque"
            )
        ),
        Country(
            code = "CI",
            nameEn = "Ivory Coast",
            nameFr = "Côte d'Ivoire",
            flag = "🇨🇮",
            phoneCode = "+225",
            cities = listOf(
                "Abidjan", "Bouaké", "Daloa", "Yamoussoukro", "San-Pédro",
                "Korhogo", "Man", "Gagnoa"
            )
        ),

        // Oceania
        Country(
            code = "AU",
            nameEn = "Australia",
            nameFr = "Australie",
            flag = "🇦🇺",
            phoneCode = "+61",
            cities = listOf(
                "Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide",
                "Gold Coast", "Canberra", "Newcastle", "Wollongong"
            )
        ),
        Country(
            code = "NZ",
            nameEn = "New Zealand",
            nameFr = "Nouvelle-Zélande",
            flag = "🇳🇿",
            phoneCode = "+64",
            cities = listOf(
                "Auckland", "Wellington", "Christchurch", "Hamilton",
                "Tauranga", "Dunedin", "Palmerston North", "Napier"
            )
        ),

        // South America
        Country(
            code = "BR",
            nameEn = "Brazil",
            nameFr = "Brésil",
            flag = "🇧🇷",
            phoneCode = "+55",
            cities = listOf(
                "São Paulo", "Rio de Janeiro", "Brasília", "Salvador",
                "Fortaleza", "Belo Horizonte", "Manaus", "Curitiba", "Recife"
            )
        ),
        Country(
            code = "AR",
            nameEn = "Argentina",
            nameFr = "Argentine",
            flag = "🇦🇷",
            phoneCode = "+54",
            cities = listOf(
                "Buenos Aires", "Córdoba", "Rosario", "Mendoza", "La Plata",
                "San Miguel de Tucumán", "Mar del Plata", "Salta"
            )
        )
    )
}

// Helper functions
fun getCountriesByRegion(): Map<String, List<Country>> {
    val countries = getAllCountries()
    return mapOf(
        "Afrique du Nord" to countries.filter {
            it.code in listOf("TN", "DZ", "MA", "LY", "EG")
        },
        "Moyen-Orient" to countries.filter {
            it.code in listOf("SA", "AE", "QA", "KW", "OM", "BH", "JO", "LB", "SY", "IQ", "PS")
        },
        "Europe" to countries.filter {
            it.code in listOf("FR", "DE", "IT", "ES", "GB", "BE", "NL", "CH", "SE", "NO")
        },
        "Amérique du Nord" to countries.filter {
            it.code in listOf("US", "CA", "MX")
        },
        "Asie" to countries.filter {
            it.code in listOf("CN", "JP", "KR", "IN", "SG", "MY", "TH", "TR")
        },
        "Afrique Subsaharienne" to countries.filter {
            it.code in listOf("ZA", "NG", "KE", "SN", "CI")
        },
        "Océanie" to countries.filter {
            it.code in listOf("AU", "NZ")
        },
        "Amérique du Sud" to countries.filter {
            it.code in listOf("BR", "AR")
        }
    )
}

fun searchCountries(query: String): List<Country> {
    return getAllCountries().filter {
        it.nameEn.contains(query, ignoreCase = true) ||
                it.nameFr.contains(query, ignoreCase = true) ||
                it.code.contains(query, ignoreCase = true)
    }
}

fun getCitiesByCountry(countryCode: String): List<String> {
    return getAllCountries().find { it.code == countryCode }?.cities ?: emptyList()
}

@Composable
fun CountrySelector(
    onSelect: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onDismissRequest()
                        }
                )

                androidx.compose.material3.Text(
                    text = "Pays",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Rechercher un pays...") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val filteredCountries = if (searchQuery.isEmpty()) {
                getAllCountries()
            } else {
                searchCountries(searchQuery)
            }

            LazyColumn {
                items(filteredCountries.size) { index ->
                    val country = filteredCountries[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selectedCountry = country
                                onSelect(country.nameFr)
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = country.flag, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = country.nameFr,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${country.phoneCode} · ${country.cities.size} villes",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// Usage Example
@Composable
@Preview
fun CountryCitySelector() {
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(White)) {
        Text(
            text = "Sélectionnez un pays et une ville",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Rechercher un pays...") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Country selection
        if (selectedCountry == null) {
            val filteredCountries = if (searchQuery.isEmpty()) {
                getAllCountries()
            } else {
                searchCountries(searchQuery)
            }

            LazyColumn {
                items(filteredCountries.size) { index ->
                    val country = filteredCountries[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCountry = country }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = country.flag, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = country.nameFr,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${country.phoneCode} · ${country.cities.size} villes",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        } else {
            // City selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedCountry = null }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("←", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${selectedCountry!!.flag} ${selectedCountry!!.nameFr}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(selectedCountry!!.cities.size) { index ->
                    val city = selectedCountry!!.cities[index]
                    Text(
                        text = city,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCity = city }
                            .padding(vertical = 12.dp),
                        fontSize = 15.sp,
                        color = if (selectedCity == city) Color(0xFF049344) else Color.Black
                    )
                }
            }
        }
    }
}