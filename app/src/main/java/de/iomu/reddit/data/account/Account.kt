package de.iomu.reddit.data.account

typealias AccessToken = String
typealias RefreshToken = String

sealed class Account {
    object LoggedOut : Account()
    data class LoggedIn(val accessToken: AccessToken, val refreshToken: RefreshToken) : Account()
}