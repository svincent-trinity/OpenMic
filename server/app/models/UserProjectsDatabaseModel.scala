package models

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.duration.Duration

class UserProjectsDatabaseModel(db: Database)(implicit ec: ExecutionContext) {


	def validateUser(username: String, password: String): Future[Option[Int]] = {
		val matches = db.run(Users.filter(userRow => userRow.username === username).result)
        matches.map(userRows => userRows.headOption.flatMap {
        	userRow => if (BCrypt.checkpw(password, userRow.password)) Some(userRow.id) else None
        })
	}



	def createUser(username: String, password: String): Future[Option[Int]] = {
		val matches = db.run(Users.filter(userRow => userRow.username === username).result)
		matches.flatMap { userRows =>
			if(userRows.isEmpty) {
			db.run(Users += UsersRow(-1, username, BCrypt.hashpw(password, BCrypt.gensalt())))
			  .flatMap { addCount => 
			  	if(addCount > 0) db.run(Users.filter(userRow => userRow.username === username).result)
			  		.map(_.headOption.map(_.id))
                else Future.successful(None)
              }
			} else Future.successful(None)
		}
	}

    def getUsers(): Future[Iterable[String]] = {
        db.run(
            (for {
                user <- Users if !(user.username === "Public") 
            } yield {
            	user.username
            }).result
        )
    }

	def getProjects(username: String): Future[Seq[ProjectItem]] = {
        db.run(
            (for {
                user <- Users if user.username === username
                item <- Items if item.userId === user.id
            } yield {
                item
            }).result
        ).map(items => items.map(item => ProjectItem(item.itemId, item.text, item.privacy, item.mididata.getOrElse(""))))
        
        
        	}
    
    def getPublicProjects(): Future[Seq[ProjectItem]] = {
        db.run(
            (for {
                item <- Items if item.privacy === "Public"
            } yield {
                item
            }).result
        ).map(items => items.map(item => ProjectItem(item.itemId, item.text, item.privacy, item.mididata.getOrElse(""))))

    }

    def getUserIdBySongId(id: Int): Future[Seq[Int]] = {

        db.run(
            (for {
                item <- Items if item.itemId === id
            } yield {
                item.userId
            }).result
        )
    }

    def getIdByUsername(username: String): Future[Seq[Int]] = {
        db.run(
            (for {
                user <- Users if user.username === username
            } yield {
                user.id
            }).result
        )
        
    }

    def getUsernameById(id: Int): Future[Seq[String]] = {
        db.run(
            (for {
                user <- Users if user.id === id
            } yield {
                user.username
            }).result
        )
    }

    /*def createPublicProjects(username: String, message: String): Future[Int] = {
        db.run(Items += ItemsRow(-1, 2, message + " (sent by: " + username + ")"))

    }*/

	def addProject(userid: Int, task: String, isPublic: String): Future[Int] = {
       val userSent = scala.concurrent.Await.result(getUsernameById(userid), Duration(10000, "millis"))
        //val userToSendTo = scala.concurrent.Await.result(getIdByUsername(whoReceives), Duration(10000, "millis"))
        db.run(Items += ItemsRow(-1, userid, task, isPublic))
        
        
	}


	def removeProject(itemId: Int): Future[Boolean] = {
	    db.run( Items.filter(_.itemId === itemId).delete ).map(count => count > 0)
	}

	def removePublicProject(itemId: Int): Future[Boolean] = {
	    db.run( Items.filter(_.itemId === itemId).delete ).map(count => count > 0)

	}
}