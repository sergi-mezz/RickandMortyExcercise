package com.mezzyservices.rickmorty.navigation

import android.widget.Toast
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mezzyservices.rickmorty.ui.CharacterDetailScreen
import com.mezzyservices.rickmorty.ui.CharacterListScreen
import com.mezzyservices.rickmorty.ui.CharacterLocationScreen
import com.mezzyservices.rickmorty.ui.FavouriteEpisodesScreen
import kotlinx.serialization.Serializable


@Serializable
object CharacterList

@Serializable
data class CharacterDetail(val characterId: Int?)

@Serializable
object FavouriteEpisodes

@Serializable
object CharacterLocation


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        navController = navController,
        startDestination = CharacterList
    ) {
        composable<CharacterList> {
            CharacterListScreen({ navController.navigate(CharacterDetail(it)) }, {
                openFavouritesScreen(navController)
            })
        }
        composable<CharacterDetail> { backStackEntry ->
            val detail = backStackEntry.toRoute<CharacterDetail>()
            CharacterDetailScreen(detail.characterId) { navController.navigate(CharacterLocation ) }
        }
        composable<FavouriteEpisodes> { FavouriteEpisodesScreen() }

        composable<CharacterLocation> { CharacterLocationScreen() }
    }
}

fun openFavouritesScreen(navController: NavHostController) {

    val context = navController.context
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    context.applicationContext,
                    "Authentication error: $errString", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                navController.navigate(FavouriteEpisodes)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    context.applicationContext, "Authentication failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Favoritos")
        .setSubtitle("Coloca tu huella para acceder a tus favoritos")
        .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    biometricPrompt.authenticate(promptInfo)
}