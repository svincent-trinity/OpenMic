package models

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.duration.Duration
import scala.Option



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

    def getRecordings(): Future[Seq[RecordingData]] = {
        db.run(
            (for {
                recording <- Recordings if recording.privacy === "Public"
            } yield {
                recording
            }).result
        ).map(recs => recs.map(rec => RecordingData(rec.recordingId, rec.name, rec.description)))
    }

    def getInstruments(): Future[Seq[InstrumentData]] = {
        db.run(
            (for {
                instrument <- Instruments
            } yield{
                instrument
            }).result
        ).map(insts => insts.map(inst => InstrumentData(inst.instrumentId, inst.userId.toString, inst.name, inst.description, inst.privacy)))
        
    }

    def getRecAudio(recId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                recording <- Recordings if recording.recordingId === recId
            } yield {
                recording.audio
            }).result
        )
    }

    def getInstrumentAudioC4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.c4
            }).result
        )
    }
    def getInstrumentAudioC5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.c5
            }).result
        )
    }
    def getInstrumentAudioC6(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.c6
            }).result
        )
    }

    def getInstrumentAudioDb4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.db4
            }).result
        )
    }
    def getInstrumentAudioDb5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.db5
            }).result
        )
    }
    def getInstrumentAudioD4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.d4
            }).result
        )
    }
    def getInstrumentAudioD5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.d5
            }).result
        )
    }
    def getInstrumentAudioEb4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.eb4
            }).result
        )
    }
    def getInstrumentAudioEb5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.eb5
            }).result
        )
    }
    def getInstrumentAudioE4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.e4
            }).result
        )
    }
    def getInstrumentAudioE5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.e5
            }).result
        )
    }
    def getInstrumentAudioF4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.f4
            }).result
        )
    }
    def getInstrumentAudioF5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.f5
            }).result
        )
    }
    def getInstrumentAudioGb4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.gb4
            }).result
        )
    }
    def getInstrumentAudioGb5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.gb5
            }).result
        )
    }
    def getInstrumentAudioG4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.g4
            }).result
        )
    }
    def getInstrumentAudioG5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.g5
            }).result
        )
    }
    def getInstrumentAudioAb4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.ab4
            }).result
        )
    }
    def getInstrumentAudioAb5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.ab5
            }).result
        )
    }
    def getInstrumentAudioA4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.a4
            }).result
        )
    }
    def getInstrumentAudioA5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.a5
            }).result
        )
    }
    def getInstrumentAudioBb4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.bb4
            }).result
        )
    }
    def getInstrumentAudioBb5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.bb5
            }).result
        )
    }
    def getInstrumentAudioB4(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.b4
            }).result
        )
    }
    def getInstrumentAudioB5(instId: Int): Future[Seq[Array[Byte]]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.instrumentId === instId
            } yield {
                instrument.b5
            }).result
        )
    }


    def getInstrumentIdByName(instName: String): Future[Seq[Int]] = {
        db.run(
            (for {
                instrument <- Instruments if instrument.name === instName
            } yield {
                instrument.instrumentId
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

    def uploadResource(filename: String, file: Array[Byte], userid: Int): Future[Int] = {
        //val userId = scala.concurrent.Await.result(getIdByUsername(username), Duration(10000, "millis"))
        db.run(Recordings += RecordingsRow(-1, userid, filename, "Test", "Public", file))
    }

    def uploadInstrument(instrumentName: String, userid: Int, privacy: String, description: String, C4: Array[Byte], Db4: Array[Byte], D4: Array[Byte], Eb4: Array[Byte], E4: Array[Byte], F4: Array[Byte], Gb4: Array[Byte], G4: Array[Byte], Ab4: Array[Byte], A4: Array[Byte], Bb4: Array[Byte], B4: Array[Byte], C5: Array[Byte], Db5: Array[Byte], D5: Array[Byte], Eb5: Array[Byte], E5: Array[Byte], F5: Array[Byte], Gb5: Array[Byte], G5: Array[Byte], Ab5: Array[Byte], A5: Array[Byte], Bb5: Array[Byte], B5: Array[Byte], C6: Array[Byte]): Future[Int] = {
        //val userId = scala.concurrent.Await.result(getIdByUsername(username), Duration(10000, "millis"))
        db.run(Instruments += InstrumentsRow(-1, userid, instrumentName, description, privacy, C4, Db4, D4, Eb4, E4, F4, Gb4, G4, Ab4, A4, Bb4, B4, C5, Db5, D5, Eb5, E5, F5, Gb5, G5, Ab5, A5, Bb5, B5, C6))//files(13), files(14), files(15), files(16), files(17), files(18), files(19), files(20), files(21), files(22), files(23), files(24)))
    }

	def removeProject(itemId: Int): Future[Boolean] = {
	    db.run( Items.filter(_.itemId === itemId).delete ).map(count => count > 0)
	}

	def removePublicProject(itemId: Int): Future[Boolean] = {
	    db.run( Items.filter(_.itemId === itemId).delete ).map(count => count > 0)

	}
}