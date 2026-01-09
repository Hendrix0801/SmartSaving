package com.smartsaving.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartsaving.android.ui.*
import com.smartsaving.android.ui.theme.SmartSavingTheme
import com.smartsaving.android.viewmodel.BudgetViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BudgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartSavingTheme {
                MainScreen(viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Budget : Screen("budget", "预算", Icons.Default.AccountBalance)
    object Transactions : Screen("transactions", "记账", Icons.Default.Edit)
    object Expenses : Screen("expenses", "支出", Icons.Default.ArrowDownward)
    object Income : Screen("income", "收入", Icons.Default.ArrowUpward)
    object Stats : Screen("stats", "统计", Icons.Default.List)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: BudgetViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                val screens = listOf(
                    Screen.Budget,
                    Screen.Transactions,
                    Screen.Expenses,
                    Screen.Income,
                    Screen.Stats
                )

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Budget.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Budget.route) {
                BudgetView(viewModel)
            }
            composable(Screen.Transactions.route) {
                TransactionsView(viewModel)
            }
            composable(Screen.Expenses.route) {
                ExpensesListView(viewModel)
            }
            composable(Screen.Income.route) {
                IncomeView(viewModel)
            }
            composable(Screen.Stats.route) {
                StatsView(viewModel)
            }
        }
    }
}
