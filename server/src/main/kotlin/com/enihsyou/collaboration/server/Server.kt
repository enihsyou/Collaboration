package com.enihsyou.collaboration.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**主程序*/
@SpringBootApplication
class ServerApplication

/**主程序入口*/
fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
