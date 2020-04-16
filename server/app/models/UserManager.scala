package models

import collection.mutable

object UserManager {
	private val users = mutable.Map[String, Array[String]]("mlewis" -> Array("prof", "Public"), "web" -> Array("apps", "Private"))
	//private val users = mutable.Map[String, String]("mlewis" -> "prof", "web" -> "apps")

    def getUsers(): Iterable[String] = {
    	val ret = users.keys
    	ret
    }

    def showPrivacy(user: String):String = {
    	val ret = users.get(user).map(e => e(1)).getOrElse("Error: No data")
    	return ret
    }

    def changePrivacy(newPrivacy: String, username: String, password:String): Boolean = {
        if(validateUser(username, password)) {
            users.get(username).map(e => e(1) = newPrivacy)
        	return true
        } else { return false }
    }

	def validateUser(username: String, password: String): Boolean = {
	   users.get(username).map(_(0) == password).getOrElse(false)
	}

	def createUser(username: String, password: String, privacy: String): Boolean = {
		if(users.contains(username)) false
		else {
			users(username) = Array(password, privacy)
			true
		}
	}

}