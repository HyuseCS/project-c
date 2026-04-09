package com.hyuse.projectc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform