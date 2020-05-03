package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.collection.heterogeneous._
  import slick.collection.heterogeneous.syntax._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Instruments.schema ++ Items.schema ++ Recordings.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Instruments
   *  @param instrumentId Database column instrument_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param name Database column name SqlType(varchar), Length(256,true)
   *  @param description Database column description SqlType(varchar), Length(2000,true)
   *  @param privacy Database column privacy SqlType(varchar), Length(7,true)
   *  @param c4 Database column c4 SqlType(bytea), Default(None)
   *  @param db4 Database column db4 SqlType(bytea), Default(None)
   *  @param d4 Database column d4 SqlType(bytea), Default(None)
   *  @param eb4 Database column eb4 SqlType(bytea), Default(None)
   *  @param e4 Database column e4 SqlType(bytea), Default(None)
   *  @param f4 Database column f4 SqlType(bytea), Default(None)
   *  @param gb4 Database column gb4 SqlType(bytea), Default(None)
   *  @param g4 Database column g4 SqlType(bytea), Default(None)
   *  @param ab4 Database column ab4 SqlType(bytea), Default(None)
   *  @param a4 Database column a4 SqlType(bytea), Default(None)
   *  @param bb4 Database column bb4 SqlType(bytea), Default(None)
   *  @param b4 Database column b4 SqlType(bytea), Default(None)
   *  @param c5 Database column c5 SqlType(bytea), Default(None)
   *  @param db5 Database column db5 SqlType(bytea), Default(None)
   *  @param d5 Database column d5 SqlType(bytea), Default(None)
   *  @param eb5 Database column eb5 SqlType(bytea), Default(None)
   *  @param e5 Database column e5 SqlType(bytea), Default(None)
   *  @param f5 Database column f5 SqlType(bytea), Default(None)
   *  @param gb5 Database column gb5 SqlType(bytea), Default(None)
   *  @param g5 Database column g5 SqlType(bytea), Default(None)
   *  @param ab5 Database column ab5 SqlType(bytea), Default(None)
   *  @param a5 Database column a5 SqlType(bytea), Default(None)
   *  @param bb5 Database column bb5 SqlType(bytea), Default(None)
   *  @param b5 Database column b5 SqlType(bytea), Default(None)
   *  @param c6 Database column c6 SqlType(bytea), Default(None) */
  case class InstrumentsRow(instrumentId: Int, userId: Int, name: String, description: String, privacy: String, c4: Option[Array[Byte]] = None, db4: Option[Array[Byte]] = None, d4: Option[Array[Byte]] = None, eb4: Option[Array[Byte]] = None, e4: Option[Array[Byte]] = None, f4: Option[Array[Byte]] = None, gb4: Option[Array[Byte]] = None, g4: Option[Array[Byte]] = None, ab4: Option[Array[Byte]] = None, a4: Option[Array[Byte]] = None, bb4: Option[Array[Byte]] = None, b4: Option[Array[Byte]] = None, c5: Option[Array[Byte]] = None, db5: Option[Array[Byte]] = None, d5: Option[Array[Byte]] = None, eb5: Option[Array[Byte]] = None, e5: Option[Array[Byte]] = None, f5: Option[Array[Byte]] = None, gb5: Option[Array[Byte]] = None, g5: Option[Array[Byte]] = None, ab5: Option[Array[Byte]] = None, a5: Option[Array[Byte]] = None, bb5: Option[Array[Byte]] = None, b5: Option[Array[Byte]] = None, c6: Option[Array[Byte]] = None)
  /** GetResult implicit for fetching InstrumentsRow objects using plain SQL queries */
  implicit def GetResultInstrumentsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Array[Byte]]]): GR[InstrumentsRow] = GR{
    prs => import prs._
    InstrumentsRow(<<[Int], <<[Int], <<[String], <<[String], <<[String], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]], <<?[Array[Byte]])
  }
  /** Table description of table instruments. Objects of this class serve as prototypes for rows in queries. */
  class Instruments(_tableTag: Tag) extends profile.api.Table[InstrumentsRow](_tableTag, "instruments") {
    def * = (instrumentId :: userId :: name :: description :: privacy :: c4 :: db4 :: d4 :: eb4 :: e4 :: f4 :: gb4 :: g4 :: ab4 :: a4 :: bb4 :: b4 :: c5 :: db5 :: d5 :: eb5 :: e5 :: f5 :: gb5 :: g5 :: ab5 :: a5 :: bb5 :: b5 :: c6 :: HNil).mapTo[InstrumentsRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(instrumentId) :: Rep.Some(userId) :: Rep.Some(name) :: Rep.Some(description) :: Rep.Some(privacy) :: c4 :: db4 :: d4 :: eb4 :: e4 :: f4 :: gb4 :: g4 :: ab4 :: a4 :: bb4 :: b4 :: c5 :: db5 :: d5 :: eb5 :: e5 :: f5 :: gb5 :: g5 :: ab5 :: a5 :: bb5 :: b5 :: c6 :: HNil).shaped.<>(r => InstrumentsRow(r(0).asInstanceOf[Option[Int]].get, r(1).asInstanceOf[Option[Int]].get, r(2).asInstanceOf[Option[String]].get, r(3).asInstanceOf[Option[String]].get, r(4).asInstanceOf[Option[String]].get, r(5).asInstanceOf[Option[Array[Byte]]], r(6).asInstanceOf[Option[Array[Byte]]], r(7).asInstanceOf[Option[Array[Byte]]], r(8).asInstanceOf[Option[Array[Byte]]], r(9).asInstanceOf[Option[Array[Byte]]], r(10).asInstanceOf[Option[Array[Byte]]], r(11).asInstanceOf[Option[Array[Byte]]], r(12).asInstanceOf[Option[Array[Byte]]], r(13).asInstanceOf[Option[Array[Byte]]], r(14).asInstanceOf[Option[Array[Byte]]], r(15).asInstanceOf[Option[Array[Byte]]], r(16).asInstanceOf[Option[Array[Byte]]], r(17).asInstanceOf[Option[Array[Byte]]], r(18).asInstanceOf[Option[Array[Byte]]], r(19).asInstanceOf[Option[Array[Byte]]], r(20).asInstanceOf[Option[Array[Byte]]], r(21).asInstanceOf[Option[Array[Byte]]], r(22).asInstanceOf[Option[Array[Byte]]], r(23).asInstanceOf[Option[Array[Byte]]], r(24).asInstanceOf[Option[Array[Byte]]], r(25).asInstanceOf[Option[Array[Byte]]], r(26).asInstanceOf[Option[Array[Byte]]], r(27).asInstanceOf[Option[Array[Byte]]], r(28).asInstanceOf[Option[Array[Byte]]], r(29).asInstanceOf[Option[Array[Byte]]]), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column instrument_id SqlType(serial), AutoInc, PrimaryKey */
    val instrumentId: Rep[Int] = column[Int]("instrument_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column name SqlType(varchar), Length(256,true) */
    val name: Rep[String] = column[String]("name", O.Length(256,varying=true))
    /** Database column description SqlType(varchar), Length(2000,true) */
    val description: Rep[String] = column[String]("description", O.Length(2000,varying=true))
    /** Database column privacy SqlType(varchar), Length(7,true) */
    val privacy: Rep[String] = column[String]("privacy", O.Length(7,varying=true))
    /** Database column c4 SqlType(bytea), Default(None) */
    val c4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("c4", O.Default(None))
    /** Database column db4 SqlType(bytea), Default(None) */
    val db4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("db4", O.Default(None))
    /** Database column d4 SqlType(bytea), Default(None) */
    val d4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("d4", O.Default(None))
    /** Database column eb4 SqlType(bytea), Default(None) */
    val eb4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("eb4", O.Default(None))
    /** Database column e4 SqlType(bytea), Default(None) */
    val e4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("e4", O.Default(None))
    /** Database column f4 SqlType(bytea), Default(None) */
    val f4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("f4", O.Default(None))
    /** Database column gb4 SqlType(bytea), Default(None) */
    val gb4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("gb4", O.Default(None))
    /** Database column g4 SqlType(bytea), Default(None) */
    val g4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("g4", O.Default(None))
    /** Database column ab4 SqlType(bytea), Default(None) */
    val ab4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("ab4", O.Default(None))
    /** Database column a4 SqlType(bytea), Default(None) */
    val a4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("a4", O.Default(None))
    /** Database column bb4 SqlType(bytea), Default(None) */
    val bb4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("bb4", O.Default(None))
    /** Database column b4 SqlType(bytea), Default(None) */
    val b4: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("b4", O.Default(None))
    /** Database column c5 SqlType(bytea), Default(None) */
    val c5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("c5", O.Default(None))
    /** Database column db5 SqlType(bytea), Default(None) */
    val db5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("db5", O.Default(None))
    /** Database column d5 SqlType(bytea), Default(None) */
    val d5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("d5", O.Default(None))
    /** Database column eb5 SqlType(bytea), Default(None) */
    val eb5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("eb5", O.Default(None))
    /** Database column e5 SqlType(bytea), Default(None) */
    val e5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("e5", O.Default(None))
    /** Database column f5 SqlType(bytea), Default(None) */
    val f5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("f5", O.Default(None))
    /** Database column gb5 SqlType(bytea), Default(None) */
    val gb5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("gb5", O.Default(None))
    /** Database column g5 SqlType(bytea), Default(None) */
    val g5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("g5", O.Default(None))
    /** Database column ab5 SqlType(bytea), Default(None) */
    val ab5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("ab5", O.Default(None))
    /** Database column a5 SqlType(bytea), Default(None) */
    val a5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("a5", O.Default(None))
    /** Database column bb5 SqlType(bytea), Default(None) */
    val bb5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("bb5", O.Default(None))
    /** Database column b5 SqlType(bytea), Default(None) */
    val b5: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("b5", O.Default(None))
    /** Database column c6 SqlType(bytea), Default(None) */
    val c6: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("c6", O.Default(None))

    /** Foreign key referencing Users (database name instruments_user_id_fkey) */
    lazy val usersFk = foreignKey("instruments_user_id_fkey", userId :: HNil, Users)(r => r.id :: HNil, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Instruments */
  lazy val Instruments = new TableQuery(tag => new Instruments(tag))

  /** Entity class storing rows of table Items
   *  @param itemId Database column item_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param text Database column text SqlType(varchar), Length(2000,true)
   *  @param privacy Database column privacy SqlType(varchar), Length(7,true)
   *  @param mididata Database column mididata SqlType(varchar), Length(10000,true), Default(None) */
  case class ItemsRow(itemId: Int, userId: Int, text: String, privacy: String, mididata: Option[String] = None)
  /** GetResult implicit for fetching ItemsRow objects using plain SQL queries */
  implicit def GetResultItemsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ItemsRow] = GR{
    prs => import prs._
    ItemsRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table items. Objects of this class serve as prototypes for rows in queries. */
  class Items(_tableTag: Tag) extends profile.api.Table[ItemsRow](_tableTag, "items") {
    def * = (itemId, userId, text, privacy, mididata) <> (ItemsRow.tupled, ItemsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(itemId), Rep.Some(userId), Rep.Some(text), Rep.Some(privacy), mididata)).shaped.<>({r=>import r._; _1.map(_=> ItemsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column item_id SqlType(serial), AutoInc, PrimaryKey */
    val itemId: Rep[Int] = column[Int]("item_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column text SqlType(varchar), Length(2000,true) */
    val text: Rep[String] = column[String]("text", O.Length(2000,varying=true))
    /** Database column privacy SqlType(varchar), Length(7,true) */
    val privacy: Rep[String] = column[String]("privacy", O.Length(7,varying=true))
    /** Database column mididata SqlType(varchar), Length(10000,true), Default(None) */
    val mididata: Rep[Option[String]] = column[Option[String]]("mididata", O.Length(10000,varying=true), O.Default(None))

    /** Foreign key referencing Users (database name items_user_id_fkey) */
    lazy val usersFk = foreignKey("items_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Items */
  lazy val Items = new TableQuery(tag => new Items(tag))

  /** Entity class storing rows of table Recordings
   *  @param recordingId Database column recording_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param name Database column name SqlType(varchar), Length(256,true)
   *  @param description Database column description SqlType(varchar), Length(2000,true)
   *  @param privacy Database column privacy SqlType(varchar), Length(7,true)
   *  @param audio Database column audio SqlType(bytea) */
  case class RecordingsRow(recordingId: Int, userId: Int, name: String, description: String, privacy: String, audio: Array[Byte])
  /** GetResult implicit for fetching RecordingsRow objects using plain SQL queries */
  implicit def GetResultRecordingsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Array[Byte]]): GR[RecordingsRow] = GR{
    prs => import prs._
    RecordingsRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[String], <<[Array[Byte]]))
  }
  /** Table description of table recordings. Objects of this class serve as prototypes for rows in queries. */
  class Recordings(_tableTag: Tag) extends profile.api.Table[RecordingsRow](_tableTag, "recordings") {
    def * = (recordingId, userId, name, description, privacy, audio) <> (RecordingsRow.tupled, RecordingsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(recordingId), Rep.Some(userId), Rep.Some(name), Rep.Some(description), Rep.Some(privacy), Rep.Some(audio))).shaped.<>({r=>import r._; _1.map(_=> RecordingsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column recording_id SqlType(serial), AutoInc, PrimaryKey */
    val recordingId: Rep[Int] = column[Int]("recording_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column name SqlType(varchar), Length(256,true) */
    val name: Rep[String] = column[String]("name", O.Length(256,varying=true))
    /** Database column description SqlType(varchar), Length(2000,true) */
    val description: Rep[String] = column[String]("description", O.Length(2000,varying=true))
    /** Database column privacy SqlType(varchar), Length(7,true) */
    val privacy: Rep[String] = column[String]("privacy", O.Length(7,varying=true))
    /** Database column audio SqlType(bytea) */
    val audio: Rep[Array[Byte]] = column[Array[Byte]]("audio")

    /** Foreign key referencing Users (database name recordings_user_id_fkey) */
    lazy val usersFk = foreignKey("recordings_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Recordings */
  lazy val Recordings = new TableQuery(tag => new Recordings(tag))

  /** Entity class storing rows of table Users
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(20,true)
   *  @param password Database column password SqlType(varchar), Length(200,true) */
  case class UsersRow(id: Int, username: String, password: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (id, username, password) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20,varying=true))
    /** Database column password SqlType(varchar), Length(200,true) */
    val password: Rep[String] = column[String]("password", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
