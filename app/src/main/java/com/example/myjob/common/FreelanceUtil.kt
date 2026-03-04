import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.feature.signup.RoleSection
import kotlinx.serialization.Serializable

// Data Models
@Serializable
data class FreelanceSector(
    var id: String = "",
    var name: String = "",
    var icon: String = "",
    var description: String = "",
    var services: List<FreelanceService> = emptyList()
)

@Serializable
data class FreelancesSector(
    var id: String = "",
    var name: String = "",
    var icon: String = "",
    var description: String = ""
)

@Serializable
data class FreelanceService(
    var id: String = "",
    var name: String = "",
    var icon: String = "",
    var description: String = "",
    var averagePrice: String? = null
)

// Main Freelance Sectors with Their Services
fun getAllFreelanceSectors(): List<FreelanceSector> {
    return listOf(
        // 1. Bâtiment & Construction
        FreelanceSector(
            id = "batiment",
            name = "Bâtiment & Construction",
            icon = "🏗️",
            description = "Travaux de construction et rénovation",
            services = listOf(
                FreelanceService(
                    "menuisier",
                    "Menuisier",
                    "🔨",
                    "Fabrication et installation de portes, fenêtres, escaliers",
                    "50-150 DT/jour"
                ),
                FreelanceService(
                    "plombier",
                    "Plombier",
                    "🔧",
                    "Installation et réparation sanitaire, tuyauterie",
                    "40-120 DT"
                ),
                FreelanceService(
                    "electricien",
                    "Électricien",
                    "💡",
                    "Installation électrique, dépannage, tableaux",
                    "50-150 DT"
                ),
                FreelanceService(
                    "peintre",
                    "Peintre",
                    "🎨",
                    "Peinture intérieure/extérieure, décoration murale",
                    "30-100 DT/jour"
                ),
                FreelanceService(
                    "macon",
                    "Maçon",
                    "🧱",
                    "Construction murs, dalles, fondations",
                    "60-150 DT/jour"
                ),
                FreelanceService(
                    "carreleur",
                    "Carreleur",
                    "⬜",
                    "Pose de carrelage, faïence, marbre",
                    "40-120 DT/jour"
                ),
                FreelanceService(
                    "platrier",
                    "Plâtrier",
                    "🏗️",
                    "Plâtrerie, faux plafonds, cloisons",
                    "40-100 DT/jour"
                ),
                FreelanceService(
                    "soudeur",
                    "Soudeur",
                    "⚡",
                    "Soudure métallique, fer forgé, aluminium",
                    "50-120 DT"
                ),
                FreelanceService(
                    "vitrier",
                    "Vitrier",
                    "🪟",
                    "Installation et réparation de vitrerie",
                    "40-100 DT"
                ),
                FreelanceService(
                    "charpentier",
                    "Charpentier",
                    "🪵",
                    "Construction de charpentes en bois",
                    "60-140 DT/jour"
                ),
                FreelanceService(
                    "couvreur",
                    "Couvreur",
                    "🏠",
                    "Réparation et installation de toitures",
                    "50-130 DT/jour"
                ),
                FreelanceService(
                    "ferronnier",
                    "Ferronnier",
                    "⚒️",
                    "Fabrication de portails, grilles, garde-corps",
                    "60-150 DT"
                ),
                FreelanceService(
                    "installateur_climatisation",
                    "Installateur Climatisation",
                    "❄️",
                    "Installation et maintenance clim",
                    "80-200 DT"
                )
            )
        ),

        // 2. Jardinage & Espaces Verts
        FreelanceSector(
            id = "jardinage",
            name = "Jardinage & Espaces Verts",
            icon = "🌱",
            description = "Entretien et aménagement d'espaces verts",
            services = listOf(
                FreelanceService(
                    "jardinier",
                    "Jardinier",
                    "🌿",
                    "Entretien de jardins, tonte, taille",
                    "30-80 DT/jour"
                ),
                FreelanceService(
                    "paysagiste",
                    "Paysagiste",
                    "🌳",
                    "Conception et aménagement paysager",
                    "80-200 DT"
                ),
                FreelanceService(
                    "elagueur",
                    "Élagueur",
                    "🌲",
                    "Élagage et abattage d'arbres",
                    "60-150 DT"
                ),
                FreelanceService(
                    "arrosage_automatique",
                    "Installateur Arrosage",
                    "💧",
                    "Systèmes d'arrosage automatique",
                    "70-180 DT"
                ),
                FreelanceService(
                    "entretien_piscine",
                    "Pisciniste",
                    "🏊",
                    "Entretien et nettoyage de piscines",
                    "50-120 DT"
                )
            )
        ),

        // 3. Nettoyage & Entretien
        FreelanceSector(
            id = "nettoyage",
            name = "Nettoyage & Entretien",
            icon = "🧹",
            description = "Services de nettoyage professionnel",
            services = listOf(
                FreelanceService(
                    "femme_menage",
                    "Femme de Ménage",
                    "🧽",
                    "Nettoyage domestique régulier",
                    "20-50 DT/jour"
                ),
                FreelanceService(
                    "nettoyage_profond",
                    "Nettoyage Profond",
                    "✨",
                    "Nettoyage en profondeur de locaux",
                    "80-200 DT"
                ),
                FreelanceService(
                    "nettoyage_vitres",
                    "Laveur de Vitres",
                    "🪟",
                    "Nettoyage de vitres et façades",
                    "40-100 DT"
                ),
                FreelanceService(
                    "desinfection",
                    "Désinfection",
                    "🦠",
                    "Désinfection et traitement antibactérien",
                    "100-250 DT"
                ),
                FreelanceService(
                    "nettoyage_apres_travaux",
                    "Nettoyage Après Travaux",
                    "🏗️",
                    "Nettoyage de chantier",
                    "80-200 DT"
                )
            )
        ),

        // 4. Déménagement & Transport
        FreelanceSector(
            id = "demenagement",
            name = "Déménagement & Transport",
            icon = "📦",
            description = "Services de déménagement et transport",
            services = listOf(
                FreelanceService(
                    "demenageur",
                    "Déménageur",
                    "🚚",
                    "Déménagement résidentiel et commercial",
                    "150-500 DT"
                ),
                FreelanceService(
                    "transporteur",
                    "Transporteur",
                    "🚛",
                    "Transport de marchandises",
                    "Variable"
                ),
                FreelanceService(
                    "montage_meubles",
                    "Monteur de Meubles",
                    "🛋️",
                    "Montage et démontage de meubles",
                    "30-80 DT"
                ),
                FreelanceService(
                    "garde_meuble",
                    "Garde-Meuble",
                    "📦",
                    "Stockage temporaire",
                    "Variable"
                )
            )
        ),

        // 5. Réparation & Maintenance
        FreelanceSector(
            id = "reparation",
            name = "Réparation & Maintenance",
            icon = "🔧",
            description = "Réparation d'appareils et équipements",
            services = listOf(
                FreelanceService(
                    "reparateur_electromenager",
                    "Réparateur Électroménager",
                    "🔌",
                    "Réparation frigo, machine à laver, etc.",
                    "40-150 DT"
                ),
                FreelanceService(
                    "reparateur_informatique",
                    "Réparateur Informatique",
                    "💻",
                    "Réparation PC, ordinateurs portables",
                    "30-100 DT"
                ),
                FreelanceService(
                    "reparateur_mobile",
                    "Réparateur Mobile",
                    "📱",
                    "Réparation smartphones et tablettes",
                    "30-200 DT"
                ),
                FreelanceService(
                    "serrurier",
                    "Serrurier",
                    "🔑",
                    "Ouverture de portes, changement de serrures",
                    "50-150 DT"
                ),
                FreelanceService(
                    "reparateur_tv",
                    "Réparateur TV",
                    "📺",
                    "Réparation télévisions et écrans",
                    "40-120 DT"
                ),
                FreelanceService(
                    "couturier",
                    "Couturier/Couturière",
                    "🧵",
                    "Retouches et réparations de vêtements",
                    "10-50 DT"
                ),
                FreelanceService(
                    "cordonnier",
                    "Cordonnier",
                    "👞",
                    "Réparation de chaussures et maroquinerie",
                    "10-40 DT"
                )
            )
        ),

        // 6. Automobile
        FreelanceSector(
            id = "automobile",
            name = "Automobile",
            icon = "🚗",
            description = "Services automobiles",
            services = listOf(
                FreelanceService(
                    "mecanicien",
                    "Mécanicien Auto",
                    "🔧",
                    "Réparation et entretien automobile",
                    "50-200 DT"
                ),
                FreelanceService(
                    "carrossier",
                    "Carrossier",
                    "🚗",
                    "Réparation de carrosserie",
                    "100-500 DT"
                ),
                FreelanceService(
                    "electricien_auto",
                    "Électricien Auto",
                    "⚡",
                    "Réparation électrique automobile",
                    "40-150 DT"
                ),
                FreelanceService(
                    "vitrage_auto",
                    "Vitrage Automobile",
                    "🪟",
                    "Remplacement pare-brise",
                    "80-300 DT"
                ),
                FreelanceService(
                    "lavage_auto",
                    "Lavage Auto",
                    "🚿",
                    "Lavage et nettoyage de véhicules",
                    "15-50 DT"
                ),
                FreelanceService(
                    "depanneur",
                    "Dépanneur",
                    "🆘",
                    "Dépannage et remorquage",
                    "80-200 DT"
                )
            )
        ),

        // 7. Services à Domicile
        FreelanceSector(
            id = "services_domicile",
            name = "Services à Domicile",
            icon = "🏡",
            description = "Assistance et services personnels",
            services = listOf(
                FreelanceService(
                    "garde_enfants",
                    "Garde d'Enfants",
                    "👶",
                    "Baby-sitting et garde d'enfants",
                    "15-40 DT/heure"
                ),
                FreelanceService(
                    "aide_personnes_agees",
                    "Aide aux Personnes Âgées",
                    "👴",
                    "Assistance et accompagnement",
                    "25-60 DT/jour"
                ),
                FreelanceService(
                    "aide_menagere",
                    "Aide Ménagère",
                    "🏠",
                    "Aide aux tâches domestiques",
                    "20-50 DT/jour"
                ),
                FreelanceService(
                    "cuisinier_domicile",
                    "Cuisinier à Domicile",
                    "👨‍🍳",
                    "Préparation de repas à domicile",
                    "50-150 DT"
                ),
                FreelanceService(
                    "chauffeur_prive",
                    "Chauffeur Privé",
                    "🚗",
                    "Service de chauffeur personnel",
                    "30-80 DT/heure"
                ),
                FreelanceService(
                    "professeur_particulier",
                    "Professeur Particulier",
                    "📚",
                    "Cours de soutien scolaire",
                    "20-60 DT/heure"
                )
            )
        ),

        // 8. Événementiel
        FreelanceSector(
            id = "evenementiel",
            name = "Événementiel",
            icon = "🎉",
            description = "Organisation d'événements",
            services = listOf(
                FreelanceService(
                    "traiteur",
                    "Traiteur",
                    "🍽️",
                    "Service traiteur pour événements",
                    "Variable"
                ),
                FreelanceService(
                    "photographe",
                    "Photographe",
                    "📸",
                    "Photographie événementielle",
                    "200-800 DT"
                ),
                FreelanceService(
                    "videaste",
                    "Vidéaste",
                    "🎥",
                    "Vidéographie et montage",
                    "300-1000 DT"
                ),
                FreelanceService("dj", "DJ", "🎧", "Animation musicale", "200-600 DT"),
                FreelanceService(
                    "animateur",
                    "Animateur",
                    "🎤",
                    "Animation d'événements",
                    "100-400 DT"
                ),
                FreelanceService(
                    "decorateur",
                    "Décorateur",
                    "🎀",
                    "Décoration événementielle",
                    "150-500 DT"
                ),
                FreelanceService(
                    "maquilleur",
                    "Maquilleur/Maquilleuse",
                    "💄",
                    "Maquillage professionnel",
                    "50-200 DT"
                )
            )
        ),

        // 9. Beauté & Bien-être
        FreelanceSector(
            id = "beaute",
            name = "Beauté & Bien-être",
            icon = "💅",
            description = "Services de beauté à domicile",
            services = listOf(
                FreelanceService(
                    "coiffeur_domicile",
                    "Coiffeur à Domicile",
                    "💇",
                    "Coupe et coiffure à domicile",
                    "30-80 DT"
                ),
                FreelanceService(
                    "estheticienne",
                    "Esthéticienne",
                    "✨",
                    "Soins esthétiques à domicile",
                    "40-120 DT"
                ),
                FreelanceService(
                    "manucure",
                    "Manucure/Pédicure",
                    "💅",
                    "Soins des ongles",
                    "20-60 DT"
                ),
                FreelanceService(
                    "masseur",
                    "Masseur/Masseuse",
                    "💆",
                    "Massage thérapeutique ou relaxant",
                    "50-150 DT"
                ),
                FreelanceService(
                    "barbier",
                    "Barbier",
                    "💈",
                    "Coupe et entretien de barbe",
                    "15-40 DT"
                )
            )
        ),

        // 10. Informatique & Technologie
        FreelanceSector(
            id = "informatique",
            name = "Informatique & Technologie",
            icon = "💻",
            description = "Services informatiques",
            services = listOf(
                FreelanceService(
                    "technicien_informatique",
                    "Technicien Informatique",
                    "🖥️",
                    "Dépannage et maintenance PC",
                    "40-100 DT"
                ),
                FreelanceService(
                    "installateur_reseau",
                    "Installateur Réseau",
                    "📡",
                    "Installation réseau et Wi-Fi",
                    "60-150 DT"
                ),
                FreelanceService(
                    "installateur_antenne",
                    "Installateur Antenne",
                    "📡",
                    "Installation paraboles et antennes",
                    "40-100 DT"
                ),
                FreelanceService(
                    "recuperation_donnees",
                    "Récupération de Données",
                    "💾",
                    "Récupération de données perdues",
                    "80-300 DT"
                )
            )
        ),

        // 11. Artisanat
        FreelanceSector(
            id = "artisanat",
            name = "Artisanat",
            icon = "🎨",
            description = "Travaux artisanaux",
            services = listOf(
                FreelanceService(
                    "tapissier",
                    "Tapissier",
                    "🛋️",
                    "Réfection de sièges et meubles",
                    "50-200 DT"
                ),
                FreelanceService(
                    "miroitier",
                    "Miroitier",
                    "🪞",
                    "Installation et encadrement de miroirs",
                    "40-150 DT"
                ),
                FreelanceService(
                    "forgeron",
                    "Forgeron",
                    "⚒️",
                    "Travaux de forge artistique",
                    "80-250 DT"
                ),
                FreelanceService(
                    "potier",
                    "Potier",
                    "🏺",
                    "Création de poteries artisanales",
                    "Variable"
                ),
                FreelanceService(
                    "bijoutier",
                    "Bijoutier",
                    "💍",
                    "Création et réparation de bijoux",
                    "Variable"
                )
            )
        ),

        // 12. Sécurité & Surveillance
        FreelanceSector(
            id = "securite",
            name = "Sécurité & Surveillance",
            icon = "🔐",
            description = "Services de sécurité",
            services = listOf(
                FreelanceService(
                    "agent_securite",
                    "Agent de Sécurité",
                    "👮",
                    "Surveillance et sécurité",
                    "40-100 DT/jour"
                ),
                FreelanceService(
                    "installateur_alarme",
                    "Installateur Alarme",
                    "🚨",
                    "Installation systèmes d'alarme",
                    "80-250 DT"
                ),
                FreelanceService(
                    "installateur_camera",
                    "Installateur Caméra",
                    "📹",
                    "Installation caméras de surveillance",
                    "100-300 DT"
                ),
                FreelanceService(
                    "gardien",
                    "Gardien",
                    "🔐",
                    "Gardiennage de propriétés",
                    "30-80 DT/jour"
                )
            )
        ),

        // 13. Animaux
        FreelanceSector(
            id = "animaux",
            name = "Animaux",
            icon = "🐾",
            description = "Services pour animaux",
            services = listOf(
                FreelanceService(
                    "pet_sitter",
                    "Pet Sitter",
                    "🐕",
                    "Garde d'animaux à domicile",
                    "20-50 DT/jour"
                ),
                FreelanceService(
                    "promeneur_chien",
                    "Promeneur de Chiens",
                    "🦮",
                    "Promenade de chiens",
                    "10-30 DT"
                ),
                FreelanceService(
                    "toiletteur",
                    "Toiletteur",
                    "✂️",
                    "Toilettage d'animaux",
                    "30-80 DT"
                ),
                FreelanceService(
                    "dresseur",
                    "Dresseur",
                    "🎓",
                    "Éducation canine",
                    "50-150 DT/séance"
                )
            )
        ),

        // 14. Cours & Formation
        FreelanceSector(
            id = "cours",
            name = "Cours & Formation",
            icon = "📚",
            description = "Enseignement et formation",
            services = listOf(
                FreelanceService(
                    "prof_maths",
                    "Professeur de Mathématiques",
                    "➗",
                    "Cours particuliers de maths",
                    "20-60 DT/heure"
                ),
                FreelanceService(
                    "prof_langues",
                    "Professeur de Langues",
                    "🗣️",
                    "Cours de langues étrangères",
                    "25-70 DT/heure"
                ),
                FreelanceService(
                    "prof_musique",
                    "Professeur de Musique",
                    "🎵",
                    "Cours d'instrument ou chant",
                    "30-80 DT/heure"
                ),
                FreelanceService(
                    "prof_sport",
                    "Coach Sportif",
                    "💪",
                    "Entraînement personnel",
                    "40-100 DT/séance"
                ),
                FreelanceService(
                    "prof_informatique",
                    "Formateur Informatique",
                    "💻",
                    "Formation en informatique",
                    "30-80 DT/heure"
                ),
                FreelanceService(
                    "prof_arts",
                    "Professeur d'Arts",
                    "🎨",
                    "Cours de dessin et peinture",
                    "25-70 DT/heure"
                )
            )
        ),

        // 15. Autre
        FreelanceSector(
            id = "autre",
            name = "Autres Services",
            icon = "⚙️",
            description = "Services divers",
            services = listOf(
                FreelanceService(
                    "homme_toute_main",
                    "Homme Toute Main",
                    "🔧",
                    "Petits travaux et bricolage",
                    "30-80 DT/jour"
                ),
                FreelanceService(
                    "assistant_administratif",
                    "Assistant Administratif",
                    "📋",
                    "Aide administrative",
                    "25-60 DT/heure"
                ),
                FreelanceService(
                    "traducteur",
                    "Traducteur",
                    "🌐",
                    "Services de traduction",
                    "Variable"
                ),
                FreelanceService("coursier", "Coursier", "🚴", "Livraison et courses", "10-40 DT")
            )
        )
    )
}

fun getAllFreelanceServices(): List<FreelanceService> {
    val list = mutableListOf<FreelanceService>()
    getAllFreelanceSectors().map {
        list.addAll(it.services)
    }
    return list
}

// Helper Functions
fun searchServices(query: String): List<Pair<FreelanceSector, FreelanceService>> {
    val results = mutableListOf<Pair<FreelanceSector, FreelanceService>>()
    getAllFreelanceSectors().forEach { sector ->
        sector.services.forEach { service ->
            if (service.name.contains(query, ignoreCase = true) ||
                service.description.contains(query, ignoreCase = true)
            ) {
                results.add(Pair(sector, service))
            }
        }
    }
    return results
}

fun getSectorById(sectorId: String): FreelanceSector? {
    return getAllFreelanceSectors().find { it.id == sectorId }
}

// ============================================
// UI COMPOSABLES
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FreelanceSectorSelectionScreen(
    onSelectSector: (FreelanceSector) -> Unit = {},
    dismiss: () -> Unit = {}
) {

    var selectedSector by remember { mutableStateOf<FreelanceSector?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Choisir un secteur"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        dismiss()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Retour", tint = Color(0xFF049344))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SectorsGridView(
                onSectorClick = {
                    selectedSector = it
                    onSelectSector(selectedSector ?: FreelanceSector())
                }
            )
        }
    }
}

@Composable
fun FreelanceDialogItem(category: FreelanceService, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = category.icon, fontSize = 28.sp)
        Text(
            text = category.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937)
        )
    }
}

@Composable
fun FreelanceSectorDialogItem(category: FreelanceSector, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = category.icon, fontSize = 28.sp)
        Text(
            text = category.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreelanceServiceSelectionScreen(
    selectedSector: FreelanceSector,
    onSelectService: (FreelanceService) -> Unit = {},
    dismiss: () -> Unit = {}
) {
    var selectedService by remember { mutableStateOf<FreelanceService?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Choisir un service"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        dismiss()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Retour", tint = Color(0xFF049344))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ServicesListView(
                sector = selectedSector,
                onServiceClick = {
                    selectedService = it
                    selectedService?.let { service ->
                        onSelectService(service)
                    }
                }
            )
        }
    }
}

//FILTER
@Composable
fun ServicesFilterListView(
    list: List<FreelanceService>,
    listFilterService: MutableList<FreelanceService>,
    onServiceClick: (Int, FreelanceService) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        itemsIndexed(list) { index, service ->
            ServiceFilterCard(
                service = service,
                listFilterService = listFilterService,
                onClick = {
                    onServiceClick(index, service)
                }
            )
        }
    }
}

@Composable
fun SectorFilterListView(
    listSector: List<FreelanceSector>,
    listFilterSector: MutableList<FreelanceSector>,
    onSectorClick: (Int, FreelanceSector) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        itemsIndexed(listSector) { index, sector ->
            SectorFilterCard(
                sector,
                listFilterSector = listFilterSector,
                onClick = {
                    onSectorClick(index, sector)
                }
            )
        }
    }
}

@Composable
fun FreelanceFilterSection(
    hint: String,
    searchQuery: String,
    listFilter: List<String>,
    interactionSource: MutableInteractionSource,
    valueChanged: (String) -> Unit = {},
    clearSearch: () -> Unit = {},
    onClick: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            var selected by remember { mutableStateOf(0) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listFilter.forEachIndexed { index, lang ->
                    val isSelected = index == selected
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected)
                                    colorResource(id = R.color.whatsapp)
                                else
                                    Color(0xFFF3F4F6)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selected = index
                                onClick(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected)
                                Color(0xFFF3F4F6)
                            else
                                Color(0xFF6B7280)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    if (it.isEmpty()) clearSearch()
                    else valueChanged(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = {
                    Text(
                        "Rechercher un $hint",
                        color = Color(0xFF9CA3AF)
                    )
                },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color(0xFF049344)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { clearSearch() }) {
                            Icon(Icons.Filled.Close, null, tint = Color(0xFF6B7280))
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF049344),
                    unfocusedBorderColor = Color(0xFF049344)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreelanceFilterSectorScreen(
    listFilterSector: MutableList<FreelanceSector>,
    listFilterService: MutableList<FreelanceService>,
    onSelect: (Int, FreelanceSector?, FreelanceService?) -> Unit,
    onSelectListSector: (List<FreelanceSector>?, List<FreelanceService>?) -> Unit,
    dismiss: () -> Unit = {}
) {
    var selectedSector by remember { mutableStateOf<FreelanceSector?>(null) }
    var selectedService by remember { mutableStateOf<FreelanceService?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    var listSector by remember { mutableStateOf(getAllFreelanceSectors()) }
    var listService by remember { mutableStateOf(getAllFreelanceServices()) }
    var selectedIndex by remember { mutableIntStateOf(0) }


    Scaffold(
        bottomBar = {

            val isEnabled = if (selectedIndex == 0) listFilterSector.isNotEmpty()
            else listFilterService.isNotEmpty()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            if (selectedIndex == 0) {
                                onSelectListSector(listFilterSector, null)
                            } else {
                                onSelectListSector(null, listFilterService)
                            }

                            dismiss()
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF049344)
                        ),
                        enabled = isEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Approval,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Valider",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Button(
                        onClick = {
                            dismiss()
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Annuler",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            val listFilter = listOf("Secteur", "Service")

            Spacer(modifier = Modifier.height(10.dp))
            val text = if (selectedIndex == 0) "secteur" else "service"

            FreelanceFilterSection(
                hint = text,
                searchQuery = searchQuery,
                listFilter = listFilter,
                interactionSource = remember { MutableInteractionSource() },
                valueChanged = {
                    searchQuery = it

                    if (selectedIndex == 0) {
                        listSector = if (it.isEmpty()) getAllFreelanceSectors()
                        else listSector.filter { sector ->
                            sector.name.contains(
                                it,
                                ignoreCase = true
                            ) || sector.description.contains(it, ignoreCase = true)
                        }
                    } else {
                        listService = if (it.isEmpty()) getAllFreelanceServices()
                        else listService.filter { sector ->
                            sector.name.contains(
                                it,
                                ignoreCase = true
                            ) || sector.description.contains(it, ignoreCase = true)
                        }
                    }
                },
                clearSearch = {
                    searchQuery = ""
                },
                onClick = {
                    selectedIndex = it
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (selectedIndex == 1) {
                ServicesFilterListView(
                    listService,
                    listFilterService = listFilterService,
                    onServiceClick = { index, service ->
                        selectedService = service
                        onSelect(index, null, service)
                    }
                )
            } else {
                SectorFilterListView(
                    listSector,
                    listFilterSector = listFilterSector,
                    onSectorClick = { index, sector ->
                        selectedSector = sector
                        onSelect(index, sector, null)
                    })
            }
        }
    }
}

@Composable
fun SectorsFilterGridView(
    listSector: List<FreelanceSector>,
    listFilterSector: MutableList<FreelanceSector>,
    onSectorClick: (Int, FreelanceSector) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(listSector) { index, sector ->
            SectorCard(
                sector = sector,
                listFilterSector = listFilterSector,
                onClick = { onSectorClick(index, sector) })
        }
    }
}

@Composable
fun SectorFilterCard(
    sector: FreelanceSector,
    listFilterSector: MutableList<FreelanceSector>,
    onClick: () -> Unit
) {

    val borderColor = if (listFilterSector.contains(sector)) Color(0xFF049344) else Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF049344).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = sector.icon, fontSize = 28.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sector.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = sector.description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${sector.services.size} services",
                        fontSize = 12.sp,
                        color = Color(0xFF049344),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceFilterCard(
    service: FreelanceService,
    listFilterService: MutableList<FreelanceService>,
    onClick: () -> Unit
) {

    val borderColor = if (listFilterService.contains(service)) Color(0xFF049344) else Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF049344).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = service.icon, fontSize = 28.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = service.description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        maxLines = 2
                    )

                    if (service.averagePrice != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💰 ${service.averagePrice}",
                            fontSize = 12.sp,
                            color = Color(0xFF049344),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

//END FILTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreelanceSelectionScreen() {
    var selectedSector by remember { mutableStateOf<FreelanceSector?>(null) }
    var selectedService by remember { mutableStateOf<FreelanceService?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            selectedService != null -> selectedService!!.name
                            selectedSector != null -> selectedSector!!.name
                            else -> "Choisir un service"
                        }
                    )
                },
                navigationIcon = {
                    if (selectedSector != null) {
                        IconButton(onClick = {
                            if (selectedService != null) {
                                selectedService = null
                            } else {
                                selectedSector = null
                            }
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Retour", tint = Color(0xFF049344))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                selectedService != null -> {
                    ServiceDetailView(service = selectedService!!)
                }

                selectedSector != null -> {
                    ServicesListView(
                        sector = selectedSector!!,
                        onServiceClick = { selectedService = it }
                    )
                }

                else -> {
                    SectorsGridView(
                        onSectorClick = { selectedSector = it }
                    )
                }
            }
        }
    }
}

@Composable
fun SectorsGridView(onSectorClick: (FreelanceSector) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(getAllFreelanceSectors()) { sector ->
            SectorCard(sector = sector, onClick = { onSectorClick(sector) })
        }
    }
}

@Composable
fun SectorCard(
    sector: FreelanceSector,
    listFilterSector: MutableList<FreelanceSector> = mutableListOf(),
    onClick: () -> Unit
) {

    val borderColor = if (listFilterSector.contains(sector)) Color(0xFF049344) else Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = sector.icon,
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = sector.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${sector.services.size} services",
                fontSize = 11.sp,
                color = Color(0xFF049344),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ServicesListView(sector: FreelanceSector, onServiceClick: (FreelanceService) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF049344).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = sector.icon, fontSize = 40.sp)
                    Column {
                        Text(
                            text = sector.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = sector.description,
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        items(sector.services) { service ->
            ServiceCard(service = service, onClick = { onServiceClick(service) })
        }
    }
}

@Composable
fun ServiceCard(service: FreelanceService, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF049344).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = service.icon, fontSize = 28.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = service.description,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2
                )
                if (service.averagePrice != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💰 ${service.averagePrice}",
                        fontSize = 12.sp,
                        color = Color(0xFF049344),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceDetailView(service: FreelanceService) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF049344).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = service.icon, fontSize = 56.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = service.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = service.description,
                        fontSize = 15.sp,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )

                    if (service.averagePrice != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF049344).copy(alpha = 0.1f))
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Prix moyen: ${service.averagePrice}",
                                fontSize = 15.sp,
                                color = Color(0xFF049344),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Cette fonctionnalité vous permettrait de :",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoItem("👀", "Voir les professionnels disponibles")
                    InfoItem("⭐", "Consulter les avis et notes")
                    InfoItem("💬", "Contacter directement")
                    InfoItem("📅", "Réserver un rendez-vous")
                    InfoItem("💰", "Comparer les tarifs")
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = icon, fontSize = 24.sp)
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF4B5563)
        )
    }
}