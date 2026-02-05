package com.example.myjob.feature.demands

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.domain.entities.demands.MarketDemandModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Data Models

val entity = listOf(
    ServiceDemand(
        id = "1",
        title = "Installation d'une porte en bois massif",
        category = ServiceCategory.MENUISIER,
        description = "Je cherche un menuisier professionnel pour installer une porte en bois massif dans ma maison. Dimensions: 2m x 90cm. Le bois est déjà acheté, besoin uniquement de l'installation.",
        location = "Tunis, Carthage",
        budget = "300 DT",
        urgency = Urgency.MOYEN,
        status = DemandStatus.OUVERT,
        createdBy = "Karim M.",
        createdDate = Date(),
        deadline = "15/02/2026",
        phone = "+216 20 123 456"
    ),
    ServiceDemand(
        id = "2",
        title = "Réparation fuite d'eau cuisine",
        category = ServiceCategory.PLOMBIER,
        description = "Fuite d'eau importante sous l'évier de la cuisine. Besoin d'intervention rapide. L'eau coule constamment et crée des dégâts.",
        location = "Sfax, Centre-ville",
        budget = "150 DT",
        urgency = Urgency.URGENT,
        status = DemandStatus.EN_COURS,
        createdBy = "Amira B.",
        createdDate = Date(),
        deadline = "Immédiat",
        phone = "+216 21 234 567"
    ),
    ServiceDemand(
        id = "3",
        title = "Installation électrique complète appartement",
        category = ServiceCategory.ELECTRICIEN,
        description = "Nouvel appartement nécessitant installation électrique complète: prises, interrupteurs, luminaires, tableau électrique. Surface 120m².",
        location = "Sousse, Khezama",
        budget = "2500 DT",
        urgency = Urgency.FAIBLE,
        status = DemandStatus.OUVERT,
        createdBy = "Mohamed L.",
        createdDate = Date(),
        deadline = "01/03/2026",
        phone = "+216 22 345 678"
    ),
    ServiceDemand(
        id = "4",
        title = "Peinture salon et chambres",
        category = ServiceCategory.PEINTRE,
        description = "Besoin de peindre un salon (25m²) et deux chambres (15m² chacune). Préparation des murs incluse. Peinture de qualité souhaitée.",
        location = "Ariana, Raoued",
        budget = "800 DT",
        urgency = Urgency.MOYEN,
        status = DemandStatus.OUVERT,
        createdBy = "Sarah K.",
        createdDate = Date(),
        deadline = "20/02/2026",
        phone = "+216 23 456 789"
    ),
    ServiceDemand(
        id = "5",
        title = "Entretien jardin mensuel",
        category = ServiceCategory.JARDINAGE,
        description = "Recherche jardinier pour entretien mensuel d'un jardin de 200m². Tonte, taille des haies, arrosage, désherbage.",
        location = "La Marsa",
        budget = "200 DT/mois",
        urgency = Urgency.FAIBLE,
        status = DemandStatus.TERMINE,
        createdBy = "Ahmed T.",
        createdDate = Date(),
        deadline = "Flexible",
        phone = "+216 24 567 890"
    )
)

data class ServiceDemand(
    val id: String,
    val title: String,
    val category: ServiceCategory,
    val description: String,
    val location: String,
    val budget: String,
    val urgency: Urgency,
    val status: DemandStatus,
    val createdBy: String,
    val createdDate: Date,
    val deadline: String,
    val phone: String,
    val images: List<String> = emptyList()
)
enum class ServiceCategory(val displayName: String, val icon: String) {
    // Construction & Rénovation
    MENUISIER("Menuisier", "🔨"),
    PLOMBIER("Plombier", "🔧"),
    ELECTRICIEN("Électricien", "💡"),
    PEINTRE("Peintre", "🎨"),
    MACONNERIE("Maçonnerie", "🧱"),
    CARRELEUR("Carreleur", "⬜"),
    PLATRERIE("Plâtrerie", "🏗️"),
    SOUDEUR("Soudeur", "⚡"),
    VITRIER("Vitrier", "🪟"),
    CLIMATISATION("Climatisation", "❄️"),

    // Jardinage & Extérieur
    JARDINAGE("Jardinage", "🌱"),
    PAYSAGISTE("Paysagiste", "🌳"),
    ELAGAGE("Élagage", "🌲"),
    PISCINE("Entretien Piscine", "🏊"),

    // Nettoyage & Entretien
    NETTOYAGE("Nettoyage", "🧹"),
    NETTOYAGE_PROFOND("Nettoyage Profond", "✨"),
    DESINFECTION("Désinfection", "🦠"),
    PRESSING("Pressing", "👔"),

    // Déménagement & Transport
    DEMENAGEMENT("Déménagement", "📦"),
    TRANSPORT("Transport", "🚚"),
    LEVAGE("Levage", "🏗️"),

    // Réparation & Maintenance
    REPARATION_ELECTROMENAGER("Réparation Électroménager", "🔌"),
    REPARATION_INFORMATIQUE("Réparation Informatique", "💻"),
    REPARATION_MOBILE("Réparation Mobile", "📱"),
    SERRURIER("Serrurier", "🔑"),
    COUTURE("Couture", "🧵"),
    CORDONNIER("Cordonnier", "👞"),

    // Automobile
    MECANICIEN("Mécanicien Auto", "🚗"),
    CARROSSERIE("Carrosserie", "🔧"),
    LAVAGE_AUTO("Lavage Auto", "🚿"),
    DEPANNAGE_AUTO("Dépannage Auto", "🆘"),

    // Services à domicile
    FEMME_MENAGE("Femme de Ménage", "🧽"),
    GARDE_ENFANTS("Garde d'Enfants", "👶"),
    COURS_PARTICULIERS("Cours Particuliers", "📚"),
    AIDE_PERSONNES_AGEES("Aide Personnes Âgées", "👴"),
    CUISINE_DOMICILE("Cuisine à Domicile", "👨‍🍳"),

    // Événementiel
    TRAITEUR("Traiteur", "🍽️"),
    PHOTOGRAPHE("Photographe", "📸"),
    VIDEASTE("Vidéaste", "🎥"),
    DJ("DJ", "🎧"),
    ANIMATION("Animation", "🎉"),
    DECORATION("Décoration", "🎀"),

    // Bien-être & Beauté
    COIFFEUR_DOMICILE("Coiffeur à Domicile", "💇"),
    ESTHETICIENNE("Esthéticienne", "💅"),
    MASSAGE("Massage", "💆"),

    // Artisanat
    TAPISSIER("Tapissier", "🛋️"),
    FORGERON("Forgeron", "⚒️"),
    MIROITIER("Miroitier", "🪞"),

    // Location d'outils
    LOCATION_OUTILS("Location d'Outils", "🛠️"),

    // Autre
    AUTRE("Autre", "⚙️")
}

enum class ToolCategory(val displayName: String, val icon: String) {
    // Outils électriques
    PERCEUSE("Perceuse", "🔩"),
    MEULEUSE("Meuleuse", "⚙️"),
    SCIE_CIRCULAIRE("Scie Circulaire", "🪚"),
    SCIE_SAUTEUSE("Scie Sauteuse", "🔧"),
    PONCEUSE("Ponceuse", "📐"),
    MARTEAU_PIQUEUR("Marteau Piqueur", "⚒️"),
    COMPRESSEUR("Compresseur", "💨"),
    GROUPE_ELECTROGENE("Groupe Électrogène", "⚡"),
    KARCHER("Karcher", "🚿"),
    ASPIRATEUR_PROFESSIONNEL("Aspirateur Pro", "🌪️"),

    // Outils de construction
    BETONNIERE("Bétonnière", "🏗️"),
    ECHAFAUDAGE("Échafaudage", "🪜"),
    ECHELLE("Échelle", "🪜"),
    NIVEAU_LASER("Niveau Laser", "📏"),

    // Outils de jardinage
    TONDEUSE("Tondeuse", "🌱"),
    TAILLE_HAIE("Taille-haie", "✂️"),
    TRONCONNEUSE("Tronçonneuse", "🪓"),
    DEBROUSSAILLEUSE("Débroussailleuse", "🌾"),
    MOTOCULTEUR("Motoculteur", "🚜"),

    // Matériel de levage
    GRUE("Grue", "🏗️"),
    NACELLE("Nacelle", "🚧"),
    DIABLE("Diable", "📦"),
    TRANSPALETTE("Transpalette", "🔄"),

    // Matériel de nettoyage
    NETTOYEUR_VAPEUR("Nettoyeur Vapeur", "💨"),
    AUTOLAVEUSE("Autolaveuse", "🧼"),
    MONOBROSSE("Monobrosse", "⚪"),

    // Matériel de peinture
    PISTOLET_PEINTURE("Pistolet à Peinture", "🎨"),
    ROULEAU_PROFESSIONNEL("Rouleau Pro", "🖌️"),

    // Véhicules utilitaires
    CAMIONNETTE("Camionnette", "🚐"),
    CAMION("Camion", "🚚"),
    FOURGON("Fourgon", "🚙"),
    REMORQUE("Remorque", "🚛"),

    // Matériel événementiel
    TENTE("Tente", "⛺"),
    CHAISES_TABLES("Chaises & Tables", "🪑"),
    SONO("Sonorisation", "🔊"),
    ECLAIRAGE("Éclairage", "💡"),

    // Autre
    AUTRE_OUTIL("Autre Outil", "🔧")
}


enum class Urgency(val displayName: String, val color: Color) {
    FAIBLE("Faible", Color(0xFF10B981)),
    MOYEN("Moyen", Color(0xFFF59E0B)),
    URGENT("Urgent", Color(0xFFEF4444))
}

enum class DemandStatus(val displayName: String, val color: Color) {
    OUVERT("Ouvert", Color(0xFF3B82F6)),
    EN_COURS("En cours", Color(0xFFF59E0B)),
    TERMINE("Terminé", Color(0xFF10B981)),
    ANNULE("Annulé", Color(0xFF6B7280))
}

// ============================================
// 1. CREATE DEMAND FORM
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDemandScreen(
    onDemandCreated: (ServiceDemand) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var urgency by remember { mutableStateOf(Urgency.MOYEN) }
    var deadline by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showCategoryDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle demande de service") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Catégorie de service *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9FAFB))
                            .clickable { showCategoryDialog = true }
                            .padding(16.dp)
                    ) {
                        if (selectedCategory != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = selectedCategory!!.icon, fontSize = 24.sp)
                                Text(
                                    text = selectedCategory!!.displayName,
                                    color = Color(0xFF1F2937),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Text(
                                text = "Sélectionner une catégorie",
                                color = Color(0xFF9CA3AF),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // Title Field
            FormTextField(
                value = title,
                onValueChange = { title = it },
                label = "Titre de la demande *",
                placeholder = "Ex: Installation d'une porte en bois"
            )

            // Description Field
            FormTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description détaillée *",
                placeholder = "Décrivez votre besoin en détail...",
                minLines = 4,
                maxLines = 6
            )

            // Location Field
            FormTextField(
                value = location,
                onValueChange = { location = it },
                label = "Localisation *",
                placeholder = "Ville, quartier",
                leadingIcon = Icons.Filled.LocationOn
            )

            // Budget Field
            FormTextField(
                value = budget,
                onValueChange = { budget = it },
                label = "Budget estimé",
                placeholder = "Ex: 500 DT",
                leadingIcon = Icons.Filled.AttachMoney,
                keyboardType = KeyboardType.Number
            )

            // Urgency Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Urgence",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Urgency.values().forEach { urg ->
                            UrgencyChip(
                                urgency = urg,
                                selected = urgency == urg,
                                onClick = { urgency = urg },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Deadline Field
            FormTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = "Date limite souhaitée",
                placeholder = "JJ/MM/AAAA",
                leadingIcon = Icons.Filled.CalendarToday
            )

            // Phone Field
            FormTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Numéro de téléphone *",
                placeholder = "+216 XX XXX XXX",
                leadingIcon = Icons.Filled.Phone,
                keyboardType = KeyboardType.Phone
            )

            // Submit Button
            Button(
                onClick = {
                    if (selectedCategory != null && title.isNotEmpty() &&
                        description.isNotEmpty() && location.isNotEmpty() && phone.isNotEmpty()) {
                        val demand = ServiceDemand(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            category = selectedCategory!!,
                            description = description,
                            location = location,
                            budget = budget.ifEmpty { "À négocier" },
                            urgency = urgency,
                            status = DemandStatus.OUVERT,
                            createdBy = "Utilisateur",
                            createdDate = Date(),
                            deadline = deadline.ifEmpty { "Flexible" },
                            phone = phone
                        )
                        onDemandCreated(demand)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF049344)),
                enabled = selectedCategory != null && title.isNotEmpty() &&
                        description.isNotEmpty() && location.isNotEmpty() && phone.isNotEmpty()
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publier la demande", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Category Selection Dialog
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Choisir une catégorie") },
            text = {
                LazyColumn {
                    items(ServiceCategory.values()) { category ->
                        CategoryDialogItem(
                            category = category,
                            onClick = {
                                selectedCategory = category
                                showCategoryDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Annuler", color = Color(0xFF049344))
                }
            }
        )
    }
}

@Composable
fun FormTextField(
    value: String,
    borderColor: Color = Color(0xFF049344),
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    focusChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(placeholder, color = Color(0xFF9CA3AF)) },
                leadingIcon = leadingIcon?.let { { Icon(it, null, tint = Color(0xFF049344)) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedTextColor = Color(0xFF1F2937),
                    unfocusedTextColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                minLines = minLines,
                maxLines = maxLines
            )

            // Collect press events from the interaction source
            if (interactionSource.collectIsPressedAsState().value) {
                LaunchedEffect(Unit) {
                    onClick()
                }
            }
        }
    }
}

@Composable
fun UrgencyChip(
    urgency: Urgency,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (selected) urgency.color.copy(alpha = 0.15f)
                else Color(0xFFF9FAFB)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = urgency.displayName,
            color = if (selected) urgency.color else Color(0xFF6B7280),
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun CategoryDialogItem(category: ServiceCategory, onClick: () -> Unit) {
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
            text = category.displayName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937)
        )
    }
}

// ============================================
// 2. DEMANDS LIST WITH SEARCH
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DemandsListScreen(
    demands: List<ServiceDemand> = entity,
    onDemandClick: (ServiceDemand) -> Unit = {},
    onCreateClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }

    val filteredDemands = demands.filter { demand ->
        val matchesSearch = demand.title.contains(searchQuery, ignoreCase = true) ||
                demand.description.contains(searchQuery, ignoreCase = true) ||
                demand.location.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null || demand.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demandes de services") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateClick,
                containerColor = Color(0xFF049344),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Nouvelle demande")
            }
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Rechercher une demande...", color = Color(0xFF9CA3AF)) },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color(0xFF049344)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Close, null, tint = Color(0xFF6B7280))
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF049344),
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Category Filter
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Tous") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF049344),
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(ServiceCategory.entries.size) { index ->
                    val category = ServiceCategory.entries[index]
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = if (selectedCategory == category) null else category },
                        label = {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(category.icon)
                                Text(category.displayName)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF049344),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Demands List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredDemands) { demand ->
                    DemandCard(demand = demand, onClick = { onDemandClick(demand) })
                }

                if (filteredDemands.isEmpty()) {
                    item {
                        EmptyState()
                    }
                }
            }
        }
    }
}

@Composable
fun DemandCard(demand: MarketDemandModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF049344).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = demand.category.icon, fontSize = 24.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = demand.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            maxLines = 1
                        )
                        Text(
                            text = demand.category.displayName,
                            fontSize = 13.sp,
                            color = Color(0xFF049344),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                StatusBadge(status = demand.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = demand.description,
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                maxLines = 2,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(icon = Icons.Filled.LocationOn, text = demand.location)
                InfoChip(icon = Icons.Filled.AttachMoney, text = demand.budget)
                UrgencyBadge(urgency = demand.urgency)
            }
        }
    }
}

@Composable
fun DemandCardModel(demand: MarketDemandModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF049344).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = demand.category.icon, fontSize = 24.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = demand.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            maxLines = 1
                        )
                        Text(
                            text = demand.category.displayName,
                            fontSize = 13.sp,
                            color = Color(0xFF049344),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                StatusBadge(status = demand.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = demand.description,
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                maxLines = 2,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(icon = Icons.Filled.LocationOn, text = demand.location)
                InfoChip(icon = Icons.Filled.AttachMoney, text = demand.budget)
                UrgencyBadge(urgency = demand.urgency)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val statusModel = DemandStatus.entries.first { it.displayName == status }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(statusModel.color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = status,
            color = statusModel.color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun UrgencyBadge(urgency: String) {
    val urgencyModel = Urgency.entries.first {
        it.displayName == urgency
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(urgencyModel.color)
        )
        Text(
            text = urgencyModel.displayName,
            fontSize = 12.sp,
            color = urgencyModel.color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun InfoChip(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, null, tint = Color(0xFF6B7280), modifier = Modifier.size(16.dp))
        Text(text = text, fontSize = 12.sp, color = Color(0xFF6B7280))
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🔍", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aucune demande trouvée",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Text(
            text = "Essayez de modifier vos critères de recherche",
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
    }
}

// ============================================
// 3. DEMAND DETAIL SCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemandDetailScreen(
    demand: MarketDemandModel,
    onBackClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onApplyClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails de la demande") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Retour", tint = Color(0xFF049344))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomActionBar(
                onContactClick = onContactClick,
                onApplyClick = onApplyClick
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF049344).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = demand.category.icon, fontSize = 36.sp)
                        }

                        StatusBadge(status = demand.status)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = demand.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF049344).copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = demand.category.displayName,
                                color = Color(0xFF049344),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        UrgencyBadge(urgency = demand.urgency)
                    }
                }
            }

            // Quick Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickInfoCard(
                    icon = Icons.Filled.AttachMoney,
                    label = "Budget",
                    value = demand.budget,
                    modifier = Modifier.weight(1f)
                )
                QuickInfoCard(
                    icon = Icons.Filled.CalendarToday,
                    label = "Deadline",
                    value = demand.deadline,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            DetailSection(title = "Description") {
                Text(
                    text = demand.description,
                    fontSize = 15.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 22.sp
                )
            }

            // Location Section
            DetailSection(title = "Localisation") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.LocationOn,
                        null,
                        tint = Color(0xFF049344),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = demand.location,
                        fontSize = 15.sp,
                        color = Color(0xFF1F2937),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            val role = demand.userSender?.role ?: ""
            val createdBy = if (role == "candidate" || role == "candidat") demand.userSender?.fullName ?: ""
            else demand.userSender?.companyName ?: ""

            // Contact Info Section
            DetailSection(title = "Contact") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ContactRow(
                        icon = Icons.Filled.Person,
                        label = "Publié par",
                        value = createdBy
                    )
                    ContactRow(
                        icon = Icons.Filled.CalendarToday,
                        label = "Publié le",
                        value = SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.FRENCH
                        ).format(demand.date)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickInfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = Color(0xFF049344), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun DetailSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
@Composable
fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF049344), modifier = Modifier.size(20.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color(0xFF6B7280))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )
        }
    }
}
@Composable
fun BottomActionBar(
    onContactClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onContactClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF049344))
                ),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF049344))
            ) {
                Icon(Icons.Filled.Phone, null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Appeler", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}