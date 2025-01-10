package com.example.newp

val indiaStatesAndCapitals = mapOf(
    "Andhra Pradesh" to "Amaravati",
    "Arunachal Pradesh" to "Itanagar",
    "Assam" to "Dispur",
    "Bihar" to "Patna",
    "Chhattisgarh" to "Raipur",
    "Goa" to "Panaji",
    "Gujarat" to "Gandhinagar",
    "Haryana" to "Chandigarh",
    "Himachal Pradesh" to "Shimla",
    "Jharkhand" to "Ranchi",
    "Karnataka" to "Bengaluru",
    "Kerala" to "Thiruvananthapuram",
    "Madhya Pradesh" to "Bhopal",
    "Maharashtra" to "Mumbai",
    "Manipur" to "Imphal",
    "Meghalaya" to "Shillong",
    "Mizoram" to "Aizawl",
    "Nagaland" to "Kohima",
    "Odisha" to "Bhubaneswar",
    "Punjab" to "Chandigarh",
    "Rajasthan" to "Jaipur",
    "Sikkim" to "Gangtok",
    "Tamil Nadu" to "Chennai",
    "Telangana" to "Hyderabad",
    "Tripura" to "Agartala",
    "Uttar Pradesh" to "Lucknow",
    "Uttarakhand" to "Dehradun",
    "West Bengal" to "Kolkata"
)

fun getCapital(state : String) : String{
    val capital:String = indiaStatesAndCapitals[state]?:"No city Found"
    return capital
}