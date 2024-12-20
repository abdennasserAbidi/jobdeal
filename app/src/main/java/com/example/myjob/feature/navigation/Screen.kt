package com.example.myjob.feature.navigation

sealed class Screen(val route:String) {
    object recordScreen : Screen("record_screen")
    object HomeScreen : Screen("home_screen")
    object CareerScreen : Screen("career_screen")
    object CareerFormScreen : Screen("career_form_screen")
    object EducationScreen : Screen("education_screen")
    object EducationFormScreen : Screen("education_form_screen")
    object PersonalFormScreen : Screen("personal_form_screen")
    object textRecognitionScreen : Screen("text_screen")
    object SettingScreen : Screen("settings_screen")
    object ProfileScreen : Screen("profile_screen")
    object FavoritesScreen : Screen("favorites_screen")
    object CompanyProfileScreen : Screen("company_profile_screen")
    object galleryScreen : Screen("gallery_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object SplashScreen : Screen("splash_screen")
    object OnBoardingScreen : Screen("onboarding_screen")
    object ForgotPasswordScreen : Screen("forgot_password_screen")
}