import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoutineApp()
        }
    }
}

@Composable
fun RoutineApp(viewModel: RoutineViewModel = viewModel()) {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            RoutineScreen(viewModel)
        }
    }
}

@Composable
fun RoutineScreen(viewModel: RoutineViewModel) {
    val checklist = viewModel.checklistItems
    val affirmation by viewModel.currentAffirmation.collectAsState()
    var gratitude by remember { mutableStateOf("") }
    var permission by remember { mutableStateOf("") }
    var joy by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("🌅 Ranní rutina", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        items(checklist) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleItem(item) }
                    .background(if (item.checked) Color(0xFF2E7D32) else Color(0xFF424242))
                    .padding(12.dp)
            ) {
                Checkbox(
                    checked = item.checked,
                    onCheckedChange = { viewModel.toggleItem(item) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.text, fontSize = 16.sp)
            }
        }

        item {
            Button(onClick = { viewModel.shuffleAffirmation() }) {
                Text("🧘 Zobraz afirmaci")
            }
            Text(
                affirmation,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Text("✍️ Moje 3 ranní věty:", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = gratitude, onValueChange = { gratitude = it }, label = { Text("Za co si vážím sám sebe") })
            OutlinedTextField(value = permission, onValueChange = { permission = it }, label = { Text("Co si dnes dovolím") })
            OutlinedTextField(value = joy, onValueChange = { joy = it }, label = { Text("Co mi udělá radost") })
        }
    }
}

data class ChecklistItem(val text: String, var checked: Boolean = false)

class RoutineViewModel : ViewModel() {
    var checklistItems = mutableStateListOf(
        ChecklistItem("✅ Laskavé slovo po probuzení"),
        ChecklistItem("✅ Protažení těla"),
        ChecklistItem("✅ 3 věty do zápisníku"),
        ChecklistItem("✅ Dechová chvilka"),
        ChecklistItem("✅ Úsměv do zrcadla")
    )
        private set

    private val affirmations = listOf(
        "Jsem dost dobrý takový, jaký jsem.",
        "Každý malý krok se počítá.",
        "Dělám, co můžu, a to je dost.",
        "Mám právo být nedokonalý.",
        "Nejsem sám – každý občas tápe."
    )
    private val _currentAffirmation = mutableStateOf(affirmations.random())
    val currentAffirmation: State<String> = _currentAffirmation

    fun toggleItem(item: ChecklistItem) {
        item.checked = !item.checked
    }

    fun shuffleAffirmation() {
        _currentAffirmation.value = affirmations.random()
    }
}
