package com.edem.bot.services

import org.springframework.stereotype.Service

@Service
class NamingAndOrganicService {

    fun createNaming(url : String): String {
        val first = url.split("?")
        val id = first[0].split("//")[1].split("/")[1]
        val nam = try {
            "|^|${naming(first[1])}"
        } catch (e : Exception) { "" }
        return "p=ed|^|id=$id${nam}"
    }

    fun naming(c : String) =
            if(c != "") c.split("&").joinToString("|^|") else c
}

//fun main(){
//    val url = "https://edems.site/Gx2ZqnsP"
//    val first = url.split("?")
//    val id = first[0].split("//")[1].split("/")[1]
//    val nam = try {
//        "|^|${naming(first[1])}"
//    } catch (e : Exception) { "" }
//    val p = "p=ed|^|id=$id${nam}"
//    println(p)
//}
//fun naming(c : String) =
//        if(c != "") c.split("&").joinToString("|^|") else c