package models

import collection.mutable
import collection.mutable.ArrayBuffer

object UserManager {
	private val users = mutable.Map[String, ArrayBuffer[String]]("mlewis" -> ArrayBuffer("prof", "Public"), "web" -> ArrayBuffer("apps", "Private"))
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

    def changePassword(newPassword: String, username: String, password:String): Boolean = {
        if(validateUser(username, password)) {
            users.get(username).map(e => e(0) = newPassword)
        	return true
        } else { return false }
    }

	def validateUser(username: String, password: String): Boolean = {
	   users.get(username).map(_(0) == password).getOrElse(false)
	}

	def createUser(username: String, password: String, privacy: String): Boolean = {
		if(users.contains(username)) false
		else {
			users(username) = ArrayBuffer(password, privacy)
			true
		}
	}

	def createProject(username: String, projName: String):Boolean = {
	   if(users.get(username).map(e => e.contains(projName)).getOrElse(true)) {
            return false
	   } else {
            //users.get(username).map(e => e :+ projName).getOrElse(false)
            users.get(username).map(e => e += projName )
            return true
	   }
	}

    def getProjects(username: String):ArrayBuffer[String] = {
    	var retArray = ArrayBuffer[String]()
    	//var i=0
        users.get(username).map(e => {
            /*for(i <- e) {
                retArray :+ i
            }
            return retArray*/
            retArray = e
            //var ret = e
            return retArray.drop(2)
        }).getOrElse(retArray)
        //return retArray
    }

	

}