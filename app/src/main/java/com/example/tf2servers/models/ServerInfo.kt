package com.example.tf2servers.models

class ServerInfo(var address: String = "",
                 var port: Int = 0,
                 var protocol: Int = 0,
                 var serverName: String = "Error",
                 var mapName: String = "Error",
                 var folder: String = "",
                 var game: String = "Error",
                 var appId: Int = 0,
                 var playerCount: Int = 0,
                 var maxPlayers: Int = 0)