package com.sadotracker.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sadotracker.featureworkout.WorkoutHubScreen
import com.sadotracker.featureyou.YouScreen
import kotlinx.serialization.Serializable

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.navigation
import com.sadotracker.featureprograms.ExerciseSearchScreen
import com.sadotracker.featureprograms.ProgramBuilderViewModel
import com.sadotracker.featureprograms.ProgramCreationScreen
import com.sadotracker.featureprograms.ExerciseFilterScreen
import androidx.navigation.toRoute
import com.sadotracker.featureworkout.LiveWorkoutScreen
import com.sadotracker.featureworkout.LiveWorkoutViewModel
import com.sadotracker.featureworkout.WorkoutHubViewModel
import androidx.compose.runtime.LaunchedEffect

import com.sadotracker.featureprograms.ProgramListScreen
import com.sadotracker.coredatabase.entity.ExerciseEntity

// --- Type-safe Routes ---
@Serializable
object WorkoutHubRoute

@Serializable
object YouRoute

@Serializable
object ProgramBuilderGraph

@Serializable
data class ProgramListRoute(val forSelection: Boolean = false)

@Serializable
object ProgramCreationRoute

@Serializable
object ExerciseSearchRoute

@Serializable
object ExerciseFilterRoute

@Serializable
data class LiveWorkoutRoute(val workoutId: Long)

@Serializable
object LiveWorkoutSearchGraph

@Serializable
object LiveWorkoutExerciseSearchRoute

@Serializable
object LiveWorkoutExerciseFilterRoute

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun SadoApp() {
    val navController = rememberNavController()

    val navItems = listOf(
        BottomNavItem("Workout", Icons.Filled.List, WorkoutHubRoute),
        BottomNavItem("You", Icons.Filled.Person, YouRoute)
    )

    Scaffold(
        bottomBar = {
            SadoBottomNavigationBar(
                navController = navController,
                items = navItems
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WorkoutHubRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<WorkoutHubRoute> { backStackEntry ->
                val viewModel: WorkoutHubViewModel = hiltViewModel()
                val savedStateHandle = backStackEntry.savedStateHandle
                val selectedProgramId = savedStateHandle.get<Long>("selected_program_id")
                
                LaunchedEffect(selectedProgramId) {
                    if (selectedProgramId != null) {
                        viewModel.startWorkoutFromProgram(selectedProgramId) { id ->
                            navController.navigate(LiveWorkoutRoute(workoutId = id))
                        }
                        savedStateHandle.remove<Long>("selected_program_id")
                    }
                }

                WorkoutHubScreen(
                    onNavigateToProgramBuilder = {
                        navController.navigate(ProgramBuilderGraph)
                    },
                    onStartEmptyWorkout = { id ->
                        navController.navigate(LiveWorkoutRoute(workoutId = id))
                    },
                    onNavigateToProgramSelection = {
                        navController.navigate(ProgramListRoute(forSelection = true))
                    },
                    viewModel = viewModel
                )
            }
            
            composable<LiveWorkoutRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<LiveWorkoutRoute>()
                val viewModel: LiveWorkoutViewModel = hiltViewModel(backStackEntry)
                
                val savedStateHandle = backStackEntry.savedStateHandle
                val selectedExercises = savedStateHandle.get<LongArray>("selected_exercises")
                
                LaunchedEffect(selectedExercises) {
                    if (selectedExercises != null) {
                        selectedExercises.forEach { id ->
                            viewModel.addExercise(id)
                        }
                        savedStateHandle.remove<LongArray>("selected_exercises")
                    }
                }
                
                LiveWorkoutScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onAddExercise = {
                        navController.navigate(LiveWorkoutSearchGraph)
                    },
                    viewModel = viewModel
                )
            }
            
            navigation<LiveWorkoutSearchGraph>(startDestination = LiveWorkoutExerciseSearchRoute) {
                composable<LiveWorkoutExerciseSearchRoute> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(LiveWorkoutSearchGraph)
                    }
                    val searchViewModel: ProgramBuilderViewModel = hiltViewModel(parentEntry)
                    
                    ExerciseSearchScreen(
                        onNavigateBack = {
                            val selected = searchViewModel.selectedExercises.value
                            if (selected.isNotEmpty()) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selected_exercises", selected.map { it.id }.toLongArray())
                            }
                            navController.popBackStack(LiveWorkoutSearchGraph, inclusive = true)
                        },
                        onNavigateToFilter = { navController.navigate(LiveWorkoutExerciseFilterRoute) },
                        viewModel = searchViewModel
                    )
                }
                composable<LiveWorkoutExerciseFilterRoute> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(LiveWorkoutSearchGraph)
                    }
                    val searchViewModel: ProgramBuilderViewModel = hiltViewModel(parentEntry)
                    
                    ExerciseFilterScreen(
                        onNavigateBack = { navController.popBackStack() },
                        viewModel = searchViewModel
                    )
                }
            }
            
            navigation<ProgramBuilderGraph>(startDestination = ProgramListRoute()) {
                composable<ProgramListRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<ProgramListRoute>()
                    ProgramListScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToCreateProgram = { navController.navigate(ProgramCreationRoute) },
                        onProgramSelected = { id ->
                            if (route.forSelection) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selected_program_id", id)
                                navController.popBackStack()
                            } else {
                                // TODO: Phase 2 Navigate to Program Detail
                            }
                        }
                    )
                }
                composable<ProgramCreationRoute> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(ProgramBuilderGraph)
                    }
                    val viewModel: ProgramBuilderViewModel = hiltViewModel(parentEntry)
                    
                    ProgramCreationScreen(
                        onNavigateBack = { navController.navigateUp() },
                        onNavigateToExerciseSearch = { navController.navigate(ExerciseSearchRoute) },
                        viewModel = viewModel
                    )
                }
                composable<ExerciseSearchRoute> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(ProgramBuilderGraph)
                    }
                    val viewModel: ProgramBuilderViewModel = hiltViewModel(parentEntry)
                    
                    ExerciseSearchScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToFilter = { navController.navigate(ExerciseFilterRoute) },
                        viewModel = viewModel
                    )
                }
                composable<ExerciseFilterRoute> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(ProgramBuilderGraph)
                    }
                    val viewModel: ProgramBuilderViewModel = hiltViewModel(parentEntry)
                    
                    ExerciseFilterScreen(
                        onNavigateBack = { navController.popBackStack() },
                        viewModel = viewModel
                    )
                }
            }
            
            composable<YouRoute> {
                YouScreen()
            }
        }
    }
}

@Composable
private fun SadoBottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            // For type-safe routing, to get route string we check class name
            val routeString = item.route::class.qualifiedName

            val selected = currentDestination?.hierarchy?.any { 
                it.route?.contains(routeString ?: "") == true 
            } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.surface // Remove ugly pill background
                )
            )
        }
    }
}
