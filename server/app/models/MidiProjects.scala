package models

import collection.mutable
import collection.mutable.ArrayBuffer

object MidiProjects {
	//projects matches a song name with its midi data
	private val projects = mutable.Map[Array[String], ArrayBuffer[Int]]()

    def addProject(user: String, name: String):Boolean = {
    	if(projects.contains(Array(user, name))) {
    		return false
    	} else {
    	    projects(Array(user, name)) = ArrayBuffer[Int]()
            return true
    	}
    }

    def addNote(user: String, name: String, note: Int):Boolean = {
            //users.get(username).map(e => e :+ projName).getOrElse(false)
            projects.get(Array(user, name)).map(e => e += note )
            return true
    }

    def getMidiData(user:String, name:String):ArrayBuffer[Int] = {
    	return projects(Array(user, name))
    }
}