package models

import collection.mutable

object UserManager {
	private val users = mutable.Map[String, String]("mlewis" -> "prof", "web" -> "apps")

    def getUsers(): Iterable[String] = {
    	val ret = users.keys
    	ret
    }

    
	def validateUser(username: String, password: String): Boolean = {
	   users.get(username).map(_ == password).getOrElse(false)
	}
	def createUser(username: String, password: String): Boolean = {
		if(users.contains(username)) false
		else {
			users(username) = password
			true
		}
	}

}